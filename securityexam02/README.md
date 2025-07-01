# Spring Security 예제 프로젝트 - CustomAuthenticationFilter 활용

## 📚 학습 목표

이 프로젝트는 **내용.md 6.3절**의 내용을 실제 동작하는 코드로 구현한 예제입니다.

- **6.3절**: UsernamePasswordAuthenticationFilter를 활용한 로그인 API 개발

## 🔄 securityexam01과의 차이점

| 구분 | securityexam01 | securityexam02 |
|------|----------------|----------------|
| **접근 방식** | Controller 기반 | Filter 기반 |
| **처리 방식** | Spring MVC | Spring Security Filter Chain |
| **인증 위치** | `@PostMapping` 메서드 | `UsernamePasswordAuthenticationFilter` |
| **응답 처리** | Controller에서 직접 반환 | 성공/실패 핸들러 |
| **포트** | 8080 | 8081 |

## 🛠 기술 스택

- **Spring Boot**: 3.5.3
- **Spring Security**: 6.2.x
- **Java**: 21
- **Gradle**: 8.x
- **Jackson**: JSON 처리

## 📂 프로젝트 구조

```
src/main/java/com/example/securityexam02/
├── Securityexam02Application.java      # 메인 애플리케이션 클래스
├── config/
│   └── SecurityConfig.java             # 스프링 시큐리티 설정 (필터 등록)
├── filter/
│   └── CustomAuthenticationFilter.java # 커스텀 인증 필터 (핵심)
├── service/
│   └── CustomUserDetailsService.java   # 사용자 정보 서비스
├── controller/
│   └── InfoController.java             # 정보 조회 API
└── dto/
    └── LoginRequest.java               # 로그인 요청 DTO

src/test/http/
└── security-filter-test.http           # HTTP API 테스트 케이스 (14개)
```

## 🚀 실행 방법

### Windows
```cmd
run.bat
```

### Unix/Linux/macOS
```bash
chmod +x run.sh
./run.sh
```

### 직접 실행
```bash
./gradlew bootRun
```

**애플리케이션 포트**: `8081` (securityexam01과 충돌 방지)

## 🔐 테스트 사용자 계정

모든 사용자의 비밀번호는 `password` 입니다.

| 사용자명     | 권한    | 설명              |
|-------------|---------|-------------------|
| user1       | USER    | 일반 사용자       |
| admin       | ADMIN   | 관리자            |
| guest       | GUEST   | 게스트 사용자     |
| filter_user | USER    | 필터 테스트 전용  |

## 🌐 API 엔드포인트

### 📋 인증 불필요 API

| Method | URL              | 설명                    |
|--------|------------------|-------------------------|
| GET    | `/api/info`      | 애플리케이션 정보       |
| GET    | `/api/users`     | 테스트 사용자 목록      |

### 🔒 인증 API (CustomAuthenticationFilter 처리)

| Method | URL           | 설명                               |
|--------|---------------|------------------------------------|
| POST   | `/api/login`  | JSON 기반 로그인 (필터에서 처리)   |

## 📝 핵심 구현 - CustomAuthenticationFilter

### 1. UsernamePasswordAuthenticationFilter 상속
```java
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/api/login"); // 로그인 처리 URL 설정
    }
}
```

### 2. JSON 요청 처리
```java
@Override
public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
        throws AuthenticationException {
    
    // POST + application/json 검증
    if (!request.getMethod().equals("POST") || 
        !MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
        throw new AuthenticationServiceException("지원하지 않는 인증 방식입니다.");
    }

    // JSON 파싱 및 인증 토큰 생성
    LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
    UsernamePasswordAuthenticationToken authRequest = 
        new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
    
    return this.getAuthenticationManager().authenticate(authRequest);
}
```

