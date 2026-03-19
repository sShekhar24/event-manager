# 9. Assumptions & Dependencies

> **[← Back to Main](../REQUIREMENTS.md)**

---

## 9.1 Assumptions

### 9.1.1 Technical Assumptions

- **JDK Availability**: JDK 17 or higher is installed on all deployment servers
- **Node.js Availability**: Node.js 18+ is available for frontend build process
- **MySQL Server**: MySQL 8.0+ is running and accessible
- **Network Connectivity**: Stable network connection between frontend, backend, and database
- **HTTPS Support**: Production environment has valid SSL/TLS certificates
- **Git Repository**: Project code stored in Git version control system

### 9.1.2 Business Assumptions

- **Single College**: System manages events for a single college
- **Single Language**: All system content in English (no multi-language support)
- **Working Hours**: Support available during business hours (9 AM - 5 PM)
- **Annual Renewal**: System will be reviewed and updated annually
- **Admin Availability**: At least one admin user available to approve events
- **User Participation**: Students register voluntarily (no forced/automated registration)
- **Email Optional**: Email notifications are optional (not critical for MVP)

### 9.1.3 User Assumptions

- **Stable User Base**: Users have consistent email addresses throughout registration
- **Tech Savvy**: Students have basic computer literacy and browser navigation skills
- **Device Access**: Users have access to modern web browser (Chrome, Firefox, Safari, Edge)
- **Internet Connection**: Users have reliable internet connection
- **Account Security**: Users maintain password security and don't share credentials

### 9.1.4 Timeline Assumptions

- **Academic Calendar**: Events are primarily scheduled during academic year
- **Off-Season**: Minimal activity during summer/winter breaks
- **Event Seasonality**: Peak registration periods align with semester start

---

## 9.2 Dependencies

### 9.2.1 External Dependencies

#### Technology Stack
- **Spring Boot 3.x** - Web framework and dependency injection
- **React.js 18+** - Frontend UI library
- **MySQL 8.0+** - Relational database
- **Apache Maven** - Java build tool
- **npm/Node.js** - JavaScript package manager
- **Java JDK 17+** - Java runtime and compilation

#### Third-Party Libraries

**Backend (Spring Boot 3.x)**
| Library | Purpose | Version |
|---------|---------|---------|
| Spring Security | Authentication & Authorization | 6.0+ |
| Spring Data JPA | Database access via ORM | 3.0+ |
| Hibernate | JPA/ORM implementation | 6.0+ |
| Jackson | JSON serialization/deserialization | 2.15+ |
| JWT (jjwt) | JSON Web Token creation & validation | 0.12.x+ |
| MySQL Connector | Database driver | 8.0+ |

**Frontend (React 18+)**
| Library | Purpose | Version |
|---------|---------|---------|
| React | UI library | 18.0+ |
| React Router | SPA routing | 6.0+ |
| Axios | HTTP client for API calls | 1.4+ |
| React Hook Form | Form state management | 7.0+ |
| Material-UI or Bootstrap | UI component library | Latest |
| Jest | Testing framework | Latest |
| React Testing Library | Component testing | Latest |

### 9.2.2 Infrastructure Dependencies

- **Web Server**: Embedded Tomcat (via Spring Boot) or external Nginx
- **Load Balancer**: Nginx or HAProxy (for horizontal scaling)
- **Reverse Proxy**: Nginx or Apache (optional)
- **CDN**: Content Delivery Network for static assets (optional)
- **Email Server**: SMTP server for notifications (optional, future)
- **Monitoring**: Prometheus/Grafana or similar (future)

### 9.2.3 Build & Deployment Dependencies

- **Git**: Version control system
- **CI/CD Pipeline**: Jenkins, GitHub Actions, or GitLab CI
- **Docker** (optional): Container runtime
- **Container Registry** (optional): Docker Hub or private registry
- **Deployment Platform**: On-premises server or cloud (AWS, Azure, GCP)

### 9.2.4 Development Dependencies

