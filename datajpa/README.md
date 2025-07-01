# Data JPA 예제 프로젝트

**내용.md 7장 - JPA와 Spring Data JDBC 예제**

## 📋 프로젝트 개요

이 프로젝트는 Spring Boot와 Spring Data JPA를 사용하여 JPA의 핵심 기능들을 학습할 수 있는 종합적인 예제입니다. EntityManager와 Spring Data JPA Repository를 모두 활용하여 CRUD 작업과 다양한 쿼리 메서드를 실습할 수 있습니다.

## 🎯 학습 목표

- **JPA 개념**: 엔티티(Entity)와 Repository의 개념 이해
- **EntityManager**: JPA의 핵심 API를 사용한 직접적인 데이터 조작
- **Spring Data JPA**: 편리한 Repository 인터페이스 활용
- **H2 Database**: 임베딩 메모리 데이터베이스 사용
- **CRUD 작업**: 생성, 조회, 수정, 삭제 기능 구현
- **다양한 쿼리**: 메서드 이름 기반 쿼리, JPQL, 네이티브 쿼리

## 🛠️ 기술 스택

- **Spring Boot 3.5.3**
- **Java 21**
- **Spring Data JPA**
- **H2 Database** (임베딩 모드)
- **Lombok**
- **Gradle**

## 📁 프로젝트 구조

```
src/
├── main/
│   ├── java/com/example/datajpa/
│   │   ├── entity/                 # 엔티티 클래스
│   │   │   ├── Product.java       # 상품 엔티티
│   │   │   └── User.java          # 사용자 엔티티
│   │   ├── repository/             # Repository 인터페이스
│   │   │   ├── ProductRepository.java
│   │   │   └── UserRepository.java
│   │   ├── service/                # 서비스 클래스
│   │   │   ├── ProductEntityManagerService.java  # EntityManager 사용
│   │   │   ├── ProductJpaService.java            # Spring Data JPA 사용
│   │   │   └── UserService.java
│   │   ├── controller/             # REST API 컨트롤러
│   │   │   ├── ProductController.java
│   │   │   └── UserController.java
│   │   ├── runner/                 # 초기 데이터 및 예제 실행
│   │   │   └── DataJpaExampleRunner.java
│   │   └── DatajpaApplication.java # 메인 클래스
│   └── resources/
│       └── application.yml         # 설정 파일
└── test/
    └── http/
        └── datajpa-test.http      # HTTP API 테스트
```

## 🚀 실행 방법

### 1. 프로젝트 빌드

```bash
./gradlew clean build
```

### 2. 애플리케이션 실행

```bash
./gradlew bootRun
```

또는

```bash
java -jar build/libs/datajpa-0.0.1-SNAPSHOT.jar
```

### 3. 웹 브라우저에서 확인

- **애플리케이션**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (비워둠)

## 📊 초기 데이터

애플리케이션 시작 시 다음과 같은 테스트 데이터가 자동으로 생성됩니다:

### 상품 데이터 (12개)
- **Electronics**: iPhone 15, Samsung Galaxy S24, MacBook Pro, Dell XPS 13
- **Books**: Spring Boot 완벽 가이드, JPA 프로그래밍, 클린 코드
- **Clothing**: 나이키 에어맥스, 유니클로 셔츠, 아디다스 후드티
- **Home**: 3인용 소파 (품절), 다이닝 테이블

### 사용자 데이터 (6명)
- john_doe (John Doe, 28세)
- jane_smith (Jane Smith, 32세)
- bob_wilson (Bob Wilson, 45세)
- alice_brown (Alice Brown, 23세)
- charlie_davis (Charlie Davis, 35세)
- inactive_user (비활성 사용자)

## 🔧 API 엔드포인트

### 상품 API (`/api/products`)

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/products` | 모든 상품 조회 |
| GET | `/api/products/{id}` | 특정 상품 조회 |
| POST | `/api/products` | 상품 생성 |
| PUT | `/api/products/{id}` | 상품 업데이트 |
| DELETE | `/api/products/{id}` | 상품 삭제 |
| GET | `/api/products/search/name/{name}` | 이름으로 상품 검색 |
| GET | `/api/products/search?keyword={keyword}` | 키워드로 상품 검색 |
| GET | `/api/products/category/{category}` | 카테고리별 상품 조회 |
| GET | `/api/products/price-range?minPrice={min}&maxPrice={max}` | 가격 범위로 조회 |
| GET | `/api/products/price-max/{maxPrice}` | 최대 가격 이하 상품 조회 |
| GET | `/api/products/in-stock` | 재고 있는 상품 조회 |
| GET | `/api/products/category/{category}/ordered` | 카테고리별 가격순 정렬 |
| GET | `/api/products/stats` | 상품 통계 정보 |
| POST | `/api/products/demo/entity-manager` | EntityManager 예제 실행 |
| POST | `/api/products/demo/spring-data-jpa` | Spring Data JPA 예제 실행 |

### 사용자 API (`/api/users`)

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/users` | 모든 사용자 조회 |
| GET | `/api/users/{id}` | 특정 사용자 조회 |
| GET | `/api/users/username/{username}` | 사용자명으로 조회 |
| GET | `/api/users/email/{email}` | 이메일로 조회 |
| POST | `/api/users` | 사용자 생성 |
| PUT | `/api/users/{id}` | 사용자 업데이트 |
| PATCH | `/api/users/{id}/deactivate` | 사용자 비활성화 |
| DELETE | `/api/users/{id}` | 사용자 삭제 |
| GET | `/api/users/active` | 활성 사용자 조회 |
| GET | `/api/users/age-range?minAge={min}&maxAge={max}` | 나이 범위로 조회 |
| GET | `/api/users/recent?days={days}` | 최근 가입 사용자 조회 |
| GET | `/api/users/search?keyword={keyword}` | 사용자명 검색 |
| GET | `/api/users/ordered-by-age` | 나이순 정렬 조회 |
| GET | `/api/users/active/min-age/{minAge}` | 활성 사용자 중 특정 나이 이상 |
| GET | `/api/users/stats` | 사용자 통계 정보 |

