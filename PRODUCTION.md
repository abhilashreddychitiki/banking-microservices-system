# Banking Microservices System - Production Deployment Guide

This document provides instructions for deploying the Banking Microservices System to a production environment.

## Prerequisites

Before deploying to production, ensure you have the following:

1. A server or cloud environment with Docker and Docker Compose installed
2. Access to a Docker registry (Docker Hub, AWS ECR, etc.)
3. Domain name and SSL certificates for secure communication
4. SMTP server credentials for email notifications
5. Sufficient resources to run all services (minimum 8GB RAM, 4 CPU cores)

## Configuration

1. Copy the `.env.production.template` file to `.env.production`:

```bash
cp .env.production.template .env.production
```

2. Edit the `.env.production` file and update all values with your production credentials:

```
# Database Credentials
POSTGRES_USER=your_postgres_user
POSTGRES_PASSWORD=your_secure_postgres_password
POSTGRES_DB=bankingdb

# MongoDB Credentials
MONGO_USER=your_mongo_user
MONGO_PASSWORD=your_secure_mongo_password
MONGO_DB=notificationdb

# RabbitMQ Credentials
RABBITMQ_USER=your_rabbitmq_user
RABBITMQ_PASSWORD=your_secure_rabbitmq_password

# JWT Secret
JWT_SECRET=your_secure_jwt_secret_at_least_256_bits_long

# Email Configuration
EMAIL_HOST=your_smtp_server
EMAIL_PORT=your_smtp_port
EMAIL_USERNAME=your_email@example.com
EMAIL_PASSWORD=your_email_password

# SSL Configuration
SSL_ENABLED=true
SSL_KEY_STORE=path_to_your_keystore
SSL_KEY_STORE_PASSWORD=your_keystore_password
SSL_KEY_STORE_TYPE=PKCS12
SSL_KEY_ALIAS=your_key_alias

# Monitoring Credentials
GRAFANA_ADMIN_USER=your_grafana_admin
GRAFANA_ADMIN_PASSWORD=your_secure_grafana_password

# Docker Registry
DOCKER_REGISTRY=your_docker_registry
TAG=latest
```

3. Generate a strong JWT secret:

```bash
openssl rand -base64 32
```

4. Create SSL certificates for HTTPS:

```bash
keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650
```

## Deployment

1. Make the deployment script executable:

```bash
chmod +x deploy-production.sh
```

2. Run the deployment script:

```bash
./deploy-production.sh
```

This script will:
- Build all microservices
- Create Docker images
- Push images to your Docker registry
- Deploy the entire system using Docker Compose

## Post-Deployment

After deployment, verify that all services are running:

```bash
docker-compose -f docker-compose.prod.yml ps
```

Access the monitoring tools:
- Grafana: https://your-domain.com:3000
- Prometheus: https://your-domain.com:9090
- Kibana: https://your-domain.com:5601

## Scaling

To scale services horizontally:

```bash
docker-compose -f docker-compose.prod.yml up -d --scale user-service=3 --scale account-service=3 --scale transaction-service=3 --scale notification-service=3 --scale auth-service=3 --scale api-gateway=2
```

## Backup

Set up regular backups for your data volumes:

```bash
# Example backup script for PostgreSQL
docker exec postgres pg_dump -U postgres bankingdb > backup_$(date +%Y%m%d).sql

# Example backup script for MongoDB
docker exec mongodb mongodump --out /backup/$(date +%Y%m%d)
```

## Monitoring

Monitor your services using Grafana dashboards. Set up alerts for:
- High CPU/Memory usage
- Service unavailability
- High error rates
- Slow response times

## Troubleshooting

If you encounter issues:

1. Check service logs:
```bash
docker-compose -f docker-compose.prod.yml logs service-name
```

2. Verify connectivity between services:
```bash
docker exec -it api-gateway ping user-service
```

3. Check Eureka dashboard to ensure all services are registered:
```
http://your-domain.com:8761
```

## Security Considerations

1. Regularly update passwords in the `.env.production` file
2. Rotate JWT secrets periodically
3. Keep all services updated with security patches
4. Implement network security rules to restrict access to internal services
5. Enable SSL for all external communication

## Rollback Procedure

If you need to rollback to a previous version:

```bash
# Set the TAG to the previous version
export TAG=previous_version_tag

# Redeploy
docker-compose -f docker-compose.prod.yml --env-file .env.production up -d
```
