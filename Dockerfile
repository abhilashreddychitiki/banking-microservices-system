FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the Maven wrapper
COPY .mvn/ .mvn/
COPY mvnw mvnw
COPY pom.xml pom.xml

# Copy the source code
COPY src/ src/
COPY services/ services/

# Build the application
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Set the API Gateway JAR as the main application
COPY services/api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar app.jar

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=demo

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
