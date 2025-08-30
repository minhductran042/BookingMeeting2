# BOOKMEETING 2.0 - COMPLETE API DOCUMENTATION WITH DETAILED RESPONSE OBJECTS

## 📋 OVERVIEW
**Base URL:** `http://localhost:8080`

### Response Format
```json
{
  "status": "number (HTTP status code)",
  "message": "string (response message)",
  "data": "object (response data, optional)"
}
```

---

# ================================================================================
# RESPONSE OBJECTS DEFINITIONS
# ================================================================================

## 📄 NotificationResponse
```json
{
  "title": "string",
  "body": "string",
  "tokenDevice": "string",
  "imageUrl": "string",
  "sentAt": "2024-01-01 10:00:00",
  "data": {
    "additionalProp1": "string",
    "additionalProp2": "string"
  },
  "isSeen": "boolean",
  "message": "string"
}
```

## 👤 ProfileResponse
```json
{
  "id": "number",
  "username": "string",
  "email": "string",
  "fullName": "string",
  "phoneNumber": "string",
  "role": "ADMIN|USER",
  "department": "string"
}
```

## 📱 UserDevice
```json
{
  "id": "number",
  "user": "User object",
  "deviceToken": "string",
  "deviceType": "ANDROID|IOS|WEB",
  "deviceInfo": "string",
  "active": "boolean",
  "createdAt": "2024-01-01 10:00:00",
  "updatedAt": "2024-01-01 10:00:00"
}
```

## 📅 CalendarOverviewResponse
```json
{
  "startDate": "2024-01-01",
  "endDate": "2024-01-31",
  "totalBookings": "number",
  "totalRooms": "number",
  "totalLocations": "number",
  "days": [
    {
      "date": "2024-01-01",
      "dayOfWeek": "MONDAY",
      "totalBookings": "number",
      "availableRooms": "number",
      "bookings": ["array of CalendarBookingResponse objects"],
      "status": "AVAILABLE|BUSY|FULL"
    }
  ]
}
```

## 📅 CalendarDayResponse
```json
{
  "date": "2024-01-01",
  "dayOfWeek": "MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY",
  "totalBookings": "number",
  "availableRooms": "number",
  "bookings": ["array of CalendarBookingResponse objects"],
  "status": "AVAILABLE|BUSY|FULL"
}
```

## 📅 CalendarBookingResponse
```json
{
  "id": "number",
  "title": "string",
  "startTime": "2024-01-01 10:00:00",
  "endTime": "2024-01-01 11:00:00",
  "status": "PENDING|APPROVED|REJECTED|CANCELLED",
  "roomName": "string",
  "locationName": "string",
  "createdBy": "string",
  "color": "string"
}
```

## 👥 UserResponse
```json
{
  "id": "number",
  "username": "string",
  "email": "string",
  "fullName": "string",
  "phone": "string",
  "role": "ADMIN|USER",
  "avatarUrl": "string",
  "department": "string",
  "active": "boolean",
  "createdAt": "2024-01-01 10:00:00",
  "updatedAt": "2024-01-01 10:00:00"
}
```

## 🏢 MeetingRoomResponse
```json
{
  "id": "number",
  "name": "string",
  "capacity": "number",
  "description": "string",
  "equipments": "string",
  "imageUrl": "string",
  "active": "boolean",
  "location": {
    "id": "number",
    "name": "string",
    "address": "string",
    "city": "string",
    "country": "string",
    "postalCode": "string",
    "active": "boolean",
    "createdAt": "2024-01-01 10:00:00",
    "updatedAt": "2024-01-01 10:00:00"
  },
  "createdAt": "2024-01-01 10:00:00",
  "updatedAt": "2024-01-01 10:00:00"
}
```

## 📍 LocationResponse
```json
{
  "id": "number",
  "name": "string",
  "address": "string",
  "city": "string",
  "country": "string",
  "postalCode": "string",
  "active": "boolean",
  "createdAt": "2024-01-01 10:00:00",
  "updatedAt": "2024-01-01 10:00:00"
}
```

