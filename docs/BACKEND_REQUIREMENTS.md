# 5. Backend Requirements

> **[← Back to Main](../REQUIREMENTS.md)**

---

## 5.1 Technology Stack

| Component | Technology | Version | Purpose |
|-----------|-----------|---------|---------|
| **Framework** | Spring Boot | 3.0+ | Enterprise web framework |
| **Language** | Java | JDK 17+ | Programming language |
| **Build Tool** | Maven | 3.8+ | Project build automation |
| **Security** | Spring Security | 6.0+ | Authentication & authorization |
| **Data Access** | Spring Data JPA | Latest | Object-relational mapping |
| **ORM** | Hibernate | 6.0+ | Database abstraction layer |
| **REST API** | Spring Web | 6.0+ | RESTful API development |
| **Validation** | Spring Validation | Latest | Input validation framework |
| **JSON Processing** | Jackson | 2.15+ | JSON serialization/deserialization |
| **Testing** | JUnit 5 | Latest | Unit testing framework |
| **Mocking** | Mockito | 5.0+ | Mock object framework |
| **Server** | Embedded Tomcat | 10.0+ | Servlet container (via Spring Boot) |

---

## 5.2 Project Structure

**Spring Boot 3.x Maven Project**

```
backend/ (Maven project)
├── pom.xml                                    # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/com/college/eventmanager/
│   │   │   ├── EventManagerApplication.java   # Main Spring Boot entry point
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java        # Spring Security configuration
│   │   │   │   ├── CorsConfig.java            # CORS configuration
│   │   │   │   ├── JwtConfig.java             # JWT token configuration
│   │   │   │   └── WebConfig.java             # Additional web config
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java        # Authentication endpoints
│   │   │   │   ├── EventController.java       # Event management endpoints
│   │   │   │   ├── RegistrationController.java # Registration endpoints
│   │   │   │   ├── UserController.java        # User endpoints
│   │   │   │   └── AdminController.java       # Admin endpoints
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java           # Authentication business logic
│   │   │   │   ├── EventService.java          # Event business logic
│   │   │   │   ├── RegistrationService.java   # Registration business logic
│   │   │   │   ├── UserService.java           # User business logic
│   │   │   │   └── EmailService.java          # Email notifications (future)
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java        # Spring Data JPA repository
│   │   │   │   ├── EventRepository.java       # Spring Data JPA repository
│   │   │   │   └── RegistrationRepository.java # Spring Data JPA repository
│   │   │   ├── entity/
│   │   │   │   ├── User.java                  # JPA entity
│   │   │   │   ├── Event.java                 # JPA entity
│   │   │   │   ├── Registration.java          # JPA entity
│   │   │   │   └── Role.java                  # Enum for roles
│   │   │   ├── dto/
│   │   │   │   ├── LoginRequest.java          # Request DTO
│   │   │   │   ├── RegisterRequest.java       # Request DTO
│   │   │   │   ├── EventDTO.java              # Response DTO
│   │   │   │   ├── RegistrationDTO.java       # Response DTO
│   │   │   │   └── ApiResponse.java           # Standard API response wrapper
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java # @ControllerAdvice for exceptions
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── DuplicateEmailException.java
│   │   │   │   └── UnauthorizedException.java
│   │   │   ├── security/
│   │   │   │   ├── JwtTokenProvider.java      # JWT creation and validation
│   │   │   │   ├── JwtAuthenticationFilter.java # Filter for JWT validation
│   │   │   │   └── CustomUserDetailsService.java # Spring Security user details
│   │   │   └── util/
│   │   │       ├── AppConstants.java          # Constants
│   │   │       ├── ValidationUtil.java        # Validation helpers
│   │   │       └── DateUtil.java              # Date utilities
│   │   └── resources/
│   │       ├── application.properties         # Main configuration
│   │       ├── application-dev.properties     # Development profile
│   │       └── application-prod.properties    # Production profile
│   └── test/
│       └── java/com/college/eventmanager/
│           ├── controller/                    # Controller tests
│           ├── service/                       # Service tests
│           └── repository/                    # Repository tests
├── README.md
└── .gitignore
```

### Spring Boot Conventions

