# 2. Overall Description

> **[← Back to Main](../REQUIREMENTS.md)**

---

## 2.1 Product Perspective
## 2.4 Operating Environment

| Component        | Requirement                                  |
|------------------|----------------------------------------------|
| **Operating System** | Windows 10+ / Ubuntu 20.04+ / macOS 11+ |
| **Browser** | Chrome 110+, Firefox 110+, Safari 14+, Edge 110+ |
| **Java Runtime** | JDK 17 or higher (for backend) |
| **Node.js Runtime** | Node.js 18+ (for React build), any modern browser for frontend runtime |
| **Database** | MySQL 8.0+ server |
| **Backend Server** | Embedded Apache Tomcat (via Spring Boot 3.x) |
| **Frontend Build** | Vite or Create React App (during development) |
| **Network** | HTTP/HTTPS (HTTPS required in production) |
| **Frontend Serving** | Any web server (Nginx, Apache) or CDN |

### Development Environment

- **Backend Development**: JDK 17+, Maven 3.8+, IDE with Spring Boot support
- **Frontend Development**: Node.js 18+, npm 8.0+, VS Code or similar
- **Database**: MySQL 8.0+ server running locally or remote
- **Build & Test**: Maven for backend, npm for frontend, continuous integration pipeline

### Production Environment

- **Backend**: Spring Boot embedded Tomcat on server with JDK 17+
- **Frontend**: Static React bundle served by web server or CDN
- **Database**: MySQL 8.0+ on dedicated database server
- **Load Balancer**: Nginx or HAProxy for distributing traffic
- **Reverse Proxy**: Nginx for serving static files and proxying API requestsege Event Management System** is a standalone full-stack web application consisting of:

- A **React.js 18+** Single Page Application (SPA) served as the frontend
- A **Spring Boot 3.x** REST API (Java-based) as the backend
- A **MySQL 8.0+** relational database for persistent storage
- **JWT-based** stateless authentication

The React frontend communicates with the Spring Boot backend exclusively through RESTful HTTP API calls (JSON). There is no server-side rendering. The Spring Boot backend is stateless and horizontally scalable.

```
[ React.js SPA (Browser) ]
         |
         | HTTP/REST (JSON)
         ↓
[ Spring Boot 3.x REST API (Java) ]
         |
         | JDBC / Hibernate / Spring Data JPA
         ↓
    [ MySQL 8.0+ Database ]
```

---

## 2.2 Product Functions

The system provides the following high-level functions:

### Student Functions
- Register a new account
- Login and receive a JWT token
- View all approved events
- Search and filter events by category, date, or keyword
- Register for an event (subject to capacity)
- View personal event registrations and their statuses
- Cancel an event registration
- View profile and update personal information

### Admin Functions
- Login with admin credentials
- Create new events with full details
- Edit or delete existing events
- Approve or reject pending events
- View all registered users
- View registrations per event
- Remove a student from an event
- View dashboard statistics (total events, total users, registrations)

---

## 2.3 User Classes and Characteristics

### 2.3.1 Admin

| Attribute   | Details                                              |
|-------------|------------------------------------------------------|
| Description | College staff member who manages the system          |
| Access      | Full access to all system features                   |
| Auth        | Login with email + password, receives JWT with ADMIN role |
| Created By  | Pre-seeded in the database (no self-registration)    |

**Admin Permissions:**

| Permission                  | Allowed |
|-----------------------------|---------|
| Login                       | ✅      |
| Create events               | ✅      |
| Edit events                 | ✅      |
| Delete events               | ✅      |
| Approve / Reject events     | ✅      |
| View all users              | ✅      |
| View all registrations      | ✅      |
| Remove student from event   | ✅      |
| Register for events         | ❌      |

---

### 2.3.2 Student / User

| Attribute   | Details                                              |
|-------------|------------------------------------------------------|
| Description | College student who participates in events           |
| Access      | Limited to student-facing features                   |
| Auth        | Self-registers, then logs in with email + password   |
| Role        | JWT with STUDENT role                                |

**Student Permissions:**

| Permission                  | Allowed |
|-----------------------------|---------|
| Self-register account       | ✅      |
| Login                       | ✅      |
| View approved events        | ✅      |
| Search / filter events      | ✅      |
| Register for an event       | ✅      |
| Cancel event registration   | ✅      |
| View own registrations      | ✅      |
| Update own profile          | ✅      |
| Create / approve events     | ❌      |
| View other users' data      | ❌      |

---

## 2.4 Operating Environment

| Component        | Requirement                              |
|------------------|------------------------------------------|
| Operating System | Windows 10+ / Ubuntu 20.04+              |
| Browser          | Chrome 110+, Firefox 110+, Edge 110+     |
| Backend Runtime  | JDK 17 or higher                         |
| Frontend Runtime | Node.js 18+ (for build), any modern browser for runtime |
| Database         | MySQL 8.0+                               |
| Backend Server   | Embedded Apache Tomcat (via Spring Boot) |
| Network          | HTTP/HTTPS                               |

---

## 2.5 Design Constraints

- **Spring Boot 3.x or higher** MUST be used for the backend REST API
- **React.js 18.0 or higher** MUST be used for the frontend SPA
- **MySQL 8.0 or higher** MUST be used as the relational database
- **JWT (JSON Web Tokens)** MUST be used for stateless authentication (no server-side sessions)
- **Spring Security 6.x+** MUST be used for route protection and role-based access control
- **Spring Data JPA** MUST be used for database access (no raw JDBC)
- **Hibernate ORM** provides the JPA implementation
- **CORS** MUST be configured to allow the React frontend to communicate with the Spring Boot backend
- All API responses MUST use **JSON** format
- Passwords MUST be stored using **BCrypt** hashing (minimum 10 rounds) — never plain text
- The frontend MUST be a **Single Page Application** (SPA) with React Router — no full page reloads
- The system MUST enforce **role-based access control (RBAC)** on all API endpoints
- Spring Boot MUST use **embedded Apache Tomcat** (default) as the servlet container
- React MUST be built with **Vite** or **Create React App** build tool

---

## 2.6 Assumptions and Dependencies

> See full details in [Assumptions & Dependencies](./ASSUMPTIONS_DEPENDENCIES.md)

- JDK 17+ is installed on the deployment server
- Node.js 18+ is available for building the React frontend
- MySQL 8.x server is running and accessible
- The frontend and backend may be deployed on the same or different servers
- SMTP server is available for future email notification support
