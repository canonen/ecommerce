# 🛒 E-Commerce Platform

A production-ready microservices e-commerce backend built with Java 21 and Spring Boot 3.

## 🏗️ Architecture

```
┌─────────────────────────────────────────┐
│              API Gateway                │
└──────┬──────┬──────┬──────┬────────────┘
       │      │      │      │
  User  Product Order Payment Notification
Service Service Service Service  Service
```

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.4 |
| Database | PostgreSQL 16 |
| Cache | Redis 7 |
| Messaging | Apache Kafka |
| Migration | Flyway |
| Testing | JUnit 5, Mockito, Testcontainers |
| Container | Docker & Docker Compose |

## 🚀 Getting Started

### Prerequisites
- Docker & Docker Compose
- Java 21
- Maven

### Run Infrastructure
```bash
docker-compose up -d
```

### Run Services
```bash
cd user-service && mvn spring-boot:run
```

## 📐 Design Patterns
- **Hexagonal Architecture** — Order & Payment services
- **Layered Architecture** — User, Product, Notification services
- **Saga Pattern** — Distributed transaction management via Kafka
- **CQRS** — (planned)

## 📌 Services

| Service | Port | Description |
|---|---|---|
| api-gateway | 8080 | Single entry point |
| user-service | 8081 | Auth & user management |
| product-service | 8082 | Product catalog |
| order-service | 8083 | Order management |
| payment-service | 8084 | Payment processing |
| notification-service | 8085 | Email/SMS alerts |
