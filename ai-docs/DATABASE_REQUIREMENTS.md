# 6. Database Requirements

> **[← Back to Main](../REQUIREMENTS.md)**

---

## 6.1 Database Technology

- **DBMS**: MySQL 8.0+
- **Connection Pool**: HikariCP (via Spring Boot)
- **ORM**: Hibernate (via Spring Data JPA)
- **Character Set**: UTF-8
- **Collation**: utf8mb4_unicode_ci

---

## 6.2 Database Schema

### 6.2.1 Users Table

```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  phone VARCHAR(20),
  department VARCHAR(100),
  role ENUM('STUDENT', 'ADMIN') NOT NULL DEFAULT 'STUDENT',
  account_status ENUM('ACTIVE', 'INACTIVE', 'LOCKED') NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  
  INDEX idx_email (email),
  INDEX idx_role (role),
  INDEX idx_account_status (account_status),
  INDEX idx_created_at (created_at)
);
```

### 6.2.2 Events Table

```sql
CREATE TABLE events (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  description LONGTEXT NOT NULL,
  category VARCHAR(100) NOT NULL,
  start_date_time DATETIME NOT NULL,
  end_date_time DATETIME NOT NULL,
  location VARCHAR(255) NOT NULL,
  capacity INT NOT NULL CHECK (capacity > 0),
  registration_deadline DATETIME NOT NULL,
  image_url VARCHAR(500),
  status ENUM('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED', 'COMPLETED') NOT NULL DEFAULT 'PENDING',
  rejection_reason TEXT,
  organizer_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  
  CONSTRAINT fk_organizer FOREIGN KEY (organizer_id) REFERENCES users(id),
  INDEX idx_status (status),
  INDEX idx_category (category),
  INDEX idx_start_date_time (start_date_time),
  INDEX idx_organizer_id (organizer_id),
  INDEX idx_created_at (created_at)
);
```

### 6.2.3 Registrations Table

```sql
CREATE TABLE registrations (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  student_id BIGINT NOT NULL,
  event_id BIGINT NOT NULL,
  registration_date DATETIME NOT NULL,
  attendance_status ENUM('PENDING', 'ATTENDED', 'CANCELLED', 'REMOVED') NOT NULL DEFAULT 'PENDING',
  cancellation_reason TEXT,
  removal_reason TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  
  CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES users(id),
  CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events(id),
  CONSTRAINT uq_student_event UNIQUE KEY (student_id, event_id),
  
  INDEX idx_student_id (student_id),
  INDEX idx_event_id (event_id),
  INDEX idx_attendance_status (attendance_status),
  INDEX idx_registration_date (registration_date)
);
```

---

## 6.3 Relationships

```
Users (1) ──→ (Many) Events
├─ One admin creates/organizes many events
└─ Event has a single organizer (admin)

Users (1) ──→ (Many) Registrations
├─ One student has many registrations
└─ Registration belongs to one student

Events (1) ──→ (Many) Registrations
├─ One event has many registrations
└─ Registration belongs to one event
```

---

## 6.4 Indexes Strategy

### Primary Indexes (for joins and lookups)

- `users.id` - Primary key (automatic)
- `events.id` - Primary key (automatic)
- `registrations.id` - Primary key (automatic)
- `registrations.uq_student_event` - Unique constraint (prevents duplicate registrations)

### Foreign Key Indexes (for relationships)

