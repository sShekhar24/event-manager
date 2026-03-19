# Form Validation Analysis - API vs UI Mismatch

## Summary
Analyzed the Backend API docs (Swagger) against the UI forms. Found several mismatches between what the API requires and what the UI forms collect.

---

## 1. REGISTER FORM - CRITICAL ISSUES

### API Requirements (RegisterRequest)
```json
{
  "email": "string (required, email format)",
  "password": "string (required, 8+ chars, pattern: (?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]))",
  "firstName": "string (required, 2-100 chars)",
  "lastName": "string (required, 2-100 chars)",
  "phone": "string (optional)",
  "department": "string (optional)"
}
```

### Current UI Implementation (Register.jsx)
**Fields being used:**
- `name` ❌ (WRONG - API expects firstName + lastName)
- `email` ✅ (Correct)
- `password` ✅ (Correct, has validation)
- `confirmPassword` ✅ (For UX, not sent to API)

**Issues:**
1. ❌ **MISSING**: firstName field (Required)
2. ❌ **MISSING**: lastName field (Required)
3. ❌ **WRONG**: Using single "name" field instead of separate firstName/lastName
4. ❌ **MISSING**: phone field (Optional but should be available)
5. ❌ **MISSING**: department field (Optional but should be available)
6. ⚠️ **Password pattern validation needs improvement** - Current regex in validation.js checks for special chars `[!@#$%^&*]` but API spec requires `[@$!%*?&]`

### authService.js Issue
```javascript
// Current - sends wrong field structure
register: (userData) => {
  return api.post('/auth/register', userData);  // sends {name, email, password}
}
// Should send: {firstName, lastName, email, password, phone?, department?}
```

---

## 2. LOGIN FORM - OK

### API Requirements (LoginRequest)
```json
{
  "email": "string (required, email format)",
  "password": "string (required)"
}
```

### Current UI Implementation (Login.jsx)
✅ **All correct**
- email (required)
- password (required)

No issues found.

---

## 3. CREATE EVENT FORM - GOOD WITH MINOR ISSUES

### API Requirements (EventDTO)
```json
{
  "title": "string (required, 3-255 chars)",
  "description": "string (required, 50-5000 chars)",
  "category": "string (required)",
  "startDateTime": "date-time (required)",
  "endDateTime": "date-time (required)",
  "location": "string (required)",
  "capacity": "integer (required)",
  "registrationDeadline": "date-time (required)",
  "imageUrl": "string (optional)",
  "status": "string (set by backend)",
  "rejectionReason": "string (set by backend)"
}
```

### Current UI Implementation (CreateEvent.jsx)
✅ **All required fields present**
- title ✅ (with min 3 chars validation)
- description ✅ (with min 50 chars validation)
- category ✅ (required)
- startDateTime ✅ (required)
- endDateTime ✅ (required)
- location ✅ (required)
- capacity ✅ (required)
- registrationDeadline ✅ (required)
- imageUrl ✅ (optional)

**Minor issues:**
1. ⚠️ Max length validation for title (max 255) not enforced in UI
2. ⚠️ Max length validation for description (max 5000) not enforced in UI
3. ✅ Form does NOT send status/rejectionReason (correct - backend only)

### eventService.js Issue
```javascript
// Current implementation has issues:
approveEvent: (id) => {
  return api.put(`/events/${id}/approve`);  // API spec says POST not PUT
},

rejectEvent: (id, reason) => {
  return api.put(`/events/${id}/reject`, { reason });  // API uses query param, not body
}
```

---

## 4. PASSWORD VALIDATION MISMATCH

### Current validation.js regex:
```javascript
/[!@#$%^&*]/  // allows: ! @ # $ % ^ & *
```

### API spec requires:
```javascript
/[@$!%*?&]/   // allows: @ $ ! % * ? &
```

**Difference:** Current allows `^`, API also allows `?` but not `^`
**Action:** Update regex to match API spec exactly

---

## Summary of Required Fixes

### HIGH PRIORITY (Blocking Functionality)
1. ❌ Fix Register form to use `firstName` + `lastName` instead of `name`
2. ❌ Add `phone` field (optional) to Register form
3. ❌ Add `department` field (optional) to Register form
4. ❌ Update authService.register() to send correct field names
5. ❌ Update password regex in validation.js to match API spec

### MEDIUM PRIORITY (Should Enforce)
1. ⚠️ Add max length validation for title (255 chars)
2. ⚠️ Add max length validation for description (5000 chars)
3. ⚠️ Fix eventService methods (approveEvent, rejectEvent) to use correct HTTP methods

### LOW PRIORITY (Nice to Have)
1. Add optional fields styling to distinguish from required fields
2. Add character counters for fields with length limits

