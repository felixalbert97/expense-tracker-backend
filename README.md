![CI](https://github.com/felixalbert97/expense-tracker-backend/actions/workflows/ci.yml/badge.svg)

# Expense Tracker – Backend

Backend service for an Expense Tracker application built with **Spring Boot**.

The application exposes a REST API for managing expenses and incomes and is designed
to run **locally, containerized, and in the cloud**.

**Key focus:** backend architecture, user management & security, testing strategy, and production-ready configuration.

---

## 🎯 Project Goal

The main goal of this project was to gain hands-on experience with **professional backend development practices**, including:

* Designing a clean REST API with centralized exception handling
* Applying a layered testing strategy (unit, web, persistence) with automated execution via CI
* Implementing basic user management and authentication (JWT access + refresh tokens)
* Environment-specific configuration using Spring Profiles
* Containerizing a Spring Boot application with Docker
* Running PostgreSQL locally and in production
* Deploying a containerized backend to the cloud (Railway)
* Connecting a deployed backend to a separate frontend (Vercel)

The focus of this project is **architecture, testability, security, and deployment**, not feature completeness.

---

## ✨ Features

* RESTful API for managing expenses and incomes
* CRUD operations for expenses
* Layered architecture (controller, service, repository)
* User registration and authentication (JWT + refresh tokens)
* Bean validation on API boundaries (Jakarta Validation)
* Centralized exception handling via a global exception handler using standardized API error response models
* Environment-based configuration using Spring Profiles
* PostgreSQL database (local & production)
* Dockerized setup for local development and production
* Deployed to Railway
* Automated test execution via GitHub Actions (CI)

---

## 🔐 User Management & Security

The backend implements **basic user management and authentication**:

* User registration with validation and unique username/email
* Login with JWT access tokens and refresh tokens
* Secure password storage using BCrypt
* Token validation via JWT filter
* Refresh token expiration and invalidation logic
* Exception handling for authentication errors

---

## 🧪 Testing Strategy 

This project follows a **layered testing approach** with automated execution on every push via GitHub Actions to ensure fast feedback,
clear responsibilities per test type, and long-term maintainability.

The focus is on **deterministic and fast tests**, rather than many slow end-to-end tests.

### Test Types

#### 1. Service Layer – Unit Tests

* Business logic tested in isolation
* Repository dependencies are mocked
* Verifies domain behavior and exception handling

**Goal:**
Validate business rules without infrastructure dependencies.

---

#### 2. Web Layer – Controller Tests

* Uses `@WebMvcTest`
* Tests HTTP status codes, request/response mapping, and JSON payloads
* Service layer is mocked

**Goal:**
Verify the REST API contract independently of persistence concerns.

---

#### 3. Persistence Layer – Integration Tests

* Uses `@DataJpaTest`
* Runs against an in-memory H2 database (PostgreSQL compatibility mode)
* Verifies JPA mappings and repository behavior

**Goal:**
Ensure correct persistence behavior while keeping tests fast and portable.

---

### Why no full End-to-End Tests?

Full end-to-end tests (HTTP → Service → Database) were intentionally kept minimal:

* They are slower and harder to maintain
* They often duplicate coverage provided by layered tests
* For this project’s scope, they provide limited additional value

This approach provides a good balance between:

* confidence
* execution speed
* maintainability

---

## 🚧 Out of Scope / Not Implemented

The following aspects were intentionally kept minimal or are not included:

* Advanced authentication/authorization flows (e.g., roles, permissions, OAuth)
* Pagination, filtering, or sorting for API endpoints
* Full CI/CD pipeline
* Full end-to-end (E2E) test coverage

What **is implemented**:

* Basic user registration and JWT-based login with refresh tokens
* Secure password storage with BCrypt
* Token expiration and invalidation handling
* CI for test automation

---

## 🛠 Tech Stack

* **Java 21**
* **Spring Boot**

  * Spring Web (REST API)
  * Spring Data JPA (Persistence Layer)
  * Spring Security (User management, JWT authentication)
* **PostgreSQL** (local & production)
* **Hibernate ORM** (JPA provider)
* **H2** (in-memory test database)
* **Docker & Docker Compose** (local dev + production-like setups)
* **JUnit 5 & Mockito** (unit & integration testing)
* **GitHub Actions (CI)**
* **Railway** (production deployment)
* **Jakarta Validation / Bean Validation** (API-level validation)
* **BCrypt** (secure password hashing)

---

## 📂 Project Structure

```
expense-tracker-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── de/felixalbert/expensetracker/
│   │   │       ├── ExpenseTrackerApplication.java
│   │   │       ├── config/
│   │   │       │   └── CorsConfig.java
│   │   │       ├── expense/
│   │   │       │   ├── controller/
│   │   │       │   │   └── ExpenseController.java
│   │   │       │   ├── service/
│   │   │       │   │   └── ExpenseService.java
│   │   │       │   ├── repository/
│   │   │       │   │   └── ExpenseRepository.java
│   │   │       │   ├── model/
│   │   │       │   │   ├── Expense.java
│   │   │       │   │   └── ExpenseType.java
│   │   │       │   └── exception/
│   │   │       │       └── ExpenseNotFoundException.java
│   │   │       ├── security/
│   │   │       │   ├── config/
│   │   │       │   │   ├── SecurityConfig.java
│   │   │       │   │   ├── AuthenticationConfig.java
│   │   │       │   │   └── PasswordConfig.java
│   │   │       │   ├── filter/
│   │   │       │   │   └── JwtAuthenticationFilter.java
│   │   │       │   ├── controller/
│   │   │       │   │   └── AuthController.java
│   │   │       │   ├── service/
│   │   │       │   │   ├── JwtService.java
│   │   │       │   │   ├── RefreshTokenService.java
│   │   │       │   │   └── CustomUserDetailsService.java
│   │   │       │   ├── model/
│   │   │       │   │   ├── LoginRequest.java
│   │   │       │   │   ├── RegisterRequest.java
│   │   │       │   │   ├── RegisterResponse.java
│   │   │       │   │   ├── AuthResponse.java
│   │   │       │   │   └── RefreshToken.java
│   │   │       │   └── exception/
│   │   │       │       ├── InvalidRefreshTokenException.java
│   │   │       │       └── RefreshTokenExpiredException.java
│   │   │       ├── user/
│   │   │       │   ├── model/
│   │   │       │   │   └── User.java
│   │   │       │   ├── service/
│   │   │       │   │   └── UserService.java
│   │   │       │   ├── repository/
│   │   │       │   │   └── UserRepository.java
│   │   │       │   ├── exception/
│   │   │       │   │   ├── UserAlreadyInUseException.java
│   │   │       │   │   └── UserNotFoundException.java
│   │   │       │   └── ExpenseUserInitializer.java
│   │   │       └── common/
│   │   │           └── exception/
│   │   │               └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-local.properties
│   │       └── application-prod.properties
│   └── test/
│       ├── java/
│       │   └── de/felixalbert/expensetracker/
│       │       ├── ExpenseTrackerApplicationTests.java
│       │       ├── expense/
│       │       │   ├── controller/
│       │       │   │   └── ExpenseControllerTests.java
│       │       │   ├── service/
│       │       │   │   └── ExpenseServiceTests.java
│       │       │   ├── repository/
│       │       │   │   └── ExpenseRepositoryIntegrationTests.java
│       │       │   └── model/
│       │       │       └── ExpenseTestDataBuilder.java
│       │       └── user/
│       │           ├── repository/
│       │           │   └── UserRepositoryIntegrationTests.java
│       │           └── model/
│       │               └── UserTestDataBuilder.java
│       └── resources/
│           ├── application-test.yml
│           └── application-jpa-test.yml
├── Dockerfile
├── docker-compose.yml
├── docker-compose.local.yml
├── README.md
└── .gitignore

```

The backend follows a layered architecture (controller, service, repository, model) to ensure clear separation of concerns and scalability.
Some layers are intentionally kept minimal as the focus of this project is infrastructure and deployment rather than feature completeness.

---

## 🔧 Configuration & Profiles

The application uses **Spring Profiles** to separate environments:

* `local` – Local development with PostgreSQL (Docker)
* `prod` – Production environment (Railway)
* `test` – Isolated test configuration
* `jpa-test` - Test configuration for integration tests

Profiles are activated via environment variables and Maven/IDE configuration.

---

## 🚀 Running Locally

### Prerequisites

- Java 21+
- Maven
- Docker (Docker Desktop recommended)

### Steps

1. Clone the repository:

        git clone https://github.com/felixalbert97/expense-tracker-backend.git
        cd expense-tracker-backend

2. Start PostgreSQL via Docker:

        docker compose -f docker-compose.local.yml up -d

    This starts a PostgreSQL container on port 5432.

3. Start the application with the local profile using Maven:  
    (on Windows)
   
        ./mvnw "-Dspring-boot.run.profiles=local" spring-boot:run

    (on Unix/macOS)
   
        SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run

Or directly from your IDE (VS Code / IntelliJ) using the local profile.

4. The backend will be available at:

    http://localhost:8080/api/expenses

---

## 🔁 Alternative: Run Backend & Database via Docker Compose

Simulate a production-like environment locally by running **both the backend and PostgreSQL inside Docker containers.**

This setup is useful if you want to:

- verify Docker networking

- test container-based configuration

- run the application without a local Java/Maven setup

### Steps

1. Build the backend Docker image:

      docker build -t expense-tracker-backend .

2. Start backend and database using Docker Compose:

     docker compose up -d

This will start:

- PostgreSQL (`expense-postgres`)
- Backend (`expense-backend`)

Both services run in the same Docker network.

3. Access the API:

    http://localhost:8080/api/expenses


### Configuration Notes

- The backend uses Docker-internal DNS (postgres) to connect to the database:

    SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/expensetracker

- No Spring profile needs to be set explicitly in this setup,
because all required configuration (database connection, credentials)
is provided via environment variables in `docker-compose.yml`.

### When to use this setup

| Use case           | Recommended setup             |
| ------------------ | ----------------------------- |
| Active development | Local Spring Boot + Docker DB |
| Debugging          | Local Spring Boot             |
| Docker validation  | Docker Compose (this setup)   |
| Production         | Railway                       |

---

## 🌍 Deployment

The backend is deployed on **Railway** using Docker and runs with the `prod` profile.

All production configuration is provided via environment variables.

---

## 🐳 Docker Usage Overview

- Local development:
  - PostgreSQL runs in Docker
  - Spring Boot runs locally (IDE or Maven)
  - uses `docker-compose.local.yml`

- Local production-like setup
  - Backend + PostgreSQL run in Docker
  - Communication via Docker network
  - uses `docker-compose.yml`

- Production:
  - Backend runs as a Docker container on Railway
  - PostgreSQL is provided as a managed service by Railway
  - uses `Dockerfile` (build & run)

---

## 📚 Lessons Learned

* Designing a clean, layered backend architecture (controller, service, repository, model)
* Applying pragmatic, layered testing strategies (unit, controller, integration)
* Implementing user management and authentication with Spring Security and JWT
* Validating input using Jakarta Bean Validation and handling validation errors gracefully
* Standardizing API error responses for predictable frontend integration
* Managing environment-specific configuration with Spring Profiles
* Debugging and running containerized Spring Boot applications locally and in production
* Deploying and operating a backend service in the cloud (Railway)
* Setting up automated test execution using GitHub Actions (CI)

---

This project is for demonstration and learning purposes. 

