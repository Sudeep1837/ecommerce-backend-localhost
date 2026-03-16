# E-Commerce Backend System

A production-grade scalable backend system for an e-commerce platform built using Java 21, Spring Boot 3, and MySQL. It demonstrates professional backend development practices including a layered architecture, security, API documentation, and clean code.

## 🚀 Features
- **User Management**: Registration, Login (JWT), Profile Update, Admin Controls.
- **Product Management**: CRUD APIs for products with filtering and pagination.
- **Cart Management**: Add, update, remove items, calculate totals.
- **Order Management**: Checkout flow, status updates, history.
- **Inventory Management**: Auto stock deduction upon successful checkout.
- **Payment Management**: Simulated payment gateway processing.

## 🏗️ Technical Stack
- **Language**: Java 21
- **Framework**: Spring Boot 3+
- **Database**: MySQL
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security + JWT
- **Documentation**: Swagger UI (OpenAPI 3)
- **Mapping**: ModelMapper

## 🛠️ How to run locally

### 1. Prerequisites
- Java 21+
- Maven 3.8+
- MySQL Server (Running on port 3306)

### 2. Database Setup
1. Open your MySQL client.
2. Run the provided `schema.sql` (or just create the database).
```sql
CREATE DATABASE IF NOT EXISTS ecommerce_db;
```

### 3. Application Properties
Update the database credentials in `src/main/resources/application.properties` if your MySQL username/password is different from `root`/`sudeep2003`.
```properties
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 4. Build and Run
```bash
./mvnw clean install
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`.

## 🐳 Run with Docker (recommended)

### 1. Prerequisites
- Docker Desktop (with Docker Compose)

### 2. Configure Aiven MySQL via `.env`

Create a `.env` file in the project root (same folder as `docker-compose.yml`) with your **Aiven MySQL** credentials (I will not hard-code them here; you can paste them later), for example:

```env
SPRING_DATASOURCE_URL=jdbc:mysql://<AIVEN_HOST>:<PORT>/<DB_NAME>?ssl-mode=REQUIRED
SPRING_DATASOURCE_USERNAME=<AIVEN_USER>
SPRING_DATASOURCE_PASSWORD=<AIVEN_PASSWORD>
```

> These variables are picked up by Docker Compose and forwarded into the Spring Boot container, and `application.properties` is already configured to read them.

### 3. Start the stack
From the project root:

```bash
docker compose up --build
```

This starts:
- **Spring Boot API** on `http://localhost:8081` (mapped to container port 8080 to avoid conflicts with a local Tomcat already using 8080), connected to your **Aiven MySQL** instance using the `.env` variables.

> Note: Since the database is now hosted on Aiven, you should apply `schema.sql` to that Aiven database manually (via MySQL client, Aiven console, or a migration tool).

### 3. Stop

```bash
docker compose down
```

To also delete the database volume (full reset):

```bash
docker compose down -v
```

## 📚 API Documentation
Once the application is running, you can access the Swagger UI for testing all REST APIs directly from the browser:
👉 **[Swagger UI](http://localhost:8080/swagger-ui/index.html)**

**Note**: To access secure endpoints, first register/login via `/api/users/login`, copy the JWT token, click the **Authorize** button in Swagger, and paste the token.

## 📦 Deliverables included in this project
1. Complete Source Code
2. `README.md` (Project overview, instructions)
3. `Postman_Collection.json`
4. `schema.sql` (Database queries)
5. `docker-compose.yml` (For easy containerization)
