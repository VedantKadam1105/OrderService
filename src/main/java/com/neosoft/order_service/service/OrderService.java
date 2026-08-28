package com.neosoft.order_service.service;

import com.neosoft.order_service.dto.OrderRequest;
import com.neosoft.order_service.dto.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    OrderResponse getOrderById(Long id);
}