@echo off
echo ======================================
echo  Filter & Interceptor Example 실행
echo ======================================
echo.

echo 🔧 Gradle 빌드 시작...
gradlew.bat clean build -x test

if %ERRORLEVEL% NEQ 0 (
    echo ❌ 빌드 실패!
    pause
    exit /b 1
)

echo ✅ 빌드 완료!
echo.

echo 🚀 Spring Boot 애플리케이션 시작...
echo   - 포트: 8080
echo   - 프로필: default
echo   - 종료: Ctrl+C
echo.

java -jar build\libs\filterexam-0.0.1-SNAPSHOT.jar

pause 