package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.dto.CreateOrderDto;
import com.ecommerce.backend.dto.OrderResponseDto;
import com.ecommerce.backend.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

// GET /api/orders – list current user orders
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderResponseDto>> getUserOrders(Authentication auth){
        String username = auth.getName();
        return ResponseEntity.ok(orderService.getUserOrders(username));
    }

// GET /api/orders/{id} – order details (with items)
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<OrderResponseDto> getMyOrder(
        @PathVariable Long id,
        Authentication auth){
            String username = auth.getName();
            return ResponseEntity.ok(orderService.getOrderById(id,username));
        }
    

// GET /api/admin/orders – all orders (ADMIN)
        @GetMapping("/admin")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<List<OrderResponseDto>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
                return ResponseEntity.ok(orderService.getAllOrders(page,size));
            }

// POST /api/orders – create order from current user cart + selected customer (shipping)
        @PostMapping
        @PreAuthorize("isAuthentication")
        public ResponseEntity<OrderResponseDto> createOrder(
            Authentication auth,
            @Valid @RequestBody CreateOrderDto dto
        ){
            String username = auth.getName();
            OrderResponseDto created = orderService.createOrder(username, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }

// PUT /api/admin/orders/{id}/status – update OrderStatus (PLACED, SHIPPED, etc.)
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Void> deleteOrder(@PathVariable Long id){
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        }
    
}
