package com.neosoft.order_service.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class SnsService {

    private final SnsClient snsClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.sns.topic-arn}")
    private String topicArn;

    public void publishOrderCreatedEvent(OrderCreatedEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);

            PublishRequest request = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(message)
                    .subject("Order Created")
                    .build();

            snsClient.publish(request);
            log.info("Published OrderCreatedEvent to SNS for orderId={}", event.getOrderId());

        } catch (Exception e) {
            log.error("Failed to publish OrderCreatedEvent for orderId={}", event.getOrderId(), e);
            throw new RuntimeException("Failed to publish order event to SNS", e);
        }
    }
}