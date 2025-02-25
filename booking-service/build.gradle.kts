plugins {
    java
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "booking-service"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.kafka:spring-kafka")

    implementation(project(":base-domain"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")
    implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.8.3")
    implementation("org.liquibase:liquibase-core:4.31.1")


    //implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter:1.17.6")
    testImplementation("org.testcontainers:postgresql:1.17.6")
    testImplementation("com.redis:testcontainers-redis")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.testcontainers:kafka:1.17.6")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.awaitility:awaitility:4.2.0")
    testImplementation("org.mockito:mockito-core:5.5.0")
    testImplementation("com.github.javafaker:javafaker:0.12")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
