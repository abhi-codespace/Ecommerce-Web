package com.ecommerce.backend.dto;

import lombok.Data;

@Data
public class ProductResponseDto {
    private Long id;
    private String productName;
    private Double price;
    private Integer stock;
    private String brand;
    private String description;
    private Boolean isActive;
    private Long categoryId;
    private String categoryName; 
}
