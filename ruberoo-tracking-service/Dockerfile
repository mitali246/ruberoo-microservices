# Dockerfile for the Ruberoo Tracking Service

# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jre-alpine

# Set the argument for the JAR file
ARG JAR_FILE="target/tracking-service-1.0.0-SNAPSHOT.jar"

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Expose the internal port (8084) as defined in tracking-service.yml
EXPOSE 8084

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]