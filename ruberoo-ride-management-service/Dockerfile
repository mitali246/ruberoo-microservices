# Dockerfile for the Ruberoo Ride Management Service

# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jre-alpine

# Set the argument for the JAR file
ARG JAR_FILE="target/ride-management-service-1.0.0-SNAPSHOT.jar"

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Expose the internal port (8083) as defined in ride-management-service.yml
EXPOSE 8083

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]