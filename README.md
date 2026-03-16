# 🛒 E-Commerce Backend System

A **production-grade scalable backend system** for an e-commerce platform built using **Java 21, Spring Boot 3, and MySQL**.

This project demonstrates **professional backend engineering practices** including:

- Layered Architecture
- RESTful API Design
- JWT Authentication
- Dockerized Deployment
- API Documentation with Swagger
- Clean Code and Maintainable Structure

---

# 🚀 Features

## 👤 User Management
- User Registration
- Login with **JWT Authentication**
- Profile Update
- Role-based access control (**ADMIN / CUSTOMER**)
- Admin can manage users

## 📦 Product Management
- Add, update, delete products (Admin)
- View product catalog
- Filtering and pagination support

## 🛒 Cart Management
- Add items to cart
- Update cart quantity
- Remove items
- Automatic total price calculation

## 📑 Order Management
- Checkout process
- Order history tracking
- Order status updates

## 📉 Inventory Management
- Automatic stock deduction after successful checkout

## 💳 Payment Management
- Simulated payment gateway processing

---

# 🏗️ Tech Stack

| Layer | Technology |
|------|-------------|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| Database | MySQL |
| ORM | Spring Data JPA / Hibernate |
| Security | Spring Security + JWT |
| Documentation | Swagger (OpenAPI 3) |
| Mapping | ModelMapper |
| Containerization | Docker + Docker Compose |

---

# 📂 Project Architecture

```
controller  → REST API endpoints  
service     → Business logic  
repository  → Database access layer  
entity      → Database models  
dto         → Request/response models  
config      → Security & application configuration  
```

This **layered architecture ensures scalability, maintainability, and clean separation of concerns.**

---

# 🌐 Live API Documentation

The backend is deployed on **Render**.

👉 **Swagger UI:**  
https://ecommerce-backend-2-tu2o.onrender.com/swagger-ui/index.html

Swagger allows you to:

- Explore all REST APIs
- Test endpoints directly
- Authenticate using JWT

### Authentication Steps

1. Login using:

```
POST /api/users/login
```

2. Copy the returned **JWT token**

3. Click **Authorize** in Swagger

4. Paste the token like:

```
Bearer <your_token>
```

---

# 🛠️ Running Locally (Optional)

## 1️⃣ Prerequisites

Make sure the following are installed:

- Java 21+
- Maven 3.8+
- MySQL Server
- Docker Desktop (optional)

---

## 2️⃣ Database Setup

Create the database:

```sql
CREATE DATABASE IF NOT EXISTS ecommerce_db;
```

Then execute:

```
schema.sql
```

---

## 3️⃣ Configure Database Credentials

Update the credentials in:

```
src/main/resources/application.properties
```

Example:

```
spring.datasource.url=jdbc:mysql://<HOST>:<PORT>/<DATABASE>
spring.datasource.username=<USERNAME>
spring.datasource.password=<PASSWORD>
```

---

## 4️⃣ Build and Run

```
./mvnw clean install
./mvnw spring-boot:run
```

Application will start at:

```
http://localhost:8080
```

---

# 🐳 Running with Docker

## Configure Environment Variables

Create a `.env` file in the project root:

```
SPRING_DATASOURCE_URL=jdbc:mysql://<HOST>:<PORT>/<DATABASE>?sslMode=REQUIRED
SPRING_DATASOURCE_USERNAME=<USERNAME>
SPRING_DATASOURCE_PASSWORD=<PASSWORD>
```

---

## Start Containers

```
docker compose up --build
```

---

## Stop Containers

```
docker compose down
```

To reset volumes:

```
docker compose down -v
```

---

# 📦 Project Deliverables

This repository includes:

- Complete **Spring Boot Backend Source Code**
- `README.md` (Project Documentation)
- `Postman_Collection.json`
- `schema.sql` (Database Schema)
- `docker-compose.yml`

---

# 🎯 Key Learning Outcomes

This project demonstrates:

- Production-ready **Spring Boot backend architecture**
- **JWT Authentication and role-based security**
- **Dockerized backend deployment**
- **Cloud database integration**
- REST API documentation with **Swagger**

---

# 👨‍💻 Author

**Sudeep Kumar Dehury**  
B.Tech Computer Science Engineering
