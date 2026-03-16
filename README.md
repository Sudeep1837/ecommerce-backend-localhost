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

# 🛠️ Running Locally

## 1️⃣ Prerequisites

Make sure the following are installed:

- Java 21+
- Maven 3.8+
- MySQL Server
- Docker Desktop (optional but recommended)

---

## 2️⃣ Build the Project

```bash
./mvnw clean install
```

---

## 3️⃣ Run the Application

```bash
./mvnw spring-boot:run
```

The application will start on:

```
http://localhost:8080
```

---

# 📚 API Documentation

Once the application is running, access Swagger UI:

👉 **http://localhost:8081/swagger-ui/index.html**

Swagger allows you to:

- Explore all REST APIs
- Test endpoints directly
- Authenticate using JWT

### Using Authorization

1. Register or Login using:

```
/api/users/login
```

2. Copy the returned **JWT token**

3. Click **Authorize** in Swagger

4. Paste the token like this:

```
Bearer <your_token>
```

---

# 🐳 Running with Docker (Recommended)

Docker ensures **consistent environments and easy deployment**.

---

## 1️⃣ Configure Aiven MySQL

Create a `.env` file in the **project root directory**.

Example:

```
SPRING_DATASOURCE_URL=jdbc:mysql://mysql-ecom-iiit-c3b7.f.aivencloud.com:23388/defaultdb?sslMode=REQUIRED
SPRING_DATASOURCE_USERNAME=avnadmin
SPRING_DATASOURCE_PASSWORD=AVNS_kJFgGwsE0pShWgFB6ZV
```

These environment variables will automatically be injected into the Spring Boot container.

---

## ⚠️ Important Note about Aiven

The **Aiven MySQL instance may automatically power off** due to inactivity.

If the backend fails to connect to the database:

1. Open **Aiven Console**
2. Navigate to your MySQL service
3. Click **Power On**
4. Wait until the service becomes **active**

Then restart the backend container if required.

---

## 2️⃣ Start the Application

From the project root directory:

```bash
docker compose up --build
```

This will start:

| Service | URL |
|------|------|
| Spring Boot API | http://localhost:8081 |
| MySQL | Hosted on Aiven Cloud |

Port **8081** is used to avoid conflicts with local applications running on **8080**.

---

## 3️⃣ Stop the Application

```
docker compose down
```

To fully reset containers and volumes:

```
docker compose down -v
```

---

# 🗄️ Database Setup

You must apply the schema manually to the Aiven MySQL database.

Run:

```sql
CREATE DATABASE IF NOT EXISTS ecommerce_db;
```

Then execute the provided:

```
schema.sql
```

using:

- MySQL client
- Aiven console
- MySQL Workbench

---

# 📦 Project Deliverables

This repository includes:

- ✅ Complete Source Code
- ✅ README.md (Project Documentation)
- ✅ Postman_Collection.json
- ✅ schema.sql (Database Schema)
- ✅ docker-compose.yml (Containerized Setup)

---

# 🎯 Key Learning Outcomes

This project demonstrates:

- Production-ready **Spring Boot backend architecture**
- **JWT Authentication & Security**
- **Dockerized deployments**
- **Cloud database integration (Aiven MySQL)**
- REST API documentation with **Swagger**

---

# 👨‍💻 Author

**Sudeep Kumar Dehury**  
B.Tech Computer Science Engineering
