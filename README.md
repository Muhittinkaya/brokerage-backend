# Brokerage Firm Backend API

## Project Overview
This is a Spring Boot backend application for a brokerage firm, managing stock orders and customer transactions.

## Features
- Order Management (Create, List, Delete)
- Customer Operations (Deposit, Withdraw, List Assets)
- Authentication and Authorization
- Balance and Asset Integrity Checks

## Technologies Used
- Spring Boot
- Spring Data JPA
- Spring Security
- H2 Database
- Swagger OpenAPI

## Prerequisites
- Java 17+
- Maven

## Build and Run Instructions
1. Clone the repository
2. Navigate to the project directory
3. Build the project:
   ```
   mvn clean install
   ```
4. Run the application:
   ```
   mvn spring-boot:run
   ```

## API Documentation
Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

## Postman Collection
Import the Postman collection in `postman/brokerage-api.json` for API testing.

## Database
- H2 Console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:brokerage_db`
- Username: `sa`
- Password: (leave blank)

## Authentication
- Customer Login: `/api/auth/login`
- Admin Login: `/api/admin/login`

## Postman Collection Authorization
1. Obtain JWT token via login endpoint
2. Add token to Authorization header as Bearer token