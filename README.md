# Livrili Backend

Livrili is a delivery management platform that connects clients with delivery personnel to streamline package delivery. The backend is built on Spring Boot, offering a secure, scalable, and intuitive system for managing packages, offers, deliveries, and ratings.

## Folder Structure

```
backend/
├── .mvn/
├── .settings/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── sfar/
│   │   │           └── livrili/
│   │   │               ├── Mapper/
│   │   │               ├── Service/
│   │   │               ├── Validation/
│   │   │               ├── Repositories/
│   │   │               ├── Security/
│   │   │               ├── Domains/
│   │   │               ├── Controller/
│   │   │               ├── Config/
│   │   │               └── LivriliApplication.java
│   │   └── resources/
│   └── test/
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

## API Endpoints

### Authentication
- `POST /v1/api/auth/login`: User login
- `POST /v1/api/auth/signUp`: User registration
- `GET /v1/api/auth`: Get authenticated user information

### Client Pack Management
- `POST /v1/api/client/pack`: Create a new package
- `GET /v1/api/client/pack`: Get client's packages
- `PUT /v1/api/client/pack/{id}`: Update a package
- `DELETE /v1/api/client/pack/{id}`: Delete a package
- `PUT /v1/api/client/pack/{id}/rate`: Rate delivery person
- `PUT /v1/api/client/pack/offer/{id}/decision`: Approve/decline offer

### Delivery Guy Pack Management
- `GET /v1/api/dg/pack`: List available packages
- `POST /v1/api/dg/pack/offer/{id}`: Create offer
- `PUT /v1/api/dg/pack/offer/{id}`: Update offer
- `DELETE /v1/api/dg/pack/offer/{id}`: Delete offer
- `PUT /v1/api/dg/pack/{id}/delivered`: Mark as delivered
- `GET /v1/api/dg/pack/offer/{id}`: Get offer details

## Swagger Documentation

The API documentation is available via Swagger UI at: `http://localhost:8080/swagger-ui.html`. Swagger provides a user-friendly interface for exploring and testing the API endpoints.

## Features
- JWT-based authentication with roles (`CLIENT`, `DELIVERY_PERSON`)
- Package management for clients and delivery personnel
- Offer negotiation and rating system
- RESTful APIs with Swagger documentation

## Architecture
Livrili employs a monolithic architecture with a Spring Boot backend, MySQL database, and Docker containerization. It uses a layered design for modularity and maintainability.

## Technologies Used
- Java 21
- Spring Boot 3.4.2
- MySQL 8.0
- Docker
- Lombok, JJWT, Springdoc OpenAPI

## Installation
1. Clone the repository: `git clone https://github.com/subomega1/Livrili_backend`
2. Navigate to the backend directory: `cd backend`
3. Build the project: `mvn clean install`
4. Run the application: `mvn spring-boot:run`

## Usage
The backend server will start on `http://localhost:8080`.

## Security
- JWT authentication with role-based access control
- BCrypt password hashing

## Contributing
We welcome contributions! Please follow these steps:
1. Fork the repository.
2. Create a new branch: `git checkout -b feature/YourFeature`
3. Make your changes and commit them: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/YourFeature`
5. Open a pull request.

## License
This project is licensed under the MIT License.

## Contact
- Developer: Sfar Ahmed Khalil
- Email: sfarahmed32@gmail.com

This README provides an overview of the backend architecture, technologies, and implementation details for the Livrili application.
