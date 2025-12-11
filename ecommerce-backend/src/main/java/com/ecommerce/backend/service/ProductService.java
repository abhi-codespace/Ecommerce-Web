package com.ecommerce.backend.service;

import java.util.List;

import com.ecommerce.backend.dto.CreateProductDto;
import com.ecommerce.backend.dto.ProductResponseDto;
import com.ecommerce.backend.dto.UpdateProductDto;

public interface ProductService {

    ProductResponseDto createProduct(CreateProductDto createProductDto);

    List<ProductResponseDto> getProducts(
            Integer page,
            Integer size,
            Long categoryId,
            String name,
            String brand,
            Double minPrice,
            Double maxPrice,
            String sortBy,
            String sortDir);

    ProductResponseDto getProductById(Long id);

    ProductResponseDto updateProduct(Long id, UpdateProductDto dto);

    void deleteProduct(Long id);
}
