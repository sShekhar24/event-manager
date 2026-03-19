# 🚀 Quick Reference - Form Validation Fixes

## What Was Fixed? 📝

### Register Form ✅
```
BEFORE: name, email, password, confirmPassword
AFTER:  firstName, lastName, email, password, confirmPassword, phone (opt), department (opt)
```

### Password Special Chars ✅
```
BEFORE: /[!@#$%^&*]/  (wrong - allows ^ but not ?)
AFTER:  /[@$!%*?&]/   (correct - matches API spec)
```

### Event Max Length ✅
```
BEFORE: No validation
AFTER:  title: 255 chars, description: 5000 chars (with counters)
```

### Event API Methods ✅
```
BEFORE: approve (PUT), reject (PUT with body)
AFTER:  approve (POST), reject (POST with query param)
```

---

## Where Are The Changes? 🗂️

| What | File | What Changed |
|-----|------|---|
| Register fields | `Register.jsx` | firstName, lastName, phone, department |
| Register validation | `Register.jsx` | Updated min/max lengths |
| Password regex | `validation.js` | Line 14: `/[@$!%*?&]/` |
| Event validation | `CreateEvent.jsx` | Lines 50-90: max length checks |
| Event form fields | `CreateEvent.jsx` | Lines 134, 146: maxLength attributes |
| Form components | `FormInput.jsx` | Added maxLength prop |
| Form styling | `FormInput.css` | Added .char-count class |
| API methods | `eventService.js` | Lines 35-44: POST instead of PUT |

---

## How To Test? 🧪

### 1. Register
```
✓ firstName: "John" (2-100 chars)
✓ lastName: "Doe" (2-100 chars)
✓ email: "john@example.com"
✓ password: "SecurePass123@" (has upper, lower, number, special char)
✓ phone: "+1-555-0000" (optional)
✓ department: "CS" (optional)
```

### 2. Event Title/Description
```
✓ Title shows "X/255" counter
✓ Title blocked at 255 chars
✓ Description shows "X/5000" counter
✓ Description blocked at 5000 chars
```

### 3. Approve/Reject
```
✓ Approve uses POST (DevTools Network tab)
✓ Reject uses POST with query param (DevTools Network tab)
```

---

## Quick Code Reference 🔍

### Register Form State
```javascript
{
  firstName: '',      // NEW
  lastName: '',       // NEW
  email: '',
  password: '',
  confirmPassword: '',
  phone: '',          // NEW: Optional
  department: ''      // NEW: Optional
}
```

### Register API Payload
```javascript
{
  firstName: "John",
  lastName: "Doe",
  email: "john@example.com",
  password: "SecurePass123@",
  phone: "+1-555-0000",        // Optional
  department: "Computer Science" // Optional
}
```

### Password Validation
```javascript
/[@$!%*?&]/.test(password)
// Accepts: @ $ ! % * ? &
// Rejects: ^ (and others)
```

### Event API Calls
```javascript
// Approve
api.post(`/events/${id}/approve`)

// Reject
api.post(`/events/${id}/reject`, null, {
  params: { rejectionReason }
})
```

---

## Validation Rules Summary 📋

| Field | Type | Min | Max | Required | Notes |
|-------|------|-----|-----|----------|-------|
| firstName | string | 2 | 100 | Yes | |
| lastName | string | 2 | 100 | Yes | |
| email | string | - | - | Yes | Valid email format |
| password | string | 8 | - | Yes | Upper, lower, number, special(@$!%*?&) |
| phone | string | - | - | No | Optional |
| department | string | - | - | No | Optional |
| title | string | 3 | 255 | Yes | |
| description | string | 50 | 5000 | Yes | |
| category | string | - | - | Yes | |
| startDateTime | datetime | - | - | Yes | |
| endDateTime | datetime | - | - | Yes | Must be > startDateTime |
| location | string | - | - | Yes | |
| capacity | integer | 1 | - | Yes | Must be > 0 |
| registrationDeadline | datetime | - | - | Yes | Must be < startDateTime |
| imageUrl | string | - | - | No | Optional |

---

## Potential Issues & Solutions 🐛

### Issue: "Field not recognized" error on register
**Cause:** Backend still expects old `name` field  
**Solution:** Backend needs to accept `firstName` and `lastName`

### Issue: Password rejected as invalid
**Cause:** Using `^` or missing required special char  
**Solution:** Use only these special chars: @$!%*?&

### Issue: Approve/reject returning 405
**Cause:** Using PUT instead of POST  
**Solution:** Already fixed - verify code has `api.post()`

### Issue: Reject endpoint failing
**Cause:** rejectionReason in body instead of query  
**Solution:** Already fixed - verify code has `params: { rejectionReason }`

---

## Checklist Before Deployment ✓

- [ ] Register form shows firstName/lastName fields separately
- [ ] Register form shows phone and department (optional)
- [ ] Password validation accepts: @$!%*?&
- [ ] Password validation rejects: ^
- [ ] Event title has character counter (X/255)
- [ ] Event description has character counter (X/5000)
- [ ] Approve event uses POST method
- [ ] Reject event uses POST with query parameter
- [ ] Test registration end-to-end with backend
- [ ] Test event creation with max lengths
- [ ] Test approve/reject functionality

---

## API Endpoints Reference 🔗

```
POST /auth/register
  Expects: {firstName, lastName, email, password, phone?, department?}
  
POST /auth/login
  Expects: {email, password}
  
POST /events
  Expects: {title, description, category, startDateTime, endDateTime, 
            location, capacity, registrationDeadline, imageUrl?}
  
POST /events/{id}/approve
  Expects: {id in path}
  
POST /events/{id}/reject
  Expects: {id in path, rejectionReason in query}
```

---

## File Locations 📂

```
event-manager-ui/src/
├── components/
│   ├── auth/
│   │   ├── Register.jsx ⭐ CHANGED
│   │   └── Login.jsx ✓ OK
│   ├── admin/
│   │   ├── CreateEvent.jsx ⭐ CHANGED
│   │   └── ManageEvents.jsx ✓ OK
│   └── common/
│       ├── FormInput.jsx ⭐ CHANGED
│       └── FormInput.css ⭐ CHANGED
├── services/
│   ├── eventService.js ⭐ CHANGED
│   └── authService.js ✓ OK
└── utils/
    └── validation.js ⭐ CHANGED
```

---

## Questions? 🤔

**Q: Do I need to update the backend?**  
A: Only if it's not already expecting firstName/lastName in RegisterRequest

**Q: What if existing user data uses "name" field?**  
A: Migration script might be needed - consult backend team

**Q: Are phone and department required?**  
A: No, both are optional - users can skip them

**Q: Why was the password regex wrong?**  
A: It allowed `^` which API doesn't accept, and didn't allow `?` which API does

---

