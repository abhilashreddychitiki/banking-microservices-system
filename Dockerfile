FROM nginx:alpine

# Create a simple HTML page
RUN echo '<html>\
<head>\
  <title>Banking Microservices System</title>\
  <style>\
    body {\
      font-family: Arial, sans-serif;\
      line-height: 1.6;\
      margin: 0;\
      padding: 0;\
      display: flex;\
      justify-content: center;\
      align-items: center;\
      height: 100vh;\
      background-color: #f5f5f5;\
    }\
    .container {\
      max-width: 800px;\
      padding: 20px;\
      background-color: white;\
      border-radius: 8px;\
      box-shadow: 0 0 10px rgba(0,0,0,0.1);\
    }\
    h1 {\
      color: #2c3e50;\
      border-bottom: 2px solid #3498db;\
      padding-bottom: 10px;\
    }\
    p {\
      color: #34495e;\
    }\
    .footer {\
      margin-top: 30px;\
      font-size: 0.9em;\
      color: #7f8c8d;\
      text-align: center;\
    }\
  </style>\
</head>\
<body>\
  <div class="container">\
    <h1>Banking Microservices System</h1>\
    <p>This is a placeholder for the Banking Microservices System.</p>\
    <p>The full application is under development.</p>\
    <p>Please check back later for the complete demo.</p>\
    <div class="footer">\
      <p>© 2023 Banking Microservices System. All rights reserved.</p>\
    </div>\
  </div>\
</body>\
</html>' > /usr/share/nginx/html/index.html

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
