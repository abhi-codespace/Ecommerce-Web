package com.ecommerce.backend.service;

import java.util.List;

import com.ecommerce.backend.dto.CategoryResponseDto;
import com.ecommerce.backend.dto.CreateCategoryDto;
import com.ecommerce.backend.dto.UpdateCategoryDto;

public interface CategoryService {

    List<CategoryResponseDto> getAllCategories();

    CategoryResponseDto getCategoryById(Long id);
    
    CategoryResponseDto createCategory(CreateCategoryDto dto);
    
    CategoryResponseDto updateCategory(Long id, UpdateCategoryDto dto);
    
    void deleteCategory(Long id);
}