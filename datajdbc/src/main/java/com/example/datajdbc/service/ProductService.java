package com.example.datajdbc.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.example.datajdbc.entity.Product;
import com.example.datajdbc.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    
    // 모든 상품 조회
    public List<Product> getAllProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .toList();
    }
    
    // ID로 상품 조회
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    // 상품 생성
    public Product createProduct(Product product) {
        log.info("새 상품 생성: {}", product.getName());
        return productRepository.save(product);
    }
    
    // 상품 업데이트
    public Product updateProduct(Long id, Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productDetails.getName());
            product.setPrice(productDetails.getPrice());
            product.setDescription(productDetails.getDescription());
            product.setCategory(productDetails.getCategory());
            product.setInStock(productDetails.getInStock());
            
            log.info("상품 업데이트: ID {}, 이름 {}", id, product.getName());
            return productRepository.save(product);
        }
        throw new RuntimeException("상품을 찾을 수 없습니다: ID " + id);
    }
    
    // 상품 삭제
    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            log.info("상품 삭제: ID {}", id);
        } else {
            throw new RuntimeException("상품을 찾을 수 없습니다: ID " + id);
        }
    }
    
    // 이름으로 상품 찾기
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }
    
    // 카테고리별 상품 찾기
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    // 가격 범위로 상품 찾기
    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
    
    // 재고 있는 상품 찾기
    public List<Product> getInStockProducts() {
        return productRepository.findByInStockTrue();
    }
    
    // 이름에 키워드 포함된 상품 찾기
    public List<Product> searchProductsByName(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    // 가격 기준 정렬
    public List<Product> getProductsSortedByPriceAsc() {
        return productRepository.findAllByOrderByPriceAsc();
    }
    
    public List<Product> getProductsSortedByPriceDesc() {
        return productRepository.findAllByOrderByPriceDesc();
    }
    
    // 통계 정보
    public long countProductsAbovePrice(Double price) {
        return productRepository.countByPriceGreaterThan(price);
    }
    
    public Double getAveragePriceByCategory(String category) {
        return productRepository.getAveragePriceByCategory(category);
    }
    
    public List<Product> getTopExpensiveProducts(int limit) {
        return productRepository.findTopNExpensiveProducts(limit);
    }
    
    // Spring Data JDBC CRUD 예제
    public void jdbcCrudExample() {
        log.info("=== Spring Data JDBC CRUD 예제 시작 ===");
        
        // Create
        Product product = new Product("JDBC Monitor", 250.0, "고성능 모니터", "Electronics", true);
        Product savedProduct = productRepository.save(product);
        log.info("상품 생성: {}", savedProduct);
        
        // Read
        Optional<Product> retrieved = productRepository.findById(savedProduct.getId());
        retrieved.ifPresent(p -> log.info("상품 조회: {}", p));
        
        // Update
        if (retrieved.isPresent()) {
            Product p = retrieved.get();
            p.setPrice(270.0);
            p.setDescription("업데이트된 고성능 모니터");
            Product updated = productRepository.save(p);
            log.info("상품 업데이트: {}", updated);
        }
        
        // Delete
        productRepository.deleteById(savedProduct.getId());
        log.info("상품 삭제 완료: ID {}", savedProduct.getId());
        
        log.info("=== Spring Data JDBC CRUD 예제 완료 ===");
    }
} 