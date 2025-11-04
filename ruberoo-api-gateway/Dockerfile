# Dockerfile for the Ruberoo API Gateway
# Multi-stage build: Build the JAR inside Docker
# NOTE: This Dockerfile must be built from the repository ROOT with build context set to root

# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-alpine AS builder

# Set working directory
WORKDIR /build

# Copy parent POM first (required for multi-module Maven project)
COPY pom.xml ./pom.xml

# Copy this service's files
COPY ruberoo-api-gateway/mvnw ./ruberoo-api-gateway/mvnw
COPY ruberoo-api-gateway/.mvn ./ruberoo-api-gateway/.mvn
COPY ruberoo-api-gateway/pom.xml ./ruberoo-api-gateway/pom.xml

# Make Maven wrapper executable
RUN chmod +x ruberoo-api-gateway/mvnw

# Download dependencies (cached layer)
WORKDIR /build/ruberoo-api-gateway
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY ruberoo-api-gateway/src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /build/ruberoo-api-gateway/target/api-gateway-1.0.0-SNAPSHOT.jar app.jar

# Expose the internal port (8085) as defined in api-gateway.yml
EXPOSE 8085

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]