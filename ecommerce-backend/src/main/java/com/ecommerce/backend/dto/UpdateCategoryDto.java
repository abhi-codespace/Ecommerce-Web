package com.ecommerce.backend.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCategoryDto {
    
    @Size(max = 100, message= "Name must be at most 100")
    private String name;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;
}
