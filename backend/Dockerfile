# STAGE 1: Build-Umgebung
FROM eclipse-temurin:17-jdk-focal AS builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src
RUN ./gradlew bootJar

# -------------------------------------------------------------------
# STAGE 2: Laufzeit-Umgebung (Schlankes JRE)
# -------------------------------------------------------------------
FROM eclipse-temurin:17-jre-focal AS final
EXPOSE 8080
RUN useradd --create-home springuser
USER springuser
WORKDIR /home/springuser
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]