# 종합 실습 프로젝트 (Total Exam)

Spring Boot + JWT 인증 + 게시판 CRUD API를 구현한 종합 실습 프로젝트입니다.

## 📋 프로젝트 개요

이 프로젝트는 실제 웹 애플리케이션 개발에 필요한 핵심 기능들을 종합적으로 구현한 학습용 프로젝트입니다.

### 🎯 학습 목표

- JWT 기반 사용자 인증 및 인가 구현
- RESTful API 설계 및 구현
- Spring Security를 활용한 보안 구현
- JPA를 활용한 데이터베이스 연동
- 예외 처리 및 유효성 검증
- API 문서화 및 테스트

## 🛠 기술 스택

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Security 6.x**
- **Spring Data JPA**
- **JWT (JSON Web Token)**
- **H2 Database** (임베딩 데이터베이스)
- **Lombok** (코드 간소화)
- **Gradle** (빌드 도구)

## 🏗 프로젝트 구조

```
src/
├── main/
│   ├── java/com/example/totalexam/
│   │   ├── config/              # 설정 클래스
│   │   │   └── SecurityConfig.java
│   │   ├── controller/          # REST API 컨트롤러
│   │   │   ├── AuthController.java
│   │   │   └── PostController.java
│   │   ├── dto/                 # 데이터 전송 객체
│   │   │   ├── LoginRequest.java
│   │   │   ├── JwtResponse.java
│   │   │   ├── PostRequest.java
│   │   │   └── PostResponse.java
│   │   ├── entity/              # JPA 엔티티
│   │   │   ├── User.java
│   │   │   └── Post.java
│   │   ├── exception/           # 예외 처리
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── repository/          # 데이터 접근 계층
│   │   │   ├── UserRepository.java
│   │   │   └── PostRepository.java
│   │   ├── security/            # 보안 관련
│   │   │   ├── JwtTokenProvider.java
│   │   │   └── JwtAuthenticationFilter.java
│   │   ├── service/             # 비즈니스 로직
│   │   │   ├── AuthService.java
│   │   │   ├── CustomUserDetailsService.java
│   │   │   └── PostService.java
│   │   └── TotalexamApplication.java
│   └── resources/
│       ├── application.yml      # 애플리케이션 설정
│       └── data.sql            # 초기 데이터
└── test/
    └── http/
        └── totalexam-test.http  # API 테스트 파일
```

## 🚀 실행 방법

### 1. 프로젝트 클론 및 빌드

```bash
# 프로젝트 디렉토리로 이동
cd projects/totalexam

# 프로젝트 빌드
./gradlew clean build

# 애플리케이션 실행
./gradlew bootRun
```

### 2. JAR 파일로 실행

```bash
# JAR 파일 빌드
./gradlew clean bootJar

# JAR 파일 실행
java -jar build/libs/totalexam-0.0.1-SNAPSHOT.jar
```

### 3. 접속 URL

- **애플리케이션**: http://localhost:8082
- **H2 Console**: http://localhost:8082/h2-console
- **Actuator Health**: http://localhost:8082/actuator/health

### 4. H2 데이터베이스 접속 정보

- **JDBC URL**: `jdbc:h2:mem:totalexam`
- **Username**: `sa`
- **Password**: (비워둠)

## 📡 API 엔드포인트

### 🔐 인증 API

| 메서드 | URL | 설명 | 인증 필요 |
|--------|-----|------|----------|
| POST | `/api/auth/login` | 사용자 로그인 (JWT 토큰 발급) | ❌ |
| GET | `/api/auth/validate` | JWT 토큰 검증 | ✅ |

### 📝 게시판 API

| 메서드 | URL | 설명 | 인증 필요 |
|--------|-----|------|----------|
| GET | `/api/posts` | 게시글 목록 조회 | ❌ |
| GET | `/api/posts/{id}` | 게시글 상세 조회 | ❌ |
| POST | `/api/posts` | 게시글 생성 | ✅ |
| PUT | `/api/posts/{id}` | 게시글 수정 | ✅ |
| DELETE | `/api/posts/{id}` | 게시글 삭제 | ✅ |
| GET | `/api/posts/search?keyword={keyword}` | 게시글 검색 | ❌ |
| GET | `/api/posts/user/{username}` | 사용자별 게시글 조회 | ❌ |

## 🔑 인증 및 권한

### JWT 토큰 사용법

