# Dockerfile for the Ruberoo User Service
# Multi-stage build: Build the JAR inside Docker
# NOTE: This Dockerfile must be built from the repository ROOT with build context set to root

# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-alpine AS builder

# Set working directory
WORKDIR /build

# Copy parent POM first (required for multi-module Maven project)
COPY pom.xml ./pom.xml

# Copy this service's files
COPY ruberoo-user-service/mvnw ./ruberoo-user-service/mvnw
COPY ruberoo-user-service/.mvn ./ruberoo-user-service/.mvn
COPY ruberoo-user-service/pom.xml ./ruberoo-user-service/pom.xml

# Make Maven wrapper executable
RUN chmod +x ruberoo-user-service/mvnw

# Download dependencies (cached layer)
WORKDIR /build/ruberoo-user-service
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY ruberoo-user-service/src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /build/ruberoo-user-service/target/user-service-1.0.0-SNAPSHOT.jar app.jar

# Expose the internal port (8081) as defined in user-service.yml
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]