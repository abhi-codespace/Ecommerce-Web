package com.ecommerce.backend.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateProductDto {

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotNull(message = "Price is required")
    @Positive(message = "Price mjust be positive")
    private Double price;

    @NotBlank(message = "Brand is required")
    private String brand;

    private String description;

    @NotNull(message = "Category id is required")
    private Long categoryId;

    @NotNull(message = "Stock is required")
    @Positive(message = "Stock must be positive")
    private Integer stock;

    private MultipartFile image;
}
