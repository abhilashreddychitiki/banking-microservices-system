FROM maven:3.8.6-openjdk-17-slim AS build

WORKDIR /app

# Copy the source code
COPY . .

# Build the API Gateway
WORKDIR /app/services/api-gateway
RUN mvn clean package -DskipTests

# Create the runtime image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/services/api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar app.jar

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=demo

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
