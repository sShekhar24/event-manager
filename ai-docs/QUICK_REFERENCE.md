# Quick Reference - API Integration Guide

## 🔐 Authentication Flow

### Login
```javascript
// 1. User submits credentials
const response = await authService.login({ email, password });

// 2. Response is automatically unwrapped by interceptor
// Before: response.data = { success, message, data: {...}, error, statusCode }
// After:  response.data = { accessToken, refreshToken, tokenType, expiresIn, user }

// 3. Store in AuthContext
login(data.user, data.accessToken, data.refreshToken, data.tokenType, data.expiresIn);

// 4. localStorage is populated:
// - authToken: <accessToken>
// - refreshToken: <refreshToken>
// - user: <user object>
// - tokenType: Bearer
// - expiresIn: <ms>
```

### LocalStorage Keys
```javascript
TOKEN_KEY = 'authToken'              // Access token
REFRESH_TOKEN_KEY = 'refreshToken'   // Refresh token
USER_KEY = 'user'                    // User object
'tokenType'                          // Always "Bearer"
'expiresIn'                          // Expiration time in ms
```

---

## 📝 Form Field Requirements

### Register Form
```javascript
{
  firstName: string,     // Required, 2-100 chars
  lastName: string,      // Required, 2-100 chars
  email: string,         // Required, valid email
  password: string,      // Required, 8+ chars, special rules below
  phone: string,         // Optional
  department: string     // Optional
}

// Password requirements: min 8 chars, 1 uppercase, 1 lowercase, 1 number, 1 special char (@$!%*?&)
```

### Event Creation Form
```javascript
{
  title: string,                    // Required, 3-255 chars
  description: string,              // Required, 50-5000 chars
  category: string,                 // Required, from EVENT_CATEGORIES
  startDateTime: ISO-8601,          // Required, before endDateTime
  endDateTime: ISO-8601,            // Required, after startDateTime
  location: string,                 // Required
  capacity: number,                 // Required, > 0
  registrationDeadline: ISO-8601,   // Required, before startDateTime
  imageUrl: string                  // Optional
}
```

---

## 🔄 Pagination

**Old way (WRONG):**
```javascript
eventService.getAllEvents(page, pageSize, params)  // ❌ pageSize
```

**New way (CORRECT):**
```javascript
eventService.getAllEvents(page, size, params)      // ✅ size
registrationService.getMyRegistrations(page, size, params)  // ✅ size
```

---

## 🎯 API Endpoints Reference

### Events
```javascript
// Get all approved events
eventService.getAllEvents(page, size)
// GET /events?page=0&size=10

// Get event by ID
eventService.getEventById(id)
// GET /events/{id}

// Create event
eventService.createEvent(eventData)
// POST /events

// Update event
eventService.updateEvent(id, eventData)
// PUT /events/{id}

// Delete event
eventService.deleteEvent(id)
// DELETE /events/{id}

// Approve event
eventService.approveEvent(id)
// POST /events/{id}/approve

// Reject event
eventService.rejectEvent(id, rejectionReason)
// POST /events/{id}/reject?rejectionReason=...

// Get upcoming events
eventService.getUpcomingEvents()
// GET /events/upcoming

// Search events
eventService.searchEvents(keyword, page, size)
// GET /events/search?keyword=...&page=0&size=10

// Get events by organizer
eventService.getEventsByOrganizer(organizerId)
// GET /events/organizer/{id}

// Get events by date range
eventService.getEventsByDateRange(startDate, endDate, page, size)
// GET /events/date-range?startDate=...&endDate=...&page=0&size=10

// Get events by category
eventService.getEventsByCategory(category, page, size)
// GET /events/category/{category}?page=0&size=10
```