- `events.organizer_id` - FK index (lookups when fetching admin's events)
- `registrations.student_id` - FK index (lookups when fetching student's registrations)
- `registrations.event_id` - FK index (lookups when fetching event's registrations)

### Search/Filter Indexes

- `users.email` - Unique (fast login lookup)
- `events.status` - Common filter
- `events.category` - Student event filtering
- `events.start_date_time` - Date range queries
- `registrations.attendance_status` - Status filtering

### Sorting/Pagination Indexes

- `events.created_at` - Latest events first
- `registrations.registration_date` - Recent registrations first
- `users.created_at` - Recent users first

---

## 6.5 Query Optimization Guidelines

### Common Queries

#### Student Login
```sql
SELECT * FROM users WHERE email = ? AND account_status = 'ACTIVE';
-- Uses: idx_email (UNIQUE INDEX)
```

#### Get Approved Events (paginated)
```sql
SELECT * FROM events 
WHERE status = 'APPROVED' 
ORDER BY start_date_time DESC 
LIMIT 10 OFFSET ?;
-- Uses: idx_status, idx_start_date_time
```

#### Get Student's Registrations
```sql
SELECT r.*, e.title, e.start_date_time 
FROM registrations r
JOIN events e ON r.event_id = e.id
WHERE r.student_id = ?
ORDER BY e.start_date_time DESC;
-- Uses: idx_student_id, idx_event_id
```

#### Get Event Registrations with Student Details
```sql
SELECT r.*, u.first_name, u.last_name, u.email
FROM registrations r
JOIN users u ON r.student_id = u.id
WHERE r.event_id = ?
ORDER BY r.registration_date DESC;
-- Uses: idx_event_id, idx_student_id
```

#### Check if Student Already Registered
```sql
SELECT 1 FROM registrations 
WHERE student_id = ? AND event_id = ?;
-- Uses: UNIQUE KEY (student_id, event_id)
```

#### Get Event Capacity Status
```sql
SELECT e.capacity, COUNT(r.id) as registered_count
FROM events e
LEFT JOIN registrations r ON e.id = r.event_id 
  AND r.attendance_status != 'CANCELLED'
WHERE e.id = ?
GROUP BY e.id;
-- Uses: idx_event_id
```

---

## 6.6 Data Integrity & Constraints

### Primary Keys
- All tables have surrogate keys (auto-increment BIGINT)

### Foreign Keys
- `events.organizer_id` → `users.id` (CASCADE DELETE not allowed - soft delete)
- `registrations.student_id` → `users.id` (CASCADE DELETE not allowed)
- `registrations.event_id` → `events.id` (CASCADE DELETE not allowed)

### Unique Constraints
- `users.email` - Must be unique
- `registrations(student_id, event_id)` - Student can register for event only once

### Check Constraints
- `events.capacity > 0` - Event must have positive capacity
- `events.registration_deadline` must be before `start_date_time`
- `events.end_date_time` must be after `start_date_time`

### Default Values
- `users.role` - Defaults to 'STUDENT'
- `users.account_status` - Defaults to 'ACTIVE'
- `events.status` - Defaults to 'PENDING'
- `registrations.attendance_status` - Defaults to 'PENDING'
- All `created_at` and `updated_at` - Defaults to CURRENT_TIMESTAMP

---

## 6.7 Data Retention & Archive Policy

| Data | Retention | Action |
|------|-----------|--------|
| Cancelled Registrations | 1 year | Archive to backup, then delete |
| Completed Events | 2 years | Archive to backup, then delete |
| Inactive User Accounts | 6 months | Archive to backup, then delete |
| Activity Logs | 90 days | Archive daily |
| Deleted Events/Users | Soft delete, keep indefinitely | For audit trail |

---

## 6.8 Backup & Recovery

### Backup Strategy
- **Frequency**: Daily full backups at 2 AM UTC
- **Retention**: 7 days of daily backups + 4 weeks of weekly backups
- **Location**: Separate storage server with encryption
- **Verification**: Weekly restore test on test database

### Recovery Procedures
- Point-in-time recovery: Up to 7 days back
- Test recovery procedure monthly
- Recovery time objective (RTO): < 1 hour
- Recovery point objective (RPO): < 1 hour

---

## 6.9 Performance Benchmarks

| Operation | Target | Query Type |
|-----------|--------|-----------|
| User Login | < 50ms | Single row lookup |
| Get Events List (10 per page) | < 100ms | Filtered + paginated query |
| Get Event Details | < 50ms | Single row + join |
| Register for Event | < 100ms | Insert + capacity check |
| Get User's Registrations | < 150ms | Join query |
| Get Event Registrations (20 per page) | < 200ms | Join + paginated query |

### Connection Pool Configuration
- **Min Connections**: 5
- **Max Connections**: 20
- **Idle Timeout**: 10 minutes
- **Max Lifetime**: 30 minutes
- **Connection Timeout**: 10 seconds

---

## 6.10 MySQL Configuration Recommendations

```sql
-- Character set configuration
SET GLOBAL character_set_server = 'utf8mb4';
SET GLOBAL collation_server = 'utf8mb4_unicode_ci';

-- Query cache (MySQL 5.7 and earlier)
SET GLOBAL query_cache_type = 1;
SET GLOBAL query_cache_size = 268435456;

-- InnoDB settings
SET GLOBAL innodb_buffer_pool_size = 1073741824;
SET GLOBAL innodb_log_file_size = 536870912;
SET GLOBAL innodb_flush_log_at_trx_commit = 2;

-- Slow query log
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 2;
```

---

## 6.11 Development Database Seeding

On application startup, seed the database with:

1. **Default Admin User**
   - Email: admin@college.edu
   - Password: Admin@123 (hash with BCrypt)
   - Role: ADMIN

2. **Event Categories** (for dropdowns)
   - Workshop
   - Seminar
   - Sports
   - Cultural
   - Academic
   - Social
   - Other

3. **Sample Events** (for testing)
   - 5-10 sample events with various statuses