- **Annotation-based Configuration**: Uses `@Configuration` and `@Bean` annotations
- **Component Scanning**: Auto-detection of Spring components via `@Component`, `@Service`, `@Repository`
- **Embedded Server**: Tomcat runs as part of the Spring Boot application
- **Application Properties**: Configuration via `application.properties` or `application.yml`
- **Spring Data JPA**: Repositories extend `JpaRepository` for database operations
- **Spring Security**: Authentication/authorization configured via `SecurityConfig`
- **Exception Handling**: Global exception handler via `@ControllerAdvice`
- **Dependency Injection**: Constructor injection preferred over field injection

---

## 5.3 Core Entities & Database Models

### 5.3.1 User Entity

```
User
├── id (Long, PK)
├── email (String, UNIQUE, NOT NULL)
├── password (String, NOT NULL) - BCrypt hashed
├── firstName (String, NOT NULL)
├── lastName (String, NOT NULL)
├── phone (String)
├── department (String)
├── role (Enum: STUDENT, ADMIN) - NOT NULL
├── accountStatus (Enum: ACTIVE, INACTIVE, LOCKED) - NOT NULL
├── createdAt (LocalDateTime)
├── updatedAt (LocalDateTime)
└── registrations (One-to-Many → Registration)
```

### 5.3.2 Event Entity

```
Event
├── id (Long, PK)
├── title (String, NOT NULL)
├── description (String, NOT NULL)
├── category (String, NOT NULL)
├── startDateTime (LocalDateTime, NOT NULL)
├── endDateTime (LocalDateTime, NOT NULL)
├── location (String, NOT NULL)
├── capacity (Integer, NOT NULL) - > 0
├── registrationDeadline (LocalDateTime, NOT NULL)
├── imageUrl (String)
├── status (Enum: PENDING, APPROVED, REJECTED, CANCELLED, COMPLETED) - NOT NULL
├── rejectionReason (String) - if REJECTED
├── organizer (Many-to-One → User)
├── createdAt (LocalDateTime)
├── updatedAt (LocalDateTime)
└── registrations (One-to-Many → Registration)
```

### 5.3.3 Registration Entity

```
Registration
├── id (Long, PK)
├── student (Many-to-One → User) - NOT NULL
├── event (Many-to-One → Event) - NOT NULL
├── registrationDate (LocalDateTime, NOT NULL)
├── attendanceStatus (Enum: PENDING, ATTENDED, CANCELLED, REMOVED) - NOT NULL
├── cancellationReason (String) - if cancelled by student
├── removalReason (String) - if removed by admin
├── createdAt (LocalDateTime)
└── updatedAt (LocalDateTime)
```

### 5.3.4 Role Enum

```
Role
├── STUDENT
└── ADMIN
```

---

## 5.4 API Endpoints Specification

### 5.4.1 Authentication APIs

#### Register User
- **Endpoint**: `POST /api/auth/register`
- **Auth**: Public
- **Request Body**:
  ```json
  {
    "email": "student@college.edu",
    "firstName": "John",
    "lastName": "Doe",
    "password": "Password@123"
  }
  ```
- **Response** (201): `{ "message": "User registered successfully", "id": 1 }`
- **Response** (400): `{ "error": "Email already exists" }`

#### Login
- **Endpoint**: `POST /api/auth/login`
- **Auth**: Public
- **Request Body**:
  ```json
  {
    "email": "student@college.edu",
    "password": "Password@123"
  }
  ```
- **Response** (200):
  ```json
  {
    "accessToken": "jwt_token_here",
    "refreshToken": "refresh_token_here",
    "user": { "id": 1, "email": "...", "role": "STUDENT" }
  }
  ```

#### Refresh Token
- **Endpoint**: `POST /api/auth/refresh`
- **Auth**: Public
- **Request Body**: `{ "refreshToken": "token" }`
- **Response** (200): `{ "accessToken": "new_token" }`

#### Logout
- **Endpoint**: `POST /api/auth/logout`
- **Auth**: User (STUDENT or ADMIN)
- **Response** (200): `{ "message": "Logged out successfully" }`

---

### 5.4.2 Event APIs

#### Get All Events (with pagination & filters)
- **Endpoint**: `GET /api/events?page=0&size=10&category=Workshop&status=APPROVED`
- **Auth**: STUDENT
- **Response** (200):
  ```json
  {
    "content": [ { "id": 1, "title": "...", "date": "...", ... } ],
    "totalElements": 50,
    "totalPages": 5,
    "currentPage": 0,
    "size": 10
  }
  ```

#### Get Event by ID
- **Endpoint**: `GET /api/events/{id}`
- **Auth**: STUDENT
- **Response** (200): Full event details with registration count

