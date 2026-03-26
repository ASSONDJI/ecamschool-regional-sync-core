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
**Host: localhost:3306**

**Database: ecamschool_regional_db**

**Username: root**

**Password: root123**

