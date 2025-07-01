# Filter & Interceptor Example

## 🎯 프로젝트 개요

Spring Boot에서 **Filter**와 **Interceptor**의 개념과 차이점을 학습할 수 있는 종합적인 교육용 예제입니다.

### 📚 학습 목표

- Servlet Filter의 동작 원리 이해
- Spring Interceptor의 동작 원리 이해
- Filter와 Interceptor의 차이점 비교
- 실행 순서와 처리 흐름 확인
- 실제 사용 사례와 적용 방법 학습

## 🏗️ 프로젝트 구조

```
filterexam/
├── src/main/java/com/example/filterexam/
│   ├── filter/                    # Servlet Filter들
│   │   ├── LoggingFilter.java     # HTTP 요청/응답 로깅
│   │   ├── PerformanceFilter.java # 성능 측정 및 통계
│   │   ├── CorsFilter.java        # CORS 헤더 처리
│   │   └── AuthenticationFilter.java # 인증 처리
│   ├── interceptor/               # Spring Interceptor들
│   │   ├── LoggingInterceptor.java # Controller 호출 로깅
│   │   └── AuthInterceptor.java   # 권한 체크
│   ├── controller/                # 테스트용 Controller들
│   │   ├── FilterTestController.java
│   │   ├── InterceptorTestController.java
│   │   └── ComparisonController.java
│   ├── config/                    # 설정 클래스들
│   │   └── WebConfig.java         # Interceptor 등록
│   ├── dto/                       # 데이터 클래스들
│   └── model/                     # 모델 클래스들
├── src/test/http/
│   └── filter-interceptor-test.http # HTTP 테스트 파일
└── README.md
```

## 🔧 기술 스택

- **Spring Boot**: 3.5.3
- **Java**: 21
- **Build Tool**: Gradle
- **Dependencies**: 
  - Spring Web
  - Spring Actuator
  - Spring Validation
  - Lombok
  - Jackson

## 🚀 실행 방법

### Windows
```bash
run.bat
```

### Linux/Mac
```bash
chmod +x run.sh
./run.sh
```

### 수동 실행
```bash
./gradlew clean build -x test
java -jar build/libs/filterexam-0.0.1-SNAPSHOT.jar
```

## 📡 API 엔드포인트

### 1. Filter 테스트

| 엔드포인트 | 메서드 | 설명 |
|-----------|--------|------|
| `/filter/test` | GET | 기본 Filter 체인 테스트 |
| `/filter/slow` | GET | 성능 측정 Filter 테스트 |
| `/filter/cors` | GET | CORS Filter 테스트 |
| `/filter/performance` | GET | 성능 통계 조회 |

### 2. Interceptor 테스트

| 엔드포인트 | 메서드 | 설명 |
|-----------|--------|------|
| `/interceptor/test` | GET | 기본 Interceptor 체인 테스트 |
| `/interceptor/auth/user` | GET | 사용자 권한 필요 |
| `/interceptor/auth/admin` | GET | 관리자 권한 필요 |
| `/interceptor/slow` | GET | 느린 처리 테스트 |
| `/interceptor/error` | GET | 예외 발생 테스트 |

### 3. 비교 분석

| 엔드포인트 | 메서드 | 설명 |
|-----------|--------|------|
| `/comparison/execution-order` | GET | 실행 순서 확인 |
| `/comparison/differences` | GET | 차이점 설명 |
| `/comparison/use-cases` | GET | 사용 사례 |
| `/comparison/performance` | GET | 성능 비교 |

## 🔐 인증 방법 (테스트용)

이 예제는 **교육 목적**으로 간단한 인증 메커니즘을 구현했습니다.

### 1. Bearer Token
```
Authorization: Bearer test-token-123
Authorization: Bearer admin-token-456
Authorization: Bearer user-token-789
```

### 2. Custom Header
```
X-Auth-Token: test-token-123
X-User-Role: USER
X-User-Role: ADMIN
```

### 3. Basic Auth
```
Authorization: Basic YWRtaW46cGFzc3dvcmQ=
# (base64: admin:password)
```

### 4. Cookie
```
Cookie: auth-token=test-token-123; user-role=USER
```

## 📊 Filter vs Interceptor 비교

| 구분 | Filter | Interceptor |
|------|--------|-------------|
| **실행 시점** | Servlet Container 레벨 | Spring MVC 레벨 |
| **적용 범위** | 모든 요청 (정적 리소스 포함) | Controller 요청만 |
| **실행 순서** | Spring 이전 | DispatcherServlet 이후 |
| **Spring 의존성** | 독립적 동작 가능 | Spring 컨텍스트 필수 |
| **예외 처리** | try-catch 블록 | afterCompletion에서 확인 |
| **주요 용도** | 인증, CORS, 로깅, 성능측정 | 인가, 세션 체크, 공통 로직 |

## 🔄 실행 흐름

```
요청 → Filter 체인 → DispatcherServlet → Interceptor → Controller
     ↓
응답 ← Filter 체인 ← DispatcherServlet ← Interceptor ← Controller
```

