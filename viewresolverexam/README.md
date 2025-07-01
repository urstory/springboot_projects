# ViewResolver & MessageConverter 예제 프로젝트

Spring Boot의 **ViewResolver**와 **MessageConverter** 개념을 이해하고 비교할 수 있는 실습 프로젝트입니다.

## 📋 프로젝트 개요

이 프로젝트는 동일한 데이터를 두 가지 다른 방식으로 처리하여 응답하는 예제를 제공합니다:

1. **ViewResolver**: HTML 페이지로 응답 (웹 페이지)
2. **MessageConverter**: JSON 데이터로 응답 (REST API)

## 🎯 학습 목표

- **ViewResolver** 동작 원리 이해
- **MessageConverter** 동작 원리 이해
- **DispatcherServlet**의 응답 변환 과정 학습
- **@Controller**와 **@RestController**의 차이점 이해

## 🔍 주요 개념

### ViewResolver
- Controller가 반환한 View 이름을 실제 HTML 페이지로 변환
- 주로 웹 페이지 응답에 사용
- 예: `"home"` → `/templates/home.html`

### MessageConverter
- Controller가 반환한 객체를 JSON/XML 등의 데이터 형식으로 변환
- 주로 REST API 응답에 사용
- 예: `User 객체` → `JSON 데이터`

## 📁 프로젝트 구조

```
src/main/java/com/example/viewresolverexam/
├── ViewResolverExamApplication.java         # 메인 애플리케이션
├── dto/
│   └── User.java                            # 사용자 데이터 모델
└── controller/
    ├── PageController.java                  # ViewResolver 예제 (@Controller)
    └── ApiController.java                   # MessageConverter 예제 (@RestController)

src/main/resources/
├── templates/                               # Thymeleaf 템플릿 파일들
│   ├── home.html                           # 홈 페이지
│   ├── user-list.html                      # 사용자 목록
│   ├── user-detail.html                    # 사용자 상세
│   └── error.html                          # 에러 페이지
└── application.yml                          # 애플리케이션 설정

src/test/http/
└── viewresolver-test.http                   # HTTP 테스트 파일
```

## 🚀 실행 방법

### Windows 환경
```bash
run.bat
```

### Unix/Linux/Mac 환경
```bash
chmod +x run.sh
./run.sh
```

### 수동 실행
```bash
./gradlew bootRun
```

## 🧪 테스트 방법

애플리케이션 실행 후 다음 URL로 접속하여 예제를 확인할 수 있습니다:

### 🌐 ViewResolver 예제 (HTML 응답)

| URL | 설명 | 응답 형태 |
|-----|------|-----------|
| http://localhost:8080/pages/home | 홈 페이지 | HTML 페이지 |
| http://localhost:8080/pages/users | 사용자 목록 | HTML 테이블 |
| http://localhost:8080/pages/users/1 | 사용자 상세 | HTML 카드 |

### 🔗 MessageConverter 예제 (JSON 응답)

| URL | 설명 | 응답 형태 |
|-----|------|-----------|
| http://localhost:8080/api/info | API 정보 | JSON 객체 |
| http://localhost:8080/api/users | 사용자 목록 | JSON 배열 |
| http://localhost:8080/api/users/1 | 사용자 상세 | JSON 객체 |
| http://localhost:8080/api/users/stats | 사용자 통계 | JSON 객체 |

## 🔄 처리 흐름 비교

### ViewResolver 흐름
```
1. 요청: GET /pages/users
2. PageController.userList() 실행
3. Model에 데이터 추가
4. "user-list" View 이름 반환
5. ThymeleafViewResolver가 처리
6. /templates/user-list.html → HTML 응답
```

### MessageConverter 흐름
```
1. 요청: GET /api/users
2. ApiController.getAllUsers() 실행
3. List<User> 객체 반환
4. MappingJackson2HttpMessageConverter가 처리
5. JSON 형태로 직렬화 → JSON 응답
```

## 💡 비교 학습

같은 사용자 데이터를 다른 방식으로 확인해보세요:

1. **HTML 형태**: http://localhost:8080/pages/users
2. **JSON 형태**: http://localhost:8080/api/users

두 URL을 비교하여 ViewResolver와 MessageConverter의 차이점을 직접 확인할 수 있습니다!

## 🛠️ 기술 스택

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Web**
- **Spring Boot Starter Thymeleaf**
- **Lombok**
- **Gradle**

## 📝 주요 어노테이션

| 어노테이션 | 용도 | 반환 방식 |
|-----------|------|-----------|
| `@Controller` | 웹 페이지 컨트롤러 | View 이름 → ViewResolver 처리 |
| `@RestController` | REST API 컨트롤러 | 객체 → MessageConverter 처리 |

## 🔧 설정 파일

### application.yml
```yaml
spring:
  thymeleaf:
    prefix: classpath:/templates/
    suffix: .html
    cache: false  # 개발 시 캐시 비활성화
```

## 📚 학습 포인트

이 프로젝트를 통해 다음을 학습할 수 있습니다:

1. **ViewResolver 동작 원리**: View 이름 → HTML 변환 과정
2. **MessageConverter 동작 원리**: 객체 → JSON 변환 과정
3. **DispatcherServlet의 역할**: 응답 변환 방식 결정
4. **Thymeleaf 템플릿 엔진**: 데이터 바인딩 및 렌더링
5. **REST API vs 웹 페이지**: 동일 데이터의 다른 표현 방식

## 🚀 확장 아이디어

- XML MessageConverter 추가
- 다른 ViewResolver (JSP 등) 비교
- 커스텀 MessageConverter 구현
- 에러 처리 방식 비교

Happy Learning! 🎓 