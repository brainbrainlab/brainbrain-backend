#!/bin/bash

# 실패 시 스크립트 즉시 종료
set -e

echo "📂 프로젝트 폴더로 이동"
cd ~/brainbrain-backend

echo "📄 .env 파일 로딩"
if [ -f ".env" ]; then
  export $(grep -v '^#' .env | xargs)
else
  echo ".env 파일이 존재하지 않습니다."
  exit 1
fi

echo "🔄 최신 코드 가져오기"
git pull

echo "🛠️ 빌드 시작"
./gradlew build -x test

echo "🛑 기존 애플리케이션 종료 중"
# 현재 실행 중인 jar 프로세스 종료
PID=$(pgrep -f 'iqtest-0.0.1-SNAPSHOT.jar')
if [ -n "$PID" ]; then
  echo "기존 프로세스 종료: PID=$PID"
  kill -9 $PID
  sleep 2
else
  echo "종료할 프로세스 없음"
fi

echo "🚀 새 버전 실행"
nohup java -jar build/libs/iqtest-0.0.1-SNAPSHOT.jar > app.log 2>&1 &

echo "✅ 배포 완료! 앱 로그는 app.log 확인"
