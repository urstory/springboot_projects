# Spring Security 예제 프로젝트

## 📚 학습 목표

이 프로젝트는 **내용.md 6.1절, 6.2절**의 내용을 실제 동작하는 코드로 구현한 예제입니다.

- **6.1절**: 스프링 시큐리티의 기본 구조와 인증 메커니즘 이해
- **6.2절**: API 기반 로그인 처리 구현 실습

## 🛠 기술 스택

- **Spring Boot**: 3.5.3
- **Spring Security**: 6.2.x
- **Java**: 21
- **Gradle**: 8.x
- **Spring Web**: RESTful API 구현

## 📂 프로젝트 구조

```
src/main/java/com/example/securityexam01/
├── Securityexam01Application.java     # 메인 애플리케이션 클래스
├── config/
│   └── SecurityConfig.java            # 스프링 시큐리티 설정
├── controller/
│   └── AuthenticationController.java  # 인증 관련 API 컨트롤러
├── dto/
│   └── LoginRequest.java              # 로그인 요청 DTO
└── service/
    └── CustomUserDetailsService.java  # 사용자 정보 서비스

src/test/http/
└── security-test.http                 # HTTP API 테스트 케이스
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

## 🔐 테스트 사용자 계정

모든 사용자의 비밀번호는 `password` 입니다.

| 사용자명 | 권한    | 설명          |
|---------|---------|---------------|
| user1   | USER    | 일반 사용자   |
| admin   | ADMIN   | 관리자        |
| guest   | GUEST   | 게스트 사용자 |

## 🌐 API 엔드포인트

### 📋 인증 불필요 API

| Method | URL              | 설명                |
|--------|------------------|---------------------|
| GET    | `/api/info`      | 애플리케이션 정보   |
| GET    | `/api/users`     | 테스트 사용자 목록  |
| POST   | `/api/login`     | 로그인 API          |

### 🔒 인증 필요 API

| Method | URL               | 권한    | 설명                    |
|--------|-------------------|---------|-------------------------|
| GET    | `/api/me`         | 인증됨  | 현재 사용자 정보        |
| GET    | `/api/admin/info` | ADMIN   | 관리자 전용 정보        |

## 📝 API 사용 예제

### 1. 로그인 요청
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"username": "user1", "password": "password"}'
```

**성공 응답 (200 OK):**
```json
{
  "message": "로그인 성공",
  "username": "user1",
  "authorities": [{"authority": "ROLE_USER"}]
}
```

**실패 응답 (401 Unauthorized):**
```json
{
  "message": "로그인 실패: 잘못된 사용자명 또는 비밀번호"
}
```

### 2. 현재 사용자 정보 조회
```bash
curl -X GET http://localhost:8080/api/me \
  --cookie-jar cookies.txt \
  --cookie cookies.txt
```

### 3. 관리자 API 접근 (admin 계정 필요)
```bash
curl -X GET http://localhost:8080/api/admin/info \
  --cookie-jar cookies.txt \
  --cookie cookies.txt
```

## 🧪 테스트 방법

1. **애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```

2. **HTTP 파일을 이용한 테스트**
   - `src/test/http/security-test.http` 파일을 IntelliJ IDEA나 VS Code에서 실행
   - 각 API 호출 결과를 확인

3. **브라우저에서 테스트**
   - http://localhost:8080/api/info 접속하여 기본 정보 확인

## 📖 학습 포인트

### 6.1절 - 스프링 시큐리티 기본 구조
- **SecurityContextHolder**: 인증된 사용자 정보 보관
- **Authentication**: 사용자 인증 정보 객체
- **AuthenticationManager**: 인증 처리 관리자
- **UserDetailsService**: 사용자 정보 제공 서비스
- **PasswordEncoder**: 비밀번호 암호화 처리

### 6.2절 - API 기반 로그인 처리
- **JSON 기반 로그인**: Form 로그인 대신 JSON 요청/응답
- **RESTful API**: HTTP 상태 코드와 JSON 응답
- **인증 흐름**: 요청 → 필터 → AuthenticationManager → Provider → Service
- **권한 기반 접근 제어**: 역할별 API 접근 제한

## 🔍 핵심 코드 분석

### SecurityConfig.java
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())                    // CSRF 비활성화
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/login", "/api/users", "/api/info").permitAll()
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .formLogin(formLogin -> formLogin.disable())     // Form 로그인 비활성화
        .httpBasic(httpBasic -> httpBasic.disable())     // HTTP Basic 비활성화
        .build();
}
```

### AuthenticationController.java
```java
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
    
    try {
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(/* 성공 응답 */);
    } catch (BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                           .body(/* 실패 응답 */);
    }
}
```

## ⚠️ 주의사항

1. **비밀번호 암호화**: BCrypt를 사용하여 안전하게 암호화
2. **세션 관리**: 로그인 후 세션 쿠키가 자동으로 생성됨
3. **CSRF 비활성화**: API 서버이므로 CSRF 보호 기능 비활성화
4. **실제 운영 환경**: 데이터베이스 기반 사용자 관리 권장

## 🎯 실습 과제

1. **새로운 사용자 추가**: CustomUserDetailsService에 새로운 테스트 사용자 추가
2. **새로운 API 엔드포인트**: 다른 권한이 필요한 API 엔드포인트 구현
3. **로그아웃 API**: 로그아웃 처리 API 구현
4. **비밀번호 변경 API**: 인증된 사용자의 비밀번호 변경 API 구현

## 📞 문제 해결

- **포트 충돌**: 8080 포트가 사용 중인 경우 `application.yml`에서 `server.port` 변경
- **패키지 인식 오류**: IDE 재시작 또는 프로젝트 새로 고침
- **인증 실패**: 사용자명/비밀번호 확인 및 로그 확인 