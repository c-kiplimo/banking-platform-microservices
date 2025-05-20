# Banking Platform Microservices Assessment

![Microservices Architecture](https://img.shields.io/badge/Architecture-Microservices-blue)
![Domain Driven Design](https://img.shields.io/badge/Design-Domain%20Driven-orange)
![Spring Boot](https://img.shields.io/badge/Framework-Spring%20Boot-green)
![Feign Client](https://img.shields.io/badge/Communication-Feign%20Client-purple)
![Database Migration](https://img.shields.io/badge/Migration-Flyway-red)
![Testing](https://img.shields.io/badge/Testing-JUnit5%20%7C%20Mockito-success)

## Overview

This project implements a banking platform that enables customers to access card services using a microservices
architecture. The system is built with Domain-Driven Design principles and consists of several interconnected services.

## Architecture

![System Architecture Diagram](architecture-diagram.png)

The platform consists of the following components:

1. **Discovery Service**: Service registration and discovery (Eureka Server)
2. **API Gateway**: Central entry point for all client requests
3. **Customer Service**: Manages customer biodata
4. **Account Service**: Handles customer account information
5. **Card Service**: Manages card-related operations

## Services

### 1. Customer Service

- Manages customer biodata with CRUD operations
- Fields:
    - Customer ID (auto-generated)
    - First Name (mandatory)
    - Last Name (mandatory)
    - Other Name (optional)

### 2. Account Service

- Manages customer account information
- Fields:
    - Account ID (auto-generated)
    - IBAN
    - BIC/SWIFT code
    - Customer ID (foreign key)

### 3. Card Service

- Manages card operations with sensitive data protection
- Fields:
    - Card ID (auto-generated, non-editable)
    - Card Alias (editable)
    - Account ID (foreign key, non-editable)
    - Card Type (VIRTUAL/PHYSICAL, non-editable)
    - PAN (masked by default)
    - CVV (masked by default)

## Technical Stack

- **Framework**: Spring Boot 3.x
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Inter-service Communication**: OpenFeign
- **Database**: PostgreSQL
- **Database Migration**: Flyway
- **Testing**:
    - JUnit 5 (Business and Infrastructure layers)
    - Mockito
    - Testcontainers (Integration tests)
- **Build Tool**: Maven
- **Shared Library**: [c-kiplimo/shared](https://github.com/c-kiplimo/shared)

## Key Features

1. **Comprehensive Testing Strategy**:
    - Business logic tests with JUnit 5
    - Infrastructure layer tests (repositories, controllers)
    - Integration tests with Testcontainers
    - Test coverage for all critical paths

2. **Pagination and Filtering**:
    - Customers: Full-text name search, date created range
    - Accounts: Filter by IBAN, BIC/SWIFT, Card Alias
    - Cards: Filter by Card Alias, Type, and PAN

3. **Data Masking**:
    - PAN and CVV are masked by default in API responses
    - Optional query parameter `?showSensitive=true` to unmask

4. **Database Versioning**:
    - Flyway migrations for all database changes
    - Version-controlled SQL scripts
    - Repeatable migrations for reference data

5. **Business Rules**:
    - Each customer can have multiple accounts
    - Accounts cannot belong to more than one customer
    - An account can have maximum 2 cards (1 physical, 1 virtual)

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.4+
- PostgreSQL 16+
- Docker (for Testcontainers and optional deployment)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/c-kiplimo/banking-platform-microservices
   cd banking-platform-microservices