#!/bin/bash

echo "========================================"
echo "   Bean Example Application 실행"
echo "========================================"
echo

echo "🔧 Gradle 빌드 시작..."
./gradlew clean build --no-daemon

if [ $? -ne 0 ]; then
    echo "❌ 빌드 실패!"
    exit 1
fi

echo
echo "✅ 빌드 성공!"
echo
echo "🚀 애플리케이션 시작..."
echo
echo "📋 사용 가능한 엔드포인트:"
echo "   - Bean 정보: http://localhost:8080/api/beans/info"
echo "   - Prototype 테스트: http://localhost:8080/api/beans/prototype-test"  
echo "   - Scope 비교: http://localhost:8080/api/beans/scope-comparison"
echo "   - 사용자 API: http://localhost:8080/api/beans/users"
echo "   - Actuator Bean: http://localhost:8080/actuator/beans"
echo "   - Health Check: http://localhost:8080/actuator/health"
echo
echo "🛑 애플리케이션 종료: Ctrl+C"
echo "========================================"
echo

java -jar build/libs/beanexam-0.0.1-SNAPSHOT.jar 