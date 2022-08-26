FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=target/*.jar
COPY target/content-repository.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=docker"]
