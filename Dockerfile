FROM openjdk:17-jdk-slim

WORKDIR /app

# Create a simple Spring Boot application
RUN echo 'package com.example.demo; \

import org.springframework.boot.SpringApplication; \
import org.springframework.boot.autoconfigure.SpringBootApplication; \
import org.springframework.web.bind.annotation.GetMapping; \
import org.springframework.web.bind.annotation.RestController; \

@SpringBootApplication \
@RestController \
public class DemoApplication { \

    public static void main(String[] args) { \
        SpringApplication.run(DemoApplication.class, args); \
    } \

    @GetMapping("/") \
    public String home() { \
        return "<h1>Banking Microservices System</h1>" + \
               "<p>This is a placeholder for the Banking Microservices System.</p>" + \
               "<p>The full application is under development.</p>" + \
               "<p>Please check back later for the complete demo.</p>"; \
    } \
}' > DemoApplication.java

# Create a simple pom.xml
RUN echo '<?xml version="1.0" encoding="UTF-8"?>\
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">\
    <modelVersion>4.0.0</modelVersion>\
    <parent>\
        <groupId>org.springframework.boot</groupId>\
        <artifactId>spring-boot-starter-parent</artifactId>\
        <version>2.7.0</version>\
        <relativePath/>\
    </parent>\
    <groupId>com.example</groupId>\
    <artifactId>demo</artifactId>\
    <version>0.0.1-SNAPSHOT</version>\
    <name>demo</name>\
    <description>Demo project for Spring Boot</description>\
    <properties>\
        <java.version>17</java.version>\
    </properties>\
    <dependencies>\
        <dependency>\
            <groupId>org.springframework.boot</groupId>\
            <artifactId>spring-boot-starter-web</artifactId>\
        </dependency>\
    </dependencies>\
    <build>\
        <plugins>\
            <plugin>\
                <groupId>org.springframework.boot</groupId>\
                <artifactId>spring-boot-maven-plugin</artifactId>\
            </plugin>\
        </plugins>\
    </build>\
</project>' > pom.xml

# Install Maven
RUN apt-get update && apt-get install -y maven

# Build the application
RUN mvn package -DskipTests

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=demo

# Expose the port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
