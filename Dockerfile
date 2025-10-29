# Use official Java 17 runtime as base image
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/StudentManagement.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]