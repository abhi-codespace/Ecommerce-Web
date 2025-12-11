package com.ecommerce.backend.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.backend.dto.CategoryResponseDto;
import com.ecommerce.backend.dto.CreateCategoryDto;
import com.ecommerce.backend.dto.UpdateCategoryDto;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.service.CategoryService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
        .map(this::toResponseDto)
        .toList();
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id) {
           Category category = categoryRepository.findById(id)
            .orElseThrow(()-> new EntityNotFoundException("Category not found"));
            return toResponseDto(category);
    }

    @Override
    public CategoryResponseDto createCategory(CreateCategoryDto dto) {
        Category category = modelMapper.map(dto, Category.class);
        Category saved = categoryRepository.save(category);
        return toResponseDto(saved);
    }
    @Override
    public CategoryResponseDto updateCategory(Long id, UpdateCategoryDto dto) {
         Category category= categoryRepository.findById(id)
        .orElseThrow(()-> new EntityNotFoundException("Category not found"));

        if(dto.getName() !=null){
            category.setName(dto.getName());
        }
        if(dto.getDescription() != null){
            category.setDescription(dto.getDescription());
        }

        Category updated = categoryRepository.save(category);
        return toResponseDto(updated);
        }

    @Override
    public void deleteCategory(Long id) {
        if(!categoryRepository.existsById(id)){
            throw new EntityNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
      }

      private CategoryResponseDto toResponseDto(Category category){
        CategoryResponseDto dto = modelMapper.map(category, CategoryResponseDto.class);
        dto.setProductCount(
            category.getProducts() != null 
            ? category.getProducts().size() : 0);
        return dto;
      }
    
}
