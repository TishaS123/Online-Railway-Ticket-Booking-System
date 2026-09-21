# 🚆 Online Railway Ticket Booking System

A production-oriented **microservices-based railway ticket booking platform** built with Java 21 and Spring Boot.

The system demonstrates real-world backend engineering concepts including **microservices architecture, concurrent seat allocation, optimistic locking, temporary seat locking, idempotent booking and payment workflows, fault-tolerant service communication, JWT/RBAC security, API Gateway routing, service discovery, Stripe payment integration, and application observability**.

---

## 📌 Project Overview

The Online Railway Ticket Booking System simulates a real-world railway reservation platform where users can:

- Search available trains
- View coaches and seats
- Reserve seats
- Make payments
- View booking details
- Cancel reservations
- Handle concurrent booking attempts safely
- Authenticate using JWT
- Perform role-based operations

The primary engineering goal of the project is not just CRUD-based ticket booking, but solving problems that occur in **distributed systems and concurrent transaction processing**.

---

## 🏗️ Architecture

```text
                         ┌──────────────────────┐
                         │     Angular UI       │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │    API Gateway       │
                         │  Spring Cloud       │
                         │      Gateway        │
                         └──────────┬───────────┘
                                    │
                  ┌─────────────────┼─────────────────┐
                  │                 │                 │
                  ▼                 ▼                 ▼
          ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
          │ Auth Service │  │ Train Service│  │ Reservation  │
          │              │  │              │  │   Service    │
          └──────────────┘  └──────┬───────┘  └──────┬───────┘
                                    │                 │
                                    │    OpenFeign    │
                                    └────────┬────────┘
                                             │
                                             ▼
                                    ┌─────────────────┐
                                    │ Payment Service │
                                    │  + Stripe       │
                                    └─────────────────┘

                         ┌──────────────────────┐
                         │   Eureka Server      │
                         │ Service Discovery    │
                         └──────────────────────┘

                         ┌──────────────────────┐
                         │       MySQL          │
                         └──────────────────────┘
# Service Responsibilities
| Service                 | Responsibility                                        |
| ----------------------- | ----------------------------------------------------- |
| **API Gateway**         | Central entry point and service routing               |
| **Auth Service**        | User authentication, JWT generation and authorization |
| **Train Service**       | Train, coach and seat management                      |
| **Reservation Service** | Booking, ticket, passenger and reservation workflows  |
| **Payment Service**     | Payment processing and Stripe integration             |
| **Eureka Server**       | Service discovery                                     |
| **Angular Frontend**    | User-facing booking application                       |

🧰 # Technology Stack
# Backend
Java 21
Spring Boot
Spring Security
Spring Data JPA
Hibernate
REST APIs
OpenFeign

**Microservices & Distributed Systems**
Spring Cloud Gateway
Netflix Eureka
Resilience4j
Service-to-service communication
Circuit Breaker
Idempotency
Optimistic Locking
Transaction Management

**Database**
MySQL
JPA/Hibernate

**Frontend**
Angular
TypeScript
HTML
CSS

**Payments**
Stripe

**Testing & API Development**
JUnit
Postman

**Observability**
Spring Boot Actuator
Resilience4j metrics

**Build & Version Control**
Maven
Git
GitHub

🔐 **Authentication & Authorization**

The application uses **Spring Security with JWT-based authentication**.

**Authentication Flow**
User Login
    │
    ▼
Auth Service
    │
    ├── Validate credentials
    │
    └── Generate JWT
           │
           ▼
       Angular Client
           │
           ▼
       API Gateway
           │
           ▼
    Protected Microservice

JWT contains the required user information and authorization role.

Role-based access control is implemented for protected operations such as administrative and passenger workflows.

🎟️ **Concurrent Seat Booking**

One of the main engineering challenges is preventing **double booking when multiple users attempt to reserve the same seat concurrently**.

The system uses:

JPA optimistic locking using @Version
Transactional seat updates
Temporary seat locking
Reservation identifiers
Lock expiration
Database-level consistency checks

**Seat Lifecycle**
AVAILABLE
    │
    │ Reservation request
    ▼
 LOCKED
    │
    ├───────────────┐
    │               │
 Payment Success    Payment Failure / Expiry
    │               │
    ▼               ▼
 BOOKED          AVAILABLE

The temporary LOCKED state prevents another reservation from taking the seat while payment is being processed.

**Optimistic Locking**

The seat entity maintains a version field:

@Version
private Long version;

When concurrent transactions attempt to modify the same seat, the JPA version check prevents stale updates from silently overwriting another transaction.

This allows the application to detect a concurrency conflict instead of allowing two users to successfully book the same seat.

🔄**Idempotent Booking & Payment**

Distributed systems can receive duplicate requests due to:

Client retries
Network timeouts
Delayed responses
Users repeatedly submitting requests
Intermediate service failures

The booking workflow therefore uses **idempotency keys**.

**Booking Flow**
Client Request
      │
      ▼
Idempotency Key
      │
      ▼
Reservation Service
      │
      ├── Already processed?
      │       │
      │       ├── YES → Return previous result
      │       │
      │       └── NO
      │
      ▼
Create Reservation
      │
      ▼
Lock Seats
      │
      ▼
Process Payment
      │
      ▼
Confirm / Release

The payment workflow also uses the reservation/idempotency information when interacting with Stripe to reduce the risk of duplicate payment processing.

💳 **Payment Processing**

Stripe is integrated as the external payment provider.

**Payment Flow**
Reservation
     │
     ▼
Seats LOCKED
     │
     ▼
Payment Request
     │
     ▼
Payment Service
     │
     ▼
Stripe
     │
 ┌───┴────────────┐
 │                │
Success          Failure
 │                │
 ▼                ▼
CONFIRMED      Release Seats
 │                │
 ▼                ▼
BOOKED         CANCELLED

The workflow is designed so that a failed payment does not leave seats permanently unavailable.

🛡️ **Fault Tolerance with Resilience4j**

Payment processing depends on a downstream service, so the Reservation Service uses **Resilience4j Circuit Breaker.**

**Circuit States**
       failures exceed threshold
CLOSED ─────────────────────────► OPEN
  ▲                                │
  │                                │ wait duration
  │                                ▼
  └────────────── HALF_OPEN ◄──────┘
                  │
             test requests

The configuration includes:

Failure-rate threshold
Slow-call threshold
Sliding window
Minimum number of calls
Open-state wait duration
Half-open permitted calls
Automatic state transition
Fallback handling

OpenFeign connection and read timeouts are also configured so that downstream failures do not cause requests to wait indefinitely.

📊 **Observability**

Spring Boot Actuator is used to expose application health and operational metrics.

Monitoring includes:

Application health
Service availability
Circuit breaker state
Circuit breaker metrics
Failure rates
Service-level operational information

This provides visibility into the behavior of the microservices and downstream dependencies.

⚡ **Performance Optimization**

The application was profiled and optimized at the database and application layers.

Optimization areas included:

MySQL query optimization
JPA data access
Entity relationships
Reducing unnecessary data retrieval
Improving database interaction patterns

**Result**
Performance testing demonstrated approximately:

**~33% reduction in API latency**

The improvement was measured after optimizing the relevant MySQL/JPA data-access paths.

🔁 **Reservation Lifecycle**
User
 │
 ▼
Search Train
 │
 ▼
Select Seat
 │
 ▼
Create Reservation
 │
 ▼
Lock Seat
 │
 ▼
Process Payment
 │
 ├───────────────┐
 │               │
Success          Failure
 │               │
 ▼               ▼
Confirm       Release Seat
 │               │
 ▼               ▼
BOOKED        CANCELLED

Expired temporary seat locks are also cleaned up so that abandoned reservations do not permanently consume available inventory.

🧩 **Key Engineering Challenges**
1. **Preventing Double Booking**

Solved using optimistic locking, transactional state transitions, and temporary seat locks.

2. **Duplicate Requests**

Solved through idempotency keys and controlled processing of repeated requests.

3. **Payment Service Failure**

Handled using Circuit Breaker, Feign timeouts, fallback processing, and seat-release workflows.

4. **Distributed Service Communication**

Implemented through API Gateway, Eureka service discovery, and OpenFeign.

5. **Database Performance**

Improved through query/data-access optimization and reducing unnecessary database retrieval.

📂 **Project Structure**
OnlineRailwayTicketBookingSystem/
│
├── Frontend/
│
├── Auth-Microservice/
│
├── Train-Microservice/
│
├── Reservation-Microservice/
│
├── PaymentGateway-Microservice/
│
├── API-Gateway/
│
├── Eureka-Server/
│
├── pom.xml
└── README.md

🚀** Getting Started**
**Prerequisites**

Install the following:

Java 21
Maven
Node.js
Angular CLI
MySQL
Git

Verify installations:

java -version
mvn -version
node -v
npm -v
ng version
⚙️ **Database Configuration**

Create a MySQL database for the application.

Configure the database connection in the relevant Spring Boot service configuration:

spring.datasource.url=jdbc:mysql://localhost:3306/<database>
spring.datasource.username=<username>
spring.datasource.password=<password>

Never commit real database credentials, API keys, Stripe secrets, or other sensitive configuration to GitHub.

▶️ **Running the Backend**

Start the services in the following order:

1. Eureka Server
2. API Gateway
3. Auth Service
4. Train Service
5. Reservation Service
6. Payment Service

For each Spring Boot service:

mvn spring-boot:run

Or build and run:

mvn clean package
java -jar target/<service-name>.jar

▶️ **Running the Angular Frontend**

Navigate to the frontend:

cd Frontend

Install dependencies:

npm install

Start the application:

ng serve

The application can then be accessed through the configured Angular development server.

🧪 **Testing**

The project can be tested using:

JUnit
Spring Boot testing
Postman
Concurrent booking requests
Payment failure scenarios
Circuit breaker failure scenarios
Duplicate/idempotent requests

**Important Test Scenarios**
✓ Successful booking
✓ Concurrent booking for the same seat
✓ Optimistic locking conflict
✓ Expired seat lock
✓ Successful payment
✓ Failed payment
✓ Payment service unavailable
✓ Circuit breaker OPEN state
✓ Repeated idempotent request
✓ JWT authentication failure
✓ Unauthorized role access

🔭 **Future Improvements**

The project is designed to be extended with additional production capabilities such as:

Docker containerization
CI/CD automation
Cloud deployment
Distributed tracing
Centralized logging
Redis-backed distributed idempotency
Message-driven asynchronous workflows
Centralized configuration
Advanced monitoring and alerting
Load testing and capacity analysis

🎯 **Engineering Focus**

This project was built to explore practical backend engineering challenges rather than only CRUD functionality.

The major engineering areas demonstrated are:

**Microservices Architecture
Distributed Systems
Concurrency Control
Optimistic Locking
Idempotency
Fault Tolerance
API Reliability
Database Optimization
Security & RBAC
Payment Integration
Observability**

👨‍💻 **Author**

**Tisha Sorte**

Software Engineer | Java | Spring Boot | Microservices | Distributed Systems

⭐ **Project Highlights**
| **Area**          | **Implementation**              |
| ----------------- | ------------------------------- |
| Architecture      | Microservices                   |
| Backend           | Java 21 + Spring Boot           |
| Service Discovery | Eureka                          |
| API Gateway       | Spring Cloud Gateway            |
| Communication     | OpenFeign                       |
| Database          | MySQL                           |
| Security          | Spring Security + JWT + RBAC    |
| Concurrency       | Optimistic Locking + Seat Locks |
| Reliability       | Resilience4j Circuit Breaker    |
| Idempotency       | Booking & Payment Workflows     |
| Payments          | Stripe                          |
| Observability     | Spring Boot Actuator            |
| Frontend          | Angular                         |
| Performance       | ~33% API latency reduction      |
