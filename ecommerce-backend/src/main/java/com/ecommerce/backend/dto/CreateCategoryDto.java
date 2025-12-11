package com.ecommerce.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryDto {
    
    @NotBlank(message = "Category name is required")
    private String name;

   @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;
    
}
