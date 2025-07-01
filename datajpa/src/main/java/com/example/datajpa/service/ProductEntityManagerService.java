package com.example.datajpa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.datajpa.entity.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

/**
 * EntityManager를 사용한 Product 서비스
 * 내용.md 7장 - JPA와 Spring Data JDBC 예제
 */
@Service
@Transactional
public class ProductEntityManagerService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 상품 저장
     */
    public Product save(Product product) {
        if (product.getId() == null) {
            entityManager.persist(product);
            return product;
        } else {
            return entityManager.merge(product);
        }
    }

    /**
     * ID로 상품 조회
     */
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return entityManager.find(Product.class, id);
    }

    /**
     * 모든 상품 조회
     */
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p", Product.class);
        return query.getResultList();
    }

    /**
     * 이름으로 상품 조회 (JPQL 사용)
     */
    @Transactional(readOnly = true)
    public List<Product> findByName(String name) {
        TypedQuery<Product> query = entityManager.createQuery(
            "SELECT p FROM Product p WHERE p.name = :name", Product.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    /**
     * 가격 범위로 상품 조회 (JPQL 사용)
     */
    @Transactional(readOnly = true)
    public List<Product> findByPriceRange(Double minPrice, Double maxPrice) {
        TypedQuery<Product> query = entityManager.createQuery(
            "SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max ORDER BY p.price", Product.class);
        query.setParameter("min", minPrice);
        query.setParameter("max", maxPrice);
        return query.getResultList();
    }

    /**
     * 카테고리별 상품 조회
     */
    @Transactional(readOnly = true)
    public List<Product> findByCategory(String category) {
        TypedQuery<Product> query = entityManager.createQuery(
            "SELECT p FROM Product p WHERE p.category = :category", Product.class);
        query.setParameter("category", category);
        return query.getResultList();
    }

    /**
     * 상품 업데이트
     */
    public Product update(Product product) {
        return entityManager.merge(product);
    }

    /**
     * 상품 삭제
     */
    public void delete(Product product) {
        if (entityManager.contains(product)) {
            entityManager.remove(product);
        } else {
            // 영속성 컨텍스트에 없는 경우, 먼저 영속 상태로 만든 후 삭제
            Product managedProduct = entityManager.merge(product);
            entityManager.remove(managedProduct);
        }
    }

    /**
     * ID로 상품 삭제
     */
    public void deleteById(Long id) {
        Product product = findById(id);
        if (product != null) {
            delete(product);
        }
    }

    /**
     * 상품 개수 조회
     */
    @Transactional(readOnly = true)
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(p) FROM Product p", Long.class);
        return query.getSingleResult();
    }

    /**
     * EntityManager CRUD 예제 실행
     */
    public void entityManagerCRUDExample() {
        System.out.println("=== EntityManager CRUD 예제 시작 ===");

        // Create
        Product newProduct = new Product("EntityManager Laptop", 1500.0, "고성능 노트북", "Electronics");
        save(newProduct);
        System.out.println("상품 생성: " + newProduct);

        // Read
        Product foundProduct = findById(newProduct.getId());
        System.out.println("상품 조회: " + foundProduct);

        // Update
        if (foundProduct != null) {
            foundProduct.setPrice(1600.0);
            foundProduct.setDescription("업데이트된 고성능 노트북");
            update(foundProduct);
            System.out.println("상품 업데이트: " + foundProduct);
        }

        // Read again
        Product updatedProduct = findById(newProduct.getId());
        System.out.println("업데이트 후 조회: " + updatedProduct);

        // Delete
        if (updatedProduct != null) {
            delete(updatedProduct);
            System.out.println("상품 삭제 완료: ID " + updatedProduct.getId());
        }

        System.out.println("=== EntityManager CRUD 예제 완료 ===");
    }
} 