### 상세 실행 순서

1. **LoggingFilter** (요청 로깅)
2. **PerformanceFilter** (시간 측정 시작)
3. **CorsFilter** (CORS 헤더 설정)
4. **AuthenticationFilter** (인증 확인)
5. **DispatcherServlet** 진입
6. **LoggingInterceptor.preHandle()** (컨트롤러 호출 전)
7. **AuthInterceptor.preHandle()** (권한 체크)
8. **Controller 메서드 실행**
9. **AuthInterceptor.postHandle()** (컨트롤러 호출 후)
10. **LoggingInterceptor.postHandle()** (뷰 렌더링 전)
11. **LoggingInterceptor.afterCompletion()** (완료 후)
12. **AuthInterceptor.afterCompletion()** (완료 후)
13. **AuthenticationFilter** 완료
14. **CorsFilter** 완료
15. **PerformanceFilter** 완료 (시간 측정 종료)
16. **LoggingFilter** 완료 (응답 로깅)

## 🧪 테스트 방법

### 1. HTTP 파일 사용
```bash
# IntelliJ IDEA, VS Code REST Client 확장 사용
src/test/http/filter-interceptor-test.http
```

### 2. cURL 예제
```bash
# 기본 테스트
curl -X GET "http://localhost:8080/filter/test"

# 인증 테스트
curl -X GET "http://localhost:8080/interceptor/auth/user" \
  -H "Authorization: Bearer test-token-123"

# CORS 테스트
curl -X GET "http://localhost:8080/filter/cors" \
  -H "Origin: https://example.com"

# 성능 테스트
curl -X GET "http://localhost:8080/filter/slow?delay=3000"
```

### 3. Postman Collection
주요 테스트 시나리오:
- ✅ Filter 체인 동작 확인
- ✅ Interceptor 체인 동작 확인
- ✅ 인증/인가 플로우 테스트
- ✅ 성능 측정 기능 테스트
- ✅ CORS 처리 테스트
- ✅ 예외 처리 방식 비교

## 📈 모니터링

### Actuator 엔드포인트
- `/actuator/health` - 애플리케이션 헬스 체크
- `/actuator/metrics` - 메트릭 정보
- `/actuator/beans` - 등록된 Bean 목록
- `/actuator/env` - 환경 설정 정보

### 로그 확인
```bash
# 실시간 로그 확인
tail -f logs/application.log

# 특정 패턴 검색
grep "Filter" logs/application.log
grep "Interceptor" logs/application.log
```

## 📝 학습 포인트

### 1. Filter의 특징
- ✅ 모든 HTTP 요청에 대해 실행
- ✅ Spring Context 없이도 동작
- ✅ 요청/응답 데이터 변경 가능
- ✅ 체인 패턴으로 다중 필터 적용

### 2. Interceptor의 특징
- ✅ Controller 메서드 호출 전후 실행
- ✅ Spring Bean 주입 가능
- ✅ 메서드별 세밀한 제어 가능
- ✅ ModelAndView 접근 가능

### 3. 실제 사용 권장사항
- **Filter 사용**: 인증, CORS, 로깅, 성능측정, 보안 헤더
- **Interceptor 사용**: 인가, 세션 관리, 감사 로그, 공통 비즈니스 로직

## 🚨 주의사항

1. **간단한 인증 구현**
   - 교육 목적으로 간단한 토큰 기반 인증 구현
   - 실제 환경에서는 JWT, OAuth2 등 실제 인증 시스템 사용 권장
   - 인증이 필요 없는 경로: `/public/**`, `/health`, `/actuator/**`

2. **테스트 환경 설정**
   - 모든 요청이 기본적으로 허용됨
   - 테스트를 위해 유효한 토큰들이 하드코딩되어 있음
   - 실제 환경에서는 적절한 보안 설정 필요

3. **로그 레벨**
   - DEBUG 레벨로 상세한 로그 출력
   - 운영 환경에서는 INFO 이상 권장

## 🔧 트러블슈팅

### 포트 충돌
```bash
# 다른 포트로 실행
java -jar build/libs/filterexam-0.0.1-SNAPSHOT.jar --server.port=8081
```

### 메모리 부족
```bash
# JVM 힙 메모리 증가
java -Xmx1024m -jar build/libs/filterexam-0.0.1-SNAPSHOT.jar
```

### 빌드 실패
```bash
# 테스트 없이 빌드
./gradlew clean build -x test
```

## 📚 추가 학습 자료

- [Spring Boot Filter 공식 문서](https://docs.spring.io/spring-boot/docs/current/reference/html/web.html#web.servlet.filter)
- [Spring MVC Interceptor 공식 문서](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-interceptors)
- [Servlet Filter vs Spring Interceptor](https://stackoverflow.com/questions/8000844/spring-interceptor-vs-filter)

---

**Happy Learning! 🎉**

Filter와 Interceptor의 차이점을 실제 코드와 로그를 통해 확인하며 학습하세요! 