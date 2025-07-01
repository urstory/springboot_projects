# TotalWeb - Spring Boot 웹 애플리케이션

TotalWeb은 `totalexam` API 서버와 연동하여 동작하는 Spring Boot 기반의 웹 애플리케이션입니다. Thymeleaf 템플릿 엔진과 바닐라 JavaScript를 사용하여 실시간으로 API를 호출하고 데이터를 관리할 수 있습니다.

## 🚀 주요 기능

### 📊 대시보드
- **실시간 통계**: 총 게시글 수, 활성 사용자, API 상태, JWT 토큰 상태
- **시각화**: Chart.js를 사용한 게시글 현황 차트
- **최근 게시글**: 최신 게시글 목록과 요약
- **자동 새로고침**: 5분마다 데이터 자동 업데이트

### 📝 게시글 관리
- **CRUD 기능**: 게시글 생성, 조회, 수정, 삭제
- **검색 및 필터링**: 제목/내용 검색, 작성자별 필터, 정렬 옵션
- **페이지네이션**: 대용량 데이터 효율적 처리
- **모달 기반 UI**: 사용자 친화적인 편집 인터페이스

### 👥 사용자 관리
- **사용자 정보 조회**: 카드 형태의 사용자 목록
- **테스트 로그인**: 다른 사용자 계정으로 간편 로그인
- **권한 표시**: 관리자/일반 사용자 구분

### 🔐 인증 시스템
- **이중 로그인**: Spring Security 기본 인증 + API JWT 인증
- **자동 토큰 관리**: JWT 토큰 자동 저장 및 갱신
- **세션 보안**: 토큰 유효성 검증 및 자동 로그아웃

## 🛠 기술 스택

- **Backend**: Spring Boot 3.5.3, Spring Security, Spring Web
- **Frontend**: Thymeleaf, Bootstrap 5.3.0, Vanilla JavaScript
- **API 통신**: Fetch API, JWT 인증
- **UI/UX**: Font Awesome 6.0.0, Chart.js
- **빌드**: Gradle

## 📋 사전 요구사항

1. **Java 21** 이상
2. **totalexam API 서버** 실행 중 (포트 8082)
3. **Gradle** (프로젝트에 포함된 Gradle Wrapper 사용 가능)

## 🚀 실행 방법

### 1. totalexam API 서버 실행
```bash
# totalexam 디렉토리로 이동
cd ../totalexam

# API 서버 실행 (포트 8082)
./gradlew bootRun
# 또는 Windows에서
gradlew.bat bootRun
```

### 2. totalweb 웹 애플리케이션 실행
```bash
# totalweb 디렉토리에서
./gradlew bootRun
# 또는 Windows에서
gradlew.bat bootRun
```

### 3. 웹 브라우저에서 접속
- **웹 애플리케이션**: http://localhost:8083
- **API 서버**: http://localhost:8082

## 🔑 로그인 정보

### 웹 관리자 계정 (Spring Security)
- **사용자명**: `admin`
- **비밀번호**: `admin123`

### API 테스트 계정 (JWT)
- **관리자**: `admin` / `password123`
- **일반 사용자**: `user1`, `user2`, `john`, `jane` / `password123`

## 📱 사용 가이드

### 로그인 페이지
1. **Spring Security 로그인**: 웹 관리자 계정으로 로그인
2. **API 로그인**: totalexam API 계정으로 JWT 토큰 발급 및 로그인

### 대시보드
- 실시간 통계 확인
- API 연결 상태 모니터링
- 최근 게시글 빠른 조회

### 게시글 관리
1. **새 게시글 작성**: "새 게시글" 버튼 클릭
2. **검색**: 제목/내용으로 검색 가능
3. **필터링**: 작성자별, 정렬 옵션 선택
4. **편집**: 각 게시글의 수정/삭제 버튼 사용

### 사용자 관리
- 등록된 사용자 목록 조회
- "테스트 로그인" 버튼으로 다른 계정 체험

## 🌐 API 연동

### 주요 API 엔드포인트
- **인증**: `POST /api/auth/login`, `GET /api/auth/validate`
- **게시글**: `GET /api/posts`, `POST /api/posts`, `PUT /api/posts/{id}`, `DELETE /api/posts/{id}`
- **헬스체크**: `GET /actuator/health`

### CORS 설정
totalexam API 서버에 CORS 설정이 추가되어 웹 애플리케이션에서 API 호출이 가능합니다.

## 🎨 UI/UX 특징

### 반응형 디자인
- Bootstrap 5.3.0 기반 모바일 친화적 UI
- 다양한 화면 크기 지원

### 현대적 인터페이스
- 그라데이션 배경과 카드 기반 레이아웃
- Font Awesome 아이콘으로 직관적 UI
- 부드러운 애니메이션과 호버 효과

### 사용자 경험
- 로딩 상태 표시
- 에러 메시지 자동 표시/숨김
- 실시간 데이터 업데이트

## 🔧 개발자 정보

### 프로젝트 구조
```
src/main/
├── java/com/example/totalweb/
│   ├── config/SecurityConfig.java      # Spring Security 설정
│   └── controller/MainController.java  # 웹 페이지 컨트롤러
└── resources/
    ├── application.properties          # 애플리케이션 설정
    └── templates/                     # Thymeleaf 템플릿
        ├── login.html                 # 로그인 페이지
        ├── dashboard.html             # 대시보드
        ├── posts.html                 # 게시글 관리
        └── users.html                 # 사용자 관리
```

### 주요 JavaScript 기능
- **TokenManager**: JWT 토큰 관리 (저장, 검증, 제거)
- **apiCall()**: 공통 API 호출 함수
- **실시간 차트**: Chart.js 기반 데이터 시각화
- **모달 관리**: Bootstrap 모달을 통한 폼 처리

## 🐛 트러블슈팅

### API 연결 실패
- totalexam API 서버가 포트 8082에서 실행 중인지 확인
- 방화벽 설정 확인
- CORS 설정 확인

### 로그인 실패
- 계정 정보 확인 (대소문자 구분)
- JWT 토큰 만료 시 재로그인 필요
- 브라우저 LocalStorage 확인

### 페이지 로딩 실패
- 브라우저 개발자 도구 콘솔 확인
- 네트워크 연결 상태 확인
- CDN 리소스 로딩 확인

## 📄 라이선스

이 프로젝트는 학습 목적으로 작성되었습니다.

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

**📞 문의사항이나 버그 리포트가 있으시면 언제든지 연락주세요!** 