## 📋 BookingResponse
```json
{
  "id": "number",
  "createdBy": {
    "id": "number",
    "username": "string",
    "email": "string",
    "fullName": "string",
    "phone": "string",
    "role": "ADMIN|USER",
    "avatarUrl": "string",
    "department": "string",
    "active": "boolean",
    "createdAt": "2024-01-01 10:00:00",
    "updatedAt": "2024-01-01 10:00:00"
  },
  "meetingRoom": {
    "id": "number",
    "name": "string",
    "capacity": "number",
    "description": "string",
    "equipments": "string",
    "imageUrl": "string",
    "active": "boolean",
    "location": {
      "id": "number",
      "name": "string",
      "address": "string",
      "city": "string",
      "country": "string",
      "postalCode": "string",
      "active": "boolean",
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    },
    "createdAt": "2024-01-01 10:00:00",
    "updatedAt": "2024-01-01 10:00:00"
  },
  "title": "string",
  "description": "string",
  "purpose": "string",
  "startTime": "2024-01-01 10:00:00",
  "endTime": "2024-01-01 11:00:00",
  "status": "PENDING|APPROVED|REJECTED|CANCELLED",
  "approver": "UserResponse object (optional)",
  "approvedAt": "2024-01-01 10:00:00 (optional)",
  "cancelledAt": "2024-01-01 10:00:00 (optional)",
  "cancelledBy": "UserResponse object (optional)",
  "adminNotes": "string",
  "createdAt": "2024-01-01 10:00:00",
  "updatedAt": "2024-01-01 10:00:00",
  "participants": [
    {
      "id": "number",
      "username": "string",
      "email": "string",
      "fullName": "string",
      "phone": "string",
      "role": "ADMIN|USER",
      "avatarUrl": "string",
      "department": "string",
      "active": "boolean",
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    }
  ]
}
```

---

# ================================================================================
# AUTHENTICATION APIs
# ================================================================================

## Authentication
### Login
- **URL:** `POST /api/auth/login`
- **Headers:**
  ```json
  {
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "email": "string (required)",
    "password": "string (required)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Login successful",
    "data": {
      "accessToken": "string",
      "refreshToken": "string",
      "tokenType": "string",
      "expiresIn": "number",
      "expiresInFormatted": "string",
      "userInfo": {
        "id": "number",
        "username": "string",
        "email": "string",
        "fullName": "string",
        "role": "ADMIN|USER",
        "isActive": "boolean",
        "authorities": ["array"]
      }
    }
  }
  ```

### Register
- **URL:** `POST /api/auth/register`
- **Headers:**
  ```json
  {
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "username": "string (optional)",
    "email": "string (required)",
    "password": "string (required, min 6 chars)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 201,
    "message": "Registration successful",
    "data": {
      "id": "number",
      "username": "string",
      "email": "string"
    }
  }
  ```

### Refresh Token
- **URL:** `POST /api/auth/refresh-token`
- **Description:** Uses refresh token from HttpOnly cookie
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Token refreshed successfully",
    "data": {
      "accessToken": "string",
      "refreshToken": "string",
      "tokenType": "string",
      "expiresIn": "number",
      "expiresInFormatted": "string"
    }
  }
  ```

### Logout
- **URL:** `POST /api/auth/logout`
- **Description:** Uses refresh token from HttpOnly cookie
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Logout successful"
  }
  ```

### Get Current User
- **URL:** `GET /api/auth/me`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "User info retrieved successfully",
    "data": {
      "id": "number",
      "username": "string",
      "email": "string",
      "fullName": "string",
      "role": "ADMIN|USER",
      "isActive": "boolean",
      "authorities": ["array"]
    }
  }
  ```

---

# ================================================================================
# USER MANAGEMENT APIs (ADMIN ONLY)
# ================================================================================

## User Management
### Create User
- **URL:** `POST /api/admin/users`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "username": "string (required)",
    "fullName": "string (required)",
    "email": "string (required, valid email)",
    "password": "string (required, min 6 chars)",
    "phone": "string (optional)",
    "department": "string (optional)",
    "role": "ADMIN|USER (required)",
    "avatarUrl": "string (optional)",
    "active": "boolean (default: true)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 201,
    "message": "User created successfully",
    "data": {
      "id": "number",
      "username": "string",
      "email": "string",
      "fullName": "string",
      "phone": "string",
      "role": "ADMIN|USER",
      "avatarUrl": "string",
      "department": "string",
      "active": "boolean",
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    }
  }
  ```

### Update User
- **URL:** `PUT /api/admin/users/{id}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "username": "string (optional)",
    "fullName": "string (optional)",
    "email": "string (optional, valid email)",
    "phone": "string (optional)",
    "department": "string (optional)",
    "role": "ADMIN|USER (optional)",
    "avatarUrl": "string (optional)",
    "active": "boolean (optional)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "User updated successfully",
    "data": {
      "id": "number",
      "username": "string",
      "email": "string",
      "fullName": "string",
      "phone": "string",
      "role": "ADMIN|USER",
      "avatarUrl": "string",
      "department": "string",
      "active": "boolean",
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    }
  }
  ```

