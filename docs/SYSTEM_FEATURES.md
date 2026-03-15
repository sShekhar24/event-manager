# 3. System Features

> **[← Back to Main](../REQUIREMENTS.md)**

---

## 3.1 Authentication & Authorization

### 3.1.1 User Registration
**Feature ID:** `AUTH-001`

- Students can self-register with email, name, and password
- Email validation required (format check)
- Password strength requirements:
  - Minimum 8 characters
  - At least 1 uppercase letter
  - At least 1 lowercase letter
  - At least 1 number
  - At least 1 special character
- Duplicate email prevention
- Registration confirmation message
- User account created with STUDENT role by default

### 3.1.2 User Login
**Feature ID:** `AUTH-002`

- Email/password authentication
- JWT token generation upon successful login (valid for 24 hours)
- Refresh token for obtaining new JWT (valid for 7 days)
- Login failure tracking for security
- Account lockout after 5 failed login attempts (30-minute lockout)
- Clear error messages for invalid credentials

### 3.1.3 Logout
**Feature ID:** `AUTH-003`

- Token invalidation on logout
- Clear client-side session/token
- Redirect to login page

---

## 3.2 Student Features

### 3.2.1 Dashboard
**Feature ID:** `STUDENT-001`

- Display welcome message with student name
- Show count of registered events
- Show count of pending approvals
- Quick links to "Browse Events" and "My Registrations"
- Display upcoming events (next 7 days)

### 3.2.2 Browse Events
**Feature ID:** `STUDENT-002`

- View all approved events in a list/card format
- Display event details:
  - Event title
  - Description
  - Date and time
  - Location
  - Category
  - Organizer name
  - Available seats / Total capacity
  - Registration status (not registered / registered / cancelled)
- Pagination with 10 events per page
- Load more functionality as alternative

### 3.2.3 Search & Filter Events
**Feature ID:** `STUDENT-003`

- Search by event title or keywords
- Filter by:
  - Category (Workshop, Seminar, Sports, Cultural, etc.)
  - Date range
  - Organizer
  - Registration status (my events, available, full)
- Sort by:
  - Relevance
  - Date (ascending/descending)
  - Popularity (most registered)
- Search results update in real-time
- Clear filters button

### 3.2.4 Event Details
**Feature ID:** `STUDENT-004`

- View complete event information:
  - Full description
  - Start and end time
  - Location with optional map
  - Category and tags
  - Organizer details
  - Registration deadline
  - Current registrations count
  - Event status (upcoming / completed / cancelled)
- View list of registered students (for admin-approved events only)
- Related events suggestion

### 3.2.5 Register for Event
**Feature ID:** `STUDENT-005`

- One-click registration button
- Validation:
  - Check if user already registered
  - Check event capacity
  - Check registration deadline
  - Check event status (not cancelled)
- Confirmation dialog before registering
- Success notification with confirmation
- Registration ID provided
- Cannot register after registration deadline

### 3.2.6 View Registrations
**Feature ID:** `STUDENT-006`

- Display all registrations (active, cancelled, past)
- Show registration status:
  - Registered
  - Attended (marked by admin)
  - Cancelled by student
  - Removed by admin
- Filter by status
- Show event details for each registration
- Sorting by date, status, or event name

### 3.2.7 Cancel Registration
**Feature ID:** `STUDENT-007`

- Cancel registration before event date
- Confirmation dialog
- Success notification
- Update available seats immediately
- Cancellation must be before event start time

### 3.2.8 Profile Management
**Feature ID:** `STUDENT-008`

- View personal profile:
  - Email
  - Full name
  - Registration date
  - Total events attended
  - Account status
- Update profile:
  - Full name
  - Phone number (optional)
  - College department (optional)
- Change password
- View account activity/login history

---

## 3.3 Admin Features

### 3.3.1 Admin Dashboard
**Feature ID:** `ADMIN-001`

- Display system statistics:
  - Total registered students
  - Total events (created, approved, pending, rejected)
  - Total registrations
  - Upcoming events this month
  - Recent activity log
- Quick action buttons (Create Event, View Users, View Registrations)
- Chart visualization:
  - Events created per month
  - Registrations trend
  - Popular events

### 3.3.2 Create Event
**Feature ID:** `ADMIN-002`

