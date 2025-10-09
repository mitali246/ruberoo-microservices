# Use a lightweight OpenJDK 24 JRE base image
FROM eclipse-temurin:24-jre-alpine

# The JAR_FILE argument is passed in via docker-compose.yml
ARG JAR_FILE="target/ruberoo-api-gateway-0.0.1-SNAPSHOT.jar"

COPY ${JAR_FILE} app.jar

# Expose the internal port (8085) as defined in api-gateway.yml
EXPOSE 8085

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]