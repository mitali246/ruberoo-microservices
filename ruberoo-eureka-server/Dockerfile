# Dockerfile for the Ruberoo Eureka Server
# Multi-stage build: Build the JAR inside Docker
# NOTE: This Dockerfile must be built from the repository ROOT with build context set to root

# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-alpine AS builder

# Set working directory
WORKDIR /build

# Copy parent POM first (required for multi-module Maven project)
COPY pom.xml ./pom.xml

# Copy this service's files
COPY ruberoo-eureka-server/mvnw ./ruberoo-eureka-server/mvnw
COPY ruberoo-eureka-server/.mvn ./ruberoo-eureka-server/.mvn
COPY ruberoo-eureka-server/pom.xml ./ruberoo-eureka-server/pom.xml

# Make Maven wrapper executable
RUN chmod +x ruberoo-eureka-server/mvnw

# Download dependencies (cached layer)
WORKDIR /build/ruberoo-eureka-server
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY ruberoo-eureka-server/src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /build/ruberoo-eureka-server/target/eureka-server-1.0.0-SNAPSHOT.jar app.jar

# Eureka's default port is 8761
EXPOSE 8761

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]