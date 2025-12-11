package com.ecommerce.backend.service;

import java.util.List;

import com.ecommerce.backend.dto.CreateOrderDto;
import com.ecommerce.backend.dto.OrderResponseDto;
import com.ecommerce.backend.entity.type.OrderStatus;

public interface OrderService {
 
    List<OrderResponseDto> getUserOrders(String username);
    OrderResponseDto getOrderById(Long id,String username);
    List<OrderResponseDto> getAllOrders(int page,int size);
    OrderResponseDto createOrder(String username, CreateOrderDto dto);
    OrderResponseDto updateOrderStatus(Long id, OrderStatus status);
    void deleteOrder(Long id);
}
