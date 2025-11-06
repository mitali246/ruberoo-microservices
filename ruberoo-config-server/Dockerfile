# Dockerfile for the Ruberoo Config Server
# Simple runtime image - expects pre-built JAR
# Build JAR first with: mvn clean package -pl ruberoo-config-server -am -DskipTests

FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the pre-built JAR (built from project root)
# Build context should be set to service directory
COPY target/ruberoo-config-server-1.0.0-SNAPSHOT.jar app.jar

# Expose the port the Config Server runs on (8888)
EXPOSE 8888

# Set the entry point to run the application when the container starts
ENTRYPOINT ["java","-jar","app.jar"]