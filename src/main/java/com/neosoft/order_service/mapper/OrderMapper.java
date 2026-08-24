package com.neosoft.order_service.mapper;

import com.neosoft.order_service.dto.OrderRequest;
import com.neosoft.order_service.dto.OrderResponse;
import com.neosoft.order_service.entity.Order;

public class OrderMapper {

    public static Order toEntity(OrderRequest request) {
        return Order.builder()
                .customerName(request.getCustomerName())
                .productName(request.getProductName())
                .quantity(request.getQuantity())
                .totalAmount(request.getTotalAmount())
                .build();
    }

    public static OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .invoiceUrl(order.getInvoiceUrl())
                .createdAt(order.getCreatedAt())
                .build();
    }
}