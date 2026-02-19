FROM gradle:8.14.3-jdk21 AS builder
WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

COPY gradle.properties .


RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

COPY src ./src

RUN ./gradlew buildFatJar --no-daemon

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=builder /app/build/libs/app.jar app.jar

COPY src/main/resources/application.yaml application.yaml


ENTRYPOINT ["java", "-jar", "app.jar"]

