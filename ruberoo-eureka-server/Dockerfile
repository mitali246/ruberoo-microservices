# Dockerfile for the Ruberoo Eureka Server
# Simple runtime image - expects pre-built JAR
# Build JAR first with: mvn clean package -pl ruberoo-eureka-server -am -DskipTests

FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the pre-built JAR (built from project root)
# Build context should be set to service directory
COPY target/eureka-server-1.0.0-SNAPSHOT.jar app.jar

# Eureka's default port is 8761
EXPOSE 8761

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]