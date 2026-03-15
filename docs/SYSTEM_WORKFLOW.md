# 8. System Workflow

> **[← Back to Main](../REQUIREMENTS.md)**

---

## 8.1 Authentication Workflows

### 8.1.1 Student Registration Workflow

```
┌─────────────────────────────────────────────────────────────┐
│ STUDENT REGISTRATION WORKFLOW                               │
└─────────────────────────────────────────────────────────────┘

1. Student Access Application
   ├─ Navigate to /register or click "Sign Up"
   └─ Registration page displayed

2. Enter Registration Information
   ├─ Email (with validation)
   ├─ First Name
   ├─ Last Name
   └─ Password (with strength indicator)

3. Client-Side Validation
   ├─ Email format validation
   ├─ Password strength check
   ├─ All required fields filled
   └─ Display validation errors if any

4. Submit Registration
   └─ POST /api/auth/register with form data

5. Server-Side Validation
   ├─ Email format validation
   ├─ Check for duplicate email
   ├─ Validate password strength
   └─ Return 400 if validation fails

6. Account Creation
   ├─ Hash password with BCrypt
   ├─ Create User entity with STUDENT role
   ├─ Save to database
   └─ Set account_status = ACTIVE

7. Success Response
   ├─ Return 201 Created
   ├─ Display success message
   └─ Redirect to login page

8. Student Login (see 8.1.2)
```

### 8.1.2 Student Login Workflow

```
┌─────────────────────────────────────────────────────────────┐
│ STUDENT LOGIN WORKFLOW                                      │
└─────────────────────────────────────────────────────────────┘

1. Student Access Login Page
   ├─ Navigate to /login
   └─ Login form displayed

2. Enter Credentials
   ├─ Email
   └─ Password

3. Client-Side Validation
   ├─ Email and password not empty
   └─ Display validation errors if any

4. Submit Login Request
   └─ POST /api/auth/login with credentials

5. Server-Side Authentication
   ├─ Find user by email
   ├─ Check if user exists
   ├─ Check account_status = ACTIVE
   ├─ Verify password (BCrypt comparison)
   ├─ Increment failed login counter if wrong
   ├─ Check if account locked (5 failed attempts)
   └─ Return 401 if authentication fails

6. Token Generation
   ├─ Create JWT Access Token (24-hour expiry)
   ├─ Create JWT Refresh Token (7-day expiry)
   └─ Reset failed login counter

7. Success Response
   ├─ Return 200 OK
   ├─ Return Access Token, Refresh Token, User Data
   └─ Store tokens in localStorage

8. Redirect to Dashboard
   ├─ Clear login form
   ├─ Redirect to /dashboard
   └─ Load dashboard data using access token
```

### 8.1.3 Admin Login Workflow

Same as Student Login (8.1.2) but:
- Admin account must exist (pre-seeded)
- Role = ADMIN
- Redirects to /admin/dashboard instead

---

## 8.2 Event Management Workflows

### 8.2.1 Create Event Workflow (Admin)

```
┌─────────────────────────────────────────────────────────────┐
│ EVENT CREATION WORKFLOW (ADMIN)                             │
└─────────────────────────────────────────────────────────────┘

1. Admin Navigate to Event Creation
   ├─ Click "Create Event" button
   ├─ Navigate to /admin/events/create
   └─ Event creation form displayed

2. Fill Event Details
   ├─ Title
   ├─ Description
   ├─ Category (select from dropdown)
   ├─ Start Date & Time
   ├─ End Date & Time
   ├─ Location
   ├─ Maximum Capacity
   ├─ Registration Deadline
   ├─ Image Upload (optional)
   └─ Tags (optional)

3. Preview Event
   ├─ Real-time preview on right side
   ├─ Shows how event will appear to students
   └─ Admin can make adjustments

4. Client-Side Validation
   ├─ All required fields present
   ├─ Title min 3 chars, max 255
   ├─ Description min 50 chars, max 5000
   ├─ Capacity is positive integer
   ├─ End date/time after start date/time
   ├─ Deadline before start date/time
   └─ Display validation errors if any

5. Submit Event Creation
   └─ POST /api/events with event data

6. Server-Side Processing
   ├─ Authenticate and verify ADMIN role
   ├─ Validate all fields (same as client)
   ├─ Set organizer_id = current admin id
   ├─ Set status = PENDING
   ├─ Create Event entity
   ├─ Save to database
   └─ Return 201 Created

7. Success Response
   ├─ Display success notification
   ├─ Show event created ID
   ├─ Offer options:
   │   ├─ Edit Event
   │   ├─ Create Another Event
   │   └─ Go to Dashboard
   └─ Refresh event list
```