#### Create Event (Admin only)
- **Endpoint**: `POST /api/events`
- **Auth**: ADMIN
- **Request Body**:
  ```json
  {
    "title": "Web Development Workshop",
    "description": "Learn modern web development...",
    "category": "Workshop",
    "startDateTime": "2026-04-01T10:00:00",
    "endDateTime": "2026-04-01T12:00:00",
    "location": "Room 101",
    "capacity": 50,
    "registrationDeadline": "2026-03-25T23:59:59",
    "imageUrl": "https://..."
  }
  ```
- **Response** (201): Event created with PENDING status

#### Update Event (Admin only)
- **Endpoint**: `PUT /api/events/{id}`
- **Auth**: ADMIN
- **Request Body**: Same as create (all fields optional for PATCH)
- **Response** (200): Updated event

#### Delete Event (Admin only)
- **Endpoint**: `DELETE /api/events/{id}`
- **Auth**: ADMIN
- **Response** (204): No content
- **Response** (400): Cannot delete if event has registrations

#### Approve Event (Admin only)
- **Endpoint**: `PATCH /api/events/{id}/approve`
- **Auth**: ADMIN
- **Response** (200): Event status changed to APPROVED

#### Reject Event (Admin only)
- **Endpoint**: `PATCH /api/events/{id}/reject`
- **Auth**: ADMIN
- **Request Body**: `{ "reason": "Inappropriate content" }`
- **Response** (200): Event status changed to REJECTED

#### Cancel Event (Admin only)
- **Endpoint**: `PATCH /api/events/{id}/cancel`
- **Auth**: ADMIN
- **Request Body**: `{ "reason": "Instructor unavailable" }`
- **Response** (200): Event status changed to CANCELLED

---

### 5.4.3 Registration APIs

#### Register for Event
- **Endpoint**: `POST /api/registrations`
- **Auth**: STUDENT
- **Request Body**: `{ "eventId": 1 }`
- **Response** (201): `{ "id": 1, "studentId": 1, "eventId": 1, "date": "..." }`
- **Response** (400): Event full / Already registered / Event cancelled / Deadline passed

#### Get My Registrations
- **Endpoint**: `GET /api/registrations/my-registrations?status=PENDING`
- **Auth**: STUDENT
- **Query Params**: `status` (optional: PENDING, ATTENDED, CANCELLED)
- **Response** (200): Array of registrations with event details

#### Cancel Registration
- **Endpoint**: `DELETE /api/registrations/{id}`
- **Auth**: STUDENT (who registered)
- **Request Body**: `{ "reason": "Schedule conflict" }`
- **Response** (204): No content
- **Response** (400): Cannot cancel if event already started

#### Get Event Registrations (Admin only)
- **Endpoint**: `GET /api/events/{eventId}/registrations?page=0&size=20`
- **Auth**: ADMIN
- **Response** (200): List of all registrations for event with student details

#### Mark Attendance (Admin only)
- **Endpoint**: `PATCH /api/registrations/{id}/mark-attended`
- **Auth**: ADMIN
- **Response** (200): Registration status changed to ATTENDED

#### Remove Student from Event (Admin only)
- **Endpoint**: `DELETE /api/registrations/{id}/remove`
- **Auth**: ADMIN
- **Request Body**: `{ "reason": "No-show" }`
- **Response** (204): No content

---

### 5.4.4 User APIs

#### Get User Profile
- **Endpoint**: `GET /api/users/profile`
- **Auth**: User (STUDENT or ADMIN)
- **Response** (200): User details including role, department, account status

#### Update User Profile
- **Endpoint**: `PUT /api/users/profile`
- **Auth**: User (STUDENT or ADMIN)
- **Request Body**:
  ```json
  {
    "firstName": "Jane",
    "lastName": "Smith",
    "phone": "9876543210",
    "department": "Computer Science"
  }
  ```
- **Response** (200): Updated user profile

#### Change Password
- **Endpoint**: `POST /api/users/change-password`
- **Auth**: User (STUDENT or ADMIN)
- **Request Body**:
  ```json
  {
    "currentPassword": "OldPass@123",
    "newPassword": "NewPass@456"
  }
  ```
- **Response** (200): `{ "message": "Password changed successfully" }`

#### Get All Users (Admin only)
- **Endpoint**: `GET /api/admin/users?page=0&size=20&search=john`
- **Auth**: ADMIN
- **Response** (200): Paginated list of users

#### Get User by ID (Admin only)
- **Endpoint**: `GET /api/admin/users/{id}`
- **Auth**: ADMIN
- **Response** (200): User details with registration history

