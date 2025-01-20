#FROM gradle:8.11.1 AS builder
#WORKDIR /app
#COPY . .
#RUN ./gradlew clean bootJar

FROM openjdk:17-oracle

WORKDIR /app
#COPY --from=builder /app /app
COPY ../build/libs/booking.jar booking.jar

ENTRYPOINT ["java", "-jar", "booking.jar"]