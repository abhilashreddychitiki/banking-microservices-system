#!/bin/bash

# Production Deployment Script for Banking Microservices System
# This script builds and deploys the banking microservices system to production

set -e

# Configuration
DOCKER_REGISTRY=${DOCKER_REGISTRY:-banking}
TAG=$(date +%Y%m%d%H%M%S)
ENV_FILE=.env.production

# Check if .env.production exists, if not create it with default values
if [ ! -f "$ENV_FILE" ]; then
    echo "Creating $ENV_FILE with default values..."
    cat > "$ENV_FILE" << EOF
# Database Credentials
POSTGRES_USER=postgres
POSTGRES_PASSWORD=secure_postgres_password
POSTGRES_DB=bankingdb

# MongoDB Credentials
MONGO_USER=admin
MONGO_PASSWORD=secure_mongo_password
MONGO_DB=notificationdb

# RabbitMQ Credentials
RABBITMQ_USER=guest
RABBITMQ_PASSWORD=secure_rabbitmq_password

# JWT Secret
JWT_SECRET=$(openssl rand -base64 32)

# Email Configuration
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password

# SSL Configuration
SSL_ENABLED=true
SSL_KEY_STORE=classpath:keystore.p12
SSL_KEY_STORE_PASSWORD=secure_keystore_password
SSL_KEY_STORE_TYPE=PKCS12
SSL_KEY_ALIAS=tomcat

# Monitoring Credentials
GRAFANA_ADMIN_USER=admin
GRAFANA_ADMIN_PASSWORD=secure_grafana_password

# Docker Registry
DOCKER_REGISTRY=banking
TAG=$TAG
EOF
    echo "$ENV_FILE created. Please update it with your actual production values."
    exit 1
fi

# Load environment variables
source "$ENV_FILE"

echo "Starting production deployment with tag: $TAG"

# Build all services
echo "Building all services..."

# Build Service Registry
echo "Building Service Registry..."
cd services/service-registry
./mvnw clean package -DskipTests
docker build -t $DOCKER_REGISTRY/service-registry:$TAG .
cd ../..

# Build User Service
echo "Building User Service..."
cd services/user-service
./mvnw clean package -DskipTests
docker build -t $DOCKER_REGISTRY/user-service:$TAG .
cd ../..

# Build Account Service
echo "Building Account Service..."
cd services/account-service
./mvnw clean package -DskipTests
docker build -t $DOCKER_REGISTRY/account-service:$TAG .
cd ../..

# Build Transaction Service
echo "Building Transaction Service..."
cd services/transaction-service
./mvnw clean package -DskipTests
docker build -t $DOCKER_REGISTRY/transaction-service:$TAG .
cd ../..

# Build Notification Service
echo "Building Notification Service..."
cd services/notification-service
./mvnw clean package -DskipTests
docker build -t $DOCKER_REGISTRY/notification-service:$TAG .
cd ../..

# Build Auth Service
echo "Building Auth Service..."
cd services/auth-service
./mvnw clean package -DskipTests
docker build -t $DOCKER_REGISTRY/auth-service:$TAG .
cd ../..

# Build API Gateway
echo "Building API Gateway..."
cd services/api-gateway
./mvnw clean package -DskipTests
docker build -t $DOCKER_REGISTRY/api-gateway:$TAG .
cd ../..

# Push images to registry
echo "Pushing images to registry..."
docker push $DOCKER_REGISTRY/service-registry:$TAG
docker push $DOCKER_REGISTRY/user-service:$TAG
docker push $DOCKER_REGISTRY/account-service:$TAG
docker push $DOCKER_REGISTRY/transaction-service:$TAG
docker push $DOCKER_REGISTRY/notification-service:$TAG
docker push $DOCKER_REGISTRY/auth-service:$TAG
docker push $DOCKER_REGISTRY/api-gateway:$TAG

# Update latest tag
docker tag $DOCKER_REGISTRY/service-registry:$TAG $DOCKER_REGISTRY/service-registry:latest
docker tag $DOCKER_REGISTRY/user-service:$TAG $DOCKER_REGISTRY/user-service:latest
docker tag $DOCKER_REGISTRY/account-service:$TAG $DOCKER_REGISTRY/account-service:latest
docker tag $DOCKER_REGISTRY/transaction-service:$TAG $DOCKER_REGISTRY/transaction-service:latest
docker tag $DOCKER_REGISTRY/notification-service:$TAG $DOCKER_REGISTRY/notification-service:latest
docker tag $DOCKER_REGISTRY/auth-service:$TAG $DOCKER_REGISTRY/auth-service:latest
docker tag $DOCKER_REGISTRY/api-gateway:$TAG $DOCKER_REGISTRY/api-gateway:latest

docker push $DOCKER_REGISTRY/service-registry:latest
docker push $DOCKER_REGISTRY/user-service:latest
docker push $DOCKER_REGISTRY/account-service:latest
docker push $DOCKER_REGISTRY/transaction-service:latest
docker push $DOCKER_REGISTRY/notification-service:latest
docker push $DOCKER_REGISTRY/auth-service:latest
docker push $DOCKER_REGISTRY/api-gateway:latest

# Deploy to production
echo "Deploying to production..."
docker-compose -f docker-compose.prod.yml --env-file $ENV_FILE up -d

echo "Deployment completed successfully!"
echo "The banking microservices system is now running in production mode."
echo "API Gateway is accessible at https://your-domain.com"
echo "Monitoring is available at:"
echo "  - Grafana: http://your-domain.com:3000"
echo "  - Prometheus: http://your-domain.com:9090"
echo "  - Kibana: http://your-domain.com:5601"
