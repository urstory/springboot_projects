@echo off
echo ==========================================
echo Spring Security 예제 실행 - Custom Filter
echo 내용.md 6.3절 - UsernamePasswordAuthenticationFilter 활용
echo ==========================================

echo.
echo 포트: 8081
echo 로그인: POST /api/login (JSON)
echo.
echo 애플리케이션을 시작합니다...
call gradlew.bat bootRun

pause 