### Delete User
- **URL:** `DELETE /api/admin/users/{id}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 204,
    "message": "User deleted successfully"
  }
  ```

### Get User by ID
- **URL:** `GET /api/admin/users/{id}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "User found",
    "data": {
      "id": "number",
      "username": "string",
      "email": "string",
      "fullName": "string",
      "phone": "string",
      "role": "ADMIN|USER",
      "avatarUrl": "string",
      "department": "string",
      "active": "boolean",
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    }
  }
  ```

### Get User List
- **URL:** `GET /api/admin/users/list`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Query Params:**
  ```json
  {
    "page": "number (default: 0)",
    "size": "number (default: 10)",
    "sortBy": "string (default: 'id')",
    "sortDirection": "asc|desc (default: 'asc')"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Users retrieved successfully",
    "data": [
      {
        "id": "number",
        "username": "string",
        "email": "string",
        "fullName": "string",
        "phone": "string",
        "role": "ADMIN|USER",
        "avatarUrl": "string",
        "department": "string",
        "active": "boolean",
        "createdAt": "2024-01-01 10:00:00",
        "updatedAt": "2024-01-01 10:00:00"
      }
    ]
  }
  ```

### Change User Status
- **URL:** `POST /api/admin/users/{id}/status`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Query Params:**
  ```json
  {
    "status": "boolean (true/false)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "User status changed successfully",
    "data": {
      "id": "number",
      "username": "string",
      "email": "string",
      "fullName": "string",
      "phone": "string",
      "role": "ADMIN|USER",
      "avatarUrl": "string",
      "department": "string",
      "active": "boolean",
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    }
  }
  ```