### 8.2.2 Event Approval Workflow (Admin)

```
┌─────────────────────────────────────────────────────────────┐
│ EVENT APPROVAL WORKFLOW (ADMIN)                             │
└─────────────────────────────────────────────────────────────┘

1. Admin Views Pending Events
   ├─ Navigate to /admin/events
   ├─ Filter by status = PENDING
   └─ List of pending events displayed

2. Select Event for Review
   ├─ Click on event to view details
   ├─ Full event information displayed
   └─ View registrations (if any) button

3. Review Event Details
   ├─ Verify all information is appropriate
   ├─ Check for any issues
   └─ Decide to Approve or Reject

4. Approve Event
   ├─ Click "Approve" button
   ├─ Confirmation dialog shown
   ├─ Admin confirms
   └─ PATCH /api/events/{id}/approve

5. Server Processing
   ├─ Authenticate and verify ADMIN role
   ├─ Update event status = APPROVED
   ├─ Save changes
   ├─ Update updated_at timestamp
   └─ Return 200 OK

6. Event Becomes Visible
   ├─ Event now appears in student event listings
   ├─ Students can see and register for event
   ├─ Registration deadline enforced
   └─ Notification: "Event approved and published"

Alternative: Reject Event
   ├─ Click "Reject" button
   ├─ Show rejection reason form
   ├─ Admin enters reason
   ├─ PATCH /api/events/{id}/reject with reason
   ├─ Event status = REJECTED
   ├─ Event hidden from students
   └─ Organizer notified (future: email)
```

### 8.2.3 Edit Event Workflow (Admin)

```
┌─────────────────────────────────────────────────────────────┐
│ EVENT EDIT WORKFLOW (ADMIN)                                 │
└─────────────────────────────────────────────────────────────┘

1. Admin Navigate to Edit Event
   ├─ Go to /admin/events
   ├─ Find event in list
   ├─ Click "Edit" button
   └─ Edit form pre-filled with current data

2. Validate Edit Constraints
   ├─ Cannot edit if event already started (start_date_time < now)
   ├─ Cannot decrease capacity below current registrations
   ├─ Cannot change status (use Approve/Reject workflow)
   ├─ Show error if constraints violated
   └─ Allow edits if constraints satisfied

3. Update Event Details
   ├─ Modify any editable field
   ├─ Real-time preview updates
   └─ Client-side validation same as create

4. Submit Changes
   └─ PUT /api/events/{id} with updated data

5. Server Processing
   ├─ Authenticate and verify ADMIN role
   ├─ Validate constraints (event not started)
   ├─ Validate all fields
   ├─ Check capacity constraint
   ├─ Update Event entity
   ├─ Update updated_at timestamp
   ├─ Store edit history (future: audit log)
   └─ Return 200 OK

6. Event Updated
   ├─ Display success notification
   ├─ Refresh event details
   ├─ Updated event visible to students
   └─ Students notified of changes (future: email)
```

### 8.2.4 Delete Event Workflow (Admin)

```
┌─────────────────────────────────────────────────────────────┐
│ EVENT DELETION WORKFLOW (ADMIN)                             │
└─────────────────────────────────────────────────────────────┘

1. Admin Select Event to Delete
   ├─ Go to /admin/events
   ├─ Find event
   ├─ Click "Delete" button
   └─ Confirmation dialog shown

2. Validate Deletion
   ├─ Check event status (only PENDING, CANCELLED allowed)
   ├─ Check if event has registrations
   ├─ Show warning if registrations exist
   ├─ Show registration count
   └─ Ask for final confirmation

3. Admin Confirms Deletion
   ├─ Click "Confirm" in dialog
   └─ DELETE /api/events/{id}

4. Server Processing
   ├─ Authenticate and verify ADMIN role
   ├─ Verify event can be deleted
   ├─ Soft delete: Set deleted_at timestamp
   ├─ Mark status = DELETED (or CANCELLED)
   ├─ Preserve data for audit trail
   ├─ If registrations exist, notify students (future: email)
   └─ Return 204 No Content

5. Event Removed
   ├─ Event no longer visible to students
   ├─ Event removed from admin list
   ├─ Display success notification
   └─ Students notified (future: email)
```

