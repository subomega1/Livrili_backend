# Livrili Application Documentation

## 1. Conception

### 1.1 Purpose
Livrili is a delivery management platform that connects clients with delivery personnel to streamline package delivery. Built on Spring Boot, it offers a secure, scalable, and intuitive system for managing packages, offers, deliveries, and ratings.

### 1.2 Objectives
- Secure Access: Role-based authentication for clients and delivery personnel.
- Package Workflow: Support package creation, offer negotiation, delivery tracking, and ratings.
- Scalability: Utilize Docker for containerized deployment.
- Usability: Provide RESTful APIs with Swagger documentation.

### 1.3 Key Features
- User Registration/Login: JWT-based authentication with roles (`CLIENT`, `DELIVERY_PERSON`).
- Package Management: Clients create/update packages; Delivery personnel submit offers and complete deliveries.
- Offer Negotiation: Delivery personnel propose offers; clients accept/decline them.
- Rating System: Clients rate delivery personnel post-delivery.
- Error Handling: Detailed error responses for validation and runtime issues.

## 2. Architecture

### 2.1 Overall Architecture
Livrili employs a monolithic architecture with a Spring Boot backend, MySQL database, and Docker containerization. It uses a layered design for modularity and maintainability.

### 2.2 Components
- Frontend: Assumed separate (e.g., React at `http://localhost:5173`), consuming REST APIs.
- Backend: Spring Boot with REST endpoints, JPA persistence, and JWT security.
- Database: MySQL 8.0 container linked via Docker Compose.
- Deployment: Dockerized services (`mysql` and `spring-boot-app`).

### 2.3 Layered Architecture
- DTO Layer: Data transfer objects (e.g., `UserDto`, `ApprovedPackDto`) for API communication.
- Entity Layer: JPA entities (`User`, `Pack`, `Offer`) mapped to database tables.
- Repository Layer: JPA repositories (`UserRepository`, `PackRepository`) for data access.
- Service Layer: Business logic (`AuthenticationServiceImpl`, `ClientPackServiceImpl`).
- Controller Layer: REST controllers handling HTTP requests.
- Security Layer: JWT filter (`JwtAuthenticationFilter`) and user details service (`LivriliUserDetailsService`).

### 2.4 Deployment
- Docker Compose: Configures `mysql` (port `3366:3306`) and `spring-boot-app` (port `8080:8080`).
- Dockerfile: Builds app image with Eclipse Temurin JDK 21 Alpine, running as non-root.

## 3. Technologies Used

### 3.1 Core Technologies
- Java 21: Modern language features.
- Spring Boot 3.4.2: Framework for REST, JPA, and security.
  - `spring-boot-starter-web`: RESTful APIs.
  - `spring-boot-starter-data-jpa`: Database persistence.
  - `spring-boot-starter-security`: Authentication/authorization.
- MySQL 8.0: Relational database.
- Docker: Containerization.

### 3.2 Libraries and Tools
- Lombok 1.18.36: Reduces boilerplate code.
- JJWT 0.12.6: JWT token management.
- Springdoc OpenAPI 2.8.5: Swagger API documentation.
- Hibernate: ORM with MySQL dialect.
- BCrypt: Password hashing.
- Jakarta Validation: DTO validation (`@NotBlank`, `@Email`).
- Maven: Dependency and build management.

## 4. Database Design

### 4.1 Database Technology
- MySQL 8.0: Docker-hosted with persistent volume (`mysql_data`).
- JPA/Hibernate: ORM with `create-drop` DDL strategy for development.

### 4.2 Schema
#### Tables
1. **users**
   - `id` (UUID, PK)
   - `email` (VARCHAR, UNIQUE, NOT NULL)
   - `password` (VARCHAR, NOT NULL)
   - `firstName` (VARCHAR, NOT NULL)
   - `lastName` (VARCHAR, NOT NULL)
   - `phone` (VARCHAR, UNIQUE, NOT NULL)
   - `gender` (ENUM: 'MALE', 'FEMALE', NOT NULL)
   - `role` (ENUM: 'CLIENT', 'DELIVERY_PERSON', NOT NULL)
   - `createdAt` (DATETIME, NOT NULL)
   - `updatedAt` (DATETIME, NOT NULL)

2. **clients** (Extends `users`)
   - `id` (UUID, PK, FK to `users.id`)

3. **delivery_persons** (Extends `users`)
   - `id` (UUID, PK, FK to `users.id`)
   - `rating` (FLOAT)
   - `ratingCount` (INT)