### Change User Role
- **URL:** `PATCH /api/admin/users/{id}/role`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "role": "ADMIN|USER (required)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "User role changed successfully",
    "data": {
      "id": "number",
      "username": "string",
      "email": "string",
      "fullName": "string",
      "phone": "string",
      "role": "ADMIN|USER",
      "avatarUrl": "string",
      "department": "string",
      "active": "boolean",
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    }
  }
  ```

---

# ================================================================================
# BOOKING MANAGEMENT APIs (ADMIN ONLY)
# ================================================================================

## Booking Management
### Get Booking by ID
- **URL:** `GET /api/admin/booking/{id}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Booking retrieved successfully",
    "data": {
      "id": "number",
      "createdBy": {
        "id": "number",
        "username": "string",
        "email": "string",
        "fullName": "string",
        "phone": "string",
        "role": "ADMIN|USER",
        "avatarUrl": "string",
        "department": "string",
        "active": "boolean",
        "createdAt": "2024-01-01 10:00:00",
        "updatedAt": "2024-01-01 10:00:00"
      },
      "meetingRoom": {
        "id": "number",
        "name": "string",
        "capacity": "number",
        "description": "string",
        "equipments": "string",
        "imageUrl": "string",
        "active": "boolean",
        "location": {
          "id": "number",
          "name": "string",
          "address": "string",
          "city": "string",
          "country": "string",
          "postalCode": "string",
          "active": "boolean",
          "createdAt": "2024-01-01 10:00:00",
          "updatedAt": "2024-01-01 10:00:00"
        },
        "createdAt": "2024-01-01 10:00:00",
        "updatedAt": "2024-01-01 10:00:00"
      },
      "title": "string",
      "description": "string",
      "purpose": "string",
      "startTime": "2024-01-01 10:00:00",
      "endTime": "2024-01-01 11:00:00",
      "status": "PENDING|APPROVED|REJECTED|CANCELLED",
      "approver": "UserResponse object (optional)",
      "approvedAt": "2024-01-01 10:00:00 (optional)",
      "cancelledAt": "2024-01-01 10:00:00 (optional)",
      "cancelledBy": "UserResponse object (optional)",
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00",
      "participants": [
        {
          "id": "number",
          "username": "string",
          "email": "string",
          "fullName": "string",
          "phone": "string",
          "role": "ADMIN|USER",
          "avatarUrl": "string",
          "department": "string",
          "active": "boolean",
          "createdAt": "2024-01-01 10:00:00",
          "updatedAt": "2024-01-01 10:00:00"
        }
      ]
    }
  }
  ```

### Get All Bookings
- **URL:** `GET /api/admin/booking`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Query Params:**
  ```json
  {
    "page": "number (default: 0)",
    "size": "number (default: 10)",
    "sortBy": "string (default: 'id')",
    "sortDir": "ASC|DESC (default: 'ASC')"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Bookings retrieved successfully",
    "data": ["array of BookingResponse objects"]
  }
  ```

### Update Booking
- **URL:** `PUT /api/admin/booking/{id}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "title": "string (max 200 chars, optional)",
    "description": "string (max 500 chars, optional)",
    "purpose": "string (max 500 chars, optional)",
    "startTime": "datetime (optional)",
    "endTime": "datetime (optional)",
    "status": "PENDING|APPROVED|REJECTED|CANCELLED (required)",
    "adminNotes": "string (optional)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Booking updated successfully",
    "data": "BookingResponse object"
  }
  ```

### Delete Booking
- **URL:** `DELETE /api/admin/booking/{id}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Booking deleted successfully"
  }
  ```

### Approve Booking
- **URL:** `POST /api/admin/booking/approve/{id}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "adminNotes": "string (max 500 chars, optional)",
    "reason": "string (max 200 chars, optional)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Booking approved successfully"
  }
  ```

### Reject Booking
- **URL:** `POST /api/admin/booking/reject/{id}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "adminNotes": "string (max 500 chars, optional)",
    "reason": "string (max 200 chars, required)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Booking rejected successfully"
  }
  ```

### Search Bookings
- **URL:** `POST /api/admin/booking/search`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Query Params:**
  ```json
  {
    "page": "number (default: 0)",
    "size": "number (default: 10)",
    "sortBy": "string (default: 'id')",
    "sortDir": "ASC|DESC (default: 'ASC')"
  }
  ```
- **Input:**
  ```json
  {
    "title": "string (optional)",
    "createdByUsername": "string (optional)",
    "meetingRoomId": "number (optional)",
    "status": "PENDING|APPROVED|REJECTED|CANCELLED (optional)",
    "startTimeFrom": "datetime (optional)",
    "startTimeTo": "datetime (optional)",
    "endTimeFrom": "datetime (optional)",
    "endTimeTo": "datetime (optional)",
    "createdAtFrom": "datetime (optional)",
    "createdAtTo": "datetime (optional)",
    "department": "string (optional)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Bookings retrieved successfully",
    "data": ["array of BookingResponse objects"]
  }
  ```

### Get Bookings by Status
- **URL:** `GET /api/admin/booking/status/{status}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Query Params:**
  ```json
  {
    "page": "number (default: 0)",
    "size": "number (default: 10)",
    "sortBy": "string (default: 'id')",
    "sortDir": "ASC|DESC (default: 'ASC')"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Bookings retrieved successfully",
    "data": ["array of BookingResponse objects"]
  }
  ```

### Get Total Booking Count
- **URL:** `GET /api/admin/booking/statistics/count`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Total booking count retrieved successfully",
    "data": "number"
  }
  ```

### Get Booking Count by Status
- **URL:** `GET /api/admin/booking/statistics/count/status/{status}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Booking count by status retrieved successfully",
    "data": "number"
  }
  ```

### Get Booking Count by Meeting Room
- **URL:** `GET /api/admin/booking/statistics/count/meeting-room/{meetingRoomId}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Booking count by meeting room retrieved successfully",
    "data": "number"
  }
  ```

### Get Booking Count by User
- **URL:** `GET /api/admin/booking/statistics/count/user/{userId}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Booking count by user retrieved successfully",
    "data": "number"
  }
  ```

---

# ================================================================================
# CLIENT BOOKING MANAGEMENT APIs
# ================================================================================

## Client Booking Management
### Get My Bookings
- **URL:** `GET /api/client/booking/my`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Bookings fetched successfully",
    "data": ["array of BookingResponse objects"]
  }
  ```

### Get Participated Bookings
- **URL:** `GET /api/client/booking/participated`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Participated bookings fetched successfully",
    "data": ["array of BookingResponse objects"]
  }
  ```

### Get My Approved Bookings
- **URL:** `GET /api/client/booking/my/approved`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Approved bookings fetched successfully",
    "data": ["array of BookingResponse objects"]
  }
  ```

### Get Participated Approved Bookings
- **URL:** `GET /api/client/booking/participated/approved`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Participated approved bookings fetched successfully",
    "data": ["array of BookingResponse objects"]
  }
  ```

### Get My Pending Bookings
- **URL:** `GET /api/client/booking/my/pending`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Pending bookings fetched successfully",
    "data": ["array of BookingResponse objects"]
  }
  ```

