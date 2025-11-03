# Dockerfile for the Ruberoo User Service

# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:24-jre-alpine

# Set the argument for the JAR file
ARG JAR_FILE="target/user-service-1.0.0-SNAPSHOT.jar"

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Expose the internal port (8081) as defined in user-service.yml
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]