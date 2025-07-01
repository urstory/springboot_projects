package com.example.datajpa.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.datajpa.entity.Product;
import com.example.datajpa.service.ProductEntityManagerService;
import com.example.datajpa.service.ProductJpaService;

/**
 * Product REST API Controller
 * 내용.md 7장 - JPA와 Spring Data JDBC 예제
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductJpaService productJpaService;
    private final ProductEntityManagerService productEntityManagerService;

    public ProductController(ProductJpaService productJpaService, 
                           ProductEntityManagerService productEntityManagerService) {
        this.productJpaService = productJpaService;
        this.productEntityManagerService = productEntityManagerService;
    }

    /**
     * 모든 상품 조회
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productJpaService.findAll();
        return ResponseEntity.ok(products);
    }

    /**
     * ID로 상품 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productJpaService.findById(id);
        return product.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 상품 생성
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productJpaService.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    /**
     * 상품 업데이트
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            Product updatedProduct = productJpaService.updateProduct(
                id, product.getName(), product.getPrice(), 
                product.getDescription(), product.getCategory());
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productJpaService.existsById(id)) {
            productJpaService.deleteProduct(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 이름으로 상품 검색
     */
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<Product>> getProductsByName(@PathVariable String name) {
        List<Product> products = productJpaService.findByName(name);
        return ResponseEntity.ok(products);
    }

    /**
     * 키워드로 상품 검색
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productJpaService.searchByName(keyword);
        return ResponseEntity.ok(products);
    }

    /**
     * 카테고리별 상품 조회
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productJpaService.findByCategory(category);
        return ResponseEntity.ok(products);
    }

    /**
     * 가격 범위로 상품 조회
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam Double minPrice, 
            @RequestParam Double maxPrice) {
        List<Product> products = productJpaService.findByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * 가격 이하의 상품 조회
     */
    @GetMapping("/price-max/{maxPrice}")
    public ResponseEntity<List<Product>> getProductsByMaxPrice(@PathVariable Double maxPrice) {
        List<Product> products = productJpaService.findByPriceLessThanEqual(maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * 재고 있는 상품 조회
     */
    @GetMapping("/in-stock")
    public ResponseEntity<List<Product>> getInStockProducts() {
        List<Product> products = productJpaService.findInStockProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * 카테고리별 가격순 정렬 조회
     */
    @GetMapping("/category/{category}/ordered")
    public ResponseEntity<List<Product>> getProductsByCategoryOrdered(@PathVariable String category) {
        List<Product> products = productJpaService.findByCategoryOrderByPrice(category);
        return ResponseEntity.ok(products);
    }

    /**
     * 통계 정보 조회
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getProductStats() {
        long totalCount = productJpaService.count();
        long expensiveCount = productJpaService.countByPriceGreaterThan(1000.0);
        
        Map<String, Object> stats = Map.of(
            "totalCount", totalCount,
            "expensiveProductsCount", expensiveCount,
            "averageExpensiveRatio", totalCount > 0 ? (double) expensiveCount / totalCount : 0.0
        );
        
        return ResponseEntity.ok(stats);
    }

    /**
     * EntityManager 예제 실행
     */
    @PostMapping("/demo/entity-manager")
    public ResponseEntity<String> runEntityManagerDemo() {
        productEntityManagerService.entityManagerCRUDExample();
        return ResponseEntity.ok("EntityManager CRUD 예제가 콘솔에 실행되었습니다.");
    }

    /**
     * Spring Data JPA 예제 실행
     */
    @PostMapping("/demo/spring-data-jpa")
    public ResponseEntity<String> runSpringDataJpaDemo() {
        productJpaService.jpaRepositoryCRUDExample();
        return ResponseEntity.ok("Spring Data JPA CRUD 예제가 콘솔에 실행되었습니다.");
    }
} 