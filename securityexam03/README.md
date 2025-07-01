# Security Exam 03 - JWT 기반 인증 시스템

## 📋 프로젝트 개요

이 프로젝트는 **내용.md 6.4절, 6.5절, 6.6절**을 기반으로 **JWT(Json Web Token) 기반 인증 시스템**을 구현한 Spring Boot 애플리케이션입니다.

## 🎯 학습 목표

- JWT의 개념과 구조 이해
- JWT 토큰 생성 및 검증 구현
- JWT 기반 인증 필터 구현
- 토큰 블랙리스트를 통한 보안 강화
- JWT 사용 시 보안 주의사항 적용

## 🛠️ 기술 스택

- **Spring Boot**: 3.5.3
- **Spring Security**: JWT 기반 인증
- **Java**: 21
- **Gradle**: 빌드 도구
- **JWT**: io.jsonwebtoken 0.11.5
- **Redis**: 토큰 블랙리스트 관리 (선택사항)

## 📁 프로젝트 구조

```
src/main/java/com/example/securityexam03/
├── config/
│   └── SecurityConfig.java          # JWT 보안 설정
├── controller/
│   ├── AuthController.java          # JWT 인증 컨트롤러
│   ├── AdminController.java         # 관리자 전용 컨트롤러
│   └── InfoController.java          # 애플리케이션 정보
├── dto/
│   ├── LoginRequest.java            # 로그인 요청 DTO
│   └── LoginResponse.java           # 로그인 응답 DTO
├── filter/
│   └── JwtAuthenticationFilter.java # JWT 인증 필터
├── service/
│   ├── CustomUserDetailsService.java # 사용자 정보 서비스
│   └── JwtBlacklistService.java     # JWT 블랙리스트 서비스
├── util/
│   └── JwtUtil.java                 # JWT 토큰 유틸리티
└── Securityexam03Application.java   # 메인 애플리케이션
```

## 🚀 실행 방법

### 1. 애플리케이션 실행
```bash
# Windows
run.bat

# 또는 Gradle 직접 실행
gradlew bootRun
```

### 2. 애플리케이션 정보 확인
```bash
curl http://localhost:8082/api/info
```

### 3. 테스트 사용자 확인
```bash
curl http://localhost:8082/api/auth/users
```

## 🔑 테스트 사용자

| 사용자명 | 비밀번호 | 권한 |
|---------|---------|------|
| user1 | password | ROLE_USER |
| admin | password | ROLE_ADMIN, ROLE_USER |
| guest | password | ROLE_GUEST |
| jwt_user | password | ROLE_USER |
| jwt_admin | admin123 | ROLE_ADMIN, ROLE_USER |

## 🔗 API 엔드포인트

### 공개 엔드포인트
- `GET /api/info` - 애플리케이션 정보
- `GET /api/health` - 헬스 체크
- `GET /api/auth/users` - 테스트 사용자 목록
- `POST /api/auth/login` - JWT 로그인

### 인증 필요 엔드포인트
- `GET /api/auth/me` - 현재 사용자 정보
- `GET /api/auth/validate` - 토큰 유효성 검증
- `POST /api/auth/logout` - 로그아웃 (토큰 무효화)

### 관리자 전용 엔드포인트 (ROLE_ADMIN)
- `GET /api/admin/info` - 관리자 정보
- `POST /api/admin/blacklist/token` - 특정 토큰 무효화
- `POST /api/admin/blacklist/current` - 현재 토큰 무효화
- `GET /api/admin/blacklist/stats` - 블랙리스트 통계
- `DELETE /api/admin/blacklist/clear` - 블랙리스트 초기화

## 📝 JWT 토큰 사용법

### 1. 로그인하여 JWT 토큰 발급
```bash
curl -X POST http://localhost:8082/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "user1", "password": "password"}'
```

**응답 예시:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "user1",
  "roles": ["ROLE_USER"],
  "tokenType": "Bearer",
  "expiresAt": "2025-06-30T00:00:00.000+00:00",
  "message": "로그인 성공"
}
```

### 2. JWT 토큰으로 API 호출
```bash
curl -X GET http://localhost:8082/api/auth/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