### Get Participated Pending Bookings
- **URL:** `GET /api/client/booking/participated/pending`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Participated pending bookings fetched successfully",
    "data": ["array of BookingResponse objects"]
  }
  ```

### Get Booking by ID
- **URL:** `GET /api/client/booking/{id}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Booking fetched successfully",
    "data": "BookingResponse object"
  }
  ```

### Create Booking
- **URL:** `POST /api/client/booking`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "title": "string (required)",
    "description": "string (optional)",
    "purpose": "string (optional)",
    "meetingRoomId": "number (required)",
    "startTime": "datetime (required)",
    "endTime": "datetime (required)",
    "participantIds": ["array of user IDs (optional)"]
  }
  ```
- **Response:**
  ```json
  {
    "status": 201,
    "message": "Booking created successfully",
    "data": "BookingResponse object"
  }
  ```

### Update Booking
- **URL:** `PUT /api/client/booking/{id}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "title": "string (optional)",
    "description": "string (optional)",
    "purpose": "string (optional)",
    "startTime": "datetime (optional)",
    "endTime": "datetime (optional)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Booking updated successfully",
    "data": "BookingResponse object"
  }
  ```

### Cancel Booking
- **URL:** `DELETE /api/client/booking/{id}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 204,
    "message": "Booking cancelled successfully"
  }
  ```

### Add Participant
- **URL:** `POST /api/client/booking/add-participant/{bookingId}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "participantId": "number (required)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Participant added successfully"
  }
  ```

### Remove Participant
- **URL:** `POST /api/client/booking/remove-participant/{bookingId}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "participantId": "number (required)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Participant removed successfully"
  }
  ```

---

# ================================================================================
# MEETING ROOM MANAGEMENT APIs
# ================================================================================

## Meeting Room Management
### Get Active Rooms
- **URL:** `GET /api/rooms/active`
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Active meeting rooms retrieved successfully",
    "data": [
      {
        "id": "number",
        "name": "string",
        "capacity": "number",
        "description": "string",
        "equipments": "string",
        "imageUrl": "string",
        "active": "boolean",
        "location": {
          "id": "number",
          "name": "string",
          "address": "string",
          "city": "string",
          "country": "string",
          "postalCode": "string",
          "active": "boolean",
          "createdAt": "2024-01-01 10:00:00",
          "updatedAt": "2024-01-01 10:00:00"
        },
        "createdAt": "2024-01-01 10:00:00",
        "updatedAt": "2024-01-01 10:00:00"
      }
    ]
  }
  ```

### Get Room by ID
- **URL:** `GET /api/rooms/{id}`
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Meeting room retrieved successfully",
    "data": {
      "id": "number",
      "name": "string",
      "capacity": "number",
      "description": "string",
      "equipments": "string",
      "imageUrl": "string",
      "active": "boolean",
      "location": {
        "id": "number",
        "name": "string",
        "address": "string",
        "city": "string",
        "country": "string",
        "postalCode": "string",
        "active": "boolean",
        "createdAt": "2024-01-01 10:00:00",
        "updatedAt": "2024-01-01 10:00:00"
      },
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    }
  }
  ```

### Get Available Rooms
- **URL:** `GET /api/rooms/available`
- **Headers:**
  ```json
  {
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "startTime": "datetime (required)",
    "endTime": "datetime (required)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Available meeting rooms retrieved successfully",
    "data": ["array of MeetingRoomResponse objects"]
  }
  ```

### Get All Rooms (Admin)
- **URL:** `GET /api/rooms`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Query Params:**
  ```json
  {
    "page": "number (default: 0)",
    "size": "number (default: 10)",
    "sortBy": "string (default: 'id')",
    "sortDirection": "asc|desc (default: 'asc')"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Meeting rooms retrieved successfully",
    "data": ["array of MeetingRoomResponse objects"]
  }
  ```

### Get Room by ID (Admin)
- **URL:** `GET /api/rooms/admin/{id}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Meeting room retrieved successfully",
    "data": {
      "id": "number",
      "name": "string",
      "capacity": "number",
      "description": "string",
      "equipments": "string",
      "imageUrl": "string",
      "active": "boolean",
      "location": {
        "id": "number",
        "name": "string",
        "address": "string",
        "city": "string",
        "country": "string",
        "postalCode": "string",
        "active": "boolean",
        "createdAt": "2024-01-01 10:00:00",
        "updatedAt": "2024-01-01 10:00:00"
      },
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    }
  }
  ```

