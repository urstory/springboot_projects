# Spring Boot Configuration Demo

이 프로젝트는 **내용.md 5.3절과 5.4절**의 Spring Boot 설정 관리 예제를 실제 동작하는 코드로 구현한 교육용 애플리케이션입니다.

## 📚 학습 목표

- **5.3절**: application.yml 파일 설정 방법 학습
- **5.4절**: 프로파일(Profile) 설정 방법 및 환경별 설정 변경 실습
- YAML 형식의 계층적 설정 구조 이해
- @ConfigurationProperties를 통한 커스텀 프로퍼티 매핑
- 환경변수와 Placeholder 활용 방법
- List와 Map 타입의 복잡한 설정 구조 활용

## 🛠️ 기술 스택

- **Spring Boot**: 3.5.3
- **Java**: 21
- **빌드 도구**: Gradle 8.x
- **설정 형식**: YAML
- **문서화**: Spring Boot Actuator

## 📁 프로젝트 구조

```
src/main/
├── java/com/example/configdemo/
│   ├── ConfigdemoApplication.java          # 메인 애플리케이션
│   ├── config/                            # 설정 클래스들
│   │   ├── AppProperties.java             # 커스텀 프로퍼티 매핑
│   │   └── DatabaseProperties.java        # 데이터베이스 설정 매핑
│   ├── service/
│   │   └── EnvironmentService.java        # 환경 설정 서비스
│   ├── controller/
│   │   └── ProfileController.java         # 설정 조회 API
│   └── runner/
│       └── ConfigurationRunner.java       # 콘솔 출력 러너
└── resources/
    ├── application.yml                     # 메인 설정 (프로파일 포함)
    ├── application-local.yml              # 로컬 환경 설정
    ├── application-dev.yml                # 개발 환경 설정
    └── application-prod.yml               # 운영 환경 설정
```

## 🔧 주요 기능

### 📖 5.3절 - application.yml 설정 예제

#### 1. 기본적인 YAML 구성
```yaml
spring:
  application:
    name: configdemo

app:
  name: "Configuration Demo Application"
  version: "1.0.0"
  api-key: "abcdef12345"
```

#### 2. 커스텀 프로퍼티 및 중첩 객체
```yaml
app:
  timeout:
    connection: 5000
    read: 10000
```

#### 3. List와 Map 구조
```yaml
app:
  servers:
    - http://server1.example.com
    - http://server2.example.com
  error-codes:
    NOT_FOUND: 404
    INTERNAL_ERROR: 500
```

#### 4. 환경변수 및 Placeholder 활용
```yaml
database:
  url: jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:configdemo}
  username: ${DB_USER:root}
  password: ${DB_PASS:password}
```

### 📊 5.4절 - 프로파일 설정 예제

#### 프로파일별 설정 분리
- **Local** (port: 8080): "Local 환경입니다."
- **Dev** (port: 8081): "개발 환경입니다."
- **Prod** (port: 80): "운영 환경입니다."
- **Test** (port: 8082): "테스트 환경입니다."

#### 두 가지 프로파일 설정 방법
1. **방법 1**: `application.yml` 파일 내에서 `---`로 구분
2. **방법 2**: 별도 파일 (`application-{profile}.yml`)

## 🚀 실행 방법

### 1. 프로파일별 실행 스크립트

#### Windows 환경
```cmd
# 로컬 환경 (포트 8080)
run-local.bat

# 개발 환경 (포트 8081)
run-dev.bat

# 운영 환경 (포트 80 - 관리자 권한 필요)
run-prod.bat

# 환경변수 테스트
run-with-env.bat
```

#### Unix/Linux/Mac 환경
```bash
# 실행 권한 부여
chmod +x *.sh

# 로컬 환경
./run-local.sh

# 개발 환경
./run-dev.sh

# 환경변수 테스트
./run-with-env.sh
```

### 2. 직접 실행

#### 기본 빌드 및 실행
```bash
./gradlew clean build -x test
java -jar build/libs/configdemo-0.0.1-SNAPSHOT.jar
```

#### 특정 프로파일 활성화
```bash
# 개발 환경
java -jar build/libs/configdemo-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# 운영 환경
java -jar build/libs/configdemo-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

# 여러 프로파일 동시 활성화
java -jar build/libs/configdemo-0.0.1-SNAPSHOT.jar --spring.profiles.active=local,debug
```