### 3. 로그아웃 (토큰 무효화)
```bash
curl -X POST http://localhost:8082/api/auth/logout \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

## 🧪 테스트 방법

### IntelliJ IDEA / VS Code 사용
1. `src/test/http/jwt-test.http` 파일 열기
2. 각 HTTP 요청을 순서대로 실행
3. JWT 토큰을 복사하여 다음 요청에 사용

### Postman 사용
1. 로그인 API로 JWT 토큰 획득
2. Authorization 탭에서 Bearer Token 선택
3. 토큰 입력 후 다른 API 테스트

## 🔐 JWT 보안 특징

### 1. 토큰 구조
- **Header**: 알고리즘 정보 (HS256)
- **Payload**: 사용자 정보 및 권한
- **Signature**: 토큰 무결성 보장

### 2. 보안 기능
- ✅ 토큰 만료 시간: 1시간
- ✅ 블랙리스트 기반 토큰 무효화
- ✅ 권한 기반 접근 제어 (RBAC)
- ✅ CSRF 보호 비활성화 (JWT 특성)
- ✅ Stateless 인증

### 3. 보안 주의사항 적용
- 짧은 토큰 만료 시간 설정
- 블랙리스트를 통한 즉시 무효화
- HTTPS 전송 권장
- 민감한 정보 토큰에 미포함

## 🗃️ Redis 설정 (선택사항)

블랙리스트 기능을 위해 Redis를 사용할 수 있습니다:

### Redis 설치 및 실행
```bash
# Windows (Chocolatey)
choco install redis-64

# 또는 Docker
docker run -d -p 6379:6379 redis:alpine
```

### application.yml 설정
```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

**참고**: Redis가 없어도 애플리케이션은 정상 작동하며, 블랙리스트 기능만 비활성화됩니다.

## 📊 학습 포인트

### 1. JWT 토큰 구조 이해
- Header, Payload, Signature의 역할
- Base64Url 인코딩 방식
- Claims (sub, iat, exp, roles 등)

### 2. Spring Security 통합
- JwtAuthenticationFilter 구현
- SecurityFilterChain 설정
- 권한 기반 접근 제어

### 3. 토큰 생명주기 관리
- 토큰 생성 및 검증
- 만료 시간 관리
- 블랙리스트를 통한 무효화

### 4. 보안 모범 사례
- 적절한 토큰 만료 시간
- 안전한 키 관리
- 오류 처리 및 로깅

## 🔍 주요 차이점 비교

| 구분 | securityexam01 | securityexam02 | securityexam03 |
|------|----------------|----------------|----------------|
| 인증 방식 | Controller 기반 | Filter 기반 | JWT 토큰 기반 |
| 상태 관리 | Session 기반 | Session 기반 | Stateless |
| 토큰 사용 | ❌ | ❌ | ✅ JWT |
| 블랙리스트 | ❌ | ❌ | ✅ Redis |
| 포트 | 8080 | 8081 | 8082 |

## 🚨 문제 해결

### 1. 포트 충돌
```bash
# 다른 포트로 실행
gradlew bootRun --args='--server.port=8083'
```

### 2. Redis 연결 오류
- Redis 서버 실행 상태 확인
- application.yml의 Redis 설정 확인
- Redis 없이도 기본 기능은 작동함

### 3. JWT 토큰 오류
- 토큰 만료 시간 확인 (1시간)
- 올바른 Bearer 형식 사용
- 블랙리스트 여부 확인

## 📚 참고 자료

- [JWT 공식 문서](https://jwt.io/)
- [Spring Security JWT](https://docs.spring.io/spring-security/reference/)
- [RFC 7519 - JWT 표준](https://tools.ietf.org/html/rfc7519)
- 내용.md 6.4절: JWT 개념 및 사용 목적
- 내용.md 6.5절: JWT 생성 및 API 요청 시 토큰 전달 방법
- 내용.md 6.6절: JWT 보안 주의사항과 유효성 관리

---

**📞 문의사항이나 오류 발견 시 이슈를 등록해 주세요!** 