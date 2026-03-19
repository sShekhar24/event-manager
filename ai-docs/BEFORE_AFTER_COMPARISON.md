# API Integration - Side-by-Side Changes

## 1. Login Response Extraction

### ❌ BEFORE (src/components/auth/Login.jsx)
```javascript
try {
  setLoading(true);
  const response = await authService.login({
    email: formData.email,
    password: formData.password
  });

  const { data } = response;
  login(data.user, data.token);  // ❌ WRONG: data.token doesn't exist!
  success('Login successful!');
```

### ✅ AFTER (src/components/auth/Login.jsx)
```javascript
try {
  setLoading(true);
  const response = await authService.login({
    email: formData.email,
    password: formData.password
  });

  const { data } = response;
  login(
    data.user,
    data.accessToken,        // ✅ CORRECT
    data.refreshToken,       // ✅ NEW
    data.tokenType,          // ✅ NEW
    data.expiresIn           // ✅ NEW
  );
  success('Login successful!');
```

---

## 2. AuthContext Login Method

### ❌ BEFORE (src/context/AuthContext.js)
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
- Only stores 2 values
- Missing refreshToken
- Missing tokenType
- Missing expiresIn

### ✅ AFTER (src/context/AuthContext.js)
```javascript
const login = useCallback((userData, accessToken, refreshTokenValue, tokenTypeValue = 'Bearer', expiresInValue) => {
  setUser(userData);
  setToken(accessToken);
  setRefreshToken(refreshTokenValue);  // ✅ NEW
  setTokenType(tokenTypeValue);        // ✅ NEW
  if (expiresInValue) {
    setExpiresIn(expiresInValue);      // ✅ NEW
  }
  
  localStorage.setItem(TOKEN_KEY, accessToken);
  localStorage.setItem(REFRESH_TOKEN_KEY, refreshTokenValue);  // ✅ NEW
  localStorage.setItem(USER_KEY, userData);
  localStorage.setItem('tokenType', tokenTypeValue);           // ✅ NEW
  if (expiresInValue) {
    localStorage.setItem('expiresIn', expiresInValue.toString());  // ✅ NEW
  }
  setError(null);
}, []);
```

---

## 3. Register Form Fields

### ❌ BEFORE (src/components/auth/Register.jsx)
```javascript
const [formData, setFormData] = useState({
  name: '',
  email: '',
  password: '',
  confirmPassword: ''
});

// Form JSX
<FormInput
  name="name"
  label="Full Name"
  type="text"
  value={formData.name}
  // ...
/>
```

**Issues:**
- Single "name" field
- No phone field
- No department field
- Doesn't match API spec

### ✅ AFTER (src/components/auth/Register.jsx)
```javascript
const [formData, setFormData] = useState({
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  confirmPassword: '',
  phone: '',        // ✅ NEW (optional)
  department: ''    // ✅ NEW (optional)
});

// Form JSX
<FormInput
  name="firstName"
  label="First Name"
  type="text"
  value={formData.firstName}
  // ...
  required
/>

<FormInput
  name="lastName"
  label="Last Name"
  type="text"
  value={formData.lastName}
  // ...
  required
/>

<FormInput
  name="phone"
  label="Phone Number (Optional)"
  type="tel"
  value={formData.phone}
  // ...
/>

<FormInput
  name="department"
  label="Department (Optional)"
  type="text"
  value={formData.department}
  // ...
/>
```

---

## 4. Event Service Endpoints

### ❌ BEFORE (src/services/eventService.js)

| Method | Endpoint | Issue |
|--------|----------|-------|
| `getAllEvents(page, pageSize)` | `/events` | ❌ pageSize param |
| `searchEvents(query, page, pageSize)` | `/events/search?q=` | ❌ q param, pageSize |
| `approveEvent(id)` | `/events/{id}/approve` | ✅ OK |
| `rejectEvent(id, reason)` | `/events/{id}/reject` | ✅ OK |
| `cancelEvent(id)` | `/events/{id}/cancel` | ❌ Not in API |
| `getEventsByStatus(status)` | `/events/by-status` | ❌ Not in API |

