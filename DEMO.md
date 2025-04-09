# Banking Microservices System - Live Demo

This document provides instructions for accessing and using the live demo of the Banking Microservices System.

## Demo URL

The live demo is available at: [https://banking-microservices-demo.up.railway.app](https://banking-microservices-demo.up.railway.app)

## Demo Credentials

Use the following credentials to access the demo:

**Admin User:**
- Username: `admin`
- Password: `admin123`

**Regular User:**
- Username: `demouser`
- Password: `password123`

## Available Features

The demo includes the following features:

1. **User Management**
   - Registration
   - Login/Authentication
   - Profile management

2. **Account Management**
   - View accounts
   - Create new accounts
   - Check balance

3. **Transaction Management**
   - Make deposits
   - Make withdrawals
   - Transfer between accounts
   - View transaction history

4. **Notifications**
   - View notifications
   - Email notifications (simulated in demo)

## API Documentation

The API documentation is available at: [https://banking-microservices-demo.up.railway.app/swagger-ui.html](https://banking-microservices-demo.up.railway.app/swagger-ui.html)

## Demo Limitations

Please note the following limitations of the demo:

1. The demo is running on a free tier with limited resources
2. Data is reset periodically
3. Email notifications are simulated (not actually sent)
4. Some features may be disabled or limited in the demo

## Testing the Demo

### 1. Creating a New User

1. Navigate to the registration page
2. Fill in the required information
3. Submit the form
4. Login with your new credentials

### 2. Creating a Bank Account

1. Login to the system
2. Navigate to "Accounts" section
3. Click "Create New Account"
4. Select account type (Checking/Savings)
5. Enter initial deposit amount
6. Submit the form

### 3. Making a Transaction

1. Navigate to "Transactions" section
2. Select transaction type (Deposit/Withdrawal/Transfer)
3. Enter the amount and other required details
4. Submit the transaction

### 4. Viewing Notifications

1. Click on the notification icon
2. View all your transaction and account notifications

## Deploying Your Own Demo

If you want to deploy your own version of this demo:

1. Fork this repository
2. Copy `.env.demo.template` to `.env.demo` and update values
3. Run the deployment script:

```bash
chmod +x deploy-railway.sh
./deploy-railway.sh
```

4. Follow the prompts to complete the deployment

## Feedback

If you encounter any issues with the demo or have suggestions for improvements, please open an issue on the GitHub repository.