- Form to create new event:
  - Event title (required)
  - Description (required, min 50 chars, max 5000 chars)
  - Category (required) - select from predefined list
  - Date and time (required)
  - End date and time (required, must be after start)
  - Location (required)
  - Maximum capacity (required, positive integer)
  - Registration deadline (required, must be before event start)
  - Image/poster upload (optional)
  - Tags/keywords (optional)
  - Organizer details (auto-filled with admin name)
- Event created in "PENDING" status
- Confirmation of successful creation
- Option to edit immediately after creation

### 3.3.3 Edit Event
**Feature ID:** `ADMIN-003`

- Edit event details before event starts
- Editable fields:
  - Title, Description, Category
  - Date, Time, Location
  - Capacity (can only increase if not exceeded current registrations)
  - Registration deadline
  - Image/poster
  - Tags
- Cannot edit if event has already started
- Edit history/audit trail
- Confirmation before saving

### 3.3.4 Delete Event
**Feature ID:** `ADMIN-004`

- Delete event (only if status is PENDING or CANCELLED)
- Confirmation dialog with warning
- If event has registrations, show count and ask for confirmation
- Notification sent to registered students when event is deleted
- Event moved to DELETED status (soft delete) for audit trail

### 3.3.5 Manage Event Status
**Feature ID:** `ADMIN-005`

- Change event status:
  - PENDING → APPROVED (make visible to students)
  - PENDING → REJECTED (with optional rejection reason)
  - APPROVED → CANCELLED (with notification to all registered students)
- Status change history
- Only one status change per action (no instant status toggle)

### 3.3.6 View All Users
**Feature ID:** `ADMIN-006`

- Display list of all registered students
- Show columns:
  - Name, Email, Registration Date
  - Total events attended
  - Account status (active/inactive)
- Search by name or email
- Sort by name, registration date, events attended
- Pagination
- View detailed user profile by clicking on user

### 3.3.7 View User Profile
**Feature ID:** `ADMIN-007`

- View full user profile:
  - Personal details
  - Account creation date
  - Email, phone, department
  - List of all registrations (past and upcoming)
  - Events attended count
  - Account activity
- Ability to temporarily disable/enable user account

### 3.3.8 View Event Registrations
**Feature ID:** `ADMIN-008`

- View all registrations for a specific event:
  - Student name, email
  - Registration date
  - Attendance status (pending, attended, cancelled)
- Mark student as attended (during or after event)
- Remove student from event (with optional reason/notification)
- Export registrations list (CSV or PDF)
- Filter by attendance status

### 3.3.9 Cancel/Remove Student from Event
**Feature ID:** `ADMIN-009`

- Remove student from specific event
- Reason for removal (optional)
- Notification to student
- Seat becomes available for other students
- Record kept in system (soft delete)

### 3.3.10 View System Statistics
**Feature ID:** `ADMIN-010`

- Generate reports:
  - Total events created/approved
  - Total registrations
  - Event attendance rate
  - Popular events (by registrations)
  - Student engagement metrics
  - User activity logs
- Filter reports by date range
- Export reports as PDF/CSV

---

## 3.4 Common Features

### 3.4.1 Navigation
**Feature ID:** `COMMON-001`

- Consistent navigation bar/sidebar across application
- Different navigation based on user role (Student vs Admin)
- Current page highlighting
- Logout button with confirmation
- User profile menu

### 3.4.2 Error Handling
**Feature ID:** `COMMON-002`

- User-friendly error messages
- Error codes displayed (for support purposes)
- Suggestions for resolution when possible
- Error logging on backend for debugging

### 3.4.3 Loading States
**Feature ID:** `COMMON-003`

- Loading spinners for async operations
- Skeleton screens for better UX during data fetch
- Disabled buttons/forms during processing
- Clear indication of progress for long operations

### 3.4.4 Notifications
**Feature ID:** `COMMON-004`

- Toast notifications for:
  - Successful actions (registration, login, profile update)
  - Errors (validation failures, server errors)
  - Informational messages
- Notification auto-dismiss after 5 seconds (or manual close)
- Notifications appear in top-right corner
- Different visual styles for success/error/info

### 3.4.5 Responsive Design
**Feature ID:** `COMMON-005`

- Desktop-first responsive design
- Mobile support (768px and above)
- Tablet optimization
- Touch-friendly buttons and forms
- Responsive navigation (hamburger menu on mobile)