### Create Room
- **URL:** `POST /api/rooms`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "name": "string (required)",
    "locationId": "number (required)",
    "capacity": "number (optional)",
    "description": "string (optional)",
    "equipments": "string (optional)",
    "imageUrl": "string (optional)",
    "active": "boolean (default: true)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 201,
    "message": "Meeting room created successfully",
    "data": {
      "id": "number",
      "name": "string",
      "capacity": "number",
      "description": "string",
      "equipments": "string",
      "imageUrl": "string",
      "active": "boolean",
      "location": {
        "id": "number",
        "name": "string",
        "address": "string",
        "city": "string",
        "country": "string",
        "postalCode": "string",
        "active": "boolean",
        "createdAt": "2024-01-01 10:00:00",
        "updatedAt": "2024-01-01 10:00:00"
      },
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    }
  }
  ```

### Update Room
- **URL:** `PUT /api/rooms/{id}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "name": "string (min 1, max 100 chars, optional)",
    "locationId": "number (optional)",
    "capacity": "number (optional)",
    "description": "string (optional)",
    "equipments": "string (optional)",
    "imageUrl": "string (optional)",
    "active": "boolean (optional)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Meeting room updated successfully",
    "data": {
      "id": "number",
      "name": "string",
      "capacity": "number",
      "description": "string",
      "equipments": "string",
      "imageUrl": "string",
      "active": "boolean",
      "location": {
        "id": "number",
        "name": "string",
        "address": "string",
        "city": "string",
        "country": "string",
        "postalCode": "string",
        "active": "boolean",
        "createdAt": "2024-01-01 10:00:00",
        "updatedAt": "2024-01-01 10:00:00"
      },
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    }
  }
  ```

### Delete Room
- **URL:** `DELETE /api/rooms/{id}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 204,
    "message": "Meeting room deleted successfully"
  }
  ```

### Change Room Status
- **URL:** `PATCH /api/rooms/{id}/status`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Query Params:**
  ```json
  {
    "active": "boolean (true/false)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Meeting room status changed successfully",
    "data": {
      "id": "number",
      "name": "string",
      "capacity": "number",
      "description": "string",
      "equipments": "string",
      "imageUrl": "string",
      "active": "boolean",
      "location": {
        "id": "number",
        "name": "string",
        "address": "string",
        "city": "string",
        "country": "string",
        "postalCode": "string",
        "active": "boolean",
        "createdAt": "2024-01-01 10:00:00",
        "updatedAt": "2024-01-01 10:00:00"
      },
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    }
  }
  ```

---

# ================================================================================
# LOCATION MANAGEMENT APIs
# ================================================================================

## Location Management
### Get Location by ID
- **URL:** `GET /api/locations/{id}`
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Location found",
    "data": {
      "id": "number",
      "name": "string",
      "address": "string",
      "city": "string",
      "country": "string",
      "postalCode": "string",
      "active": "boolean",
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    }
  }
  ```

### Get Location List
- **URL:** `GET /api/locations`
- **Query Params:**
  ```json
  {
    "page": "number (default: 0)",
    "size": "number (default: 10)",
    "sortBy": "string (default: 'id')",
    "sortDirection": "asc|desc (default: 'asc')"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Locations retrieved successfully",
    "data": [
      {
        "id": "number",
        "name": "string",
        "address": "string",
        "city": "string",
        "country": "string",
        "postalCode": "string",
        "active": "boolean",
        "createdAt": "2024-01-01 10:00:00",
        "updatedAt": "2024-01-01 10:00:00"
      }
    ]
  }
  ```

