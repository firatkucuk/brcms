FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=target/*.jar
COPY target/cms-backend.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=docker"]
