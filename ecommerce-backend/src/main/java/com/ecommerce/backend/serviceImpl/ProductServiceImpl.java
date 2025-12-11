package com.ecommerce.backend.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.backend.dto.CreateProductDto;
import com.ecommerce.backend.dto.ProductResponseDto;
import com.ecommerce.backend.dto.UpdateProductDto;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.service.ProductService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductResponseDto createProduct(CreateProductDto createProductDto) {
        Category category = categoryRepository.findById(createProductDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Product product = modelMapper.map(createProductDto, Product.class);
        product.setCategory(category);
        product.setIsActive(true);

        Product saved = productRepository.save(product);

        return toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        return toResponseDto(product);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProducts(Integer page,
            Integer size,
            Long categoryId,
            String name,
            String brand,
            Double minPrice,
            Double maxPrice,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage;

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));

            productPage = productRepository.findByCategory(category, pageable);
        } else if (name != null && !name.isBlank()) {
            productPage = productRepository.findByProductNameContainingIgnoreCase(name, pageable);
        } else if (brand != null && !brand.isBlank()) {
            productPage = productRepository.findByBrandContainingIgnoreCase(brand, pageable);
        } else if (minPrice != null && maxPrice != null) {
            productPage = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);

        } else {
            productPage = productRepository.findAll(pageable);
        }
        return productPage
                .map(this::toResponseDto)
                .getContent();
    }

    @Override
    public ProductResponseDto updateProduct(Long id, UpdateProductDto dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (dto.getProductName() != null) {
            product.setProductName(dto.getProductName());
        }
        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }
        if (dto.getBrand() != null) {
            product.setBrand(dto.getBrand());
        }
        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }
        if (dto.getIsActive() != null) {
            product.setIsActive(dto.getIsActive());
        }
        if(dto.getStock() != null){
            product.setStock(dto.getStock());
        }
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            product.setCategory(category);
        }
        Product update = productRepository.save(product);

        return toResponseDto(update);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    private ProductResponseDto toResponseDto(Product product) {
        ProductResponseDto dto = modelMapper.map(product, ProductResponseDto.class);
        dto.setCategoryId(product.getCategory().getId());
        dto.setCategoryName(product.getCategory().getName());
        return dto;
    }

}
