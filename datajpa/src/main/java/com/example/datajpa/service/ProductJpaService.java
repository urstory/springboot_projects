package com.example.datajpa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.datajpa.entity.Product;
import com.example.datajpa.repository.ProductRepository;

/**
 * Spring Data JPA를 사용한 Product 서비스
 * 내용.md 7장 - JPA와 Spring Data JDBC 예제
 */
@Service
@Transactional
public class ProductJpaService {

    private final ProductRepository productRepository;

    public ProductJpaService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 상품 생성
     */
    public Product createProduct(String name, Double price) {
        Product product = new Product(name, price);
        return productRepository.save(product);
    }

    /**
     * 상품 생성 (전체 정보)
     */
    public Product createProduct(String name, Double price, String description, String category) {
        Product product = new Product(name, price, description, category);
        return productRepository.save(product);
    }

    /**
     * 상품 저장/업데이트
     */
    public Product save(Product product) {
        return productRepository.save(product);
    }

    /**
     * ID로 상품 조회
     */
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * 모든 상품 조회
     */
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * 이름으로 상품 조회
     */
    @Transactional(readOnly = true)
    public List<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    /**
     * 가격 이하의 상품 조회
     */
    @Transactional(readOnly = true)
    public List<Product> findByPriceLessThanEqual(Double price) {
        return productRepository.findByPriceLessThanEqual(price);
    }

    /**
     * 가격 범위로 상품 조회
     */
    @Transactional(readOnly = true)
    public List<Product> findByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    /**
     * 카테고리별 상품 조회
     */
    @Transactional(readOnly = true)
    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * 재고 있는 상품 조회
     */
    @Transactional(readOnly = true)
    public List<Product> findInStockProducts() {
        return productRepository.findByInStockTrue();
    }

    /**
     * 상품명 검색 (키워드 포함)
     */
    @Transactional(readOnly = true)
    public List<Product> searchByName(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    /**
     * 카테고리별 가격순 정렬
     */
    @Transactional(readOnly = true)
    public List<Product> findByCategoryOrderByPrice(String category) {
        return productRepository.findByCategoryOrderByPriceAsc(category);
    }

    /**
     * 커스텀 쿼리로 가격 범위 조회
     */
    @Transactional(readOnly = true)
    public List<Product> findProductsInRange(Double min, Double max) {
        return productRepository.findProductsInRange(min, max);
    }

    /**
     * 상품 업데이트
     */
    public Product updateProduct(Long id, String name, Double price, String description, String category) {
        Optional<Product> optionalProduct = findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(name);
            product.setPrice(price);
            product.setDescription(description);
            product.setCategory(category);
            return save(product);
        }
        throw new RuntimeException("상품을 찾을 수 없습니다: ID " + id);
    }

    /**
     * 상품 삭제
     */
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * 상품 존재 여부 확인
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    /**
     * 전체 상품 개수
     */
    @Transactional(readOnly = true)
    public long count() {
        return productRepository.count();
    }

    /**
     * 특정 가격 이상의 상품 개수
     */
    @Transactional(readOnly = true)
    public long countByPriceGreaterThan(Double price) {
        return productRepository.countByPriceGreaterThan(price);
    }

    /**
     * Spring Data JPA CRUD 예제 실행
     */
    public void jpaRepositoryCRUDExample() {
        System.out.println("=== Spring Data JPA CRUD 예제 시작 ===");

        // Create
        Product product = createProduct("JPA Smartphone", 800.0, "최신 스마트폰", "Electronics");
        System.out.println("상품 생성: " + product);

        // Read
        Optional<Product> retrievedProduct = findById(product.getId());
        if (retrievedProduct.isPresent()) {
            System.out.println("상품 조회: " + retrievedProduct.get());

            // Update
            Product updatedProduct = updateProduct(product.getId(), "JPA Smartphone Pro", 850.0, 
                "업데이트된 최신 스마트폰", "Electronics");
            System.out.println("상품 업데이트: " + updatedProduct);
        }

        // Delete
        deleteProduct(product.getId());
        System.out.println("상품 삭제 완료: ID " + product.getId());

        // 삭제 확인
        boolean exists = existsById(product.getId());
        System.out.println("삭제 후 존재 여부: " + exists);

        System.out.println("=== Spring Data JPA CRUD 예제 완료 ===");
    }
} 