4. **packs**
   - `id` (UUID, PK)
   - `description` (VARCHAR, NOT NULL)
   - `weight` (FLOAT, NOT NULL)
   - `pickUpLocation` (VARCHAR, NOT NULL)
   - `dropOffLocation` (VARCHAR, NOT NULL)
   - `status` (ENUM: 'PENDING', 'APPROVED', 'OFFERED', 'DELIVERED', 'RATED', NOT NULL)
   - `client_id` (UUID, FK to `clients.id`, NOT NULL)
   - `createdAt` (DATETIME, NOT NULL)
   - `updatedAt` (DATETIME, NOT NULL)

5. **offers**
   - `id` (UUID, PK)
   - `price` (DOUBLE, NOT NULL)
   - `daysToGetDelivered` (INT, NOT NULL)
   - `status` (ENUM: 'PENDING', 'ACCEPTED', 'DECLINED', 'DISPOSED', NOT NULL)
   - `delivery_guy_id` (UUID, FK to `delivery_persons.id`, NOT NULL)
   - `pack_id` (UUID, FK to `packs.id`, NOT NULL)
   - `createdAt` (DATETIME, NOT NULL)
   - `updatedAt` (DATETIME, NOT NULL)

#### Relationships
- Inheritance: `Client` and `DeliveryPerson` extend `User` (`JOINED` strategy).
- Client ↔ Pack: One-to-Many (`Client.packList` → `Pack.client`).
- DeliveryPerson ↔ Offer: One-to-Many (`DeliveryPerson.offers` → `Offer.deliveryPerson`).
- Pack ↔ Offer: One-to-Many (`Pack.offers` → `Offer.pack`).

### 4.3 Repository Layer
- UserRepository: Checks email/phone existence, finds users by email.
- ClientRepository: Basic CRUD with existence checks.
- DeliveryPersonRepository: Basic CRUD with rating updates.
- PackRepository: Queries for client packs, status filtering, and deletion.
- OfferRepository: Queries for delivery person offers, pack associations, and transactional updates.

## 5. API Design

### 5.1 Base Path
- `/v1/api`: Versioned API root.

### 5.2 Key Endpoints
- **Authentication**:
  - `POST /v1/api/auth/signUp`: Register user (Request: `UserDtoRequest`, Response: `AuthResponseDto`, Error: `ApiErrorResponse`).
  - `POST /v1/api/auth/login`: Login (Request: `LoginRequestDto`, Response: `AuthResponseDto`, Error: `ApiErrorResponse`).
  - `GET /v1/api/auth`: Get authenticated user details (Response: `UserDto`).

- **Client Pack Management**:
  - `POST /v1/api/client/pack`: Create package (Request: `PackRequestDto`, Response: `PackResponseDto`, Error: `ApiErrorResponse`).
  - `GET /v1/api/client/pack`: List client packages (Response: List<`PackResponseDto`>).
  - `PUT /v1/api/client/pack/{id}`: Update package (Request: `PackRequestDto`, Response: `PackResponseDto`, Error: `ApiErrorResponse`).
  - `DELETE /v1/api/client/pack/{id}`: Delete package.
  - `PUT /v1/api/client/pack/{id}/rate`: Rate delivery person (Request: `RattingRequestDto`, Response: `DeliveryGuyDto`).
  - `PUT /v1/api/client/pack/offer/{id}/decision`: Approve/decline offer (Request: `OfferDecisionRequest`, Response: `ApprovedPackDto`, Error: `ApiErrorResponse`).

- **Delivery Guy Pack Management**:
  - `GET /v1/api/dg/pack`: List available packages (Response: List<`DeliveryGuyPackResponseDto`>).
  - `POST /v1/api/dg/pack/offer/{id}`: Create offer (Request: `OfferRequest`, Response: `OfferResDto`, Error: `ApiErrorResponse`).
  - `PUT /v1/api/dg/pack/offer/{id}`: Update offer (Request: `OfferRequest`, Response: `OfferResDto`, Error: `ApiErrorResponse`).
  - `DELETE /v1/api/dg/pack/offer/{id}`: Delete offer.
  - `PUT /v1/api/dg/pack/{id}/delivered`: Mark as delivered (Response: `ApprovedPackDto`).
  - `GET /v1/api/dg/pack/offer/{id}`: Get offer details (Response: `GetOfferRes`).

### 5.3 DTOs
#### User-Related DTOs
- **UserDto**: (`email`, `firstName`, `lastName`, `gender`, `role`, `rating`) - General user details.
- **ClientDto**: (`firstName`, `lastName`, `gender`, `role`) - Client-specific details.
- **DeliveryGuyDto**: (`firstName`, `lastName`, `gender`, `rating`, `rattingCount`, `role`) - Delivery personnel details.

