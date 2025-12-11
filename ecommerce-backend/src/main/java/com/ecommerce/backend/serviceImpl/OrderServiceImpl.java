package com.ecommerce.backend.serviceImpl;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.backend.dto.CreateOrderDto;
import com.ecommerce.backend.dto.OrderItemDto;
import com.ecommerce.backend.dto.OrderItemSummaryDto;
import com.ecommerce.backend.dto.OrderResponseDto;
import com.ecommerce.backend.entity.Customer;
import com.ecommerce.backend.entity.Order;
import com.ecommerce.backend.entity.OrderItem;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.entity.type.OrderStatus;
import com.ecommerce.backend.repository.CustomerRepository;
import com.ecommerce.backend.repository.OrderItemRepository;
import com.ecommerce.backend.repository.OrderRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.service.OrderService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

     private final OrderRepository orderRepository;
     private final OrderItemRepository orderItemRepository;
     private final UserRepository userRepository;
     private final CustomerRepository customerRepository;
     private final ProductRepository productRepository;
     private final ModelMapper modelMapper;

     @Override
     @Transactional(readOnly = true)
     public List<OrderResponseDto> getUserOrders(String username) {
          User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

          return orderRepository.findByUser(user).stream()
                    .map(this::toResponseDto)
                    .toList();
     }

     @Override
     @Transactional(readOnly = true)
     public OrderResponseDto getOrderById(Long id, String username) {
          Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found"));

          if (!order.getUser().getUsername().equals(username)) {
               throw new EntityNotFoundException("Order not found");
          }
          return toResponseDto(order);

     }

     @Override
     @Transactional(readOnly = true)
     public List<OrderResponseDto> getAllOrders(int page, int size) {

          Pageable pageable = PageRequest.of(page, size);
          return orderRepository.findAll(pageable)
                    .map(this::toResponseDto)
                    .getContent();
     }

     @Override
     public OrderResponseDto createOrder(String username, CreateOrderDto dto) {

          User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

          Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

          Order order = new Order();
          order.setUser(user);
          order.setCustomer(customer);
          order.setOrderStatus(OrderStatus.PENDING);
          order.setAmount(0.0);

          Order savedOrder = orderRepository.save(order);

          double total = 0.0;

          if (dto.getItems() != null && !dto.getItems().isEmpty()) {
               for (OrderItemDto itemDto : dto.getItems()) {
                    Product product = productRepository.findById(itemDto.getProductId())
                              .orElseThrow(() -> new EntityNotFoundException("Product not found"));

                    BigDecimal priceAtPurchase = BigDecimal.valueOf(product.getPrice());

                    int quantity = itemDto.getQuantity() != null ? itemDto.getQuantity() : 1;

                    OrderItem item = new OrderItem();
                    item.setOrder(savedOrder);
                    item.setProduct(product);
                    item.setQuantity(quantity);
                    item.setPriceAtPurchase(priceAtPurchase);
                    orderItemRepository.save(item);

                    total += priceAtPurchase.doubleValue() * quantity;
               }
          }

          savedOrder.setAmount(total);
          Order finalOrder = orderRepository.save(savedOrder);

          return toResponseDto(finalOrder);

     }

     @Override
     public OrderResponseDto updateOrderStatus(Long id, OrderStatus status) {
          Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found"));

          order.setOrderStatus(status);
          Order updated = orderRepository.save(order);
          return toResponseDto(updated);
     }

     @Override
     public void deleteOrder(Long id) {
          if (!orderRepository.existsById(id)) {
               throw new EntityNotFoundException("Order not found");
          }
          orderRepository.deleteById(id);
     }

     private OrderResponseDto toResponseDto(Order order) {
          List<OrderItemSummaryDto> itemDtos = order.getItems() != null
                    ? order.getItems().stream()
                              .map(this::toItemSummaryDto)
                              .toList()
                    : List.of();

          return OrderResponseDto.builder()
                    .id(order.getId())
                    .orderDate(order.getOrderDate())
                    .amount(order.getAmount())
                    .orderStatus(order.getOrderStatus())
                    .customerId(order.getCustomer().getId())
                    .customerName(order.getCustomer().getName())
                    .userId(order.getUser().getId())
                    .username(order.getUser().getUsername())
                    .items(itemDtos)
                    .build();
     }

     private OrderItemSummaryDto toItemSummaryDto(OrderItem orderItem) {
          return OrderItemSummaryDto.builder()
                    .productId(orderItem.getProduct().getId())
                    .productName(orderItem.getProduct().getProductName())
                    .quantity(orderItem.getQuantity())
                    .priceAtPurchase(orderItem.getPriceAtPurchase()).build();
     }
}
