# 🔧 RUBEROO MICROSERVICES - BACKEND TESTING GUIDE

**Purpose:** Test all microservices connectivity and functionality  
**Tools:** Thunder Client (VS Code), Postman, or curl  
**Date:** November 5, 2025

---

## 📊 MICROSERVICES ARCHITECTURE

```
┌─────────────────────────────────────────────────────────────────┐
│                                                                 │
│                    RUBEROO BACKEND SERVICES                     │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘

    🌐 API Gateway (Port 8085)
         ↓
    🔍 Eureka Server (Port 8761) ← All services register here
         ↓
    ⚙️  Config Server (Port 8888) ← Centralized configuration
         ↓
    ┌────────────────┬─────────────────┬──────────────────┐
    ↓                ↓                 ↓                  ↓
👤 User Service  🚗 Ride Service  📍 Tracking Service
   (Port 8081)      (Port 8083)       (Port 8084)
    ↓                ↓                 ↓
    └────────────────┴─────────────────┴──────────────────┘
                           ↓
                    💾 PostgreSQL Database
                    🔴 Redis Cache
```

---

## 🎯 TESTING STRATEGY

### Phase 1: Infrastructure Services
1. ✅ Eureka Server - Service Discovery
2. ✅ Config Server - Configuration Management
3. ✅ API Gateway - Entry Point

### Phase 2: Business Services
4. ✅ User Service - Authentication
5. ✅ Ride Service - Ride Management
6. ✅ Tracking Service - Emergency & Tracking

### Phase 3: Integration Tests
7. ✅ Service-to-Service Communication
8. ✅ End-to-End Workflows

---

## 🔍 PHASE 1: INFRASTRUCTURE SERVICES

### 1.1 Test Eureka Server

**Check Health:**
```http
GET https://ruberoo-eureka-server.onrender.com/actuator/health
```

**Expected Response:**
```json
{
  "status": "UP"
}
```

**View Eureka Dashboard:**
```
https://ruberoo-eureka-server.onrender.com
```

**Expected:** Dashboard showing all registered services

**Check Registered Services:**
```http
GET https://ruberoo-eureka-server.onrender.com/eureka/apps
Accept: application/json
```

**Expected Services:**
- CONFIG-SERVER
- API-GATEWAY
- USER-SERVICE
- RIDE-MANAGEMENT-SERVICE
- TRACKING-SERVICE

---

### 1.2 Test Config Server

**Check Health:**
```http
GET https://ruberoo-config-server.onrender.com/actuator/health
```

**Expected Response:**
```json
{
  "status": "UP"
}
```

**Get Configuration for User Service:**
```http
GET https://ruberoo-config-server.onrender.com/user-service/prod
```

**Expected:** Configuration properties for user-service

**Test Refresh Endpoint:**
```http
POST https://ruberoo-config-server.onrender.com/actuator/refresh
Content-Type: application/json
```

---

### 1.3 Test API Gateway

**Check Health:**
```http
GET https://ruberoo-api-gateway.onrender.com/actuator/health
```

**Expected Response:**
```json
{
  "status": "UP"
}
```

**Check Gateway Routes:**
```http
GET https://ruberoo-api-gateway.onrender.com/actuator/gateway/routes
```

