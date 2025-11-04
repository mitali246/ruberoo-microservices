# Dockerfile for the Ruberoo Config Server
# Multi-stage build: Build the JAR inside Docker
# NOTE: This Dockerfile must be built from the repository ROOT with build context set to root

# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-alpine AS builder

# Set working directory
WORKDIR /build

# Copy parent POM first (required for multi-module Maven project)
COPY pom.xml ./pom.xml

# Copy this service's files
COPY ruberoo-config-server/mvnw ./ruberoo-config-server/mvnw
COPY ruberoo-config-server/.mvn ./ruberoo-config-server/.mvn
COPY ruberoo-config-server/pom.xml ./ruberoo-config-server/pom.xml

# Make Maven wrapper executable
RUN chmod +x ruberoo-config-server/mvnw

# Download dependencies (cached layer)
WORKDIR /build/ruberoo-config-server
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY ruberoo-config-server/src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /build/ruberoo-config-server/target/ruberoo-config-server-1.0.0-SNAPSHOT.jar app.jar

# Expose the port the Config Server runs on (8888)
EXPOSE 8888

# Set the entry point to run the application when the container starts
ENTRYPOINT ["java","-jar","app.jar"]