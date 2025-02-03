#FROM gradle:8.11.1 AS builder
#WORKDIR /app
#COPY . .
#RUN ./gradlew clean bootJar

FROM openjdk:17-oracle

WORKDIR /app
#COPY --from=builder /app /app
COPY ../booking-service/build/libs/booking-service.jar booking-service.jar

ENTRYPOINT ["java", "-jar", "booking-service.jar"]