---

## 8.3 Student Event Management Workflows

### 8.3.1 Browse Events Workflow (Student)

```
┌─────────────────────────────────────────────────────────────┐
│ BROWSE EVENTS WORKFLOW (STUDENT)                            │
└─────────────────────────────────────────────────────────────┘

1. Student Navigate to Browse Events
   ├─ Go to /events or click "Browse Events"
   ├─ Default: Show all approved events, sorted by date
   └─ Display loading state

2. Fetch Events from API
   ├─ GET /api/events?page=0&size=10&status=APPROVED
   ├─ Backend queries events with:
   │   ├─ status = APPROVED
   │   ├─ start_date_time > now (or include past events)
   │   └─ Sorted by start_date_time ascending
   └─ Paginated results (10 per page)

3. Display Events
   ├─ Show event cards grid (2 columns on desktop)
   ├─ Each card shows:
   │   ├─ Event title
   │   ├─ Date & time
   │   ├─ Location
   │   ├─ Available seats / Total capacity
   │   └─ "View Details" / "Register" button
   ├─ Show pagination controls
   └─ Display empty state if no events

4. Search/Filter Events (Optional)
   ├─ Student enters search term
   ├─ Select filter criteria:
   │   ├─ Category
   │   ├─ Date range
   │   └─ Status (My Events, Available, Full)
   ├─ GET /api/events?search=term&category=Workshop...
   └─ Results update dynamically

5. Pagination
   ├─ Click "Next" or page number
   ├─ Load next page of events
   ├─ Scroll to top
   └─ Display new set of events
```

### 8.3.2 Register for Event Workflow (Student)

```
┌─────────────────────────────────────────────────────────────┐
│ REGISTER FOR EVENT WORKFLOW (STUDENT)                       │
└─────────────────────────────────────────────────────────────┘

1. Student View Event
   ├─ Browse events (see 8.3.1)
   ├─ Click event card
   ├─ GET /api/events/{id}
   └─ Event details displayed

2. Display Event Details
   ├─ Full description
   ├─ Date, Time, Location
   ├─ Capacity info (X/100 registered)
   ├─ Registration deadline
   ├─ Event status
   ├─ Show "Register" button if:
   │   ├─ Event status = APPROVED
   │   ├─ Event not full
   │   ├─ Registration deadline not passed
   │   ├─ Student not already registered
   │   └─ Current time before start time
   └─ Show alternative messages if conditions not met

3. Student Clicks Register
   ├─ Confirmation dialog shown
   ├─ Display: "You are about to register for [Event Title]"
   ├─ Show: Date, Time, Location
   ├─ "Cancel" and "Confirm" buttons
   └─ Student can cancel or confirm

4. Student Confirms Registration
   ├─ POST /api/registrations with eventId
   ├─ Request sent with JWT token

5. Server Processing
   ├─ Authenticate and verify STUDENT role
   ├─ Extract student_id from JWT token
   ├─ Validate event exists
   ├─ Validate event status = APPROVED
   ├─ Check event capacity:
   │   ├─ Count current active registrations
   │   ├─ If count >= capacity, return 409 (Conflict)
   │   └─ Otherwise, proceed
   ├─ Check registration deadline:
   │   ├─ If now > registration_deadline, return 400
   │   └─ Otherwise, proceed
   ├─ Check for duplicate registration:
   │   ├─ Query: SELECT * FROM registrations 
   │   │   WHERE student_id = ? AND event_id = ?
   │   ├─ If exists, return 409 (Already registered)
   │   └─ Otherwise, proceed
   ├─ Create Registration entity:
   │   ├─ student_id = current user id
   │   ├─ event_id = event id
   │   ├─ registration_date = now
   │   ├─ attendance_status = PENDING
   │   └─ Save to database
   └─ Return 201 Created with registration details

6. Success Response
   ├─ Display success notification
   ├─ Show: "Successfully registered for [Event Title]"
   ├─ Display registration confirmation ID
   ├─ Close confirmation dialog
   ├─ Update event capacity display
   ├─ Change button to "Registered" (disabled state)
   └─ Optional: Show "Add to Calendar" link

7. Error Handling
   ├─ Event full: "Sorry, this event is at capacity"
   ├─ Already registered: "You are already registered for this event"
   ├─ Deadline passed: "Registration deadline has passed"
   ├─ Event cancelled: "This event is no longer available"
   └─ Server error: "Unable to register. Please try again later"
```

