@echo off
echo ===============================================
echo Spring Boot Profile Demo - 테스트 환경 실행
echo ===============================================
echo.

echo [정보] 테스트 환경 프로파일로 애플리케이션을 시작합니다...
echo [정보] 프로파일: test
echo [정보] 포트: 8082
echo [정보] 캐시: 활성화
echo.

echo [실행] gradlew bootRun --args='--spring.profiles.active=test'
call gradlew.bat bootRun --args="--spring.profiles.active=test"

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [오류] 애플리케이션 실행 중 오류가 발생했습니다.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo [완료] 애플리케이션이 종료되었습니다.
pause 