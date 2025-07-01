#!/bin/bash

echo "==============================================="
echo "Spring Boot Profile Demo - 운영 환경 실행"
echo "==============================================="
echo

echo "[정보] 운영 환경 프로파일로 애플리케이션을 시작합니다..."
echo "[정보] 프로파일: prod"
echo "[정보] 포트: 80"
echo "[정보] 로그 레벨: ERROR"
echo "[경고] 운영 환경 모드입니다!"
echo

echo "[실행] ./gradlew bootRun --args='--spring.profiles.active=prod'"
./gradlew bootRun --args="--spring.profiles.active=prod"

if [ $? -ne 0 ]; then
    echo
    echo "[오류] 애플리케이션 실행 중 오류가 발생했습니다."
    exit 1
fi

echo
echo "[완료] 애플리케이션이 종료되었습니다." 