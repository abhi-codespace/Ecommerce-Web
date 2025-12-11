package com.ecommerce.backend.dto;

import lombok.Data;

@Data
public class OrderItemSummaryDto {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double priceAtPurchase;

}