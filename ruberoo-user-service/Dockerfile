# Dockerfile for the Ruberoo User Service
# Simple runtime image - expects pre-built JAR
# Build JAR first with: mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the pre-built JAR (built from project root)
# Build context is set to ./ruberoo-user-service in docker-compose.yml
# JAR should be built before docker compose build
COPY target/user-service-1.0.0-SNAPSHOT.jar app.jar

# Expose the internal port (8081) as defined in user-service.yml
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]