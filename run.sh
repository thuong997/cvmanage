#!/bin/bash
kill -9 $(ps -ef | pgrep -f "java")
mvn clean package -Dmaven.test.skip=true
nohup java -jar target/spring-boot-security-jwt-0.0.1-SNAPSHOT.jar > log.txt &