### Registrations
```javascript
// Get my registrations
registrationService.getMyRegistrations(page, size)
// GET /registrations?page=0&size=10

// Get registration by ID
registrationService.getRegistrationById(id)
// GET /registrations/{id}

// Get registrations for event
registrationService.getEventRegistrations(eventId, page, size)
// GET /registrations/event/{eventId}?page=0&size=10

// Register for event
registrationService.registerForEvent(eventId)
// POST /registrations?eventId={id}

// Cancel registration
registrationService.cancelRegistration(registrationId)
// DELETE /registrations/{id}

// Mark attendance
registrationService.markAttendance(registrationId, status)
// POST /registrations/{id}/attendance?status=ATTENDED
// Status values: ATTENDED, PENDING, CANCELLED, REMOVED

// Get registration count
registrationService.getRegistrationCount(eventId)
// GET /registrations/event/{eventId}/count

// Check if registered
registrationService.checkIfRegistered(eventId, studentId)
// GET /registrations/event/{eventId}/check/{studentId}
```

### Authentication
```javascript
// Register
authService.register(userData)
// POST /auth/register
// userData: { email, password, firstName, lastName, phone?, department? }

// Login
authService.login(credentials)
// POST /auth/login
// credentials: { email, password }
// Returns: { accessToken, refreshToken, tokenType, expiresIn, user }

// Logout
authService.logout()
// POST /auth/logout

// Get current user
authService.getCurrentUser()
// GET /auth/me

// Refresh token
authService.refreshToken(refreshToken)
// POST /auth/refresh
// Header: Authorization: Bearer {refreshToken}
```

---

## 📊 Response Structure

### API Wrapper (AUTO-UNWRAPPED)
```json
{
  "success": boolean,
  "message": "string",
  "data": { /* actual data */ },
  "error": null | "error message",
  "statusCode": number
}
```

### After Interceptor Unwrapping
```javascript
// Component receives:
response.data = { /* actual data */ }

// Example for login:
response.data = {
  "accessToken": "...",
  "refreshToken": "...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": { id, email, firstName, lastName, phone, department, role, accountStatus }
}
```

---

## 🛡️ Authorization Header

Automatically added by request interceptor:
```
Authorization: Bearer {accessToken}
```

No manual action needed. All requests automatically include the token if available.

---

## ⚠️ Common Mistakes to Avoid

### ❌ WRONG - Using old parameter names
```javascript
eventService.getAllEvents(page, pageSize)  // pageSize is wrong!
```

### ✅ CORRECT
```javascript
eventService.getAllEvents(page, size)
```

---

### ❌ WRONG - Old method names
```javascript
registrationService.markAttended(id)
registrationService.removeFromEvent(id, reason)
```

### ✅ CORRECT
```javascript
registrationService.markAttendance(id, 'ATTENDED')
registrationService.markAttendance(id, 'REMOVED')
```

---

### ❌ WRONG - Accessing nested data
```javascript
const user = response.data.data.user  // ❌ Extra .data!
```

### ✅ CORRECT
```javascript
const user = response.data.user  // ✅ Unwrapper handles it
```

---

### ❌ WRONG - Using old token extraction
```javascript
login(data.user, data.token)  // data.token doesn't exist!
```

### ✅ CORRECT
```javascript
login(data.user, data.accessToken, data.refreshToken, data.tokenType, data.expiresIn)
```

---

## 🧪 Quick Test Checklist

- [ ] Login works with email/password
- [ ] Tokens stored in localStorage
- [ ] localStorage keys: authToken, refreshToken, user, tokenType, expiresIn
- [ ] Authorization header sent with requests
- [ ] Page refresh maintains authentication
- [ ] Logout clears all tokens
- [ ] Event pagination works with `size` param
- [ ] Event search works with `keyword` param
- [ ] Registration endpoints use correct paths
- [ ] Character counters display in forms
- [ ] Form validation works correctly

---

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| `FORM_VALIDATION_ANALYSIS.md` | Form fields vs API spec analysis |
| `API_RESPONSE_STORAGE_ANALYSIS.md` | Response structure and storage |
| `API_INTEGRATION_FIXES.md` | Summary of changes |
| `LOCALSTORAGE_VERIFICATION.md` | localStorage structure and flow |
| `IMPLEMENTATION_CHECKLIST.md` | Complete progress checklist |
| `QUICK_REFERENCE.md` | This file |

