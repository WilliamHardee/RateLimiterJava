#!/bin/bash

./gradlew build

./gradlew :rate-limited-api:run &
SERVER_PID=$!

sleep 5

./gradlew :request-simulator:run

trap "kill $SERVER_PID" EXIT