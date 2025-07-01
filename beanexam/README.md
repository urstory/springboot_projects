# Spring Bean 개념 학습 예제

이 프로젝트는 Spring Framework의 **Bean 개념과 역할**을 학습하기 위한 포괄적인 예제 애플리케이션입니다.

## 📚 학습 목표

- Spring Bean의 기본 개념과 생명 주기 이해
- 다양한 Bean 정의 방법 실습 (@Component, @Service, @Repository, @Configuration/@Bean)
- Bean Scope (Singleton vs Prototype) 차이점 학습
- 의존성 주입(Dependency Injection) 방법 비교
- 기본 제공 Bean 커스터마이징 실습
- Bean 검사 및 관리 방법 학습

## 🛠️ 기술 스택

- **Spring Boot**: 3.5.3
- **Java**: 21
- **빌드 도구**: Gradle 8.x
- **로깅**: Logback + SLF4J
- **문서화**: Spring Boot Actuator
- **테스트**: JUnit 5 + Spring Test

## 📁 프로젝트 구조

```
src/main/java/com/example/beanexam/
├── BeanexamApplication.java          # 메인 애플리케이션 클래스
├── service/                          # Service Bean 예제들
│   ├── UserService.java             # @Service, Singleton, 생명주기
│   ├── PrototypeService.java        # @Service, Prototype 스코프
│   └── EmailService.java            # @Component
├── repository/                      # Repository Bean 예제
│   └── UserRepository.java          # @Repository, 데이터 접근
├── config/                          # Configuration Bean 예제들
│   ├── AppConfig.java               # @Configuration, @Bean
│   └── CustomBeanConfig.java        # 기본 Bean 커스터마이징
├── controller/                      # REST Controller
│   └── BeanController.java          # Bean 사용 예제 API
└── runner/                          # 시작 시 실행
    └── BeanInspectionRunner.java    # Bean 검사 및 출력
```

## 🔧 주요 기능

### 1. Bean 정의 방법 실습

#### @Service (Singleton)
```java
@Service
public class UserService {
    @PostConstruct
    public void initialize() { ... }
    
    @PreDestroy  
    public void cleanup() { ... }
}
```

#### @Service (Prototype)
```java
@Service
@Scope("prototype")
public class PrototypeService { ... }
```

#### @Repository
```java
@Repository
public class UserRepository { ... }
```

#### @Configuration + @Bean
```java
@Configuration
public class AppConfig {
    @Bean
    public DateTimeFormatter dateTimeFormatter() { ... }
    
    @Primary
    @Bean
    public ConfigurationService primaryService() { ... }
}
```

### 2. 기본 제공 Bean 커스터마이징

```java
@Configuration
public class CustomBeanConfig {
    @Bean
    public ObjectMapper customObjectMapper() {
        // Jackson ObjectMapper 커스터마이징
    }
    
    @Bean
    public DispatcherServlet customDispatcherServlet() {
        // DispatcherServlet 커스터마이징
    }
}
```

### 3. Bean 검사 및 분석

애플리케이션 시작 시 `BeanInspectionRunner`가 자동 실행되어:
- 전체 Bean 개수 출력
- 커스텀 Bean 목록 표시
- Bean 타입별 분류 및 정보 출력

## 🚀 실행 방법

### 1. Windows 환경
```cmd
run.bat
```

### 2. Unix/Linux/Mac 환경
```bash
chmod +x run.sh
./run.sh
```

### 3. 직접 실행
```bash
./gradlew clean build
java -jar build/libs/beanexam-0.0.1-SNAPSHOT.jar
```

## 📊 API 엔드포인트

애플리케이션 실행 후 다음 엔드포인트들을 테스트할 수 있습니다:

### Bean 관련 API
- `GET /api/beans/info` - Bean 정보 조회
- `GET /api/beans/prototype-test` - Prototype Bean 테스트
- `GET /api/beans/scope-comparison` - Singleton vs Prototype 비교

### 사용자 관리 API
- `GET /api/beans/users` - 모든 사용자 조회
- `POST /api/beans/users` - 새 사용자 생성
- `GET /api/beans/users/{id}` - 특정 사용자 조회
- `DELETE /api/beans/users/{id}` - 사용자 삭제

### Actuator 엔드포인트
- `GET /actuator/beans` - 전체 Bean 목록 조회
- `GET /actuator/health` - 애플리케이션 상태 확인
- `GET /actuator/info` - 애플리케이션 정보

## 🧪 HTTP 테스트

`src/test/http/bean-test.http` 파일에 준비된 25가지 테스트 케이스를 사용하여 모든 기능을 테스트할 수 있습니다.

## 📖 내용.md 5.1절, 5.2절 예제

이 프로젝트에는 **내용.md 5.1절(Bean 정의 방법)과 5.2절(Bean Scope)** 의 모든 예제가 실제 동작하는 코드로 구현되어 있습니다.

