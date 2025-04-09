#!/bin/bash

# Railway.app Deployment Script for Banking Microservices System
# This script builds and deploys the banking microservices system to Railway.app

set -e

# Configuration
TAG=$(date +%Y%m%d%H%M%S)
ENV_FILE=.env.demo

# Check if .env.demo exists, if not create it with default values
if [ ! -f "$ENV_FILE" ]; then
    echo "Creating $ENV_FILE with default values..."
    cat > "$ENV_FILE" << EOF
# Database Credentials
POSTGRES_USER=postgres
POSTGRES_PASSWORD=demo_password
POSTGRES_DB=bankingdb

# MongoDB Credentials
MONGO_USER=admin
MONGO_PASSWORD=demo_password
MONGO_DB=notificationdb

# RabbitMQ Credentials
RABBITMQ_USER=guest
RABBITMQ_PASSWORD=demo_password

# JWT Secret
JWT_SECRET=$(openssl rand -base64 32)

# Email Configuration
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USERNAME=demo@example.com
EMAIL_PASSWORD=demo_password

# Docker Registry
TAG=$TAG
EOF
    echo "$ENV_FILE created. Please update it with your actual demo values."
    exit 1
fi

# Load environment variables
source "$ENV_FILE"

echo "Starting Railway.app deployment with tag: $TAG"

# Install Railway CLI if not already installed
if ! command -v railway &> /dev/null; then
    echo "Installing Railway CLI..."
    npm i -g @railway/cli
    echo "Railway CLI installed successfully."
fi

# Login to Railway
echo "Logging in to Railway..."
railway login

# Create a new Railway project if needed
echo "Creating/selecting Railway project..."
railway project

# Build all services
echo "Building all services..."

# Build Service Registry
echo "Building Service Registry..."
cd services/service-registry
./mvnw clean package -DskipTests
cd ../..

# Build User Service
echo "Building User Service..."
cd services/user-service
./mvnw clean package -DskipTests
cd ../..

# Build Account Service
echo "Building Account Service..."
cd services/account-service
./mvnw clean package -DskipTests
cd ../..

# Build Transaction Service
echo "Building Transaction Service..."
cd services/transaction-service
./mvnw clean package -DskipTests
cd ../..

# Build Notification Service
echo "Building Notification Service..."
cd services/notification-service
./mvnw clean package -DskipTests
cd ../..

# Build Auth Service
echo "Building Auth Service..."
cd services/auth-service
./mvnw clean package -DskipTests
cd ../..

# Build API Gateway
echo "Building API Gateway..."
cd services/api-gateway
./mvnw clean package -DskipTests
cd ../..

# Deploy to Railway
echo "Deploying to Railway..."
railway up --detach

echo "Deployment completed successfully!"
echo "Your banking microservices system is now running on Railway.app"
echo "To get the public URL, run: railway domain"
