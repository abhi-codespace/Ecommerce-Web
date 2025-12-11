package com.ecommerce.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ecommerce.backend.entity.type.OrderStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponseDto {
    private Long id;
    private LocalDateTime orderDate;
    private Double amount;
    private OrderStatus orderStatus;
    private Long customerId;
    private String customerName;
    private Long userId;
    private String username;
    private List<OrderItemSummaryDto> items;
}