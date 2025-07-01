#!/bin/bash

echo "==============================================="
echo "Spring Boot Profile Demo - 개발 환경 실행"
echo "==============================================="
echo

echo "[정보] 개발 환경 프로파일로 애플리케이션을 시작합니다..."
echo "[정보] 프로파일: dev"
echo "[정보] 포트: 8081"
echo "[정보] 디버그 모드: 활성화"
echo

echo "[실행] ./gradlew bootRun --args='--spring.profiles.active=dev'"
./gradlew bootRun --args="--spring.profiles.active=dev"

if [ $? -ne 0 ]; then
    echo
    echo "[오류] 애플리케이션 실행 중 오류가 발생했습니다."
    exit 1
fi

echo
echo "[완료] 애플리케이션이 종료되었습니다." 