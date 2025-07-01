-- 사용자 초기 데이터 (비밀번호는 모두 "password123"로 BCrypt 인코딩됨)
INSERT INTO users (username, password, email, full_name, role, created_at, active) VALUES
('user1', '$2a$10$8t4h6q/QB8bqDt76d/ifjOYi71pPX/oupMrHzB3QX0ij.IloPxBLW', 'user1@example.com', '사용자1', 'ROLE_USER', CURRENT_TIMESTAMP, true),
('user2', '$2a$10$8t4h6q/QB8bqDt76d/ifjOYi71pPX/oupMrHzB3QX0ij.IloPxBLW', 'user2@example.com', '사용자2', 'ROLE_USER', CURRENT_TIMESTAMP, true),
('admin', '$2a$10$8t4h6q/QB8bqDt76d/ifjOYi71pPX/oupMrHzB3QX0ij.IloPxBLW', 'admin@example.com', '관리자', 'ROLE_ADMIN', CURRENT_TIMESTAMP, true),
('john', '$2a$10$8t4h6q/QB8bqDt76d/ifjOYi71pPX/oupMrHzB3QX0ij.IloPxBLW', 'john@example.com', 'John Doe', 'ROLE_USER', CURRENT_TIMESTAMP, true),
('jane', '$2a$10$8t4h6q/QB8bqDt76d/ifjOYi71pPX/oupMrHzB3QX0ij.IloPxBLW', 'jane@example.com', 'Jane Smith', 'ROLE_USER', CURRENT_TIMESTAMP, true);

-- 게시글 초기 데이터
INSERT INTO posts (title, content, author_id, created_at) VALUES
('첫 번째 게시글', '안녕하세요! 이것은 첫 번째 게시글입니다. Spring Boot와 JWT를 사용한 종합 실습 프로젝트입니다.', 1, CURRENT_TIMESTAMP),
('Spring Boot 학습기', 'Spring Boot를 학습하면서 느낀 점들을 공유합니다. 특히 Spring Security와 JWT 인증 부분이 흥미로웠습니다.', 2, CURRENT_TIMESTAMP),
('JWT 인증 구현하기', 'JWT(JSON Web Token)를 사용한 인증 시스템을 구현해보았습니다. 토큰 기반 인증의 장점과 구현 방법을 정리했습니다.', 1, CURRENT_TIMESTAMP),
('RESTful API 설계 원칙', 'REST API를 설계할 때 지켜야 할 원칙들에 대해 정리해보았습니다. 리소스 중심의 URL 설계와 HTTP 메서드의 올바른 사용법을 다룹니다.', 3, CURRENT_TIMESTAMP),
('Spring Data JPA vs JDBC', 'Spring Data JPA와 Spring Data JDBC의 차이점을 비교분석했습니다. 각각의 장단점과 사용 시나리오를 소개합니다.', 4, CURRENT_TIMESTAMP),
('테스팅 전략', '효과적인 테스트 코드 작성을 위한 전략을 공유합니다. 단위 테스트, 통합 테스트, E2E 테스트의 적절한 균형을 찾아보세요.', 5, CURRENT_TIMESTAMP),
('Spring Security 심화', 'Spring Security의 고급 기능들을 살펴봅니다. 커스텀 필터, 권한 관리, OAuth2 통합 등을 다룹니다.', 2, CURRENT_TIMESTAMP),
('마이크로서비스 아키텍처', '마이크로서비스 아키텍처의 개념과 Spring Boot를 활용한 구현 방법을 소개합니다.', 3, CURRENT_TIMESTAMP),
('성능 최적화 팁', 'Spring Boot 애플리케이션의 성능을 향상시키는 다양한 방법들을 정리했습니다.', 1, CURRENT_TIMESTAMP),
('배포 자동화', 'CI/CD 파이프라인을 구축하여 Spring Boot 애플리케이션을 자동으로 배포하는 방법을 소개합니다.', 4, CURRENT_TIMESTAMP);

