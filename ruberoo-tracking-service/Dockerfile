# Dockerfile for the Ruberoo Tracking Service
# Simple runtime image - expects pre-built JAR
# Build JAR first with: mvn clean package -pl ruberoo-tracking-service -am -DskipTests

FROM public.ecr.aws/docker/library/eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the pre-built JAR (built from project root)
# Build context should be set to service directory
COPY target/tracking-service-1.0.0-SNAPSHOT.jar app.jar

# Expose the internal port (8084) as defined in tracking-service.yml
EXPOSE 8084

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]