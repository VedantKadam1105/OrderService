package com.neosoft.order_service.dto;

import com.neosoft.order_service.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private String customerName;
    private String productName;
    private Integer quantity;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String invoiceUrl;
    private LocalDateTime createdAt;
}