#### Client-Side DTOs
- **PackRequestDto**: (`description`, `weight`, `pickUpLocation`, `dropOffLocation`) - Package creation/update input.
- **PackResponseDto**: (`id`, `description`, `weight`, `pickUpLocation`, `dropOffLocation`, `status`, `createdAt`, `offers`) - Package details with offers.
- **OfferClientDto**: (`offerId`, `deliveryGuyName`, `deliveryGuyPrice`, `nbDaysToDeliver`, `offerStatus`, `deliveryGuyRating`, `rattingCount`, `createdAt`) - Offer details for clients.
- **RattingRequestDto**: (`rating`) - Rating submission.
- **OfferDecisionRequest**: (`status`) - Accept/decline offer decision.

#### Delivery Guy-Side DTOs
- **DeliveryGuyPackResponseDto**: (`id`, `clientName`, `description`, `pickUpLocation`, `dropOffLocation`, `status`, `weight`, `createdAt`, `createdBy`, `offersInPack`) - Package details for delivery personnel.
- **OfferRequest**: (`price`, `dayToDeliver`) - Offer creation/update input.
- **OfferResDto**: (`id`, `price`, `dayToDeliver`, `status`, `createdAt`) - Base offer response.
- **GetOfferRes**: Extends `OfferResDto` with (`packId`) - Detailed offer response.
- **OfferInPackDeliveryGuyDto**: (`deliveryGuyName`, `deliveryGuyRating`, `deliverGuyRatingCount`, `price`, `daysToDeliver`, `createdOn`) - Offer details within a package.

#### Approved Pack DTO
- **ApprovedPackDto**: (`packId`, `packDescription`, `packWeight`, `packPickUpLocation`, `packDropOffLocation`, `packStatus`, `packCreationDate`, `clientName`, `clientPhone`, `offer`, `deliveryGuyName`, `deliveryGuyRating`, `deliveryGuyRatingCount`, `deliveryGuyPhone`) - Detailed view of an approved package.

#### Error DTOs
- **IllegalArgs**: Extends `IllegalArgumentException` with (`errors`: List<`FieldsError`>) - Custom exception for validation errors.
- **FieldsError**: (`field`, `message`) - Specific field validation error.
- **ApiErrorResponse**: (`status`, `message`, `fields`: List<`FieldsError`>) - Standardized API error response.

### 5.4 Swagger
- Configuration: Defined in `OpenAiConfig` with JWT security scheme.
- UI: Accessible at `/swagger-ui.html`.

## 6. Design Patterns

### 6.1 MVC
- Model: Entities (`User`, `Pack`, `Offer`) and DTOs (`ApprovedPackDto`).
- View: JSON responses via REST APIs.
- Controller: REST controllers process requests.

### 6.2 Repository Pattern
- Data Access: JPA repositories (`PackRepository`, `OfferRepository`) with custom queries.

### 6.3 Service Layer Pattern
- Business Logic: Services (`ClientPackServiceImpl`, `DeliveryGuyPackServiceImpl`) handle workflows.

### 6.4 DTO Pattern
- Data Transfer: DTOs (e.g., `UserDto`, `ApiErrorResponse`) separate API data from entities.

### 6.5 State Pattern
- Enums: `PackageStatus` and `OfferStatus` manage lifecycle states.

### 6.6 Filter Pattern
- Security: `JwtAuthenticationFilter` validates JWT tokens.

### 6.7 Builder Pattern
- Object Creation: Lombok `@Builder` in DTOs and entities.

## 7. Security

### 7.1 Authentication
- JWT: HS256 tokens with 24-hour expiration, validated by `JwtAuthenticationFilter`.
- UserDetails: `LivriliUserDetailsService` loads users by email.

### 7.2 Authorization
- Roles: `CLIENT` and `DELIVERY_PERSON` with endpoint restrictions.
- Stateless: JWT-based, no server-side sessions.

### 7.3 Data Security
- Password: BCrypt hashing.
- Input Validation: Jakarta annotations on DTOs; `IllegalArgs` for custom validation errors.

## 8. Implementation Details

### 8.1 Repositories
- Custom Queries: `@Query` for filtering packs/offers by status, client, or delivery person.
- Transactional: `@Modifying` for updates/deletions (e.g., offer status changes).

### 8.2 Services
- Authentication: Manages JWT generation/validation.
- ClientPackService: Handles package lifecycle, offer decisions, and ratings; throws `IllegalArgs` for invalid inputs.
- DeliveryGuyPackService: Manages offer creation, updates, and delivery completion.

### 8.3 DTO Validation and Error Handling
- Validation: Jakarta annotations on DTOs (e.g., `PackRequestDto`); custom validation via `IllegalArgs`.
- Error Response: `ApiErrorResponse` standardizes error output with HTTP status, message, and field-specific errors (`FieldsError`).

## 9. Future Enhancements
- Microservices: Separate auth, pack, and offer services.
- Caching: Redis for faster data retrieval.
- Real-Time Updates: WebSockets for offer notifications.
- Frontend: Integrate a full UI.

## 10. Contact
- Developer: Sfar Ahmed Khalil
- Email: sfarahmed32@gmail.com