```javascript
searchEvents: (query, page = 0, pageSize = 10) => {
  return api.get('/events/search', {
    params: { q: query, page, pageSize }  // ❌ Wrong param name
  });
}
```

### ✅ AFTER (src/services/eventService.js)

| Method | Endpoint | Status |
|--------|----------|--------|
| `getAllEvents(page, size)` | `/events` | ✅ size param |
| `searchEvents(keyword, page, size)` | `/events/search?keyword=` | ✅ keyword param, size |
| `approveEvent(id)` | `/events/{id}/approve` | ✅ Verified |
| `rejectEvent(id, reason)` | `/events/{id}/reject` | ✅ Verified |
| `getUpcomingEvents()` | `/events/upcoming` | ✅ Added |
| `getEventsByOrganizer(id)` | `/events/organizer/{id}` | ✅ Added |
| `getEventsByDateRange(...)` | `/events/date-range` | ✅ Added |
| `getEventsByCategory(cat)` | `/events/category/{cat}` | ✅ Added |

```javascript
// ✅ CORRECT
searchEvents: (keyword, page = 0, size = 10) => {
  return api.get('/events/search', {
    params: { keyword, page, size }  // ✅ Correct param name and size
  });
},

// ✅ NEW METHODS
getUpcomingEvents: () => {
  return api.get('/events/upcoming');
},

getEventsByOrganizer: (organizerId) => {
  return api.get(`/events/organizer/${organizerId}`);
},

getEventsByDateRange: (startDate, endDate, page = 0, size = 10) => {
  return api.get('/events/date-range', {
    params: { startDate, endDate, page, size }
  });
},

getEventsByCategory: (category, page = 0, size = 10) => {
  return api.get(`/events/category/${category}`, {
    params: { page, size }
  });
}
```

---

## 5. Registration Service Endpoints

### ❌ BEFORE (src/services/registrationService.js)

```javascript
getMyRegistrations: (page = 0, pageSize = 10) => {
  return api.get('/registrations/my', {  // ❌ Wrong endpoint
    params: { page, pageSize }            // ❌ pageSize
  });
},

getEventRegistrations: (eventId, page = 0, pageSize = 10) => {
  return api.get(`/events/${eventId}/registrations`, {  // ❌ Wrong endpoint
    params: { page, pageSize }                          // ❌ pageSize
  });
},

registerForEvent: (eventId) => {
  return api.post('/registrations', { eventId });  // ❌ Body param
},

cancelRegistration: (registrationId) => {
  return api.put(`/registrations/${registrationId}/cancel`);  // ❌ PUT + wrong path
},

markAttended: (registrationId) => {
  return api.put(`/registrations/${registrationId}/mark-attended`);  // ❌ Wrong method
},
```

### ✅ AFTER (src/services/registrationService.js)

```javascript
getMyRegistrations: (page = 0, size = 10) => {
  return api.get('/registrations', {    // ✅ Correct endpoint
    params: { page, size }              // ✅ size parameter
  });
},

getEventRegistrations: (eventId, page = 0, size = 10) => {
  return api.get(`/registrations/event/${eventId}`, {  // ✅ Correct endpoint
    params: { page, size }                             // ✅ size parameter
  });
},

registerForEvent: (eventId) => {
  return api.post('/registrations', null, {  // ✅ Query params
    params: { eventId }
  });
},

cancelRegistration: (registrationId) => {
  return api.delete(`/registrations/${registrationId}`);  // ✅ DELETE method
},

markAttendance: (registrationId, status = 'ATTENDED') => {  // ✅ Renamed method
  return api.post(`/registrations/${registrationId}/attendance`, null, {
    params: { status }  // ✅ Query param with status
  });
},

// ✅ NEW METHODS
getRegistrationCount: (eventId) => {
  return api.get(`/registrations/event/${eventId}/count`);
},

checkIfRegistered: (eventId, studentId) => {
  return api.get(`/registrations/event/${eventId}/check/${studentId}`);
}
```

