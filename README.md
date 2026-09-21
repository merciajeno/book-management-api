# Book Management API

A small Spring Boot REST API for practicing:

- CRUD operations
- HTTP methods and status codes
- `@Valid` request validation
- A custom Bean Validation annotation
- Global exception handling with `@RestControllerAdvice`
- Pagination with Spring Data `Pageable`
- User registration/login
- BCrypt password hashing
- JWT generation and validation
- JWT authentication filter
- PostgreSQL
- Spring Boot DevTools

## Intentionally NOT included yet

As requested, this version does **not** contain:

- Refresh tokens
- Transaction rollback demo

Those can be added as the next step after the basic API is understood.

## Requirements

- Java 17+
- Maven
- PostgreSQL

## PostgreSQL setup

Create a database:

```sql
CREATE DATABASE bookdb;
```

The default configuration expects:

```text
username = postgres
password = postgres
database = bookdb
```

If your PostgreSQL credentials differ, edit:

`src/main/resources/application.properties`

## Run

```bash
mvn spring-boot:run
```

The API starts on:

`http://localhost:8080`

## Authentication flow

### 1. Register

```http
POST /auth/register
Content-Type: application/json

{
  "username": "alice",
  "password": "password123"
}
```

### 2. Login

```http
POST /auth/login
Content-Type: application/json

{
  "username": "alice",
  "password": "password123"
}
```

Response:

```json
{
  "token": "eyJ..."
}
```

### 3. Use the JWT

For book endpoints:

```http
Authorization: Bearer eyJ...
```

## Book endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/books` | Get paginated books |
| GET | `/api/books/{id}` | Get one book |
| POST | `/api/books` | Create book |
| PUT | `/api/books/{id}` | Update book |
| DELETE | `/api/books/{id}` | Delete book |

Example:

```http
POST /api/books
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "publicationYear": 2008
}
```

Pagination example:

```http
GET /api/books?page=0&size=5&sort=title,asc
```

## Custom validator

`@ValidPublicationYear` checks that the publication year is:

- at least 1000
- no later than the current year

This is separate from `@NotNull`, which handles a missing year.

## Simple architecture

```text
Controller
   |
   v
Service
   |
   v
Repository
   |
   v
PostgreSQL
```

Authentication:

```text
Login
  |
  v
AuthenticationManager
  |
  v
CustomUserService
  |
  v
UserRepository -> PostgreSQL
  |
  v
BCrypt password check
  |
  v
JwtService -> JWT
```

Request authentication:

```text
Request
  |
  v
JwtAuthenticationFilter
  |
  v
JwtService validates token
  |
  v
CustomUserService loads user
  |
  v
SecurityContext
  |
  v
BookController
```

## Important development note

`spring.jpa.hibernate.ddl-auto=update` is used to keep this project simple for the exercise. For a larger production project, database migrations such as Flyway/Liquibase are worth introducing.