### 3. 커스텀 핸들러 등록
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    CustomAuthenticationFilter customFilter = new CustomAuthenticationFilter(authenticationManager(http));

    // 성공/실패 핸들러 설정
    customFilter.setAuthenticationSuccessHandler(this::loginSuccessHandler);
    customFilter.setAuthenticationFailureHandler(this::loginFailureHandler);

    http.addFilterAt(customFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
}
```

## 🧪 API 사용 예제

### 1. 로그인 요청 (CustomAuthenticationFilter 처리)
```bash
curl -X POST http://localhost:8081/api/login \
  -H "Content-Type: application/json" \
  -d '{"username": "filter_user", "password": "password"}'
```

**성공 응답 (200 OK):**
```json
{
  "success": true,
  "message": "로그인 성공",
  "username": "filter_user",
  "authorities": ["ROLE_USER"],
  "timestamp": 1234567890
}
```

**실패 응답 (401 Unauthorized):**
```json
{
  "success": false,
  "message": "로그인 실패: Bad credentials",
  "error": "BadCredentialsException",
  "timestamp": 1234567890
}
```

### 2. 애플리케이션 정보 조회
```bash
curl -X GET http://localhost:8081/api/info
```

## 🔍 인증 흐름 (CustomAuthenticationFilter)

```
[JSON 로그인 요청]
       |
       V
[CustomAuthenticationFilter]
       |
   (JSON 파싱 및 검증)
       |
       V
[AuthenticationManager]
       |
       V
[AuthenticationProvider → UserDetailsService]
       |
       V
[성공/실패 핸들러 호출]
       |
       V
[JSON 응답 반환]
```

## 📖 학습 포인트

### 🔹 필터 기반 인증의 장점
1. **Spring Security 표준**: 프레임워크의 표준 인증 방식
2. **필터 체인 통합**: Security Filter Chain에 완전히 통합
3. **확장성**: 다양한 인증 방식으로 확장 가능
4. **핸들러 분리**: 성공/실패 처리 로직 분리

### 🔹 Controller vs Filter 비교
| 측면 | Controller 방식 | Filter 방식 |
|------|-----------------|-------------|
| **처리 위치** | Spring MVC 레이어 | Security Filter 레이어 |
| **응답 처리** | Controller 메서드 직접 | 성공/실패 핸들러 |
| **확장성** | 제한적 | 높음 |
| **Security 통합** | 부분적 | 완전함 |

### 🔹 JSON 요청 처리
- **Content-Type 검증**: `application/json`만 허용
- **HTTP 메서드 검증**: `POST`만 허용
- **필드 유효성 검증**: 사용자명/비밀번호 필수 검증
- **예외 처리**: 상세한 오류 메시지 제공

## 🧪 테스트 시나리오

HTTP 파일에서 제공하는 14가지 테스트 케이스:

### ✅ 성공 케이스
1. user1 로그인
2. admin 로그인  
3. filter_user 로그인

### ❌ 실패 케이스
4. 잘못된 사용자명
5. 잘못된 비밀번호
6. 사용자명 누락
7. 비밀번호 누락
8. 빈 JSON
9. 잘못된 Content-Type
10. GET 메서드 사용
11. 빈 사용자명
12. 공백만 있는 사용자명

## ⚠️ 주의사항

1. **포트 변경**: securityexam01과 충돌 방지를 위해 8081 포트 사용
2. **Content-Type**: 반드시 `application/json` 사용
3. **HTTP 메서드**: POST 메서드만 지원
4. **JSON 형식**: 올바른 JSON 형식 필수

## 🎯 실습 과제

1. **추가 유효성 검증**: 사용자명 길이, 비밀번호 복잡도 검증
2. **로그 개선**: 인증 시도 로그 상세화
3. **응답 커스터마이징**: 성공/실패 응답 필드 추가
4. **다중 Content-Type 지원**: XML, Form 데이터 지원 추가

## 🔗 관련 프로젝트

- **securityexam01**: Controller 기반 로그인 API 구현
- **향후 securityexam03**: JWT 토큰 기반 인증 예정

## 📞 문제 해결

- **포트 충돌**: application.yml에서 `server.port` 변경
- **인증 실패**: 로그에서 상세 오류 확인
- **JSON 파싱 오류**: Content-Type과 JSON 형식 확인 