---

## 6. Response Interceptor

### ❌ BEFORE (src/services/api.js)
```javascript
// Response interceptor - Handle errors
api.interceptors.response.use(
  (response) => response,  // ❌ No unwrapping
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem(TOKEN_KEY);
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

### ✅ AFTER (src/services/api.js)
```javascript
// Response interceptor - Handle errors and unwrap response
api.interceptors.response.use(
  (response) => {
    // ✅ NEW: Unwrap the API response wrapper
    // Backend wraps: { success, message, data: {...}, error, statusCode }
    // We unwrap to just return the data
    if (response.data && response.data.data !== undefined) {
      return {
        ...response,
        data: response.data.data  // ✅ Extract inner data
      };
    }
    return response;
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem(TOKEN_KEY);
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

---

## 7. Component Method Calls

### ❌ BEFORE (src/components/admin/ViewRegistrations.jsx)
```javascript
const handleMarkAttended = async () => {
  try {
    await registrationService.markAttended(selectedRegistration.id);  // ❌ Old method
    success('Marked as attended');
    setShowConfirm(false);
    refetch();
  } catch (err) {
    showError(err.response?.data?.message || 'Failed to mark attendance');
  }
};

const handleRemoveStudent = async () => {
  try {
    await registrationService.removeFromEvent(selectedRegistration.id, 'Removed by admin');  // ❌ Old method
    success('Student removed from event');
    setShowConfirm(false);
    refetch();
  } catch (err) {
    showError(err.response?.data?.message || 'Failed to remove student');
  }
};
```

### ✅ AFTER (src/components/admin/ViewRegistrations.jsx)
```javascript
const handleMarkAttended = async () => {
  try {
    await registrationService.markAttendance(selectedRegistration.id, 'ATTENDED');  // ✅ New method
    success('Marked as attended');
    setShowConfirm(false);
    refetch();
  } catch (err) {
    showError(err.response?.data?.message || 'Failed to mark attendance');
  }
};

const handleRemoveStudent = async () => {
  try {
    await registrationService.markAttendance(selectedRegistration.id, 'REMOVED');  // ✅ Uses attendance endpoint
    success('Student removed from event');
    setShowConfirm(false);
    refetch();
  } catch (err) {
    showError(err.response?.data?.message || 'Failed to remove student');
  }
};
```

---

## 8. Password Validation

### ❌ BEFORE (src/utils/validation.js)
```javascript
export const validatePassword = (password) => {
  const requirements = {
    minLength: password.length >= 8,
    hasUppercase: /[A-Z]/.test(password),
    hasLowercase: /[a-z]/.test(password),
    hasNumber: /[0-9]/.test(password),
    hasSpecial: /[!@#$%^&*]/.test(password)  // ❌ Allows: ! @ # $ % ^ & *
  };
  // ...
};
```

### ✅ AFTER (src/utils/validation.js)
```javascript
export const validatePassword = (password) => {
  const requirements = {
    minLength: password.length >= 8,
    hasUppercase: /[A-Z]/.test(password),
    hasLowercase: /[a-z]/.test(password),
    hasNumber: /[0-9]/.test(password),
    hasSpecial: /[@$!%*?&]/.test(password)  // ✅ Matches API spec: @ $ ! % * ? &
  };
  // ...
};
```

---

## 9. Event Creation Form Validation

### ❌ BEFORE (src/components/admin/CreateEvent.jsx)
```javascript
const validateForm = () => {
  const newErrors = {};

  if (!formData.title.trim()) {
    newErrors.title = 'Event title is required';
  }
  // ❌ Missing: max length for title (255)

  if (!formData.description.trim()) {
    newErrors.description = 'Description is required';
  } else if (formData.description.trim().length < 50) {
    newErrors.description = 'Description must be at least 50 characters';
  }
  // ❌ Missing: max length for description (5000)
```

### ✅ AFTER (src/components/admin/CreateEvent.jsx)
```javascript
const validateForm = () => {
  const newErrors = {};

  if (!formData.title.trim()) {
    newErrors.title = 'Event title is required';
  } else if (formData.title.trim().length < 3) {
    newErrors.title = 'Title must be at least 3 characters';
  } else if (formData.title.trim().length > 255) {  // ✅ NEW: Max length
    newErrors.title = 'Title must not exceed 255 characters';
  }

  if (!formData.description.trim()) {
    newErrors.description = 'Description is required';
  } else if (formData.description.trim().length < 50) {
    newErrors.description = 'Description must be at least 50 characters';
  } else if (formData.description.trim().length > 5000) {  // ✅ NEW: Max length
    newErrors.description = 'Description must not exceed 5000 characters';
  }
```

And in the form JSX:
```javascript
<FormInput
  name="title"
  label="Event Title"
  type="text"
  value={formData.title}
  onChange={handleChange}
  error={errors.title}
  placeholder="Enter event title"
  maxLength={255}  // ✅ NEW
  required
/>

<FormTextarea
  name="description"
  label="Description"
  value={formData.description}
  onChange={handleChange}
  error={errors.description}
  placeholder="Enter detailed event description (min 50 characters)"
  rows={4}
  maxLength={5000}  // ✅ NEW
  required
/>
```

---

## 10. FormInput Component

### ❌ BEFORE (src/components/common/FormInput.jsx)
```javascript
export const FormInput = ({
  name,
  label,
  type = 'text',
  value,
  onChange,
  error,
  placeholder,
  required = false,
  disabled = false,
  ...props
}) => {
  return (
    <div className="form-group">
      {label && (
        <label htmlFor={name} className="form-label">
          {label}
          {required && <span className="required">*</span>}
        </label>
      )}
      <input
        id={name}
        name={name}
        type={type}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        disabled={disabled}
        className={`form-input ${error ? 'has-error' : ''}`}
        {...props}
      />
      {error && <span className="form-error">{error}</span>}
    </div>
  );
};
```

### ✅ AFTER (src/components/common/FormInput.jsx)
```javascript
export const FormInput = ({
  name,
  label,
  type = 'text',
  value,
  onChange,
  error,
  placeholder,
  required = false,
  disabled = false,
  maxLength,  // ✅ NEW parameter
  ...props
}) => {
  return (
    <div className="form-group">
      {label && (
        <label htmlFor={name} className="form-label">
          {label}
          {required && <span className="required">*</span>}
          {maxLength && <span className="char-count">{value.length}/{maxLength}</span>}  {/* ✅ NEW */}
        </label>
      )}
      <input
        id={name}
        name={name}
        type={type}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        disabled={disabled}
        maxLength={maxLength}  {/* ✅ NEW */}
        className={`form-input ${error ? 'has-error' : ''}`}
        {...props}
      />
      {error && <span className="form-error">{error}</span>}
    </div>
  );
};
```

---

## Summary of All Changes

| Category | Before | After | Status |
|----------|--------|-------|--------|
| **Login Response** | data.token | data.accessToken + refreshToken | ✅ FIXED |
| **Token Storage** | Access token only | All tokens + type + expiration | ✅ FIXED |
| **Register Fields** | Single "name" | firstName + lastName + optional fields | ✅ FIXED |
| **Event Title Validation** | Min only | Min 3, Max 255 | ✅ FIXED |
| **Description Validation** | Min only | Min 50, Max 5000 | ✅ FIXED |
| **Pagination Param** | pageSize | size | ✅ FIXED |
| **Registration Endpoints** | 5 wrong | All correct | ✅ FIXED |
| **Event Endpoints** | 2 wrong | All correct | ✅ FIXED |
| **Password Regex** | [!@#$%^&*] | [@$!%*?&] | ✅ FIXED |
| **Response Unwrapping** | Not done | Automatic | ✅ FIXED |
| **Character Counter** | Not available | Available | ✅ ADDED |

