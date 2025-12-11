package com.ecommerce.backend.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateOrderDto {
   
    @NotNull(message = "Customer is is required")
    private Long customerId;

    @NotNull(message = "Items are required")
    private List<OrderItemDto> items;

    @Positive(message = "Amount must be poitive")
    private Double amount;

}
