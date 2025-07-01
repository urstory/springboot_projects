@echo off
echo ============================================
echo Security Exam 03 - JWT Example 실행
echo ============================================
echo.

echo 🚀 JWT 기반 인증 시스템을 시작합니다...
echo 📍 포트: 8082
echo 🔗 애플리케이션 정보: http://localhost:8082/api/info
echo 🔗 테스트 사용자: http://localhost:8082/api/auth/users
echo.

gradlew bootRun

pause 