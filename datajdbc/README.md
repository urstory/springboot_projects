# Spring Data JDBC 실습 프로젝트

## 🎯 프로젝트 개요

이 프로젝트는 **Spring Data JDBC**의 핵심 기능을 학습하고 실습하기 위한 예제 프로젝트입니다. 
Spring Data JDBC는 JPA보다 간단하고 명시적인 방식으로 데이터베이스와 상호작용할 수 있는 
경량 ORM 프레임워크입니다.

## 📚 학습 목표

- Spring Data JDBC의 기본 개념 이해
- Entity와 Repository 패턴 실습
- 다양한 쿼리 메서드 활용
- H2 임베딩 데이터베이스 활용
- RESTful API 구현 및 테스트
- Spring Data JDBC와 JPA의 차이점 이해

## 🛠 기술 스택

- **Java**: 21
- **Spring Boot**: 3.5.3
- **Spring Data JDBC**: 최신 버전
- **H2 Database**: 임베딩 모드
- **Lombok**: 코드 간소화
- **Gradle**: 빌드 도구

## 📁 프로젝트 구조

```
src/
├── main/
│   ├── java/com/example/datajdbc/
│   │   ├── entity/          # 엔티티 클래스
│   │   │   ├── Product.java
│   │   │   └── User.java
│   │   ├── repository/      # Repository 인터페이스
│   │   │   ├── ProductRepository.java
│   │   │   └── UserRepository.java
│   │   ├── service/         # 서비스 클래스
│   │   │   ├── ProductService.java
│   │   │   └── UserService.java
│   │   ├── controller/      # REST 컨트롤러
│   │   │   ├── ProductController.java
│   │   │   └── UserController.java
│   │   ├── runner/          # 초기 데이터 설정
│   │   │   └── DataJdbcExampleRunner.java
│   │   └── DatajdbcApplication.java
│   └── resources/
│       ├── application.yml  # 애플리케이션 설정
│       └── schema.sql      # 데이터베이스 스키마
└── test/
    └── http/
        └── datajdbc-test.http  # API 테스트 파일
```

## 🚀 실행 방법

### 1. 애플리케이션 빌드 및 실행

```bash
# 프로젝트 디렉토리로 이동
cd projects/datajdbc

# Gradle을 사용한 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun
```

### 2. JAR 파일로 실행

```bash
# JAR 파일 생성
./gradlew bootJar

# JAR 파일 실행
java -jar build/libs/datajdbc-0.0.1-SNAPSHOT.jar
```

## 🌐 API 엔드포인트

### 상품 API

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/api/products` | 모든 상품 조회 |
| GET | `/api/products/{id}` | ID로 상품 조회 |
| POST | `/api/products` | 새 상품 생성 |
| PUT | `/api/products/{id}` | 상품 업데이트 |
| DELETE | `/api/products/{id}` | 상품 삭제 |
| GET | `/api/products/category/{category}` | 카테고리별 상품 조회 |
| GET | `/api/products/search?keyword={keyword}` | 키워드로 상품 검색 |
| GET | `/api/products/price-range?minPrice={min}&maxPrice={max}` | 가격 범위로 조회 |
| GET | `/api/products/in-stock` | 재고 있는 상품 조회 |
| GET | `/api/products/sorted/price/asc` | 가격 오름차순 정렬 |
| GET | `/api/products/sorted/price/desc` | 가격 내림차순 정렬 |
| GET | `/api/products/statistics` | 상품 통계 정보 |
| POST | `/api/products/jdbc-example` | JDBC CRUD 예제 실행 |

### 사용자 API

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/api/users` | 모든 사용자 조회 |
| GET | `/api/users/{id}` | ID로 사용자 조회 |
| POST | `/api/users` | 새 사용자 생성 |
| PUT | `/api/users/{id}` | 사용자 업데이트 |
| DELETE | `/api/users/{id}` | 사용자 삭제 |
| PATCH | `/api/users/{id}/deactivate` | 사용자 비활성화 |
| GET | `/api/users/username/{username}` | 사용자명으로 조회 |
| GET | `/api/users/email/{email}` | 이메일로 조회 |
| GET | `/api/users/active` | 활성 사용자 조회 |
| GET | `/api/users/inactive` | 비활성 사용자 조회 |
| GET | `/api/users/age-range?minAge={min}&maxAge={max}` | 나이 범위로 조회 |
| GET | `/api/users/search?keyword={keyword}` | 사용자명 검색 |
| GET | `/api/users/recent?days={days}` | 최근 가입 사용자 |
| GET | `/api/users/statistics` | 사용자 통계 정보 |

## 🏗 Spring Data JDBC vs JPA 비교

| 특징 | Spring Data JDBC | JPA |
|------|------------------|-----|
| **복잡성** | 단순하고 명시적 | 복잡하지만 기능이 풍부 |
| **상태 관리** | 명시적 save() 호출 필요 | 자동 상태 추적 |
| **성능** | 낮은 오버헤드 | 영속성 컨텍스트 오버헤드 |
| **학습 곡선** | 낮음 | 높음 |
| **연관 관계** | 단순한 관계만 지원 | 복잡한 연관 관계 지원 |
| **캐싱** | 없음 | 1차/2차 캐시 지원 |
| **Lazy Loading** | 미지원 | 지원 |

## 📊 엔티티 설계

