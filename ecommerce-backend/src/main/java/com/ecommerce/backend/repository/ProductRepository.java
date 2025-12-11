package com.ecommerce.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategory(Category category, Pageable pageable);

    Page<Product> findByProductNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByBrandContainingIgnoreCase(String brand, Pageable pageable);

    Page<Product> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);

}
