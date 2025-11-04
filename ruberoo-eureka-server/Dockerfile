# Dockerfile for the Ruberoo Eureka Server

# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jre-alpine

# Set the argument for the JAR file
ARG JAR_FILE="target/eureka-server-1.0.0-SNAPSHOT.jar"

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Eureka's default port is 8761
EXPOSE 8761

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]