### 8.3.3 Cancel Registration Workflow (Student)

```
┌─────────────────────────────────────────────────────────────┐
│ CANCEL REGISTRATION WORKFLOW (STUDENT)                      │
└─────────────────────────────────────────────────────────────┘

1. Student View My Registrations
   ├─ Navigate to /my-registrations or profile
   ├─ Display all registrations
   ├─ Filter by status (All, Upcoming, Past, Cancelled)
   └─ Show "Cancel" button for upcoming events only

2. Validate Cancellation
   ├─ Can only cancel if:
   │   ├─ Registration status = PENDING
   │   ├─ Event start_date_time > now (not started)
   │   ├─ Event status ≠ CANCELLED
   │   └─ No attendance_status (not marked attended/removed)
   └─ Show "Cannot cancel" if event already started

3. Student Clicks Cancel
   ├─ Click "Cancel Registration" button
   ├─ Confirmation dialog shown
   ├─ Display: "Are you sure you want to cancel?"
   ├─ Show: Event details, Cancellation deadline
   ├─ Option to enter cancellation reason (optional)
   ├─ "Keep" and "Cancel" buttons
   └─ Student can change mind or proceed

4. Student Confirms Cancellation
   ├─ DELETE /api/registrations/{id}
   ├─ Optional: Include cancellation_reason in request

5. Server Processing
   ├─ Authenticate and verify STUDENT role
   ├─ Extract student_id from JWT token
   ├─ Validate registration exists
   ├─ Validate registration.student_id = current user
   ├─ Validate event not started
   ├─ Validate attendance_status = PENDING
   ├─ Update registration:
   │   ├─ attendance_status = CANCELLED
   │   ├─ cancellation_reason = provided reason
   │   └─ updated_at = now
   ├─ Save to database
   └─ Return 204 No Content

6. Cancellation Confirmed
   ├─ Display success notification
   ├─ Show: "Registration cancelled"
   ├─ Remove from "Upcoming Events" list
   ├─ Move to "Cancelled" tab
   ├─ Seat becomes available for other students
   ├─ Event capacity updated (available seats increased)
   └─ Optional: Show "Re-register" option if capacity available
```

---

## 8.4 Admin Dashboard Workflows

### 8.4.1 View Dashboard Workflow (Admin)

```
┌─────────────────────────────────────────────────────────────┐
│ ADMIN DASHBOARD WORKFLOW                                    │
└─────────────────────────────────────────────────────────────┘

1. Admin Navigate to Dashboard
   ├─ Go to /admin/dashboard
   ├─ Display loading indicators
   └─ Fetch data from multiple endpoints

2. Fetch Dashboard Data
   ├─ GET /api/admin/dashboard/stats
   ├─ GET /api/admin/dashboard/events-trend
   ├─ GET /api/admin/dashboard/top-events
   └─ GET /api/admin/dashboard/activity-log

3. Display Statistics Cards
   ├─ Total Students (count of active users)
   ├─ Total Events (count of all events by status)
   ├─ Total Registrations (count of registrations)
   ├─ Pending Approvals (count of PENDING events)
   └─ Each card clickable to filter related data

4. Display Charts
   ├─ Events Trend: Line chart of events created per month
   ├─ Top Events: Pie chart of most registered events
   └─ Charts render using Chart.js or similar

5. Display Recent Activity
   ├─ Table of recent system activity (last 20 items)
   ├─ Columns: Action, User, Event, Date, Time
   ├─ Sortable by date, action type
   ├─ Click activity to view details
   └─ Pagination if more than 20 items

6. Quick Action Buttons
   ├─ "Create Event" - Go to event creation
   ├─ "View Users" - Go to user management
   ├─ "View Registrations" - Go to registration management
   ├─ "Approve Events" - Filter pending events
   └─ "View Reports" - Go to reporting page

7. Dashboard Refreshes
   ├─ Data refreshes every 5 minutes automatically
   ├─ Manual refresh button available
   ├─ Admin can click to immediately refresh
   └─ Show "Last updated" timestamp
```

