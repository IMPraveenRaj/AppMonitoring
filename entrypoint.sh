#!/usr/bin/env bash
sleep 2s
trap  "touch /app/logs/terminated" EXIT
java $JAVA_OPTS -jar /app/java-template-canary-${gavVersion}.jar | tee -a /app/logs/${POD_NAME}_${POD_NAMSPACE}.log 2>1
exit ${PIPESTATUS[0]}