| Tool | Version | Purpose | For |
|------|---------|---------|-----|
| **JDK** | 17+ | Java compiler and runtime | Backend |
| **Maven** | 3.8+ | Backend build tool | Backend |
| **Node.js** | 18+ | JavaScript runtime | Frontend |
| **npm** | 8.0+ | Frontend package manager | Frontend |
| **Git** | 2.30+ | Version control | All |
| **IDE** | Latest | Development environment (IntelliJ, VS Code, Eclipse) | All |
| **Spring Boot CLI** | 3.0+ | Spring Boot CLI tool | Backend |
| **Vite or Create React App** | Latest | Frontend build tool | Frontend |
| **Docker** | 20+ | Containerization (optional) | DevOps |
| **SonarQube** | Latest | Code quality analysis | QA/DevOps |
| **JUnit 5** | Latest | Backend unit testing | Backend |
| **Mockito** | 5.0+ | Backend mocking framework | Backend |
| **Jest** | Latest | Frontend testing | Frontend |
| **React Testing Library** | Latest | Frontend component testing | Frontend |
| **Postman/Insomnia** | Latest | API testing | QA/Development |

### 9.2.4a Spring Boot Backend Stack

**Required**:
- Spring Boot 3.0+
- Spring Security 6.0+
- Spring Data JPA
- Hibernate 6.0+
- MySQL Connector/J 8.0+
- Jackson 2.15+
- JWT library (jjwt 0.12+)

**Optional (Recommended)**:
- Spring Boot DevTools (for hot reload)
- Lombok (for reducing boilerplate)
- MapStruct (for DTO mapping)
- Swagger/SpringFox (for API documentation)

### 9.2.4b React Frontend Stack

**Required**:
- React 18.0+
- React Router 6.0+
- Axios 1.4+
- React Hook Form 7.0+

**Optional (Recommended)**:
- Material-UI or React Bootstrap (UI components)
- TailwindCSS (utility-first styling)
- Redux or Zustand (if complex state management needed)
- React Query (for server state management)

### 9.2.5 Documentation Dependencies

- **Markdown**: Documentation format
- **Draw.io**: Diagram creation
- **Confluence** (optional): Wiki for team documentation
- **API Documentation**: Swagger/OpenAPI for API docs

---

## 9.3 Dependency Chain

```
┌──────────────────────────────────────────────────────────┐
│                     Frontend (React)                      │
│  ├─ Node.js 18+                                           │
│  ├─ npm packages (Axios, React Router, UI libraries)      │
│  ├─ Web Browser (Chrome, Firefox, Safari, Edge)           │
│  └─ HTTP/HTTPS Connectivity                              │
└────────────────┬─────────────────────────────────────────┘
                 │ REST API (JSON)
                 ↓
┌──────────────────────────────────────────────────────────┐
│                  Backend (Spring Boot)                    │
│  ├─ JDK 17+                                               │
│  ├─ Spring Framework 6.x+                                │
│  ├─ Spring Security 6.x+                                 │
│  ├─ Maven 3.8+                                            │
│  ├─ Logging (SLF4J + Logback)                             │
│  └─ Embedded Tomcat                                       │
└────────────────┬─────────────────────────────────────────┘
                 │ JDBC / Hibernate
                 ↓
┌──────────────────────────────────────────────────────────┐
│                   Database (MySQL)                        │
│  ├─ MySQL 8.0+                                            │
│  ├─ Connection Pool (HikariCP)                            │
│  ├─ Replication (optional)                                │
│  └─ Backup system                                         │
└──────────────────────────────────────────────────────────┘
```

---

## 9.4 Risk Mitigation - Dependency Risks

### 9.4.1 Technology Risks

| Risk | Likelihood | Impact | Mitigation |
|------|-----------|--------|-----------|
| Java EOL (End of Life) | Low | High | Keep 2 major versions behind latest, plan upgrades yearly |
| Spring Boot breaking changes | Medium | Medium | Use stable versions, maintain separate dev/prod versions |
| MySQL compatibility | Low | High | Use MySQL 8.0+, test migration paths |
| React major version upgrade | Medium | Medium | Use modern React patterns, maintain upgrade roadmap |
| Third-party library vulnerabilities | High | High | Use automated dependency scanning, update monthly |

### 9.4.2 Infrastructure Risks

