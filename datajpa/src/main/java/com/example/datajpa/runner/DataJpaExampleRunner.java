package com.example.datajpa.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.datajpa.entity.Product;
import com.example.datajpa.entity.User;
import com.example.datajpa.service.ProductEntityManagerService;
import com.example.datajpa.service.ProductJpaService;
import com.example.datajpa.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * JPA 예제 실행 및 초기 데이터 삽입 Runner
 * 내용.md 7장 - JPA와 Spring Data JDBC 예제
 */
@Slf4j
@Component
public class DataJpaExampleRunner implements CommandLineRunner {

    private final ProductJpaService productJpaService;
    private final ProductEntityManagerService productEntityManagerService;
    private final UserService userService;

    public DataJpaExampleRunner(ProductJpaService productJpaService,
                              ProductEntityManagerService productEntityManagerService,
                              UserService userService) {
        this.productJpaService = productJpaService;
        this.productEntityManagerService = productEntityManagerService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("🚀 Data JPA 예제 시작");
        log.info("================================================================================");

        // 초기 데이터 삽입
        insertInitialData();

        // 콘솔에서 예제 실행
        runConsoleExamples();

        log.info("================================================================================");
        log.info("🎉 Data JPA 예제 완료");
        log.info("📍 웹 API 테스트: http://localhost:8080/api/products");
        log.info("📍 H2 Console: http://localhost:8080/h2-console");
        log.info("   JDBC URL: jdbc:h2:mem:testdb");
        log.info("   Username: sa");
        log.info("   Password: (비워둠)");
    }

    /**
     * 초기 데이터 삽입
     */
    private void insertInitialData() {
        log.info("💾 초기 데이터 삽입 시작");

        // 상품 데이터 삽입
        insertInitialProducts();

        // 사용자 데이터 삽입
        insertInitialUsers();

        log.info("✅ 초기 데이터 삽입 완료");
    }

    /**
     * 초기 상품 데이터 삽입
     */
    private void insertInitialProducts() {
        log.info("🛍️ 상품 데이터 삽입 중...");

        // Electronics 카테고리
        productJpaService.createProduct("iPhone 15", 1200000.0, "최신 스마트폰", "Electronics");
        productJpaService.createProduct("Samsung Galaxy S24", 1100000.0, "안드로이드 플래그십", "Electronics");
        productJpaService.createProduct("MacBook Pro", 2500000.0, "애플 노트북", "Electronics");
        productJpaService.createProduct("Dell XPS 13", 1800000.0, "윈도우 울트라북", "Electronics");

        // Books 카테고리
        productJpaService.createProduct("Spring Boot 완벽 가이드", 35000.0, "스프링 부트 학습서", "Books");
        productJpaService.createProduct("JPA 프로그래밍", 40000.0, "JPA 심화 학습", "Books");
        productJpaService.createProduct("클린 코드", 30000.0, "로버트 마틴의 명저", "Books");

        // Clothing 카테고리
        productJpaService.createProduct("나이키 에어맥스", 150000.0, "운동화", "Clothing");
        productJpaService.createProduct("유니클로 셔츠", 25000.0, "기본 셔츠", "Clothing");
        productJpaService.createProduct("아디다스 후드티", 80000.0, "캐주얼 후드티", "Clothing");

        // Home 카테고리
        Product sofa = new Product("3인용 소파", 500000.0, "편안한 소파", "Home");
        sofa.setInStock(false); // 품절 상품 예제
        productJpaService.save(sofa);

        productJpaService.createProduct("다이닝 테이블", 300000.0, "4인용 테이블", "Home");

        log.info("📦 총 {}개의 상품이 생성되었습니다", productJpaService.count());
    }

    /**
     * 초기 사용자 데이터 삽입
     */
    private void insertInitialUsers() {
        log.info("👥 사용자 데이터 삽입 중...");

        try {
            userService.createUser("john_doe", "john@example.com", "John Doe", 28);
            userService.createUser("jane_smith", "jane@example.com", "Jane Smith", 32);
            userService.createUser("bob_wilson", "bob@example.com", "Bob Wilson", 45);
            userService.createUser("alice_brown", "alice@example.com", "Alice Brown", 23);
            userService.createUser("charlie_davis", "charlie@example.com", "Charlie Davis", 35);

            // 비활성 사용자 예제
            User inactiveUser = userService.createUser("inactive_user", "inactive@example.com", "Inactive User", 40);
            userService.deactivateUser(inactiveUser.getId());

            log.info("👤 총 {}명의 사용자가 생성되었습니다", userService.count());
        } catch (Exception e) {
            log.warn("⚠️ 사용자 데이터 삽입 중 오류 발생 (이미 존재할 수 있음): {}", e.getMessage());
        }
    }

    /**
     * 콘솔에서 예제 실행
     */
    private void runConsoleExamples() {
        log.info("🖥️ 콘솔 예제 실행 시작");

        // EntityManager 예제
        runEntityManagerExample();

        // Spring Data JPA 예제
        runSpringDataJpaExample();

        // 다양한 조회 예제
        runQueryExamples();
    }

    /**
     * EntityManager 예제 실행
     */
    private void runEntityManagerExample() {
        log.info("🔧 EntityManager CRUD 예제 실행");
        try {
            productEntityManagerService.entityManagerCRUDExample();
        } catch (Exception e) {
            log.error("❌ EntityManager 예제 실행 중 오류: {}", e.getMessage());
        }
    }

    /**
     * Spring Data JPA 예제 실행
     */
    private void runSpringDataJpaExample() {
        log.info("🌱 Spring Data JPA CRUD 예제 실행");
        try {
            productJpaService.jpaRepositoryCRUDExample();
        } catch (Exception e) {
            log.error("❌ Spring Data JPA 예제 실행 중 오류: {}", e.getMessage());
        }
    }

    /**
     * 다양한 조회 예제 실행
     */
    private void runQueryExamples() {
        log.info("🔍 다양한 JPA 쿼리 예제 실행");

        // 상품 조회 예제
        log.info("📱 Electronics 카테고리 상품: {}개", 
            productJpaService.findByCategory("Electronics").size());
        
        log.info("💰 100만원 이하 상품: {}개", 
            productJpaService.findByPriceLessThanEqual(1000000.0).size());
        
        log.info("📚 이름에 'Spring'이 포함된 상품: {}개", 
            productJpaService.searchByName("Spring").size());

        log.info("📦 재고 있는 상품: {}개", 
            productJpaService.findInStockProducts().size());

        // 사용자 조회 예제
        log.info("👤 활성 사용자: {}명", userService.findActiveUsers().size());
        
        log.info("🎂 25-35세 사용자: {}명", 
            userService.findByAgeRange(25, 35).size());

        log.info("🔍 이름에 'john'이 포함된 사용자: {}명", 
            userService.searchByUsername("john").size());
    }
} 