### Create Location
- **URL:** `POST /api/locations`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "name": "string (required)",
    "address": "string (required)",
    "description": "string (optional)",
    "city": "string (required)",
    "country": "string (required)",
    "postalCode": "string (required)",
    "active": "boolean (default: true)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 201,
    "message": "Location created successfully",
    "data": {
      "id": "number",
      "name": "string",
      "address": "string",
      "city": "string",
      "country": "string",
      "postalCode": "string",
      "active": "boolean",
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    }
  }
  ```

### Update Location
- **URL:** `PUT /api/locations/{id}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "name": "string (required)",
    "address": "string (required)",
    "description": "string (optional)",
    "city": "string (required)",
    "country": "string (required)",
    "postalCode": "string (required)",
    "active": "boolean (default: true)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Location updated successfully",
    "data": {
      "id": "number",
      "name": "string",
      "address": "string",
      "city": "string",
      "country": "string",
      "postalCode": "string",
      "active": "boolean",
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    }
  }
  ```

### Delete Location
- **URL:** `DELETE /api/locations/{id}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 204,
    "message": "Location deleted successfully"
  }
  ```

### Change Location Status
- **URL:** `PATCH /api/locations/{id}/status`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Query Params:**
  ```json
  {
    "active": "boolean (true/false)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Location status changed successfully",
    "data": {
      "id": "number",
      "name": "string",
      "address": "string",
      "city": "string",
      "country": "string",
      "postalCode": "string",
      "active": "boolean",
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    }
  }
  ```

---

# ================================================================================
# CALENDAR MANAGEMENT APIs
# ================================================================================

## Calendar Management
### Get Calendar Overview
- **URL:** `POST /api/admin/calendar`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "startDate": "LocalDate (required)",
    "endDate": "LocalDate (required)",
    "locationId": "number (optional)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Calendar overview fetched successfully",
    "data": {
      "startDate": "2024-01-01",
      "endDate": "2024-01-31",
      "totalBookings": "number",
      "totalRooms": "number",
      "totalLocations": "number",
      "days": [
        {
          "date": "2024-01-01",
          "dayOfWeek": "MONDAY",
          "totalBookings": "number",
          "availableRooms": "number",
          "bookings": [
            {
              "id": "number",
              "title": "string",
              "startTime": "2024-01-01 10:00:00",
              "endTime": "2024-01-01 11:00:00",
              "status": "PENDING|APPROVED|REJECTED|CANCELLED",
              "roomName": "string",
              "locationName": "string",
              "createdBy": "string",
              "color": "string"
            }
          ],
          "status": "AVAILABLE|BUSY|FULL"
        }
      ]
    }
  }
  ```

### Get Room Calendar
- **URL:** `POST /api/admin/calendar/room/{roomId}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "startDate": "LocalDate (required)",
    "endDate": "LocalDate (required)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Room calendar fetched successfully",
    "data": {
      "startDate": "2024-01-01",
      "endDate": "2024-01-31",
      "totalBookings": "number",
      "totalRooms": "number",
      "totalLocations": "number",
      "days": ["array of CalendarDayResponse objects"]
    }
  }
  ```

### Get Calendar Overview (Legacy GET)
- **URL:** `GET /api/admin/calendar`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Query Params:**
  ```json
  {
    "startDate": "string (yyyy-MM-dd)",
    "endDate": "string (yyyy-MM-dd)",
    "locationId": "number (optional)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Calendar overview fetched successfully",
    "data": {
      "startDate": "2024-01-01",
      "endDate": "2024-01-31",
      "totalBookings": "number",
      "totalRooms": "number",
      "totalLocations": "number",
      "days": ["array of CalendarDayResponse objects"]
    }
  }
  ```

### Get Room Calendar (Legacy GET)
- **URL:** `GET /api/admin/calendar/room/{roomId}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Query Params:**
  ```json
  {
    "startDate": "string (yyyy-MM-dd)",
    "endDate": "string (yyyy-MM-dd)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Room calendar fetched successfully",
    "data": {
      "startDate": "2024-01-01",
      "endDate": "2024-01-31",
      "totalBookings": "number",
      "totalRooms": "number",
      "totalLocations": "number",
      "days": ["array of CalendarDayResponse objects"]
    }
  }
  ```

---

# ================================================================================
# NOTIFICATION MANAGEMENT APIs
# ================================================================================

## Notification Management
### Send Notification to Device
- **URL:** `POST /api/notifications/device`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "deviceToken": "string (required)",
    "title": "string (required)",
    "body": "string (required)",
    "data": "object (optional)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Notification sent successfully",
    "data": {
      "title": "string",
      "body": "string",
      "tokenDevice": "string",
      "imageUrl": "string",
      "sentAt": "2024-01-01 10:00:00",
      "data": {
        "additionalProp1": "string",
        "additionalProp2": "string"
      },
      "isSeen": "boolean",
      "message": "string"
    }
  }
  ```

### Send Immediate Reminder
- **URL:** `POST /api/notifications/reminder/{bookingId}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Reminder sent successfully",
    "data": "Reminder sent for booking ID: {bookingId}"
  }
  ```