| Risk | Likelihood | Impact | Mitigation |
|------|-----------|--------|-----------|
| Database connection failure | Medium | High | Connection pooling, retry logic, health checks |
| Network latency | Medium | Low | CDN for static assets, API caching |
| Server downtime | Low | High | Multi-server setup, load balancing, auto-recovery |
| Storage failure | Low | High | Regular backups, replication, disaster recovery plan |

### 9.4.3 Availability Risks

| Risk | Likelihood | Impact | Mitigation |
|------|-----------|--------|-----------|
| Dependency unavailable | Low | Medium | Vendor lock-in analysis, open-source alternatives |
| License conflicts | Low | High | License compatibility checks during dependency review |
| Support discontinuation | Low | Medium | Monitor project status, maintain vendor relationships |

---

## 9.5 Transition Plan for Deprecated Dependencies

1. **Monitoring Phase** (Months 1-6)
   - Track deprecation notices
   - Monitor GitHub/community discussions
   - Assess impact on system

2. **Planning Phase** (Months 6-9)
   - Identify migration path
   - Create migration tasks
   - Update documentation

3. **Implementation Phase** (Months 9-12)
   - Update dependencies in dev environment
   - Run full test suite
   - Performance testing

4. **Deployment Phase** (Months 12-13)
   - Deploy to staging
   - UAT testing
   - Production deployment
   - Monitor for issues

---

## 9.6 System Requirements Summary

### Minimum Requirements

| Component | Minimum | Recommended |
|-----------|---------|-------------|
| **Server OS** | Windows 10 / Ubuntu 20.04 | Windows Server 2019+ / Ubuntu 22.04+ |
| **CPU** | 2 cores | 4+ cores |
| **RAM** | 4 GB | 8+ GB |
| **Disk** | 50 GB | 100+ GB |
| **Bandwidth** | 1 Mbps | 10+ Mbps |
| **Browser** | Chrome 110, Firefox 110 | Latest stable versions |

### Development Requirements

| Tool | Version | Purpose |
|------|---------|---------|
| JDK | 17+ | Backend development |
| Maven | 3.8+ | Build tool |
| Node.js | 18+ | Frontend build |
| npm | 8+ | Package manager |
| Git | 2.30+ | Version control |
| Docker | 20+ | Optional: Containerization |
| MySQL | 8.0+ | Database development |

### Production Deployment

| Component | Requirement |
|-----------|------------|
| **Load Balancer** | Nginx 1.20+ or HAProxy 2.4+ |
| **Application Server** | Spring Boot embedded Tomcat |
| **Database Server** | MySQL 8.0+ (dedicated server) |
| **Backup Storage** | Separate storage with 30-day retention |
| **Monitoring** | Prometheus/Grafana or Datadog |
| **SSL/TLS** | Valid certificate, TLS 1.2+ |

---

## 9.7 Vendor & Community Support

### Primary Technologies

| Technology | Support Type | Support Level |
|-----------|--------------|---------------|
| Spring Boot | Community + Pivotal | Commercial support available |
| React | Community | Facebook/Meta supported |
| MySQL | Community + Oracle | Commercial support available |
| OpenJDK | Community | Long-term support versions available |

### Community Channels

- **Stack Overflow**: General Q&A
- **GitHub Issues**: Bug reports and feature requests
- **Official Documentation**: Primary reference
- **Community Forums**: Spring, React, MySQL communities

---

## 9.8 Future Dependency Considerations

### Potential Additions (Future Enhancements)

| Dependency | Purpose | Timeline |
|-----------|---------|----------|
| Redis | Caching layer | Phase 2 |
| Elasticsearch | Full-text search | Phase 3 |
| WebSockets | Real-time notifications | Phase 2 |
| GraphQL | Alternative API layer | Phase 3 |
| Kubernetes | Container orchestration | Phase 2+ |
| Microservices | Architecture evolution | Phase 3+ |

### Scalability Path

1. **Phase 1 (Current)**: Monolithic Spring Boot + React + MySQL
2. **Phase 2**: Add Redis caching, WebSocket support, Kubernetes
3. **Phase 3**: Microservices architecture, event-driven system, GraphQL API
4. **Phase 4**: Machine learning for recommendations, advanced analytics

