package com.neosoft.order_service.service;

import com.neosoft.order_service.dto.OrderRequest;
import com.neosoft.order_service.dto.OrderResponse;
import com.neosoft.order_service.entity.Order;
import com.neosoft.order_service.mapper.OrderMapper;
import com.neosoft.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InvoiceGenerator invoiceGenerator;
    private final S3Service s3Service;
    private final SnsService snsService;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        Order order = OrderMapper.toEntity(request);
        Order savedOrder = orderRepository.save(order);
        log.info("Order created with id={}", savedOrder.getId());

        byte[] pdfBytes = invoiceGenerator.generateInvoicePdf(savedOrder);
        String invoiceUrl = s3Service.uploadInvoicePdf(savedOrder.getId(), pdfBytes);
        savedOrder.setInvoiceUrl(invoiceUrl);
        savedOrder = orderRepository.save(savedOrder);

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(savedOrder.getId())
                .customerName(savedOrder.getCustomerName())
                .productName(savedOrder.getProductName())
                .quantity(savedOrder.getQuantity())
                .totalAmount(savedOrder.getTotalAmount())
                .invoiceUrl(savedOrder.getInvoiceUrl())
                .createdAt(savedOrder.getCreatedAt())
                .build();
        snsService.publishOrderCreatedEvent(event);

        return OrderMapper.toResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + id));
        return OrderMapper.toResponse(order);
    }
}