### Check and Send Reminders
- **URL:** `POST /api/notifications/check-reminders`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Meeting reminders checked and sent successfully",
    "data": "Reminders processed"
  }
  ```

### Test Meeting Reminder
- **URL:** `POST /api/notifications/test-reminder/{bookingId}`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Test reminder sent successfully",
    "data": "Test reminder sent for booking ID: {bookingId}"
  }
  ```

---

# ================================================================================
# PROFILE MANAGEMENT APIs
# ================================================================================

## Profile Management
### Get My Profile
- **URL:** `GET /api/profile/my`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Successfully retrieved profile",
    "data": {
      "id": "number",
      "username": "string",
      "email": "string",
      "fullName": "string",
      "phoneNumber": "string",
      "role": "ADMIN|USER",
      "department": "string"
    }
  }
  ```

### Update My Profile
- **URL:** `PUT /api/profile`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "fullName": "string (optional)",
    "phone": "string (optional)",
    "department": "string (optional)",
    "avatarUrl": "string (optional)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Successfully updated profile",
    "data": {
      "id": "number",
      "username": "string",
      "email": "string",
      "fullName": "string",
      "phoneNumber": "string",
      "role": "ADMIN|USER",
      "department": "string"
    }
  }
  ```

### Change My Password
- **URL:** `PATCH /api/profile/change-password`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "currentPassword": "string (required)",
    "newPassword": "string (required, min 6 chars)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Successfully changed password",
    "data": {
      "id": "number",
      "username": "string",
      "email": "string",
      "fullName": "string",
      "phoneNumber": "string",
      "role": "ADMIN|USER",
      "department": "string"
    }
  }
  ```

---

# ================================================================================
# USER DEVICE MANAGEMENT APIs
# ================================================================================

## User Device Management
### Register Device
- **URL:** `POST /api/user-devices/register`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}",
    "Content-Type": "application/json"
  }
  ```
- **Input:**
  ```json
  {
    "deviceToken": "string (required)",
    "deviceType": "ANDROID|IOS|WEB (optional)",
    "deviceName": "string (optional)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Device registered successfully",
    "data": {
      "id": "number",
      "user": "User object",
      "deviceToken": "string",
      "deviceType": "ANDROID|IOS|WEB",
      "deviceInfo": "string",
      "active": "boolean",
      "createdAt": "2024-01-01 10:00:00",
      "updatedAt": "2024-01-01 10:00:00"
    }
  }
  ```

### Unregister Device
- **URL:** `POST /api/user-devices/unregister`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Query Params:**
  ```json
  {
    "deviceToken": "string (required)"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Device unregistered successfully"
  }
  ```

### Get My Devices
- **URL:** `GET /api/user-devices/my-devices`
- **Headers:**
  ```json
  {
    "Authorization": "Bearer {accessToken}"
  }
  ```
- **Response:**
  ```json
  {
    "status": 200,
    "message": "Devices retrieved successfully",
    "data": [
      {
        "id": "number",
        "user": "User object",
        "deviceToken": "string",
        "deviceType": "ANDROID|IOS|WEB",
        "deviceInfo": "string",
        "active": "boolean",
        "createdAt": "2024-01-01 10:00:00",
        "updatedAt": "2024-01-01 10:00:00"
      }
    ]
  }
  ```

---

# ================================================================================
# ENUMERATIONS & NOTES
# ================================================================================

## Enumerations
```json
{
  "Role": ["ADMIN", "USER"],
  "BookingStatus": ["PENDING", "APPROVED", "REJECTED", "CANCELLED"],
  "DeviceType": ["ANDROID", "IOS", "WEB"],
  "DayOfWeek": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"],
  "CalendarDayStatus": ["AVAILABLE", "BUSY", "FULL"]
}
```

## Notes
- **datetime_format**: `yyyy-MM-dd HH:mm:ss`
- **pagination**: `All list APIs support page, size, sortBy, sortDirection parameters`
- **authentication**: `Admin APIs require Bearer token in Authorization header`
- **validation**: `Required fields are marked with (required), optional fields with (optional)`
- **error_handling**: `All APIs return consistent error format with status, message fields`
- **response_format**: `All successful responses include status, message, and optional data field`
- **notification**: `Push notifications are sent automatically for meeting reminders (30min, 15min, 5min before)`
- **calendar**: `Calendar APIs support both POST (recommended) and GET (legacy) endpoints`
- **device_tokens**: `Device tokens are used for push notifications to mobile devices`

---

**📊 TOTAL ENDPOINTS: 70+ APIs**
**🚀 PROJECT STATUS: PRODUCTION READY**
