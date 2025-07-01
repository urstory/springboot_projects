@echo off
echo ===============================================
echo Spring Boot Profile Demo - 빌드 및 실행
echo ===============================================
echo.

echo [단계 1] 프로젝트 빌드 중...
call gradlew.bat clean build

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [오류] 빌드 실패
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo [단계 2] JAR 파일 확인...
if exist "build\libs\spring-profile-demo-1.0.jar" (
    echo [확인] JAR 파일이 생성되었습니다: build\libs\spring-profile-demo-1.0.jar
) else (
    echo [오류] JAR 파일을 찾을 수 없습니다.
    pause
    exit /b 1
)

echo.
echo 실행할 프로파일을 선택하세요:
echo 1. dev (개발환경 - 포트 8081)
echo 2. test (테스트환경 - 포트 8082)  
echo 3. prod (운영환경 - 포트 80)
echo 4. default (기본설정 - 포트 8080)
echo.
set /p choice="선택 (1-4): "

if "%choice%"=="1" (
    set PROFILE=dev
    set PORT=8081
) else if "%choice%"=="2" (
    set PROFILE=test
    set PORT=8082
) else if "%choice%"=="3" (
    set PROFILE=prod
    set PORT=80
) else (
    set PROFILE=default
    set PORT=8080
)

echo.
echo [단계 3] 애플리케이션 실행...
echo [정보] 프로파일: %PROFILE%
echo [정보] 포트: %PORT%
echo [정보] 브라우저에서 확인: http://localhost:%PORT%/api/profile
echo.

if "%PROFILE%"=="default" (
    java -jar build\libs\spring-profile-demo-1.0.jar
) else (
    java -jar build\libs\spring-profile-demo-1.0.jar --spring.profiles.active=%PROFILE%
)

echo.
echo [완료] 애플리케이션이 종료되었습니다.
pause 