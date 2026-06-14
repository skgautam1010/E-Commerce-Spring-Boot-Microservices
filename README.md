# E-Commerce Microservices Platform

## Overview

A production-style E-Commerce backend built using Spring Boot Microservices architecture.

The system is designed around independent business domains and demonstrates:

* Microservices Architecture
* API Gateway Pattern
* Service Discovery
* Event-Driven Communication using Kafka
* Distributed Inventory Reservation
* Payment Processing Integration
* Notification Processing
* JWT Authentication & Authorization
* Distributed Tracing

---

# Architecture

```text
                                             +------------------+
                     |    API Gateway   |
                     +--------+---------+
                              |
    ---------------------------------------------------------
    |           |            |          |          |         |
    |           |            |          |          |         |
+---v---+ +-----v----+ +-----v----+ +---v----+ +---v----+ +-v----------+
| User  | | Product  | |Inventory | | Order  | |Payment | |Notification|
|Service| | Service  | | Service  | |Service | |Service | |  Service   |
+--------+ +----------+ +----------+ +--------+ +--------+ +------------+

                         +----------------+
                         |     Kafka      |
                         +----------------+

Order Service --------> Kafka --------> Inventory Service

Order Service --------> Kafka --------> Notification Service
Payment Service ------> Kafka --------> Notification Service

Order Service --------> Inventory Service (Feign)

Payment Service ------> Order Service (Feign)

Order Service --------> Product Service (Feign)

Order Service --------> User Service (Feign)
Payment Service --------> User Service (Feign)


```

---

# Microservices

## API Gateway

Responsibilities:

* Single entry point
* JWT validation
* Route forwarding
* Internal header propagation
* Public endpoint management

Internal headers propagated:

```text
X-User-Id
X-User-Role
```

---

## Eureka Server

Responsibilities:

* Service discovery
* Service registration
* Load-balanced communication

---

## User Service

Responsibilities:

* User registration
* Login
* JWT generation
* User profile management

JWT Claims:

```json
{
  "sub": "userId",
  "email": "user@email.com",
  "role": "ROLE_USER"
}
```

---

## Product Service

Responsibilities:

* Product catalog
* Product management
* Product image management

---

## Inventory Service

Responsibilities:

* Inventory management
* Stock reservation
* Reservation confirmation
* Reservation release

Inventory Model:

```text
quantity
reservedQuantity
```

Available Stock Formula:

```text
availableStock = quantity - reservedQuantity
```

---

## Order Service

Responsibilities:

* Order creation
* Inventory reservation
* Order confirmation
* Order failure handling
* Kafka event publishing

Order Status:

```text
PENDING_PAYMENT
PAID
FAILED
CANCELLED
```

---

## Payment Service

Responsibilities:

* Payment creation
* Payment status management
* Razorpay integration
* Webhook processing

Payment Status:

```text
PENDING
SUCCESS
FAILED
CANCELLED
```

---

## Notification Service

Responsibilities:

* Email notifications
* SMS notifications
* Kafka notification consumption

Integrations:

* Java Mail Sender
* Twilio SMS

---

# Technology Stack

## Backend

* Java 21
* Spring Boot 4.0.5
* Spring Cloud 2025.1.1

## Database

* MySQL

## Communication

* OpenFeign
* Apache Kafka

## Security

* Spring Security
* JWT Authentication

## Observability

* Micrometer Tracing
* Zipkin

## Messaging

* Kafka

## Notifications

* Java Mail Sender
* Twilio

## Payment Gateway

* Razorpay

---

# Database Design

## User Service

```text
users
```

---

## Product Service

```text
products
```

---

## Inventory Service

```text
inventory
processed_orders
```

---

## Order Service

```text
orders
```

---

## Payment Service

```text
payments
```

---

# Kafka Topics

## order-inventory-events

Purpose:

```text
Inventory confirmation
Inventory release
```

Published By:

```text
Order Service
```

Consumed By:

```text
Inventory Service
```

---

## notification-topic

Purpose:

```text
Email notifications
SMS notifications
```

Published By:

```text
Order Service
Payment Service
```

Consumed By:

```text
Notification Service
```

---

# Inventory Reservation Flow

## Order Creation

```text
Create Order
      |
      |
Reserve Inventory
      |
      |
Payment Pending
```

---

## Payment Success

```text
Payment Success
       |
       |
Order Confirmed
       |
       |
Kafka Event
       |
       |
Inventory Confirm Reservation

quantity          -= orderedQuantity
reservedQuantity  -= orderedQuantity
```

---

## Payment Failure

```text
Payment Failure
       |
       |
Order Failed
       |
       |
Kafka Event
       |
       |
Inventory Release Reservation

reservedQuantity -= orderedQuantity
```

---

# Security Architecture

## JWT Validation

JWT validation occurs only at API Gateway.

After validation:

```text
X-User-Id
X-User-Role
```

are forwarded to downstream services.

---

## Direct Service Access Protection

Internal services reject requests not coming through API Gateway.

Validation Header:

```text
X-User-Role
```

Requests bypassing Gateway are blocked.

---

# Distributed Tracing

Zipkin integration provides:

```text
Trace ID
Span ID
```

Request tracing across:

```text
Gateway
Order
Inventory
Payment
Notification
```

---

# Running the Project

## Start Order

```text
1. MySQL
2. Kafka
3. Zipkin
4. Eureka Server
5. API Gateway
6. User Service
7. Product Service
8. Inventory Service
9. Order Service
10. Payment Service
11. Notification Service
```

---

# Future Enhancements

* Docker Compose
* Kubernetes Deployment
* Resilience4j
* Circuit Breakers
* Retry Mechanisms
* Distributed Cache
* ELK Stack
* Prometheus
* Grafana
* Full Frontend Integration
* Payment Refunds
* Order History
* Admin Dashboard

---

# Key Learning Areas Demonstrated

* Microservices Design
* Event Driven Architecture
* Inventory Reservation Pattern
* Payment Processing Workflow
* Service Discovery
* API Gateway
* JWT Security
* Kafka Messaging
* Distributed Tracing
* Notification Processing
* Feign Communication
* Domain Driven Service Separation

---

# Author

Built as a production-style learning project focused on real-world E-Commerce backend architecture using Spring Boot Microservices.