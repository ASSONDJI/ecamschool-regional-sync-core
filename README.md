# E-Camschool Regional Sync Core

## Overview
Spring Boot backend for E-Camschool regional data synchronization system.

## Context
As part of the Regional Delegation of Secondary Education (Centre Region), this system manages:
- Data matrix persistence
- Client-server synchronization
- Data projection for missing information

## Problem Statement
The current E-Camschool application suffers from data loss during network interruptions, forcing users to re-enter information multiple times.

## Technology Stack
- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- MySQL 8.0
- Maven
- Docker

## Project Structure
 ```
ecamschool-regional-sync-core/
├── src/
│ ├── main/
│ │ ├── java/com/ecamschool/regional/
│ │ │ └── RegionalSyncCoreApplication.java
│ │ └── resources/
│ │ └── application.yaml
│ └── test/
├── docker-compose.yml
├── pom.xml
└── README.md
 ```
## Getting Started

### Prerequisites
- Java 17+
- Docker Desktop
- DBeaver (or any MySQL client)
- Maven 3.8+

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/ASSONDJI/ecamschool-regional-sync-core.git
   cd ecamschool-regional-sync-core
2. **Start MySQL with Docker**

```docker-compose up -d```

3. **Run the application**

```mvn spring-boot:run```

## Database Configuration

The database is configured using Spring profiles:
- Default profile uses environment variables or `application-local.yaml`
- See `application-local.yaml.example` for reference

### Local Development Setup

1. Create `src/main/resources/application-local.yaml`
2. Add your database credentials:

```yaml
spring:
   datasource:
      url: jdbc:mysql://localhost:3306/ecamschool_regional_db
      username: your_username
      password: your_password
```
## API Documentation

Once the application is running, access the OpenAPI documentation at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI spec: http://localhost:8080/v3/api-docs

## Database Schema

### Users
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| username | VARCHAR(50) | Unique username |
| email | VARCHAR(100) | Unique email |
| password | VARCHAR(255) | Encrypted password |
| role | VARCHAR(50) | ADMIN, DELEGUE, CHEF_ETABLISSEMENT |
| full_name | VARCHAR(100) | Full name |
| delegation_region | VARCHAR(50) | Region (CENTRE, etc.) |
| department | VARCHAR(50) | Department |
| is_active | BOOLEAN | Active status |
| created_at | TIMESTAMP | Creation date |
| last_login_at | TIMESTAMP | Last login |

### Educational Establishments
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| code | VARCHAR(50) | Unique school code |
| name | VARCHAR(200) | School name |
| city | VARCHAR(100) | City |
| department | VARCHAR(50) | Department |
| delegation_region | VARCHAR(50) | Region |
| type | VARCHAR(20) | PUBLIC, PRIVATE |
| is_active | BOOLEAN | Active status |
| created_at | TIMESTAMP | Creation date |

### Data Matrices
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| client_key | VARCHAR(255) | Unique client-side key |
| data_value | TEXT | JSON data |
| sync_status | VARCHAR(20) | PENDING, SYNCED, FAILED |
| created_at | TIMESTAMP | Creation date |
| synced_at | TIMESTAMP | Sync date |
| version | INT | Data version |
| error_message | TEXT | Error details |
| data_type | VARCHAR(50) | TEACHER_NEED, SCHOOL_STATS, etc. |
| user_id | BIGINT | Foreign key to users |
| establishment_id | BIGINT | Foreign key to educational_establishments |

## Configuration

### Sensitive Data
Create a `application-local.yaml` file in `src/main/resources/` with your local configuration:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecamschool_regional_db
    username: your_username
    password: your_password
```    

### This file is ignored by Git for security.

Branch Strategy
main - Production (stable releases)

develop - Development branch (all features merged here)

feature/* - Feature branches (naming: feature/yourName-feature-name)

### Contributors
**MALAIKA LADEESSE ASSONDJI** - Lead Developer