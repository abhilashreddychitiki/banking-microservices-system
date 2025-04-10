# Banking Microservices System - Railway Deployment

This document provides information about the Railway deployment of the Banking Microservices System.

## Live Demo

The Banking Microservices System is deployed on Railway and can be accessed at:

- Service Registry: [https://banking-microservices-system-production.up.railway.app](https://banking-microservices-system-production.up.railway.app)

## Architecture

The Railway deployment includes the following services:

1. **Service Registry (Eureka)**: Service discovery server
2. **API Gateway**: Entry point for all client requests
3. **User Service**: Manages user accounts and authentication
4. **Account Service**: Manages bank accounts

## Testing the API

You can test the API using the following endpoints:

### User Service

```
# Register a new user
POST /api/users/auth/signup
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "firstName": "Test",
  "lastName": "User",
  "phoneNumber": "1234567890"
}

# Login
POST /api/users/auth/signin
{
  "username": "testuser",
  "password": "password123"
}
```

### Account Service

```
# Create a new account (requires authentication)
POST /api/accounts
{
  "userId": 1,
  "accountType": "CHECKING",
  "initialDeposit": 1000.00,
  "currency": "USD"
}

# Get account details
GET /api/accounts/{accountId}

# Deposit money
POST /api/accounts/deposit
{
  "accountNumber": "1234567890",
  "amount": 500.00,
  "description": "Deposit"
}

# Withdraw money
POST /api/accounts/withdraw
{
  "accountNumber": "1234567890",
  "amount": 200.00,
  "description": "Withdrawal"
}
```

## Notes

- This is a simplified deployment for demonstration purposes
- In-memory H2 databases are used instead of persistent databases
- Some features may be limited due to Railway's free tier constraints

## Full Project

For the full project with all services and features, please check the GitHub repository:
[https://github.com/abhilashreddychitiki/banking-microservices-system](https://github.com/abhilashreddychitiki/banking-microservices-system)
