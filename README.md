# Banking Microservices System

A comprehensive banking system built using a microservices architecture.

## Overview

This project implements a banking system with the following microservices:

- **Account Service**: Manages bank accounts (creation, updates, deletion)
- **Transaction Service**: Handles financial transactions (deposits, withdrawals, transfers)
- **User Service**: Manages user information and authentication
- **Notification Service**: Handles notifications to users
- **Auth Service**: Handles authentication and authorization
- **API Gateway**: Routes requests to appropriate services
- **Service Registry**: Service discovery with Eureka

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
- Maven

### Running Locally

```bash
# Clone the repository
git clone https://github.com/yourusername/banking-microservices-system.git
cd banking-microservices-system

# Start all services using Docker Compose
docker-compose up
```

### Live Demo

A live demo of the Banking Microservices System is deployed on Railway and can be accessed at:

- Banking Demo: [https://banking-microservices-system-production.up.railway.app](https://banking-microservices-system-production.up.railway.app)

This demo showcases the core banking functionality including:

- User registration and authentication
- Account management
- Deposits, withdrawals, and transfers
- Transaction history

For more information about the Railway deployment, see [RAILWAY.md](RAILWAY.md).

## Development

Each service can be developed and run independently. Refer to the README in each service directory for specific instructions.

## Production Deployment

For production deployment instructions, see [PRODUCTION.md](PRODUCTION.md).

The production deployment includes:

- Containerization with Docker
- Orchestration with Docker Compose (can be extended to Kubernetes)
- Monitoring with Prometheus and Grafana
- Centralized logging with ELK Stack
- SSL/TLS encryption
- Environment-specific configurations
- Scaling capabilities

## Security Features

- JWT-based authentication
- Role-based access control
- API rate limiting
- Circuit breakers for resilience
- Secure password storage with BCrypt
- HTTPS for all external communication

## License

This project is licensed under the MIT License - see the LICENSE file for details.