---

## 8.5 User Profile Workflows

### 8.5.1 View/Edit Profile Workflow (Student)

```
┌─────────────────────────────────────────────────────────────┐
│ EDIT PROFILE WORKFLOW (STUDENT)                             │
└─────────────────────────────────────────────────────────────┘

1. Student Navigate to Profile
   ├─ Click user icon/avatar in top-right
   ├─ Select "Profile" from dropdown
   ├─ Navigate to /profile
   ├─ GET /api/users/profile
   └─ Profile page displayed with current info

2. Display Profile Information
   ├─ Name (First & Last)
   ├─ Email (read-only)
   ├─ Phone (if set)
   ├─ Department (if set)
   ├─ Registration date
   ├─ Total events attended
   ├─ Edit button for editable fields
   └─ Tabs: Info, Password, Activity

3. Student Clicks Edit
   ├─ Click "Edit" button
   ├─ Form becomes editable:
   │   ├─ Name (required)
   │   ├─ Phone (optional)
   │   ├─ Department (optional)
   │   └─ Email (read-only)
   ├─ Show "Save" and "Cancel" buttons
   └─ Cancel restores original values

4. Update Profile
   ├─ Modify fields
   ├─ Client-side validation:
   │   ├─ Name not empty
   │   ├─ Phone valid format (if provided)
   │   └─ Show validation errors
   ├─ Click "Save"
   └─ PUT /api/users/profile with updated data

5. Server Processing
   ├─ Authenticate and verify user logged in
   ├─ Validate all fields
   ├─ Update User entity
   ├─ Update updated_at timestamp
   ├─ Save to database
   └─ Return 200 OK with updated user data

6. Profile Updated
   ├─ Display success notification
   ├─ Refresh profile display
   ├─ Return to read-only mode
   └─ Update user name in header if changed

Alternative: Change Password
   ├─ Click "Password" tab
   ├─ Show form:
   │   ├─ Current password (required)
   │   ├─ New password (required)
   │   └─ Confirm new password (required)
   ├─ Client-side validation:
   │   ├─ Passwords match
   │   ├─ New password meets strength requirements
   │   └─ Show validation errors
   ├─ POST /api/users/change-password
   ├─ Server validates current password
   ├─ Server validates new password strength
   ├─ Hash new password with BCrypt
   ├─ Update user password
   └─ Display success: "Password changed successfully"
```

---

## 8.6 Logout Workflow

```
┌─────────────────────────────────────────────────────────────┐
│ LOGOUT WORKFLOW                                             │
└─────────────────────────────────────────────────────────────┘

1. User Clicks Logout
   ├─ Click user icon in top-right
   ├─ Select "Logout" from dropdown
   ├─ Confirmation dialog shown (optional)
   ├─ Display: "Are you sure you want to logout?"
   └─ "Cancel" and "Logout" buttons

2. User Confirms
   ├─ Click "Logout" button
   ├─ POST /api/auth/logout (optional, for backend tracking)
   └─ Client-side logout

3. Client-Side Logout
   ├─ Remove JWT token from localStorage
   ├─ Remove refresh token from localStorage
   ├─ Clear user session data from memory
   ├─ Clear any cached data
   └─ Clear form data and sensitive info

4. Server-Side (Optional)
   ├─ Log logout event (for audit trail)
   ├─ Update last_logout timestamp (future)
   ├─ Invalidate token (if using token blacklist)
   └─ Return 200 OK

5. Redirect to Login
   ├─ Navigate to /login page
   ├─ Display logout confirmation message
   ├─ Login form ready for next user
   └─ Clear previous user's data from page

6. User Can Login Again
   ├─ New user or same user can login
   ├─ Fresh JWT token generated
   └─ New session established
```

