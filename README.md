# Notification Service (notification-service)

### Service to consume orders in a kafka topic and notify an order confirmation by email

----

### This service communicates with [commerce-service](https://github.com/AlbertoFerreiraMaiaNeto/commerce-service) for update stock of products and the order status

----

### How to Contribute
If you want to contribute to development or fixing issues, feel free to open issues or submit pull requests. Be sure to follow the project's contribution guidelines.

-----
### Technologies Used:

Framework: Quarkus

Language: Java

Data Stream: Kafka

SMTP testing tool: Mailpit

-----

### Settings:
The notification-service settings are available in the application.properties file.
This includes mailpit and kafka configurations.

-----
To acess the Swagger, run the project and type in the browser: http://localhost:8080/q/swagger-ui