### Product (상품)
```java
@Table("products")
public class Product {
    @Id private Long id;
    private String name;
    private Double price;
    private String description;
    private String category;
    private Boolean inStock;
}
```

### User (사용자)
```java
@Table("users")
public class User {
    @Id private Long id;
    private String username;
    private String email;
    private String fullName;
    private Integer age;
    private LocalDateTime createdAt;
    private Boolean active;
}
```

## 🔍 쿼리 메서드 예제

### 메서드 명명 규칙
```java
// 간단한 조회
List<Product> findByName(String name);
List<Product> findByCategory(String category);

// 조건부 조회
List<Product> findByPriceLessThanEqual(Double maxPrice);
List<Product> findByInStockTrue();
List<User> findByActiveTrue();

// 범위 조회
List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
List<User> findByAgeBetween(Integer minAge, Integer maxAge);

// 문자열 검색
List<Product> findByNameContainingIgnoreCase(String keyword);
List<User> findByUsernameContainingIgnoreCase(String keyword);

// 정렬
List<Product> findAllByOrderByPriceAsc();
List<Product> findAllByOrderByPriceDesc();

// 사용자 정의 쿼리
@Query("SELECT COUNT(*) FROM products WHERE price > :price")
long countByPriceGreaterThan(@Param("price") Double price);
```

## 🗄 H2 Database Console

애플리케이션 실행 후 브라우저에서 H2 Console에 접속할 수 있습니다:

- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (비워둠)

### 유용한 SQL 쿼리
```sql
-- 전체 데이터 조회
SELECT * FROM products;
SELECT * FROM users;

-- 카테고리별 상품 개수
SELECT category, COUNT(*) as count FROM products GROUP BY category;

-- 활성/비활성 사용자 통계
SELECT active, COUNT(*) as count FROM users GROUP BY active;

-- 가장 비싼 상품 TOP 5
SELECT * FROM products ORDER BY price DESC LIMIT 5;
```

## 🧪 테스트 실행

### HTTP 파일을 사용한 API 테스트
1. `src/test/http/datajdbc-test.http` 파일 열기
2. IntelliJ IDEA 또는 VS Code에서 각 테스트 실행
3. 38가지 다양한 테스트 케이스 제공

### 주요 테스트 시나리오
- CRUD 기본 동작 테스트
- 다양한 조회 조건 테스트
- 정렬 및 통계 기능 테스트
- 오류 상황 처리 테스트
- 중복 데이터 처리 테스트

## 📈 초기 데이터

애플리케이션 시작 시 자동으로 다음 데이터가 생성됩니다:

### 상품 데이터 (12개)
- **Electronics**: 노트북, 마우스, 키보드, 모니터
- **Books**: Java 완전정복, Spring Boot 가이드, 알고리즘 도감
- **Clothing**: 티셔츠, 청바지, 운동화
- **Home**: 커피머신, 청소기

### 사용자 데이터 (6명)
- 활성 사용자 5명, 비활성 사용자 1명
- 다양한 연령대 (25~35세)

## 🔧 주요 특징

### 1. 명시적 상태 관리
```java
// Spring Data JDBC에서는 명시적으로 save() 호출 필요
Product product = productRepository.findById(1L).orElse(null);
product.setPrice(newPrice);
productRepository.save(product); // 반드시 호출해야 함
```

### 2. 단순한 Repository 인터페이스
```java
public interface ProductRepository extends CrudRepository<Product, Long> {
    // 메서드 명명 규칙으로 자동 구현
    List<Product> findByCategory(String category);
    
    // 사용자 정의 쿼리
    @Query("SELECT * FROM products WHERE price > :price")
    List<Product> findExpensiveProducts(@Param("price") Double price);
}
```

### 3. 경량 ORM의 장점
- 빠른 실행 속도
- 명확한 데이터 흐름
- 단순한 디버깅

## 🎓 실습 과제

### 기초 과제
1. 새로운 카테고리의 상품 5개 추가하기
2. 특정 나이대의 사용자 10명 추가하기
3. 상품명에 특정 키워드가 포함된 상품 검색하기

### 심화 과제
1. 월별 가입 사용자 통계 API 구현
2. 카테고리별 재고 현황 API 구현
3. 사용자별 선호 카테고리 분석 기능 구현
4. 상품 리뷰 엔티티 추가 및 연관 관계 설정

### 도전 과제
1. Spring Data JDBC를 JPA로 마이그레이션
2. 복잡한 집계 쿼리 구현
3. 페이징 및 정렬 기능 추가
4. 캐싱 전략 구현

## 📖 참고 자료

- [Spring Data JDBC 공식 문서](https://spring.io/projects/spring-data-jdbc)
- [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [H2 Database 문서](https://www.h2database.com/html/main.html)
- [Lombok 사용 가이드](https://projectlombok.org/features/all)

## 🐛 문제 해결

### 일반적인 문제들

1. **포트 충돌**: 8080 포트가 사용 중인 경우
   - `application.yml`에서 `server.port` 변경

2. **H2 Console 접속 불가**
   - JDBC URL 확인: `jdbc:h2:mem:testdb`
   - 사용자명: `sa`, 비밀번호: 비워둠

3. **API 응답 없음**
   - 애플리케이션 정상 시작 확인
   - 로그에서 오류 메시지 확인

## 👥 기여 방법

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

---

**Happy Coding! 🎉**

이 프로젝트를 통해 Spring Data JDBC의 간결함과 효율성을 체험해보세요! 