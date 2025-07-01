# MySpringApp2 - CRUD API 애플리케이션

Spring Boot를 활용한 기본적인 CRUD API 개발 실습 프로젝트입니다.

## 프로젝트 개요

데이터베이스 없이 메모리 내 데이터 구조(Map)를 활용하여 사용자 정보를 관리하는 RESTful API를 제공합니다.

## 주요 기능

- **사용자 생성** (POST /users)
- **전체 사용자 조회** (GET /users)
- **특정 사용자 조회** (GET /users/{id})
- **사용자 정보 수정** (PUT /users/{id})
- **사용자 삭제** (DELETE /users/{id})

## 프로젝트 구조

```
src/main/java/com/example/myspringapp/
├── MyspringappApplication.java      # 메인 애플리케이션
├── dto/
│   └── User.java                    # 사용자 데이터 모델
├── repository/
│   └── UserRepository.java         # 메모리 기반 데이터 저장소
└── controller/
    └── UserController.java         # REST API 컨트롤러

src/test/http/
└── request.http                     # HTTP 테스트 파일
```

## 실행 방법

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

## API 테스트

애플리케이션 실행 후 다음과 같이 테스트할 수 있습니다:

### 1. 사용자 생성
```bash
POST http://localhost:8080/users
Content-Type: application/json

{
  "name": "Alice",
  "age": 25
}
```

### 2. 전체 사용자 조회
```bash
GET http://localhost:8080/users
```

### 3. 특정 사용자 조회
```bash
GET http://localhost:8080/users/1
```

### 4. 사용자 정보 수정
```bash
PUT http://localhost:8080/users/1
Content-Type: application/json

{
  "name": "Alice Updated",
  "age": 26
}
```

### 5. 사용자 삭제
```bash
DELETE http://localhost:8080/users/1
```

## 기술 스택

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Web**
- **Lombok**
- **Gradle**

## 학습 포인트

이 프로젝트를 통해 다음을 학습할 수 있습니다:

1. **Spring Boot 기본 구조** 이해
2. **RESTful API** 설계 및 구현
3. **@RestController, @RequestMapping** 등 어노테이션 활용
4. **HTTP 메서드** (GET, POST, PUT, DELETE) 사용법
5. **JSON** 요청/응답 처리
6. **메모리 기반 데이터 관리** 방법

## 주의사항

- 이 프로젝트는 학습 목적으로 메모리에 데이터를 저장합니다.
- 애플리케이션을 재시작하면 모든 데이터가 사라집니다.
- 실제 운영 환경에서는 데이터베이스를 사용해야 합니다.

## HTTP 클라이언트 사용법

IntelliJ IDEA나 VS Code에서 `src/test/http/request.http` 파일을 열어 각 요청을 실행할 수 있습니다.

Happy Learning! 😊 