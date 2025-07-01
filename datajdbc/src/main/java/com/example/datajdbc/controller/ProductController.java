package com.example.datajdbc.controller;

import java.util.HashMap;
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

import com.example.datajdbc.entity.Product;
import com.example.datajdbc.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    // 모든 상품 조회
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
    
    // ID로 상품 조회
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // 상품 생성
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }
    
    // 상품 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 이름으로 상품 찾기
    @GetMapping("/search/name/{name}")
    public List<Product> getProductsByName(@PathVariable String name) {
        return productService.getProductsByName(name);
    }
    
    // 카테고리별 상품 찾기
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }
    
    // 가격 범위로 상품 찾기
    @GetMapping("/price-range")
    public List<Product> getProductsByPriceRange(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        return productService.getProductsByPriceRange(minPrice, maxPrice);
    }
    
    // 재고 있는 상품 찾기
    @GetMapping("/in-stock")
    public List<Product> getInStockProducts() {
        return productService.getInStockProducts();
    }
    
    // 이름에 키워드 포함된 상품 검색
    @GetMapping("/search")
    public List<Product> searchProductsByName(@RequestParam String keyword) {
        return productService.searchProductsByName(keyword);
    }
    
    // 가격 기준 정렬 (오름차순)
    @GetMapping("/sorted/price/asc")
    public List<Product> getProductsSortedByPriceAsc() {
        return productService.getProductsSortedByPriceAsc();
    }
    
    // 가격 기준 정렬 (내림차순)
    @GetMapping("/sorted/price/desc")
    public List<Product> getProductsSortedByPriceDesc() {
        return productService.getProductsSortedByPriceDesc();
    }
    
    // 특정 가격 이상 상품 개수
    @GetMapping("/count/above-price/{price}")
    public long countProductsAbovePrice(@PathVariable Double price) {
        return productService.countProductsAbovePrice(price);
    }
    
    // 카테고리별 평균 가격
    @GetMapping("/average-price/{category}")
    public ResponseEntity<Double> getAveragePriceByCategory(@PathVariable String category) {
        Double avgPrice = productService.getAveragePriceByCategory(category);
        if (avgPrice != null) {
            return ResponseEntity.ok(avgPrice);
        }
        return ResponseEntity.notFound().build();
    }
    
    // 가장 비싼 상품들 (TOP N)
    @GetMapping("/top-expensive/{limit}")
    public List<Product> getTopExpensiveProducts(@PathVariable int limit) {
        return productService.getTopExpensiveProducts(limit);
    }
    
    // 상품 통계 정보
    @GetMapping("/statistics")
    public Map<String, Object> getProductStatistics() {
        List<Product> allProducts = productService.getAllProducts();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProducts", allProducts.size());
        stats.put("inStockProducts", productService.getInStockProducts().size());
        stats.put("expensiveProducts", productService.countProductsAbovePrice(100.0));
        
        // 카테고리별 개수
        Map<String, Long> categoryCount = new HashMap<>();
        allProducts.forEach(product -> 
            categoryCount.merge(product.getCategory(), 1L, Long::sum)
        );
        stats.put("categoryCount", categoryCount);
        
        return stats;
    }
    
    // Spring Data JDBC CRUD 예제 실행
    @PostMapping("/jdbc-example")
    public ResponseEntity<String> runJdbcExample() {
        try {
            productService.jdbcCrudExample();
            return ResponseEntity.ok("Spring Data JDBC CRUD 예제가 성공적으로 실행되었습니다. 로그를 확인하세요.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("예제 실행 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
} 