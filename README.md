# Submersible Navigation

A Spring Boot multi-module REST API for controlling and navigating a submersible probe on a configurable grid.
The system supports executing command sequences and automatically planning paths between two states while
avoiding obstacles.

---

## Table of Contents

- [Project Structure](#project-structure)
- [Architecture Overview](#architecture-overview)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Build the Project](#build-the-project)
- [Run the Application](#run-the-application)
- [Test the Project](#test-the-project)
- [API Reference](#api-reference)
- [Configuration](#configuration)
- [Swagger / OpenAPI UI](#swagger--openapi-ui)
- [Actuator & Monitoring](#actuator--monitoring)

---

## Project Structure

```
submersible-navigation/                         # Root (parent) Maven project
│
├── pom.xml                                     # Parent POM – manages shared dependencies & modules
├── mvnw / mvnw.cmd                             # Maven Wrapper scripts (Linux / Windows)
│
├── submersible-contract/                       # Module 1 – API Contract
│   ├── pom.xml
│   └── src/main/resources/
│       └── openapi.yaml                        # OpenAPI 3.0 specification (source of truth)
│
├── submersible-app/                            # Module 2 – Spring Boot Application
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/natwest/submersible/navigation/
│       │   │   ├── SubmersibleAppApplication.java          # Spring Boot entry point
│       │   │   ├── api/
│       │   │   │   ├── NavigationController.java           # POST /api/v1/navigation/execute
│       │   │   │   └── PathPlanningController.java         # POST /api/v1/navigation/plan
│       │   │   ├── service/
│       │   │   │   ├── NavigationService.java              # Execute navigation commands
│       │   │   │   ├── PathPlanningService.java            # Plan path to target state
│       │   │   │   ├── parser/
│       │   │   │   │   └── CommandParser.java              # Parse/convert command strings
│       │   │   │   ├── mapper/                             # DTO ↔ domain mappers
│       │   │   │   └── support/                            # Service support utilities
│       │   │   ├── domain/
│       │   │   │   ├── model/
│       │   │   │   │   ├── NavigationGrid.java             # Grid with dimensions & obstacles
│       │   │   │   │   ├── Position.java                   # (x, y) coordinate
│       │   │   │   │   ├── ProbeState.java                 # Position + Direction
│       │   │   │   │   └── enums/
│       │   │   │   │       ├── Command.java                # F, B, L, R
│       │   │   │   │       └── Direction.java              # NORTH, EAST, SOUTH, WEST
│       │   │   │   ├── strategy/
│       │   │   │   │   ├── MovementStrategy.java           # Strategy interface
│       │   │   │   │   └── SafeMovementStrategy.java       # Validates moves before applying
│       │   │   │   ├── validator/
│       │   │   │   │   ├── Validator.java                  # Validator interface
│       │   │   │   │   ├── BoundaryValidator.java          # Ensures probe stays in grid
│       │   │   │   │   ├── ObstacleValidator.java          # Prevents collision with obstacles
│       │   │   │   │   └── ValidatorChain.java             # Chains multiple validators
│       │   │   │   ├── context/                            # Navigation context helpers
│       │   │   │   ├── results/                            # Result wrappers
│       │   │   │   └── search/                             # Path-search algorithm support
│       │   │   ├── advisors/                               # Global exception handlers
│       │   │   └── exception/                              # Custom exceptions & error codes
│       │   └── resources/
│       │       └── application.yaml                        # Spring Boot configuration
│       └── test/
│           └── java/com/natwest/submersible/navigation/
│               ├── api/                                    # Controller (unit) tests
│               ├── service/                                # Service & parser/mapper tests
│               ├── domain/                                 # Domain model/strategy/validator tests
│               └── integration/                            # Full Spring context integration tests
│
└── pipeline/                                   # CI/CD pipeline scripts & documentation
    └── README.md
```

---

## Architecture Overview

```
Client
  │
  ▼
REST Controller  (NavigationController / PathPlanningController)
  │
  ▼
Service Layer    (NavigationService / PathPlanningService)
  │         │
  │         ▼
  │    CommandParser  ──►  Command enums (F, B, L, R)
  │
  ▼
Domain Layer
  ├── SafeMovementStrategy   ──►  ValidatorChain
  │                                 ├── BoundaryValidator
  │                                 └── ObstacleValidator
  ├── NavigationGrid  (width × height + obstacles)
  └── ProbeState      (Position + Direction)
```

The **contract module** (`submersible-contract`) owns the `openapi.yaml` and is packaged as a JAR.  
The **app module** unpacks the YAML at build time and uses the **OpenAPI Generator** Maven plugin to
auto-generate controller interfaces and request/response models — keeping the implementation in sync
with the spec.

---

## Technology Stack

| Technology               | Version   | Purpose                                  |
|--------------------------|-----------|------------------------------------------|
| Java                     | 17        | Language                                 |
| Spring Boot              | 4.0.5     | Application framework                    |
| Spring Web MVC           | —         | REST API                                 |
| Spring Boot Actuator     | —         | Health & metrics endpoints               |
| Micrometer (Prometheus)  | —         | Metrics export                           |
| Springdoc OpenAPI        | 2.7.0     | Swagger UI / API documentation           |
| OpenAPI Generator        | 7.7.0     | Code generation from openapi.yaml        |
| Lombok                   | —         | Boilerplate reduction                    |
| Jakarta Validation API   | 3.0.2     | Request validation                       |
| JUnit 5 / Spring Test    | —         | Unit & integration testing               |
| JsonUnit AssertJ         | 2.35.0    | JSON response assertions in tests        |
| Maven (Wrapper)          | 3.x       | Build tool                               |

---

## Prerequisites

| Requirement | Minimum Version |
|-------------|-----------------|
| JDK         | 17              |
| Maven       | 3.8+ *(or use the bundled `mvnw` wrapper)* |

> **Windows users:** use `mvnw.cmd` instead of `./mvnw` in all commands below.

---

## Build the Project

All commands below must be run from the **root directory** (`submersible-navigation/`).

### 1 – Clean & compile all modules

```bash
# Linux / macOS
./mvnw clean compile

# Windows
mvnw.cmd clean compile
```

### 2 – Package all modules (skip tests)

```bash
# Linux / macOS
./mvnw clean package -DskipTests

# Windows
mvnw.cmd clean package -DskipTests
```

### 3 – Full build (compile + test + package)

```bash
# Linux / macOS
./mvnw clean install

# Windows
mvnw.cmd clean install
```

The executable JAR is produced at:

```
submersible-app/target/submersible-app-0.0.1-SNAPSHOT.jar
```

> **Build order** is managed automatically by the parent POM:  
> `submersible-contract` → `submersible-app`

---

## Run the Application

### Option A – Run with Maven

```bash
# Linux / macOS
./mvnw spring-boot:run -pl submersible-app

# Windows
mvnw.cmd spring-boot:run -pl submersible-app
```

### Option B – Run the packaged JAR

```bash
java -jar submersible-app/target/submersible-app-0.0.1-SNAPSHOT.jar
```

The application starts on **http://localhost:8080/submersible-app**

---

## Test the Project

### Run all tests (unit + integration)

```bash
# Linux / macOS
./mvnw test

# Windows
mvnw.cmd test
```

### Run tests for a specific module only

```bash
# Linux / macOS
./mvnw test -pl submersible-app

# Windows
mvnw.cmd test -pl submersible-app
```

### Run a specific test class

```bash
# Linux / macOS
./mvnw test -pl submersible-app -Dtest=NavigationServiceTest

# Windows
mvnw.cmd test -pl submersible-app -Dtest=NavigationServiceTest
```

### Test reports

Surefire HTML/TXT reports are generated at:

```
submersible-app/target/surefire-reports/
```

### Test coverage overview

| Test Category        | Location                                      | Description                                             |
|----------------------|-----------------------------------------------|---------------------------------------------------------|
| Controller tests     | `api/NavigationControllerTest`                | Validates REST layer request/response mapping           |
| Controller tests     | `api/PathPlanningControllerTest`              | Validates path planning REST endpoints                  |
| Service tests        | `service/NavigationServiceTest`               | Unit tests for navigation command execution logic       |
| Service tests        | `service/PathPlanningServiceTest`             | Unit tests for path planning algorithm                  |
| Parser tests         | `service/parser/CommandParserTest`            | Validates command string parsing & conversion           |
| Mapper tests         | `service/mapper/*MapperTest`                  | DTO ↔ domain mapping correctness                        |
| Domain model tests   | `domain/model/NavigationGridTest`             | Grid construction and obstacle handling                 |
| Domain model tests   | `domain/model/PositionTest`                   | Position value-object behaviour                         |
| Domain model tests   | `domain/model/ProbeStateTest`                 | ProbeState transitions                                  |
| Strategy tests       | `domain/strategy/SafeMovementStrategyTest`    | Movement with boundary/obstacle validation              |
| Validator tests      | `domain/validator/BoundaryValidatorTest`      | Probe stays within grid boundaries                      |
| Validator tests      | `domain/validator/ObstacleValidatorTest`      | Probe does not collide with obstacles                   |
| Validator tests      | `domain/validator/ValidatorChainTest`         | Chaining multiple validators                            |
| Integration tests    | `integration/NavigationApiTest`               | Full Spring context – navigation endpoint               |
| Integration tests    | `integration/PathPlanningApiTest`             | Full Spring context – path planning endpoint            |

---

## API Reference

Base URL: `http://localhost:8080/submersible-app`

### POST `/api/v1/navigation/execute`

Execute a sequence of navigation commands on the probe.

**Request body:**
```json
{
  "grid": {
    "width": 10,
    "height": 10,
    "obstacles": [
      { "x": 3, "y": 3 }
    ]
  },
  "probeState": {
    "position": { "x": 0, "y": 0 },
    "direction": "NORTH"
  },
  "commands": "FFRFF"
}
```

**Command characters:**

| Character | Action        |
|-----------|---------------|
| `F`       | Move forward  |
| `B`       | Move backward |
| `L`       | Turn left     |
| `R`       | Turn right    |

**Response body (200 OK):**
```json
{
  "finalPosition": {
    "position": { "x": 2, "y": 2 },
    "direction": "EAST"
  },
  "status": "SUCCESS",
  "path": [
    { "x": 0, "y": 1 },
    { "x": 0, "y": 2 },
    { "x": 1, "y": 2 },
    { "x": 2, "y": 2 }
  ]
}
```

---

### POST `/api/v1/navigation/plan`

Automatically compute the command sequence needed to navigate the probe from its current state to a
target state.

**Request body:**
```json
{
  "grid": {
    "width": 10,
    "height": 10,
    "obstacles": []
  },
  "currentState": {
    "position": { "x": 0, "y": 0 },
    "direction": "NORTH"
  },
  "targetState": {
    "position": { "x": 3, "y": 3 },
    "direction": "EAST"
  }
}
```

**Response body (200 OK):**
```json
{
  "commands": "FFRFF",
  "status": "SUCCESS",
  "path": [
    { "x": 0, "y": 1 },
    { "x": 0, "y": 2 }
  ]
}
```

**Error responses:**

| HTTP Status | Scenario                                   |
|-------------|--------------------------------------------|
| `400`       | Invalid request parameters                 |
| `409`       | No valid path found (obstacles in the way) |

---

## Configuration

Key settings in `submersible-app/src/main/resources/application.yaml`:

```yaml
server:
  servlet:
    context-path: /submersible-app   # All endpoints are prefixed with this path

spring:
  application:
    name: submersible-app
  mvc:
    problemdetails:
      enabled: true                  # RFC 7807 problem details on errors

springdoc:
  swagger-ui:
    path: /swagger-ui.html           # Swagger UI path (relative to context-path)

management:
  endpoints:
    web:
      exposure:
        include: health, info, metrics, prometheus
```

---

## Swagger / OpenAPI UI

Once the application is running, visit:

```
http://localhost:8080/submersible-app/swagger-ui.html
```

The raw OpenAPI JSON is available at:

```
http://localhost:8080/submersible-app/v3/api-docs
```

---

## Actuator & Monitoring

| Endpoint                                                          | Description              |
|-------------------------------------------------------------------|--------------------------|
| `GET /submersible-app/actuator/health`                            | Application health check |
| `GET /submersible-app/actuator/info`                              | Application info         |
| `GET /submersible-app/actuator/metrics`                           | Metrics list             |
| `GET /submersible-app/actuator/prometheus`                        | Prometheus metrics scrape|

---

## Module Summary

| Module                  | Artifact ID              | Packaging | Purpose                                          |
|-------------------------|--------------------------|-----------|--------------------------------------------------|
| Root                    | `submersible-navigation` | `pom`     | Parent POM, dependency management, module list   |
| `submersible-contract`  | `submersible-contract`   | `jar`     | OpenAPI spec packaged as a JAR                   |
| `submersible-app`       | `submersible-app`        | `jar`     | Spring Boot application (executable JAR)         |

