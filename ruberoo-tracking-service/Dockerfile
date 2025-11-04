# Dockerfile for the Ruberoo Tracking Service
# Multi-stage build: Build the JAR inside Docker
# NOTE: This Dockerfile must be built from the repository ROOT with build context set to root

# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-alpine AS builder

# Set working directory
WORKDIR /build

# Copy parent POM first (required for multi-module Maven project)
COPY pom.xml ./pom.xml

# Copy this service's files
COPY ruberoo-tracking-service/mvnw ./ruberoo-tracking-service/mvnw
COPY ruberoo-tracking-service/.mvn ./ruberoo-tracking-service/.mvn
COPY ruberoo-tracking-service/pom.xml ./ruberoo-tracking-service/pom.xml

# Make Maven wrapper executable
RUN chmod +x ruberoo-tracking-service/mvnw

# Download dependencies (cached layer)
WORKDIR /build/ruberoo-tracking-service
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY ruberoo-tracking-service/src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /build/ruberoo-tracking-service/target/tracking-service-1.0.0-SNAPSHOT.jar app.jar

# Expose the internal port (8084) as defined in tracking-service.yml
EXPOSE 8084

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]