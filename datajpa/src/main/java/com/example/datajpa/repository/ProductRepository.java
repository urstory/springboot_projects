package com.example.datajpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.datajpa.entity.Product;

/**
 * ProductRepository 인터페이스
 * 내용.md 7장 - JPA와 Spring Data JDBC 예제
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 이름으로 상품 조회 (자동으로 쿼리 생성)
    List<Product> findByName(String name);

    // 가격 이하의 상품 조회
    List<Product> findByPriceLessThanEqual(Double price);

    // 가격 범위로 상품 조회
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    // 카테고리별 상품 조회
    List<Product> findByCategory(String category);

    // 재고 있는 상품 조회
    List<Product> findByInStockTrue();

    // 이름에 특정 문자열이 포함된 상품 조회 (대소문자 무시)
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // 카테고리별 가격 정렬
    List<Product> findByCategoryOrderByPriceAsc(String category);

    // @Query 어노테이션을 사용한 커스텀 쿼리
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max")
    List<Product> findProductsInRange(@Param("min") Double min, @Param("max") Double max);

    // 네이티브 쿼리 사용 예제
    @Query(value = "SELECT * FROM products WHERE category = ?1 AND in_stock = true", nativeQuery = true)
    List<Product> findAvailableProductsByCategory(String category);

    // 특정 가격 이상의 상품 개수
    long countByPriceGreaterThan(Double price);

    // 카테고리별 상품 존재 여부
    boolean existsByCategory(String category);
} 