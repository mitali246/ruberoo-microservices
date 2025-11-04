# Dockerfile for the Ruberoo Config Server

# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jre-alpine

# Set the argument for the JAR file
ARG JAR_FILE="target/ruberoo-config-server-1.0.0-SNAPSHOT.jar"

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Expose the port the Config Server runs on (8888)
EXPOSE 8888

# Set the entry point to run the application when the container starts
ENTRYPOINT ["java","-jar","/app.jar"]