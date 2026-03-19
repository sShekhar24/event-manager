# LocalStorage Structure & API Response Mapping

## Current Login API Response

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbnViaGF2ODgwNDYwMjE0OEBnbWFpbC5jb20iLCJpYXQiOjE3NzM5MTQzNDMsImV4cCI6MTc3NDAwMDc0Mywicm9sZSI6IlNUVURFTlQifQ.Ephl7IHepG_742FaVSUczbekANEZrKeonTyiD7adzpU-OY2ZPK_PXdmA5WY5YaeUwfp41F0wI7p9qo3lNGjVng",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbnViaGF2ODgwNDYwMjE0OEBnbWFpbC5jb20iLCJpYXQiOjE3NzM5MTQzNDMsImV4cCI6MTc3NDUxOTE0M30.7QQOaFPKT9X2GfT6KmCpyQtV5xvtaxn2TtfoLzTo_8BRxHSuyqpVyaGBRIfMt76KY55DA2k3TP1ItkkVMCo9HQ",
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

## Data Flow After API Response Interceptor

### Before Interceptor:
```javascript
response.data = {
  "success": true,
  "message": "Login successful",
  "data": { /* token data */ },
  "error": null,
  "statusCode": 200
}
```

### After Interceptor (Unwrapped):
```javascript
response.data = {
  "accessToken": "...",
  "refreshToken": "...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": { /* user object */ }
}
```

## What Gets Stored in localStorage

### Keys Used:
```javascript
const TOKEN_KEY = 'authToken';              // From constants.js
const REFRESH_TOKEN_KEY = 'refreshToken';   // From constants.js  
const USER_KEY = 'user';                    // From constants.js
```

### Storage After Login:

```
localStorage = {
  "authToken": "eyJhbGciOiJIUzUxMiJ9...",           // Stores accessToken
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",       // Stores refreshToken
  "user": {                                         // Stores user object
    "id": 2,
    "email": "anubhav8804602148@gmail.com",
    "firstName": "Anubhav",
    "lastName": "Sharma",
    "phone": "7543028853",
    "department": "CSE",
    "role": "STUDENT",
    "accountStatus": "ACTIVE"
  },
  "tokenType": "Bearer",                           // Stores tokenType
  "expiresIn": "86400000"                          // Stores as string
}
```

## AuthContext State After Login

```javascript
{
  user: { /* user object */ },
  token: "eyJhbGciOiJIUzUxMiJ9...",               // accessToken
  refreshToken: "eyJhbGciOiJIUzUxMiJ9...",        // refreshToken
  tokenType: "Bearer",                             // tokenType
  expiresIn: 86400000,                             // as number
  loading: false,
  error: null,
  isAuthenticated: true,
  isAdmin: false,  // role === 'ADMIN'
  isStudent: true, // role === 'STUDENT'
}
```

## API Request Flow

### Request with Token:
```javascript
// api.js request interceptor adds authorization header
config.headers.Authorization = `Bearer ${token}`;

// Request sent to backend:
GET /api/events
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

## Test Cases

### ✅ Login Response Handling:
1. User submits login form
2. authService.login() makes POST request
3. API returns wrapped response
4. Response interceptor unwraps data
5. Login.jsx receives data with accessToken, refreshToken, user
6. AuthContext.login() stores all values
7. localStorage is populated correctly
8. User is redirected to dashboard

### ✅ Token Persistence on Reload:
1. User is logged in
2. Page is refreshed (F5)
3. AuthProvider useEffect runs
4. Reads tokens and user from localStorage
5. Restores AuthContext state
6. User stays logged in

### ✅ Request Authorization:
1. User makes API call (e.g., GET /registrations)
2. Request interceptor adds Authorization header
3. Backend validates token
4. Response is returned and unwrapped
5. Component receives clean data object

### ✅ Logout:
1. User clicks logout
2. logout() is called
3. All state is cleared
4. localStorage is cleared completely
5. User is redirected to login

## Common Issues & Solutions

### Issue: "Cannot read property 'accessToken' of undefined"
**Cause:** response.data doesn't exist or response.data.data is undefined
**Solution:** Response interceptor handles unwrapping
**Status:** ✅ FIXED

### Issue: "localStorage is not storing tokens"
**Cause:** localStorage.js helper functions weren't being used
**Solution:** Using localStorage.setItem() from utils/localStorage.js
**Status:** ✅ FIXED

### Issue: "Token not in Authorization header"
**Cause:** Request interceptor fetches TOKEN_KEY which should have accessToken
**Solution:** Storing as 'authToken' key, request interceptor reads it
**Status:** ✅ VERIFIED

### Issue: "User stays logged in after logout"
**Cause:** localStorage not completely cleared
**Solution:** logout() clears all token-related keys
**Status:** ✅ FIXED

## Edge Cases to Test

1. **Token Expiration:**
   - Store expiresIn: 86400000 (24 hours in ms)
   - Implement expiration check before API calls
   - Refresh token before expiration

2. **Refresh Token Flow:**
   - Access token expires
   - Refresh token endpoint called
   - New access token obtained
   - User session continues

3. **Offline State:**
   - User goes offline
   - localStorage persists token
   - When online, token still valid
   - User session restored

4. **Multiple Tabs:**
   - User logs in on Tab A
   - localStorage updated in Tab A
   - Tab B reads updated localStorage
   - Both tabs have same auth state

