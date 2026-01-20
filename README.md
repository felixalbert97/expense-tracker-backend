# Expense Tracker – Backend

This repository contains the backend of an Expense Tracker application built with **Spring Boot**.  
It provides a REST API for managing expenses and incomes and is designed to run both locally and in the cloud.

The backend is containerized with **Docker**, uses **PostgreSQL** as its primary database, and is deployed to **Railway**.

---

## 🎯 Project Goal

The main goal of this project was to gain hands-on experience with:

- Building a REST API using Spring Boot
- Environment-based configuration with Spring Profiles
- Dockerizing a backend application
- Running PostgreSQL in Docker
- Deploying a containerized backend to the cloud (Railway)
- Connecting a deployed backend to a separate frontend (Vercel)

The focus of this project is **infrastructure, deployment, and configuration**, not feature completeness.

---

## 🚧 Out of Scope / Not Implemented

The following aspects were intentionally kept minimal or are not included:

- Automated tests (unit / integration)
- Authentication & authorization
- Advanced validation & error handling
- Pagination, filtering, or sorting
- CI/CD pipelines

The focus was on learning Docker, deployment, and environment configuration.

---

## ✨ Features

- RESTful API for managing expenses and incomes
- Create, read, update, delete (CRUD) expenses
- Supports expenses and incomes
- PostgreSQL database (production & local)
- Environment-based configuration using Spring Profiles
- Dockerized setup with separate configurations for local development and production
- Deployed to Railway

---

## 🛠 Tech Stack

- **Java 21**
- **Spring Boot**
  - Spring Web
  - Spring Data JPA
- **PostgreSQL**
- **Hibernate ORM (via Spring Data JPA)**
- **Docker (including Docker Compose)**
- **Railway** (Deployment)

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
│   │   │       │   └── DataInitializer.java
│   │   │       └── common/
│   │   │           └── exception/
│   │   │               └── GlobalExceptionHandler.java    # (currently empty / future use)
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-local.properties
│   │       └── application-prod.properties
│   └── test/
│       └── java/                                          # (currently empty / future use)
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

The application uses **Spring Profiles** to separate environments.

### Available Profiles

- `local` – Local development with PostgreSQL (Docker)
- `prod` – Production environment (Railway)

### Profile Configuration Files

- `application.properties` – shared defaults
- `application-local.properties` – local development
- `application-prod.properties` – production configuration

### Activating Profiles

Profiles are activated via environment variables:

    SPRING_PROFILES_ACTIVE=local
    SPRING_PROFILES_ACTIVE=prod

Spring Profiles are primarily used when running the application locally via IDE or Maven, or in production (Railway). Container-based setups rely mainly on environment variables.

---

## 🚀 Running Locally (Recommended)

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

Environment variables are used for all production-specific configuration:

    SPRING_PROFILES_ACTIVE=prod
    SPRING_DATASOURCE_URL=jdbc:postgresql://...
    SPRING_DATASOURCE_USERNAME=...
    SPRING_DATASOURCE_PASSWORD=...


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

## 📌 Notes

- Database schema changes are **validated** in production (ddl-auto=validate)

- No test data is created in production

- Configuration follows best practices for cloud-native Spring Boot applications

---

## 📚 Lessons Learned

- Practical use of Docker for local and cloud environments
- Understanding environment-specific configuration in Spring Boot
- Managing application behavior via environment variables
- Debugging containerized Spring Boot applications
- Designing a clean separation between application code and infrastructure

## 📄 License

This project is for demonstration and learning purposes. 

