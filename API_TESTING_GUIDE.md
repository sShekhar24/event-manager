# 🚀 API Testing Guide

## Setup Instructions

### 1. **Import Postman Collection**

**Step 1**: Download Postman
- Visit: https://www.postman.com/downloads/
- Install the Postman desktop application

**Step 2**: Import Collection
1. Open Postman
2. Click **Import** button (top-left)
3. Select **Upload Files**
4. Choose `postman_collection.json` from the project root
5. Collection will be imported with all endpoints

**Step 3**: Configure Environment
1. Click **Environments** in left sidebar
2. Click **Create**
3. Set these variables:
   - `baseUrl`: `http://localhost:8080/api`
   - Other variables will be auto-populated during test execution

---

## Quick Start Workflow

### Login as Admin
1. Go to **Authentication → Login**
2. Update body with admin credentials:
   ```json
   {
     "email": "admin@college.edu",
     "password": "Admin123!"
   }
   ```
3. Click **Send**
4. Token will auto-populate in environment

### Create an Event
1. Go to **Events → Create Event**
2. Click **Send**
3. Event ID auto-populates in environment

### Approve Event
1. Go to **Events → Approve Event**
2. Click **Send**

### Register for Event
1. Go to **Registrations → Register for Event**
2. Click **Send**

---

## Testing Different User Roles

### Admin Flow
```
1. Login with admin@college.edu
2. Create Event
3. View Events
4. Approve/Reject Events
5. Mark Attendance
```

### Student Flow
```
1. Register new account
2. Login with student credentials
3. View Approved Events
4. Search Events
5. Register for Event
6. View My Registrations
7. Cancel Registration
```

---

## Key Environment Variables

After login, these are automatically set:
- `{{accessToken}}` - JWT token (24hr expiry)
- `{{refreshToken}}` - Refresh token (7 days expiry)
- `{{userId}}` - Current user ID
- `{{eventId}}` - Last created event ID
- `{{registrationId}}` - Last registration ID

---

## Common Test Cases

### Test Case 1: Complete Registration Flow
1. Register → Login → Create Event → Approve → Register → Check Status

### Test Case 2: Event Search & Filter
1. Get All Events
2. Search by keyword
3. Filter by category
4. Filter by date range
5. Get upcoming events

### Test Case 3: Attendance Management
1. Create event and register multiple students
2. Mark attendance for each registration
3. Get registration count
4. Verify attendance status

### Test Case 4: Error Handling
1. Register twice for same event (should fail)
2. Register after deadline (should fail)
3. Register when full (should fail)
4. Access admin endpoint as student (should fail)

---

## Response Examples

### Success Response
```json
{
  "success": true,
  "message": "Event created successfully",
  "data": {
    "id": 3,
    "title": "Spring Boot Workshop"
  },
  "statusCode": 201
}
```

### Error Response
```json
{
  "success": false,
  "message": "Registration deadline has passed",
  "error": "REGISTRATION_DEADLINE_EXPIRED",
  "statusCode": 400
}
```

---

## Troubleshooting

### Issue: 401 Unauthorized
- **Cause**: Token expired or invalid
- **Solution**: Login again to refresh token

### Issue: 403 Forbidden
- **Cause**: Insufficient permissions (role-based)
- **Solution**: Use correct user role (admin/student)

### Issue: 400 Bad Request
- **Cause**: Invalid input data
- **Solution**: Check request body format and date formats

### Issue: 404 Not Found
- **Cause**: Resource doesn't exist
- **Solution**: Verify IDs and endpoint paths

---

## cURL Examples

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@college.edu","password":"Admin123!"}'
```

### Create Event
```bash
curl -X POST http://localhost:8080/api/events \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Workshop",
    "description": "...",
    "category": "Workshop",
    "startDateTime": "2026-03-22T10:00:00",
    "endDateTime": "2026-03-22T12:00:00",
    "location": "Room 101",
    "capacity": 50,
    "registrationDeadline": "2026-03-20T23:59:00"
  }'
```

### Register for Event
```bash
curl -X POST "http://localhost:8080/api/registrations?eventId=1" \
  -H "Authorization: Bearer <TOKEN>"
```

---

## API Documentation

Full API documentation is available in: `API_DOCUMENTATION.md`

**Key Sections**:
- Authentication endpoints
- Event management endpoints
- Registration management endpoints
- Response codes and error handling
- Role-based access control matrix

---

## Testing Checklist

- [ ] All endpoints accessible
- [ ] Auth tokens properly generated
- [ ] Role-based access working
- [ ] Event creation with all validations
- [ ] Event approval workflow
- [ ] Student registration with capacity checks
- [ ] Attendance tracking
- [ ] Error responses proper format
- [ ] Pagination working
- [ ] Search and filters working

---

**Last Updated**: March 15, 2026  
**API Version**: 1.0.0  
**Status**: Sprint 3-4 Complete
