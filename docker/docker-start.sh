#!/bin/sh
docker compose down
docker image prune -a -f
docker volume prune -f
docker buildx prune -f
cd ..
gradle :base-domain:clean
gradle :booking-service:clean
gradle :booking-service:bootJar
gradle :statistic-service:clean
gradle :statistic-service:bootJar
cd docker
docker compose -f docker-compose.yml up -d