### 🔧 5.1절 - Bean 정의 방법 실습

#### 1. @Bean 어노테이션 방식
```java
// AppConfig.java에 구현됨
@Bean
public MyService myService() {
    return new MyService("Hello, Spring Bean from 내용.md!");
}

@Bean
public MyEncoder myEncoder() {
    return new MyEncoder("SHA-256");
}
```

#### 2. @Component 어노테이션 방식
```java
// CommonUtil.java에 구현됨
@Component
public class CommonUtil {
    public String generateUUID() { ... }
    public boolean isValidString(String str) { ... }
    public int sum(int... numbers) { ... }
}
```

#### 3. @Service, @Repository 방식
기존 UserService.java와 UserRepository.java가 해당 예제입니다.

### 📊 5.2절 - Bean Scope 실습

#### 1. Singleton Scope (기본값)
```java
@Component // 기본적으로 Singleton Scope
public class SingletonBean {
    private int count = 0;
    
    public int incrementAndGet() {
        return ++count;
    }
}
```

#### 2. Prototype Scope
```java
@Component
@Scope("prototype")
public class PrototypeBean {
    private int count = 0;
    // 매번 새로운 인스턴스 생성
}
```

#### 3. Request Scope (웹 환경)
```java
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, 
       proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestBean {
    // HTTP 요청당 하나의 인스턴스
}
```

#### 4. Session Scope (웹 환경)
```java
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION,
       proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionBean {
    private List<String> cart = new ArrayList<>();
    // HTTP 세션당 하나의 인스턴스
}
```

### 🌐 내용.md 예제 전용 API

새로 추가된 컨트롤러를 통해 내용.md의 모든 예제를 웹에서 테스트할 수 있습니다:

- `GET /api/bean-examples/summary` - 예제 요약 정보
- `GET /api/bean-examples/bean-annotation` - @Bean 방식 테스트
- `GET /api/bean-examples/component-annotation` - @Component 방식 테스트
- `GET /api/bean-examples/singleton-scope` - Singleton Scope 테스트
- `GET /api/bean-examples/prototype-scope` - Prototype Scope 테스트
- `GET /api/bean-examples/request-scope` - Request Scope 테스트
- `GET /api/bean-examples/session-scope` - Session Scope 테스트
- `POST /api/bean-examples/session-scope/cart` - Session 장바구니 조작

### 🖥️ 콘솔 출력

애플리케이션 시작 시 `BeanExampleRunner`가 실행되어 내용.md 예제들의 동작을 콘솔에서 확인할 수 있습니다:

- Bean 정의 방법별 테스트 결과
- Singleton vs Prototype Scope 차이점 실증
- 인스턴스 해시코드 비교
- 상태 공유/독립성 확인

## 📋 Bean Scope 비교

| 특성 | Singleton | Prototype |
|------|-----------|-----------|
| 인스턴스 개수 | 애플리케이션당 1개 | 요청시마다 새로 생성 |
| 메모리 사용 | 효율적 | 매번 새로 할당 |
| 상태 공유 | 모든 요청에서 공유 | 각 요청마다 독립적 |
| 사용 사례 | Service, Repository | 상태를 가진 일시적 객체 |
| 생명주기 관리 | Spring이 완전 관리 | 생성만 Spring이 관리 |

## 🔍 학습 포인트

### 1. Bean 생명주기
- `@PostConstruct`: Bean 초기화 후 실행
- `@PreDestroy`: Bean 소멸 전 실행
- ApplicationContext를 통한 Bean 조회

### 2. 의존성 주입 방법
- **생성자 주입** (권장): `@RequiredArgsConstructor`
- **필드 주입**: `@Autowired` 
- **setter 주입**: setter 메서드에 `@Autowired`

### 3. Bean 우선순위
- `@Primary`: 동일 타입의 여러 Bean 중 우선 선택
- `@Qualifier`: 이름으로 Bean 선택
- 사용자 정의 Bean > 기본 제공 Bean

### 4. 기본 제공 Bean 커스터마이징
- ObjectMapper 설정 변경
- DispatcherServlet 동작 수정
- MessageConverter 커스터마이징

## 📝 실습 과제

1. **새로운 Service Bean 추가**: `NotificationService` 생성
2. **Configuration Properties 사용**: `@ConfigurationProperties` 활용
3. **Bean 조건부 생성**: `@ConditionalOnProperty` 사용
4. **Profile별 Bean 설정**: `@Profile` 어노테이션 활용
5. **Bean 생명주기 이벤트**: `ApplicationListener` 구현

## 🔗 관련 문서

- [Spring Framework Bean 공식 문서](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans)
- [Spring Boot Auto-configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.auto-configuration)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)

## 🎯 다음 단계

이 예제를 완료한 후에는 다음 주제들을 학습해보세요:
- Spring AOP (Aspect-Oriented Programming)
- Spring Data JPA
- Spring Security
- Spring Cloud 