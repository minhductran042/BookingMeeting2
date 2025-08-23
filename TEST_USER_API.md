# Test User API

## Test Case: Create User

### Endpoint
```
POST /api/admin/users
```

### Headers
```
Content-Type: application/json
Authorization: Bearer <your-jwt-token>
```

### Request Body
```json
{
  "username": "testuser",
  "fullName": "Test User",
  "email": "test@example.com",
  "password": "password123",
  "phone": "0123456789",
  "department": "IT",
  "role": "USER",
  "avatarUrl": "https://example.com/avatar.jpg",
  "active": true
}
```

### Expected Response
```json
{
  "status": 201,
  "message": "User created successfully",
  "data": {
    "id": 1,
    "username": "testuser",
    "fullName": "Test User",
    "email": "test@example.com",
    "phone": "0123456789",
    "department": "IT",
    "role": "USER",
    "avatarUrl": "https://example.com/avatar.jpg",
    "active": true,
    "createdAt": "2025-08-22T...",
    "updatedAt": "2025-08-22T..."
  }
}
```

## Test với Postman

1. Tạo request mới
2. Chọn method POST
3. URL: `http://localhost:8080/api/admin/users`
4. Headers: `Content-Type: application/json`
5. Body: Raw JSON với nội dung từ file `test_user_creation.json`
6. Gửi request

## Troubleshooting

### Lỗi "rawPassword cannot be null"
- Kiểm tra xem password field có được gửi trong request body không
- Đảm bảo Content-Type là `application/json`
- Kiểm tra log để xem password validation

### Lỗi Validation
- Đảm bảo tất cả required fields đều được gửi
- Kiểm tra format của email
- Kiểm tra độ dài password (tối thiểu 6 ký tự)

