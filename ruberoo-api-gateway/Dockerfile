# Dockerfile for the Ruberoo API Gateway
# Simple runtime image - expects pre-built JAR
# Build JAR first with: mvn clean package -pl ruberoo-api-gateway -am -DskipTests

FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the pre-built JAR (built from project root)
# Build context should be set to service directory
COPY target/api-gateway-1.0.0-SNAPSHOT.jar app.jar

# Expose the internal port (8085) as defined in api-gateway.yml
EXPOSE 8085

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]