#### 환경변수 설정 후 실행
```bash
# Windows
set DB_HOST=myserver.com
set DB_PORT=3307
java -jar build/libs/configdemo-0.0.1-SNAPSHOT.jar

# Unix/Linux/Mac
export DB_HOST=myserver.com
export DB_PORT=3307
java -jar build/libs/configdemo-0.0.1-SNAPSHOT.jar
```

## 📊 API 엔드포인트

애플리케이션 실행 후 다음 API들로 설정 정보를 확인할 수 있습니다:

### 프로파일 및 환경 설정 (5.4절)
- `GET /api/config/summary` - 전체 설정 정보 요약
- `GET /api/config/profile-message` - 프로파일별 커스텀 메시지
- `GET /api/config/profiles` - 활성화된 프로파일 정보
- `GET /api/config/environment` - 환경 설정 요약

### 커스텀 프로퍼티 (5.3절)
- `GET /api/config/app-properties` - 전체 애플리케이션 프로퍼티
- `GET /api/config/database-config` - 데이터베이스 설정 (환경변수 활용)
- `GET /api/config/servers` - 서버 목록 (List 타입)
- `GET /api/config/error-codes` - 에러 코드 매핑 (Map 타입)
- `GET /api/config/timeout-config` - 타임아웃 설정 (중첩 객체)
- `GET /api/config/features` - 프로파일별 기능 설정

### Actuator 엔드포인트
- `GET /actuator/configprops` - 설정 프로퍼티 정보
- `GET /actuator/env` - 환경 정보
- `GET /actuator/health` - 헬스 체크

## 🧪 HTTP 테스트

`src/test/http/config-test.http` 파일에 20가지 테스트 케이스가 준비되어 있습니다:

- 프로파일별 설정 테스트
- 커스텀 프로퍼티 확인
- 환경변수 활용 테스트
- 다양한 포트에서의 동시 테스트

## 📋 프로파일 비교표

| 프로파일 | 포트 | 커스텀 메시지 | 로깅 레벨 | 디버그 모드 | 캐시 활성화 |
|---------|-----|------------|----------|----------|----------|
| local   | 8080 | "Local 환경입니다." | debug | true | false |
| dev     | 8081 | "개발 환경입니다." | info | true | false |
| prod    | 80   | "운영 환경입니다." | warn | false | true |
| test    | 8082 | "테스트 환경입니다." | info | true | false |

## 🔍 학습 포인트

### 5.3절 핵심 개념
- **YAML 계층 구조**: 들여쓰기로 설정 계층 표현
- **@ConfigurationProperties**: YAML → Java 객체 자동 매핑
- **환경변수 활용**: `${VAR_NAME:default_value}` 문법
- **복잡한 데이터 구조**: List, Map, 중첩 객체 설정

### 5.4절 핵심 개념
- **프로파일 분리**: 환경별 설정 관리
- **프로파일 활성화**: application.yml, 명령행, 환경변수
- **설정 우선순위**: 외부 설정 > 내부 설정
- **@Value vs @ConfigurationProperties**: 용도별 사용법

### 실전 활용 팁
1. **개발 환경**: `local` 프로파일로 디버깅 활성화
2. **테스트 환경**: `test` 프로파일로 격리된 환경 구성
3. **운영 환경**: `prod` 프로파일로 성능 최적화 설정
4. **민감 정보**: 환경변수로 보안 관리

## 📝 실습 과제

1. **새로운 프로파일 추가**: `staging` 프로파일 생성
2. **커스텀 프로퍼티 확장**: 새로운 설정 그룹 추가
3. **조건부 빈 생성**: `@ConditionalOnProfile` 활용
4. **외부 설정 파일**: JAR 외부 설정 파일 사용
5. **설정 검증**: `@Validated` 어노테이션 활용

## 🎯 다음 단계

이 예제를 완료한 후에는 다음 주제들을 학습해보세요:

- Spring Cloud Config (중앙 집중식 설정 관리)
- Docker 환경에서의 설정 관리
- Kubernetes ConfigMap/Secret 연동
- 설정 값 암호화 및 보안
- 동적 설정 리로딩

## 🔗 관련 문서

- [Spring Boot Configuration Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [Spring Boot Profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)
- [YAML Support](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.files.yaml)

---

**내용.md 5.3절, 5.4절 Configuration 완벽 실습 프로젝트** 🎓 