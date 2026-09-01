pipeline {
    agent any

    environment {
        AWS_REGION      = 'us-east-1'
        AWS_ACCOUNT_ID  = '253490772981'
        ECR_REPO        = "order-management/order-service"
        ECR_URI         = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}"
        IMAGE_TAG       = "${BUILD_NUMBER}"
        ECS_CLUSTER     = 'order-management'
        ECS_SERVICE     = 'order-service'
        TASK_FAMILY     = 'order-service'
        CONTAINER_NAME  = 'order-service'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${ECR_URI}:${IMAGE_TAG} ."
            }
        }

        stage('Push to ECR') {
            steps {
                sh """
                    aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com
                    docker push ${ECR_URI}:${IMAGE_TAG}
                """
            }
        }

        stage('Register New Task Definition') {
            steps {
                sh """
                    aws ecs describe-task-definition \
                      --task-definition ${TASK_FAMILY} \
                      --query 'taskDefinition' \
                      --region ${AWS_REGION} > current-task-def.json

                    jq --arg IMAGE "${ECR_URI}:${IMAGE_TAG}" \
                      '.containerDefinitions[0].image = \$IMAGE |
                       del(.taskDefinitionArn, .revision, .status, .requiresAttributes, .compatibilities, .registeredAt, .registeredBy)' \
                      current-task-def.json > new-task-def.json

                    aws ecs register-task-definition \
                      --region ${AWS_REGION} \
                      --cli-input-json file://new-task-def.json > new-registered-task-def.json
                """
            }
        }

        stage('Deploy to ECS') {
            steps {
                sh """
                    NEW_REVISION=\$(jq -r '.taskDefinition.revision' new-registered-task-def.json)
                    aws ecs update-service \
                      --cluster ${ECS_CLUSTER} \
                      --service ${ECS_SERVICE} \
                      --task-definition ${TASK_FAMILY}:\$NEW_REVISION \
                      --region ${AWS_REGION}
                """
            }
        }
    }

    post {
        success {
            echo "Deployed ${ECR_URI}:${IMAGE_TAG} to ${ECS_SERVICE} successfully."
        }
        failure {
            echo "Pipeline failed — check logs above."
        }
    }
}