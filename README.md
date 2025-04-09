# Banking Microservices System

A comprehensive banking system built using a microservices architecture.

## Overview

This project implements a banking system with the following microservices:

- **Account Service**: Manages bank accounts (creation, updates, deletion)
- **Transaction Service**: Handles financial transactions (deposits, withdrawals, transfers)
- **User Service**: Manages user information and authentication
- **Notification Service**: Handles notifications to users
- **API Gateway**: Routes requests to appropriate services

## Architecture

The system is built using a microservices architecture, with each service having its own database and business logic. Services communicate with each other using REST APIs and/or message queues.

## Technologies

- **Backend**: Spring Boot (Java), Node.js (Express)
- **Databases**: PostgreSQL, MongoDB
- **Message Broker**: RabbitMQ/Kafka
- **API Gateway**: Spring Cloud Gateway
- **Service Discovery**: Eureka/Consul
- **Containerization**: Docker
- **Orchestration**: Kubernetes
- **CI/CD**: GitHub Actions

## Getting Started

### Prerequisites

- Docker and Docker Compose
- Java 17+
- Node.js 18+
- Maven/Gradle

### Running Locally

```bash
# Clone the repository
git clone https://github.com/yourusername/banking-microservices-system.git
cd banking-microservices-system

# Start all services using Docker Compose
docker-compose up
```

## Development

Each service can be developed and run independently. Refer to the README in each service directory for specific instructions.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
