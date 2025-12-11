package com.ecommerce.backend.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateProductDto {
    private String productName;
    @Positive(message = "Price must be positive")
    private Double price;
    private String email;

    @Positive(message = "Stock must be positive")
    private Integer stock;
    
    private String brand;
    private String description;
    private Boolean isActive;
    private Long categoryId;

}
