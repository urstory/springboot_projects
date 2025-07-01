package com.example.datajdbc.runner;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.datajdbc.entity.Product;
import com.example.datajdbc.entity.User;
import com.example.datajdbc.service.ProductService;
import com.example.datajdbc.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataJdbcExampleRunner implements CommandLineRunner {
    
    private final ProductService productService;
    private final UserService userService;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("================================================================================");
        log.info("🚀 Spring Data JDBC 예제 시작");
        log.info("================================================================================");
        
        // 초기 데이터 생성
        initializeData();
        
        // 예제 실행
        runExamples();
        
        log.info("================================================================================");
        log.info("✅ Spring Data JDBC 예제 완료");
        log.info("🌐 웹 API 테스트: http://localhost:8080/api/products");
        log.info("🌐 사용자 API: http://localhost:8080/api/users");
        log.info("🔧 H2 Console: http://localhost:8080/h2-console");
        log.info("   JDBC URL: jdbc:h2:mem:testdb");
        log.info("   Username: sa");
        log.info("   Password: (비워둠)");
        log.info("================================================================================");
    }
    
    private void initializeData() {
        log.info("📊 초기 데이터 생성 중...");
        
        // 상품 데이터 생성
        List<Product> products = List.of(
            new Product("노트북", 1200.0, "고성능 노트북", "Electronics", true),
            new Product("마우스", 25.0, "무선 마우스", "Electronics", true),
            new Product("키보드", 80.0, "기계식 키보드", "Electronics", true),
            new Product("모니터", 300.0, "27인치 모니터", "Electronics", true),
            new Product("Java 완전정복", 45.0, "Java 프로그래밍 서적", "Books", true),
            new Product("Spring Boot 가이드", 55.0, "Spring Boot 학습서", "Books", true),
            new Product("알고리즘 도감", 35.0, "알고리즘 학습서", "Books", false),
            new Product("티셔츠", 20.0, "면 티셔츠", "Clothing", true),
            new Product("청바지", 60.0, "데님 청바지", "Clothing", true),
            new Product("운동화", 120.0, "러닝화", "Clothing", true),
            new Product("커피머신", 200.0, "자동 커피머신", "Home", true),
            new Product("청소기", 150.0, "무선 청소기", "Home", false)
        );
        
        products.forEach(productService::createProduct);
        log.info("✅ 상품 {}개 추가 완료", products.size());
        
        // 사용자 데이터 생성
        List<User> users = List.of(
            new User("john_doe", "john@example.com", "John Doe", 28, true),
            new User("jane_smith", "jane@example.com", "Jane Smith", 32, true),
            new User("bob_johnson", "bob@example.com", "Bob Johnson", 25, true),
            new User("alice_brown", "alice@example.com", "Alice Brown", 29, true),
            new User("charlie_davis", "charlie@example.com", "Charlie Davis", 35, true),
            new User("diana_wilson", "diana@example.com", "Diana Wilson", 27, false)
        );
        
        users.forEach(userService::createUser);
        log.info("✅ 사용자 {}명 추가 완료", users.size());
    }
    
    private void runExamples() {
        log.info("🔍 Spring Data JDBC 쿼리 예제 실행");
        
        // 상품 조회 예제
        List<Product> allProducts = productService.getAllProducts();
        log.info("📦 전체 상품: {}개", allProducts.size());
        
        List<Product> electronicsProducts = productService.getProductsByCategory("Electronics");
        log.info("💻 Electronics 카테고리 상품: {}개", electronicsProducts.size());
        
        List<Product> affordableProducts = productService.getProductsByPriceRange(0.0, 100.0);
        log.info("💰 100달러 이하 상품: {}개", affordableProducts.size());
        
        List<Product> inStockProducts = productService.getInStockProducts();
        log.info("📋 재고 있는 상품: {}개", inStockProducts.size());
        
        List<Product> searchResults = productService.searchProductsByName("Java");
        log.info("🔍 'Java' 검색 결과: {}개", searchResults.size());
        
        long expensiveCount = productService.countProductsAbovePrice(100.0);
        log.info("💵 100달러 이상 상품: {}개", expensiveCount);
        
        // 사용자 조회 예제
        List<User> allUsers = userService.getAllUsers();
        log.info("👥 전체 사용자: {}명", allUsers.size());
        
        List<User> activeUsers = userService.getActiveUsers();
        log.info("✅ 활성 사용자: {}명", activeUsers.size());
        
        List<User> youngUsers = userService.getUsersByAgeRange(20, 30);
        log.info("🧑 20-30대 사용자: {}명", youngUsers.size());
        
        List<User> adultActiveUsers = userService.getActiveUsersByMinAge(25);
        log.info("👨‍💼 25세 이상 활성 사용자: {}명", adultActiveUsers.size());
        
        LocalDateTime lastWeek = LocalDateTime.now().minusDays(7);
        List<User> recentUsers = userService.getRecentUsers(lastWeek);
        log.info("🆕 최근 가입 사용자: {}명", recentUsers.size());
        
        // Spring Data JDBC CRUD 예제 실행
        productService.jdbcCrudExample();
    }
} 