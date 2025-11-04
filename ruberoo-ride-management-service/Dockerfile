# Dockerfile for the Ruberoo Ride Management Service
# Multi-stage build: Build the JAR inside Docker
# NOTE: This Dockerfile must be built from the repository ROOT with build context set to root

# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-alpine AS builder

# Set working directory
WORKDIR /build

# Copy parent POM first (required for multi-module Maven project)
COPY pom.xml ./pom.xml

# Copy this service's files
COPY ruberoo-ride-management-service/mvnw ./ruberoo-ride-management-service/mvnw
COPY ruberoo-ride-management-service/.mvn ./ruberoo-ride-management-service/.mvn
COPY ruberoo-ride-management-service/pom.xml ./ruberoo-ride-management-service/pom.xml

# Make Maven wrapper executable
RUN chmod +x ruberoo-ride-management-service/mvnw

# Download dependencies (cached layer)
WORKDIR /build/ruberoo-ride-management-service
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY ruberoo-ride-management-service/src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /build/ruberoo-ride-management-service/target/ride-management-service-1.0.0-SNAPSHOT.jar app.jar

# Expose the internal port (8083) as defined in ride-management-service.yml
EXPOSE 8083

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]