1. **로그인**: `/api/auth/login`에 사용자명과 비밀번호 전송
2. **토큰 획득**: 응답에서 JWT 토큰 추출
3. **API 호출**: 인증이 필요한 API 호출 시 헤더에 토큰 포함

```bash
Authorization: Bearer <JWT_TOKEN>
```

### 테스트 사용자 계정

| 사용자명 | 비밀번호 | 역할 | 설명 |
|----------|----------|------|------|
| user1 | password123 | ROLE_USER | 일반 사용자 1 |
| user2 | password123 | ROLE_USER | 일반 사용자 2 |
| admin | password123 | ROLE_ADMIN | 관리자 |
| john | password123 | ROLE_USER | 일반 사용자 3 |
| jane | password123 | ROLE_USER | 일반 사용자 4 |

## 📋 API 사용 예제

### 1. 로그인 예제

```bash
curl -X POST http://localhost:8082/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "password": "password123"
  }'
```

**응답 예시:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "username": "user1",
  "email": "user1@example.com",
  "fullName": "사용자1"
}
```

### 2. 게시글 생성 예제

```bash
curl -X POST http://localhost:8082/api/posts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "title": "새로운 게시글",
    "content": "게시글 내용입니다."
  }'
```

### 3. 게시글 목록 조회 예제

```bash
curl -X GET http://localhost:8082/api/posts
```

## 🧪 테스트 방법

### 1. HTTP 파일을 이용한 테스트

IntelliJ IDEA 또는 VS Code에서 `src/test/http/totalexam-test.http` 파일을 실행하여 API를 테스트할 수 있습니다.

### 2. Postman을 이용한 테스트

Postman을 사용하여 각 API 엔드포인트를 테스트할 수 있습니다.

### 3. curl을 이용한 테스트

명령줄에서 curl 명령어를 사용하여 API를 테스트할 수 있습니다.

## 🔧 주요 기능

### 1. JWT 기반 인증

- 사용자 로그인 시 JWT 토큰 발급
- 토큰을 통한 사용자 인증 및 인가
- 토큰 만료 시간 관리 (24시간)

### 2. 게시판 CRUD

- 게시글 생성, 조회, 수정, 삭제
- 작성자 권한 검증
- 게시글 검색 기능
- 페이징 처리

### 3. 보안 기능

- Spring Security를 활용한 인증/인가
- 비밀번호 BCrypt 암호화
- CORS 설정
- 예외 처리 및 에러 응답

### 4. 데이터베이스

- H2 인메모리 데이터베이스 사용
- JPA를 활용한 데이터 접근
- 초기 데이터 자동 삽입

## 📝 개발 노트

### 보안 고려사항

1. **JWT 토큰 보안**
   - 토큰은 HTTPS를 통해서만 전송
   - 토큰에 민감한 정보 포함 금지
   - 적절한 만료 시간 설정

2. **비밀번호 보안**
   - BCrypt 알고리즘 사용
   - 솔트(Salt) 자동 생성

3. **SQL 인젝션 방지**
   - JPA Named Parameter 사용
   - 입력값 유효성 검증

### 성능 최적화

1. **데이터베이스 최적화**
   - 지연 로딩(Lazy Loading) 적용
   - 페이징 처리로 대량 데이터 조회 최적화

2. **캐싱**
   - JWT 토큰 검증 결과 캐싱 가능
   - 자주 조회되는 데이터 캐싱 적용 가능

## 🤝 확장 가능성

이 프로젝트는 다음과 같이 확장할 수 있습니다:

1. **기능 확장**
   - 댓글 시스템 추가
   - 파일 업로드 기능
   - 이메일 인증 시스템
   - OAuth2 소셜 로그인

2. **기술 스택 확장**
   - Redis 캐싱 시스템
   - MySQL/PostgreSQL 데이터베이스
   - Docker 컨테이너화
   - Kubernetes 배포

3. **모니터링 및 로깅**
   - Micrometer + Prometheus
   - ELK Stack (Elasticsearch, Logstash, Kibana)
   - APM 도구 연동

## 📚 참고 자료

- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [Spring Security 공식 문서](https://spring.io/projects/spring-security)
- [JWT 공식 사이트](https://jwt.io/)
- [Spring Data JPA 공식 문서](https://spring.io/projects/spring-data-jpa)

## 📄 라이선스

이 프로젝트는 학습 목적으로 제작되었습니다. 