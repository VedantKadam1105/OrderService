package com.neosoft.order_service.service;

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
public class OrderCreatedEvent {
    private Long orderId;
    private String customerName;
    private String productName;
    private Integer quantity;
    private BigDecimal totalAmount;
    private String invoiceUrl;
    private LocalDateTime createdAt;
}