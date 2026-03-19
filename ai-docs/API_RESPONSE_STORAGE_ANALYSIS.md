# API Response & localStorage Storage Analysis

## CRITICAL ISSUES FOUND

### 1. LOGIN RESPONSE HANDLING - INCORRECT ❌

#### Actual API Response Structure:
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400000,
    "user": {
      "id": 2,
      "email": "anubhav8804602148@gmail.com",
      "firstName": "Anubhav",
      "lastName": "Sharma",
      "phone": "7543028853",
      "department": "CSE",
      "role": "STUDENT",
      "accountStatus": "ACTIVE"
    }
  },
  "error": null,
  "statusCode": 200
}
```

#### Current UI Code (Login.jsx line 62-63):
```javascript
const { data } = response;
login(data.user, data.token);  // ❌ WRONG - data.token doesn't exist!
```

#### Should be:
```javascript
const { data } = response;
login(data.user, data.accessToken, data.refreshToken);
```

### 2. AuthContext Storage - INCOMPLETE ❌

#### Current AuthContext.js (line 27-29):
```javascript
const login = useCallback((userData, authToken) => {
  setUser(userData);
  setToken(authToken);
  localStorage.setItem(TOKEN_KEY, authToken);
  localStorage.setItem(USER_KEY, userData);
  setError(null);
}, []);
```

**Issues:**
1. ❌ **NOT storing refreshToken** - Needed for token refresh
2. ❌ **NOT storing tokenType** - Should store "Bearer" for authorization
3. ❌ **NOT storing expiresIn** - Should track token expiration

#### Should be:
```javascript
const login = useCallback((userData, accessToken, refreshToken, tokenType = 'Bearer', expiresIn) => {
  setUser(userData);
  setToken(accessToken);
  setRefreshToken(refreshToken);
  localStorage.setItem(TOKEN_KEY, accessToken);
  localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
  localStorage.setItem(USER_KEY, userData);
  localStorage.setItem('tokenType', tokenType);
  if (expiresIn) {
    localStorage.setItem('expiresIn', expiresIn.toString());
  }
  setError(null);
}, []);
```

---

## API ENDPOINT INCONSISTENCIES

### Registration Service Issues:

| Function | Current Endpoint | API Spec Endpoint | Match |
|----------|------------------|-------------------|-------|
| `getMyRegistrations()` | `/registrations/my` | `/registrations` | ❌ |
| `getEventRegistrations()` | `/events/{eventId}/registrations` | `/registrations/event/{eventId}` | ❌ |
| `registerForEvent()` | `/registrations` with `{eventId}` | `/registrations?eventId={id}` | ⚠️ (Different format) |
| `markAttended()` | `/registrations/{id}/mark-attended` | `/registrations/{id}/attendance` | ❌ |
| `cancelRegistration()` | `/registrations/{id}/cancel` | `/registrations/{id}` (DELETE) | ❌ |
| `removeFromEvent()` | `/registrations/{id}/remove` | Not in API spec | ❓ |

### Event Service Issues:

| Function | Current Code | API Spec | Issue |
|----------|--------------|----------|-------|
| `getEventsByStatus()` | `/events/by-status` | No endpoint | ❌ Not in API |
| `searchEvents()` | `/events/search` with `q` param | `/events/search` with `keyword` param | ⚠️ Wrong param name |
| `approveEvent()` | `PUT /events/{id}/approve` | `POST /events/{id}/approve` | ❌ Wrong HTTP method |
| `rejectEvent()` | `PUT /events/{id}/reject` with body | `POST /events/{id}/reject` with query param | ❌ Wrong method & format |
| `cancelEvent()` | Not in API spec | - | ❌ Not in API |

### API Response Structure Issues:

All API calls need to handle the **wrapped response format**:
```json
{
  "success": boolean,
  "message": string,
  "data": { /* actual data */ },
  "error": null | string,
  "statusCode": number
}
```

Currently, services assume direct data but API wraps everything in this structure.

---

## DETAILED ISSUES BY SERVICE

### authService.js
✅ Structure is OK, but response handling in components is wrong

### eventService.js Issues:
```javascript
// Current (WRONG)
searchEvents: (query, page = 0, pageSize = 10) => {
  return api.get('/events/search', {
    params: { q: query, page, pageSize }
  });
}

// Should be (CORRECT)
searchEvents: (keyword, page = 0, size = 10) => {
  return api.get('/events/search', {
    params: { keyword, page, size }
  });
}
```

### registrationService.js Issues:
```javascript
// Current (WRONG)
registerForEvent: (eventId) => {
  return api.post('/registrations', { eventId });
}

// Should be (CORRECT - per API spec)
registerForEvent: (eventId) => {
  return api.post('/registrations', null, {
    params: { eventId }
  });
}

// Current (WRONG)
markAttended: (registrationId) => {
  return api.put(`/registrations/${registrationId}/mark-attended`);
}

// Should be (CORRECT)
markAttendance: (registrationId, status = 'ATTENDED') => {
  return api.post(`/registrations/${registrationId}/attendance`, null, {
    params: { status }
  });
}

// Current (WRONG)
cancelRegistration: (registrationId) => {
  return api.put(`/registrations/${registrationId}/cancel`);
}

// Should be (CORRECT)
cancelRegistration: (registrationId) => {
  return api.delete(`/registrations/${registrationId}`);
}

// Current (WRONG)
getMyRegistrations: (page = 0, pageSize = 10, params = {}) => {
  return api.get('/registrations/my', { params: { page, pageSize, ...params } });
}

// Should be (CORRECT)
getMyRegistrations: (page = 0, size = 10, params = {}) => {
  return api.get('/registrations', { params: { page, size, ...params } });
}

// Current (WRONG)
getEventRegistrations: (eventId, page = 0, pageSize = 10) => {
  return api.get(`/events/${eventId}/registrations`, { params: { page, pageSize } });
}

// Should be (CORRECT)
getEventRegistrations: (eventId, page = 0, size = 10) => {
  return api.get(`/registrations/event/${eventId}`, { params: { page, size } });
}
```

---

## API Pagination Parameter Issue

**API Spec uses**: `page` and `size`
**Current UI uses**: `page` and `pageSize`

This needs to be standardized across all services.

---

## Summary of Required Fixes

### HIGH PRIORITY (Breaking Issues):
1. ❌ Fix Login.jsx to pass correct token fields from response
2. ❌ Update AuthContext to accept and store all token data (accessToken, refreshToken, tokenType, expiresIn)
3. ❌ Fix all registration endpoints to match API spec
4. ❌ Fix event service endpoints to match API spec
5. ❌ Standardize pagination params: `pageSize` → `size`
6. ❌ Fix HTTP methods (POST vs PUT, DELETE)
7. ❌ Fix query parameter formats

### MEDIUM PRIORITY (Should Fix):
1. ⚠️ Update all components using these services to pass correct params
2. ⚠️ Add response.data wrapper handling
3. ⚠️ Store and manage refresh token for token refresh flow

### LOW PRIORITY (Nice to Have):
1. Add token expiration tracking
2. Add automatic token refresh before expiration
3. Add response interceptor to unwrap data from API response wrapper

