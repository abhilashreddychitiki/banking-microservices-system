FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /workspace/app

# Copy the pom.xml file
COPY pom.xml .

# Copy source code
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /workspace/app/target/*.jar app.jar

# Expose the port
EXPOSE 8761

# Run the application with the railway profile
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=railway"]