#### Disable User Account (Admin only)
- **Endpoint**: `PATCH /api/admin/users/{id}/disable`
- **Auth**: ADMIN
- **Response** (200): User account status set to INACTIVE

#### Enable User Account (Admin only)
- **Endpoint**: `PATCH /api/admin/users/{id}/enable`
- **Auth**: ADMIN
- **Response** (200): User account status set to ACTIVE

---

### 5.4.5 Admin Dashboard APIs

#### Get Dashboard Statistics
- **Endpoint**: `GET /api/admin/dashboard/stats`
- **Auth**: ADMIN
- **Response** (200):
  ```json
  {
    "totalStudents": 150,
    "totalEvents": 25,
    "totalRegistrations": 500,
    "pendingApprovals": 3,
    "upcomingEvents": 8
  }
  ```

#### Get Events Trend (last 6 months)
- **Endpoint**: `GET /api/admin/dashboard/events-trend`
- **Auth**: ADMIN
- **Response** (200): Array with month and event count

#### Get Top Events by Registration
- **Endpoint**: `GET /api/admin/dashboard/top-events?limit=5`
- **Auth**: ADMIN
- **Response** (200): Array of top events

#### Get Recent Activity Log
- **Endpoint**: `GET /api/admin/dashboard/activity-log?limit=20`
- **Auth**: ADMIN
- **Response** (200): Recent system activity

---

## 5.5 Authentication & Security

### 5.5.1 JWT Token Specifications

- **Algorithm**: HS512 (HMAC with SHA-512)
- **Access Token Expiry**: 24 hours
- **Refresh Token Expiry**: 7 days
- **Claims**:
  - `sub` (subject): User email
  - `id`: User ID
  - `role`: User role (STUDENT/ADMIN)
  - `iat` (issued at): Timestamp
  - `exp` (expiration): Timestamp

### 5.5.2 Password Security

- **Hashing Algorithm**: BCrypt
- **Strength**: 10 rounds
- **Requirements**:
  - Minimum 8 characters
  - At least 1 uppercase letter
  - At least 1 lowercase letter
  - At least 1 digit
  - At least 1 special character

### 5.5.3 CORS Configuration

- **Allowed Origins**: http://localhost:3000 (dev), https://yourdomain.com (prod)
- **Allowed Methods**: GET, POST, PUT, DELETE, PATCH, OPTIONS
- **Allowed Headers**: Content-Type, Authorization
- **Credentials**: true
- **Max Age**: 3600 seconds

### 5.5.4 Request/Response Security

- All requests over HTTPS in production
- CSRF protection disabled (JWT-based, stateless)
- Rate limiting: 100 requests/minute per IP
- Input validation on all endpoints
- SQL injection prevention (JPA parameterized queries)

---

## 5.6 Error Handling

### Standard Error Response Format

```json
{
  "timestamp": "2026-03-15T10:30:00Z",
  "status": 400,
  "error": "Validation Error",
  "message": "Email format is invalid",
  "path": "/api/auth/register",
  "details": [
    {
      "field": "email",
      "message": "Must be a valid email address"
    }
  ]
}
```

### HTTP Status Codes

| Code | Meaning | When Used |
|------|---------|-----------|
| 200 | OK | Successful GET/PUT |
| 201 | Created | Successful POST |
| 204 | No Content | Successful DELETE |
| 400 | Bad Request | Invalid input/validation error |
| 401 | Unauthorized | Missing/invalid JWT token |
| 403 | Forbidden | User lacks permission |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Duplicate email / Capacity exceeded |
| 500 | Server Error | Unexpected error |

---

## 5.7 Logging & Monitoring

- **Logging Framework**: SLF4J + Logback
- **Log Levels**: DEBUG, INFO, WARN, ERROR
- **Log Format**: Timestamp, Level, Thread, Logger, Message
- **Log Files**:
  - Application logs: `/logs/application.log`
  - Error logs: `/logs/error.log`
  - Rotated daily, retention: 30 days
- **Monitoring**: Track API response times, error rates, database query performance

---

## 5.8 Testing Requirements

### Unit Tests
- Service layer tests (business logic)
- Repository tests (database queries)
- Utility/Helper function tests
- Target coverage: > 80%

### Integration Tests
- API endpoint tests
- Authentication tests
- Database integration tests

### Test Framework
- JUnit 5
- Mockito for mocking
- SpringBootTest for integration tests

