#!/bin/bash

# 실패 시 스크립트 즉시 종료
set -e

echo "📂 프로젝트 폴더로 이동"
cd ~/brainbrain-backend

echo "🔄 최신 코드 가져오기"
git pull

echo "🛠️ 빌드 시작"
./gradlew build -x test

echo "🛑 기존 애플리케이션 종료 중"
# 현재 실행 중인 jar 프로세스 종료
pkill -f 'java -jar' || true

echo "🚀 새 버전 실행"
nohup java -jar build/libs/iqtest-0.0.1-SNAPSHOT.jar > app.log 2>&1 &

echo "✅ 배포 완료! 앱 로그는 app.log 확인"
