# Dockerfile for the Ruberoo Ride Management Service
# Simple runtime image - expects pre-built JAR
# Build JAR first with: mvn clean package -pl ruberoo-ride-management-service -am -DskipTests

FROM public.ecr.aws/amazoncorretto/amazoncorretto:21-alpine

# Set working directory
WORKDIR /app

# Copy the pre-built JAR (built from project root)
# Build context should be set to service directory
COPY target/ride-management-service-1.0.0-SNAPSHOT.jar app.jar

# Expose the internal port (8083) as defined in ride-management-service.yml
EXPOSE 8083

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]