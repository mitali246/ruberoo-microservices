# Use a lightweight OpenJDK 24 JRE base image, suitable for M4 chip and Java 24
FROM eclipse-temurin:24-jre-alpine

# The JAR_FILE argument is passed in via docker-compose.yml
ARG JAR_FILE="target/ruberoo-eureka-server-0.0.1-SNAPSHOT.jar"

# Copy the built JAR file into the container as 'app.jar'
COPY ${JAR_FILE} app.jar

# Eureka's default port is 8761
EXPOSE 8761

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]