**Expected Routes:**
- /api/users/** → USER-SERVICE
- /api/rides/** → RIDE-MANAGEMENT-SERVICE
- /api/emergency-contacts/** → TRACKING-SERVICE

---

## 👤 PHASE 2: BUSINESS SERVICES

### 2.1 Test User Service (Authentication)

#### Test 1: Register New User

```http
POST https://ruberoo-api-gateway.onrender.com/api/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@test.com",
  "password": "password123",
  "role": "USER"
}
```

**Expected Response (201 Created):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@test.com",
  "role": "USER",
  "createdAt": "2025-11-05T10:30:00"
}
```

#### Test 2: Login (Get JWT Token)

```http
POST https://ruberoo-api-gateway.onrender.com/api/users/auth/login
Content-Type: application/json

{
  "email": "john.doe@test.com",
  "password": "password123"
}
```

**Expected Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@test.com",
    "role": "USER"
  }
}
```

**🔑 IMPORTANT:** Save the `token` value for subsequent requests!

#### Test 3: Get All Users (Protected)

```http
GET https://ruberoo-api-gateway.onrender.com/api/users
Authorization: Bearer {YOUR_JWT_TOKEN}
```

**Expected Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@test.com",
    "role": "USER"
  }
]
```

#### Test 4: Get User by ID

```http
GET https://ruberoo-api-gateway.onrender.com/api/users/1
Authorization: Bearer {YOUR_JWT_TOKEN}
```

#### Test 5: Update User

```http
PUT https://ruberoo-api-gateway.onrender.com/api/users/1
Authorization: Bearer {YOUR_JWT_TOKEN}
Content-Type: application/json

{
  "name": "John Updated",
  "email": "john.doe@test.com",
  "role": "USER"
}
```

#### Test 6: Delete User

```http
DELETE https://ruberoo-api-gateway.onrender.com/api/users/1
Authorization: Bearer {YOUR_JWT_TOKEN}
```

---

### 2.2 Test Ride Management Service

#### Test 1: Create Ride

```http
POST https://ruberoo-api-gateway.onrender.com/api/rides
Authorization: Bearer {YOUR_JWT_TOKEN}
Content-Type: application/json

{
  "origin": "123 Main Street, Portland, OR",
  "destination": "456 Oak Avenue, Portland, OR",
  "scheduledTime": "2025-11-06T10:00:00",
  "userId": 1
}
```

**Expected Response (201 Created):**
```json
{
  "id": 1,
  "origin": "123 Main Street, Portland, OR",
  "destination": "456 Oak Avenue, Portland, OR",
  "scheduledTime": "2025-11-06T10:00:00",
  "userId": 1,
  "status": "PENDING",
  "createdAt": "2025-11-05T10:35:00"
}
```

#### Test 2: Get All Rides

```http
GET https://ruberoo-api-gateway.onrender.com/api/rides
Authorization: Bearer {YOUR_JWT_TOKEN}
```

#### Test 3: Get Ride by ID

```http
GET https://ruberoo-api-gateway.onrender.com/api/rides/1
Authorization: Bearer {YOUR_JWT_TOKEN}
```

#### Test 4: Update Ride Status

```http
PUT https://ruberoo-api-gateway.onrender.com/api/rides/1
Authorization: Bearer {YOUR_JWT_TOKEN}
Content-Type: application/json

{
  "origin": "123 Main Street, Portland, OR",
  "destination": "456 Oak Avenue, Portland, OR",
  "scheduledTime": "2025-11-06T10:00:00",
  "userId": 1,
  "status": "CONFIRMED"
}
```

#### Test 5: Delete Ride

```http
DELETE https://ruberoo-api-gateway.onrender.com/api/rides/1
Authorization: Bearer {YOUR_JWT_TOKEN}
```

---

### 2.3 Test Tracking Service (Emergency Contacts)

#### Test 1: Create Emergency Contact

```http
POST https://ruberoo-api-gateway.onrender.com/api/emergency-contacts
Authorization: Bearer {YOUR_JWT_TOKEN}
Content-Type: application/json

{
  "userId": 1,
  "contactName": "Jane Doe",
  "contactNumber": "+1-555-0123"
}
```

**Expected Response (201 Created):**
```json
{
  "id": 1,
  "userId": 1,
  "contactName": "Jane Doe",
  "contactNumber": "+1-555-0123",
  "createdAt": "2025-11-05T10:40:00"
}
```

#### Test 2: Get All Emergency Contacts

```http
GET https://ruberoo-api-gateway.onrender.com/api/emergency-contacts
Authorization: Bearer {YOUR_JWT_TOKEN}
```

#### Test 3: Get Emergency Contact by ID

```http
GET https://ruberoo-api-gateway.onrender.com/api/emergency-contacts/1
Authorization: Bearer {YOUR_JWT_TOKEN}
```

#### Test 4: Update Emergency Contact

```http
PUT https://ruberoo-api-gateway.onrender.com/api/emergency-contacts/1
Authorization: Bearer {YOUR_JWT_TOKEN}
Content-Type: application/json

{
  "userId": 1,
  "contactName": "Jane Updated",
  "contactNumber": "+1-555-9999"
}
```

#### Test 5: Delete Emergency Contact

```http
DELETE https://ruberoo-api-gateway.onrender.com/api/emergency-contacts/1
Authorization: Bearer {YOUR_JWT_TOKEN}
```

---

## 🔗 PHASE 3: INTEGRATION TESTS

### 3.1 Complete User Journey

**Step 1: Register User**
```http
POST https://ruberoo-api-gateway.onrender.com/api/users
Content-Type: application/json

{
  "name": "Integration Test User",
  "email": "integration@test.com",
  "password": "test123",
  "role": "USER"
}
```

**Step 2: Login**
```http
POST https://ruberoo-api-gateway.onrender.com/api/users/auth/login
Content-Type: application/json

{
  "email": "integration@test.com",
  "password": "test123"
}
```

**Save Token:** `eyJhbGci...`

**Step 3: Create Ride**
```http
POST https://ruberoo-api-gateway.onrender.com/api/rides
Authorization: Bearer {TOKEN}
Content-Type: application/json

{
  "origin": "Portland Airport",
  "destination": "Downtown Portland",
  "scheduledTime": "2025-11-06T14:00:00",
  "userId": {USER_ID}
}
```

**Step 4: Add Emergency Contact**
```http
POST https://ruberoo-api-gateway.onrender.com/api/emergency-contacts
Authorization: Bearer {TOKEN}
Content-Type: application/json

{
  "userId": {USER_ID},
  "contactName": "Emergency Contact",
  "contactNumber": "+1-555-1111"
}
```

**Step 5: Verify All Data**
```http
GET https://ruberoo-api-gateway.onrender.com/api/users/{USER_ID}
GET https://ruberoo-api-gateway.onrender.com/api/rides
GET https://ruberoo-api-gateway.onrender.com/api/emergency-contacts
Authorization: Bearer {TOKEN}
```

---

## 🧪 TESTING CHECKLIST

### Infrastructure Services
- [ ] Eureka Server health check passes
- [ ] Eureka Dashboard accessible
- [ ] All 5 services registered in Eureka
- [ ] Config Server health check passes
- [ ] Config Server returns configuration
- [ ] API Gateway health check passes
- [ ] API Gateway routes configured

### User Service
- [ ] User registration works
- [ ] Login returns JWT token
- [ ] JWT token is valid
- [ ] Get users with authentication works
- [ ] Get user by ID works
- [ ] Update user works
- [ ] Delete user works

### Ride Management Service
- [ ] Create ride works
- [ ] Get all rides works
- [ ] Get ride by ID works
- [ ] Update ride status works
- [ ] Delete ride works

### Tracking Service
- [ ] Create emergency contact works
- [ ] Get all emergency contacts works
- [ ] Get emergency contact by ID works
- [ ] Update emergency contact works
- [ ] Delete emergency contact works

### Integration Tests
- [ ] Complete user journey works end-to-end
- [ ] JWT authentication flows through all services
- [ ] Database persists data correctly
- [ ] Redis caching works (check logs)
- [ ] Service-to-service communication works

---

## 🛠️ THUNDER CLIENT SETUP

### Installation
1. Open VS Code
2. Go to Extensions (Cmd+Shift+X)
3. Search "Thunder Client"
4. Install Thunder Client by Ranga Vadhineni

### Create Collection

1. Open Thunder Client
2. Click "Collections"
3. Create new collection: "Ruberoo Microservices"
4. Add folders:
   - Infrastructure
   - User Service
   - Ride Service
   - Tracking Service
   - Integration Tests

### Environment Variables

Create environment: "Ruberoo Production"

```json
{
  "apiGateway": "https://ruberoo-api-gateway.onrender.com",
  "eurekaServer": "https://ruberoo-eureka-server.onrender.com",
  "configServer": "https://ruberoo-config-server.onrender.com",
  "jwtToken": "",
  "userId": ""
}
```

**Usage:**
- Base URL: `{{apiGateway}}`
- Authorization: `Bearer {{jwtToken}}`

---

## 📝 SAMPLE TEST RESULTS

### Expected Success Responses:

**Health Check:**
```
Status: 200 OK
Body: {"status":"UP"}
Time: ~500ms
```

**User Registration:**
```
Status: 201 Created
Body: {"id":1,"name":"...","email":"..."}
Time: ~800ms
```

**Login:**
```
Status: 200 OK
Body: {"token":"eyJ...","user":{...}}
Time: ~600ms
```

**Create Ride:**
```
Status: 201 Created
Body: {"id":1,"origin":"...","destination":"..."}
Time: ~700ms
```

### Expected Error Responses:

**401 Unauthorized (No Token):**
```json
{
  "timestamp": "2025-11-05T10:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Authentication required"
}
```

**409 Conflict (Duplicate Email):**
```json
{
  "timestamp": "2025-11-05T10:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Email already exists"
}
```

---

## 🚨 TROUBLESHOOTING

### Service Not Responding (503)
**Check:**
1. Service is running on Render
2. Service registered in Eureka
3. Wait 60 seconds for registration

### 401 Unauthorized
**Check:**
1. JWT token is included in header
2. Token format: `Bearer eyJhbGci...`
3. Token is not expired (24 hours)
4. User exists in database

### 502 Bad Gateway
**Check:**
1. API Gateway is running
2. Backend service is accessible
3. Eureka registration successful
4. Check Render logs for errors

### Database Connection Error
**Check:**
1. Supabase database is running
2. Connection string is correct
3. Database credentials are valid
4. Tables exist (users, rides, emergency_contacts)

---

## 🎯 SUCCESS CRITERIA

✅ **All infrastructure services UP**  
✅ **All business services registered in Eureka**  
✅ **User can register successfully**  
✅ **User can login and receive JWT token**  
✅ **Authenticated requests work**  
✅ **CRUD operations work on all services**  
✅ **Data persists in database**  
✅ **End-to-end workflow completes successfully**  

---

## 📊 PERFORMANCE BENCHMARKS

Target response times:

- Health checks: < 500ms
- User registration: < 1000ms
- Login: < 800ms
- CRUD operations: < 700ms
- List operations: < 1000ms

---

## 🔒 SECURITY TESTING

### Test 1: Access without Authentication
```http
GET https://ruberoo-api-gateway.onrender.com/api/users
```
**Expected:** 401 Unauthorized

### Test 2: Access with Invalid Token
```http
GET https://ruberoo-api-gateway.onrender.com/api/users
Authorization: Bearer invalid_token_here
```
**Expected:** 401 Unauthorized

### Test 3: Access with Expired Token
**Expected:** 401 Unauthorized with "Token expired" message

---

## 📞 NEXT STEPS AFTER TESTING

Once all tests pass:

1. ✅ Document test results
2. ✅ Take screenshots of successful requests
3. ✅ Export Thunder Client collection
4. ✅ Create automated test suite (optional)
5. ✅ Set up monitoring (optional)

---

**Last Updated:** November 5, 2025  
**Testing Tool:** Thunder Client / Postman / curl  
**Base URL:** https://ruberoo-api-gateway.onrender.com
