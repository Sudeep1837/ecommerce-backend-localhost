# 🛒 E-Commerce Backend System

A scalable **backend system for an e-commerce platform** built using **Java 21, Spring Boot 3, and MySQL**.
The project demonstrates modern backend development practices including **secure authentication, layered architecture, RESTful API design, and containerized deployment**.

---

# 🚀 Features

## 👤 User Management

* User registration and login
* Secure authentication using **JWT**
* Role-based access control (**ADMIN / CUSTOMER**)
* Admin functionality to manage users

## 📦 Product Management

* Admin can **add, update, and delete products**
* Customers can browse the **product catalog**
* Support for **filtering and pagination**

## 🛒 Cart Management

* Add products to cart
* Update product quantities
* Remove items from cart
* Automatic cart total calculation

## 📑 Order Management

* Checkout functionality
* Order history tracking
* Order status management

## 📉 Inventory Management

* Product stock automatically updates after successful checkout

## 💳 Payment Processing

* Simulated payment workflow for order completion

---

# 🏗️ Tech Stack

| Layer             | Technology                  |
| ----------------- | --------------------------- |
| Language          | Java 21                     |
| Framework         | Spring Boot 3               |
| Database          | MySQL                       |
| ORM               | Spring Data JPA / Hibernate |
| Security          | Spring Security + JWT       |
| API Documentation | Swagger (OpenAPI 3)         |
| Object Mapping    | ModelMapper                 |
| Containerization  | Docker + Docker Compose     |

---

# 📂 Project Architecture

```
controller  → Handles REST API requests
service     → Business logic layer
repository  → Database access using JPA
entity      → Database entities
dto         → Request & response objects
config      → Security and application configuration
```

This **layered architecture** improves maintainability, scalability, and separation of concerns.

---

# 🗄️ Database Design (ER Diagram)

The following ER diagram represents the relationships between the main entities such as **Users, Products, Cart, Orders, and Order Items**.

<p align="center">
  <img src="docs/images/er-diagram.png" width="800"/>
</p>

---

# 🧪 Test Coverage (JaCoCo Report)

The project includes **unit tests with JaCoCo coverage reporting** to measure code coverage and maintain code quality.

<p align="center">
  <img src="docs/images/jacoco-report.png" width="800"/>
</p>

---

# 📚 API Documentation

After starting the application, open Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

Swagger provides an interactive interface where you can:

* Explore available APIs
* Send requests directly from the browser
* Authenticate using JWT tokens

### Authentication Steps

1. Login using:

```
POST /api/users/login
```

2. Copy the returned **JWT token**

3. Click **Authorize** in Swagger

4. Enter the token in this format:

```
Bearer <your_token>
```

---

# 🛠️ Running the Application Locally

## 1️⃣ Prerequisites

Ensure the following tools are installed:

* Java 21+
* Maven 3.8+
* MySQL Server
* Docker Desktop (optional)

---

## 2️⃣ Database Setup

Create the database:

```sql
CREATE DATABASE IF NOT EXISTS ecommerce_db;
```

Then run the provided `schema.sql` file to create required tables.

---

## 3️⃣ Configure Application Properties

Update database credentials in:

```
src/main/resources/application.properties
```

Example configuration:

```
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

---

## 4️⃣ Build the Project

```
./mvnw clean install
```

---

## 5️⃣ Run the Application

```
./mvnw spring-boot:run
```

The backend server will start at:

```
http://localhost:8080
```

---

# 🐳 Running with Docker

## Configure Environment Variables

Create a `.env` file in the project root:

```
SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/ecommerce_db
SPRING_DATASOURCE_USERNAME=YOUR_USERNAME
SPRING_DATASOURCE_PASSWORD=YOUR_PASSWORD
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

To remove containers and volumes:

```
docker compose down -v
```

---

# 📦 Project Deliverables

This repository contains:

* Complete **Spring Boot Backend Source Code**
* `README.md` documentation
* `Postman_Collection.json`
* `schema.sql`
* `docker-compose.yml`

---

# 🎯 Key Learning Outcomes

This project demonstrates:

* Secure **JWT-based authentication**
* Clean **layered backend architecture**
* REST API design with **Spring Boot**
* Database integration using **Spring Data JPA**
* Containerized backend deployment using **Docker**
* Code quality tracking using **JaCoCo test coverage**
