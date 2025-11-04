# Dockerfile for the Ruberoo User Service
# Multi-stage build: Build the JAR inside Docker

# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-alpine AS builder

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /app/target/user-service-1.0.0-SNAPSHOT.jar app.jar

# Expose the internal port (8081) as defined in user-service.yml
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]