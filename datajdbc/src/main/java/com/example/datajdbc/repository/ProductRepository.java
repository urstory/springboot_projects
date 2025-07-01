package com.example.datajdbc.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.datajdbc.entity.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    
    // 이름으로 상품 찾기
    List<Product> findByName(String name);
    
    // 가격 범위로 상품 찾기
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    
    // 특정 가격 이하 상품 찾기
    List<Product> findByPriceLessThanEqual(Double maxPrice);
    
    // 카테고리별 상품 찾기
    List<Product> findByCategory(String category);
    
    // 재고 있는 상품 찾기
    List<Product> findByInStockTrue();
    
    // 재고 없는 상품 찾기
    List<Product> findByInStockFalse();
    
    // 이름에 특정 키워드 포함된 상품 찾기 (대소문자 무시)
    List<Product> findByNameContainingIgnoreCase(String keyword);
    
    // 카테고리별 재고 있는 상품 찾기
    List<Product> findByCategoryAndInStockTrue(String category);
    
    // 가격 기준 정렬
    List<Product> findAllByOrderByPriceAsc();
    List<Product> findAllByOrderByPriceDesc();
    
    // 커스텀 쿼리 - 특정 가격 이상의 상품 개수  
    @Query("SELECT COUNT(*) FROM products WHERE price > :price")
    long countByPriceGreaterThan(@Param("price") Double price);
    
    // 커스텀 쿼리 - 카테고리별 평균 가격
    @Query("SELECT AVG(price) FROM products WHERE category = :category")
    Double getAveragePriceByCategory(@Param("category") String category);
    
    // 커스텀 쿼리 - 가장 비싼 상품들 (TOP N)
    @Query("SELECT * FROM products ORDER BY price DESC LIMIT :limit")
    List<Product> findTopNExpensiveProducts(@Param("limit") int limit);
} 