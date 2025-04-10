# Banking Microservices System - Railway Deployment

This document provides information about the Railway deployment of the Banking Microservices System.

## Live Demo

The Banking Microservices System is deployed on Railway and can be accessed at:

- Banking Demo: [https://banking-microservices-system-production.up.railway.app](https://banking-microservices-system-production.up.railway.app)

## Current Deployment

Currently, we have deployed a simplified Banking Demo application that showcases the core functionality of the system. This demo is a standalone application that doesn't require the full microservices infrastructure, making it easier to deploy and demonstrate.

### Banking Demo Features

- User registration and authentication
- Account management (create and view accounts)
- Financial transactions (deposits, withdrawals, and transfers)
- Transaction history

### Technical Details

- **Framework**: Spring Boot 3.1.0
- **Database**: H2 (in-memory)
- **Security**: Spring Security
- **Frontend**: Thymeleaf templates with Bootstrap 5
- **Deployment**: Docker container on Railway

## Future Architecture

In the future, we plan to deploy the full microservices architecture, including:

1. **Service Registry (Eureka)**: Service discovery server
2. **API Gateway**: Entry point for all client requests
3. **User Service**: Manages user accounts and authentication
4. **Account Service**: Manages bank accounts

## Using the Banking Demo

You can use the Banking Demo by following these steps:

1. **Register a new account**:

   - Go to the registration page
   - Fill in your details (username, email, password, name, etc.)
   - Submit the form

2. **Log in**:

   - Enter your username and password
   - Click the login button

3. **Create a bank account**:

   - From the dashboard, click "Create New Account"
   - Select the account type (Checking, Savings, Credit)
   - Set the currency
   - Submit the form

4. **Perform transactions**:

   - **Deposit**: Add funds to your account
   - **Withdraw**: Remove funds from your account
   - **Transfer**: Move funds between accounts

5. **View transaction history**:
   - Click on an account to see its details
   - View the list of transactions for that account

## Notes

- This is a simplified deployment for demonstration purposes
- In-memory H2 databases are used instead of persistent databases
- Some features may be limited due to Railway's free tier constraints

## Full Project

For the full project with all services and features, please check the GitHub repository:
[https://github.com/abhilashreddychitiki/banking-microservices-system](https://github.com/abhilashreddychitiki/banking-microservices-system)