## 🧪 테스트 방법

### 1. HTTP 파일 사용
`src/test/http/datajpa-test.http` 파일에 36가지 테스트 케이스가 준비되어 있습니다.

### 2. Curl 예제

```bash
# 모든 상품 조회
curl -X GET http://localhost:8080/api/products

# 상품 생성
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"테스트 상품","price":50000,"description":"테스트","category":"Test"}'

# 카테고리별 조회
curl -X GET http://localhost:8080/api/products/category/Electronics

# 사용자 생성
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","fullName":"Test User","age":25}'
```

## 📚 주요 학습 포인트

### 1. **Entity 설계**
- `@Entity`, `@Table`, `@Id`, `@GeneratedValue` 사용
- `@Column` 속성 설정 (nullable, unique 등)
- `@PrePersist` 생명주기 콜백

### 2. **Repository 패턴**
- `JpaRepository<T, ID>` 상속
- 메서드 이름 기반 쿼리 자동 생성
- `@Query` 어노테이션으로 커스텀 JPQL
- 네이티브 쿼리 사용

### 3. **EntityManager vs Spring Data JPA**

| 기능 | EntityManager | Spring Data JPA |
|------|---------------|-----------------|
| **CRUD 메서드** | 직접 구현 필요 | 자동 제공 |
| **트랜잭션 관리** | 수동 관리 | 자동 관리 |
| **사용 편의성** | 낮음 (세밀한 제어) | 높음 (자동화) |
| **복잡한 쿼리** | JPQL 직접 작성 | 메서드명 기반 + JPQL |

### 4. **쿼리 메서드 예제**

```java
// 메서드 이름 기반 쿼리
List<Product> findByName(String name);
List<Product> findByPriceLessThanEqual(Double price);
List<Product> findByNameContainingIgnoreCase(String keyword);

// 커스텀 JPQL
@Query("SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max")
List<Product> findProductsInRange(@Param("min") Double min, @Param("max") Double max);

// 네이티브 쿼리
@Query(value = "SELECT * FROM products WHERE category = ?1", nativeQuery = true)
List<Product> findAvailableProductsByCategory(String category);
```

### 5. **트랜잭션 관리**
- `@Transactional` 어노테이션 사용
- `readOnly = true` 최적화
- 서비스 레이어에서 트랜잭션 경계 설정

## 🔍 H2 데이터베이스 확인

1. 애플리케이션 실행 후 http://localhost:8080/h2-console 접속
2. 연결 정보 입력:
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (비워둠)
3. Connect 클릭
4. 테이블 확인: `PRODUCTS`, `USERS`

### 주요 SQL 쿼리 예제

```sql
-- 모든 상품 조회
SELECT * FROM PRODUCTS;

-- 카테고리별 상품 수
SELECT CATEGORY, COUNT(*) FROM PRODUCTS GROUP BY CATEGORY;

-- 가격 범위별 상품
SELECT * FROM PRODUCTS WHERE PRICE BETWEEN 30000 AND 100000;

-- 활성 사용자 조회
SELECT * FROM USERS WHERE ACTIVE = TRUE;

-- 나이대별 사용자
SELECT * FROM USERS WHERE AGE BETWEEN 25 AND 35;
```

## 🎯 실습 과제

### 기본 과제
1. **새로운 카테고리** "Sports" 상품 3개 추가
2. **가격 업데이트** Electronics 카테고리 상품 10% 할인
3. **신규 사용자** 5명 추가 등록
4. **통계 조회** 카테고리별 평균 가격 조회

### 심화 과제
1. **새로운 엔티티** Order 엔티티 추가 (User-Product 연관관계)
2. **복잡한 쿼리** 사용자별 주문 통계 조회
3. **페이징 처리** Pageable을 활용한 상품 목록
4. **검색 기능** 다중 조건 검색 (이름 + 카테고리 + 가격범위)

## 🔧 설정 파일 상세

### application.yml 주요 설정

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb    # 메모리 DB
    username: sa
    password: 
  
  jpa:
    hibernate:
      ddl-auto: create-drop    # 시작시 테이블 생성, 종료시 삭제
    show-sql: true            # SQL 로깅
    format-sql: true          # SQL 포맷팅
  
  h2:
    console:
      enabled: true           # H2 콘솔 활성화
```

## 📖 참고 자료

- [Spring Data JPA 공식 문서](https://spring.io/projects/spring-data-jpa)
- [JPA 공식 명세](https://jakarta.ee/specifications/persistence/)
- [H2 Database 공식 사이트](https://www.h2database.com/)
- [Spring Boot 공식 가이드](https://spring.io/guides/gs/accessing-data-jpa/)

## 🎉 마무리

이 프로젝트를 통해 JPA의 핵심 개념부터 실제 웹 애플리케이션에서의 활용까지 종합적으로 학습할 수 있습니다. EntityManager와 Spring Data JPA를 모두 경험하여 각각의 장단점을 이해하고, 실무에서 적절한 선택을 할 수 있는 능력을 기를 수 있습니다. 