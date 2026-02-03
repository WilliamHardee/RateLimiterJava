#!/bin/bash


echo "Compiling project..."
./gradlew build

echo "Starting Server..."
./gradlew :rate-limited-api:run &
SERVER_PID=$!
sleep 3

echo "Starting Simulator..."
./gradlew :request-simulator:run

trap "kill $SERVER_PID" EXIT