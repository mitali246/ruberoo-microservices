# 🧪 Ruberoo Microservices - Backend API Testing Guide

**Purpose:** Complete guide for testing all microservices via Postman  
**Status:** Backend-only (Frontend removed)  
**Date:** November 4, 2025  
**Collection:** `Ruberoo-Microservices.postman_collection.json`  

---

## 📋 Table of Contents

1. [Quick Start](#quick-start)
2. [Service Architecture](#service-architecture)
3. [Service Endpoints](#service-endpoints)
4. [Authentication Flow](#authentication-flow)
5. [Testing with Postman](#testing-with-postman)
6. [Service Interconnections](#service-interconnections)
7. [Common Issues & Solutions](#common-issues--solutions)

---

## 🚀 Quick Start

### Prerequisites

1. **Start all services:**
   ```bash
   docker compose up -d
   ```

2. **Verify services are running:**
   ```bash
   docker ps
   ```

3. **Install Postman:**
   - Download from: https://www.postman.com/downloads/
   - Install and create free account

4. **Import Collection:**
   - Open Postman
   - Click "Import" → "File" tab
   - Select `Ruberoo-Microservices.postman_collection.json`
   - Collection imported: "Ruberoo Microservices API"

5. **Set Up Environment:**
   - Click "Environments" → "+" (create new)
   - Name: `Local Development`
   - Add variables (see POSTMAN_SETUP_GUIDE.md)
   - Select environment from dropdown (top right)

---

## 🏗️ Service Architecture

### **Service Ports & Access Points**

| Service | Internal Port | External Port | Direct URL | Gateway URL |
|---------|--------------|--------------|------------|-------------|
| **Eureka Server** | 8761 | 8761 | http://localhost:8761 | N/A |
| **Config Server** | 8888 | 8889 | http://localhost:8889 | N/A |
| **API Gateway** | 8085 | 9095 | http://localhost:9095 | http://localhost:9095 |
| **User Service** | 8081 | 9081 | http://localhost:9081 | http://localhost:9095/api/users/** |
| **Ride Management** | 8083 | 9082 | http://localhost:9082 | http://localhost:9095/api/rides/** |
| **Tracking Service** | 8084 | 8084 | http://localhost:8084 | http://localhost:9095/api/tracking/** |
| **MySQL** | 3306 | 3307 | localhost:3307 | N/A |
| **Redis** | 6379 | 6379 | localhost:6379 | N/A |

### **Service Dependencies**

```
┌─────────────────┐
│  API Gateway    │ (Port 9095) - Entry Point
│  (JWT Auth)     │
└────────┬────────┘
         │
         ├───► User Service (Port 9081)
         ├───► Ride Management (Port 9082)
         └───► Tracking Service (Port 8084)
                 │
                 └───► Emergency Contacts
         
┌─────────────────┐
│  Eureka Server  │ (Port 8761) - Service Discovery
└─────────────────┘
         ▲
         │
    All Services Register Here

┌─────────────────┐
│ Config Server   │ (Port 8889) - Configuration Management
└─────────────────┘
         ▲
         │
    All Services Load Config From Here
```

---

## 📡 Service Endpoints

### **1. Infrastructure Services**

#### **Eureka Server**
- **Dashboard:** `GET http://localhost:8761`
- **Health:** `GET http://localhost:8761/actuator/health`
- **Registered Services:** `GET http://localhost:8761/eureka/apps`

#### **Config Server**
- **Health:** `GET http://localhost:8889/actuator/health`
- **Config:** `GET http://localhost:8889/{application}/{profile}`

#### **API Gateway**
- **Health:** `GET http://localhost:9095/actuator/health`
  - **Auth:** Basic Auth (admin/admin123)
- **Info:** `GET http://localhost:9095/actuator/info`

---

### **2. User Service**

**Base Path:** `/api/users`

#### **Public Endpoints (No Auth Required)**

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `POST` | `/api/users/auth/login` | User login | `{ "email": "string", "password": "string" }` |
| `POST` | `/api/users` | Register new user | See below |

**Login Request:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Register Request:**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "phone": "+1234567890",
  "role": "USER"
}
```

#### **Protected Endpoints (JWT Token Required)**

| Method | Endpoint | Description | Headers |
|--------|----------|-------------|---------|
| `GET` | `/api/users` | Get all users | `Authorization: Bearer {token}` |
| `GET` | `/api/users/{id}` | Get user by ID | `Authorization: Bearer {token}` |
| `PUT` | `/api/users/{id}` | Update user | `Authorization: Bearer {token}` |
| `DELETE` | `/api/users/{id}` | Delete user | `Authorization: Bearer {token}` |

**Update User Request:**
```json
{
  "name": "John Updated",
  "email": "john.doe@example.com",
  "phone": "+1234567890"
}
```

---

### **3. Ride Management Service**

**Base Path:** `/api/rides`

#### **All Endpoints Require JWT Authentication**

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `POST` | `/api/rides` | Create ride | See below |
| `GET` | `/api/rides` | Get all rides | None |
| `GET` | `/api/rides/{id}` | Get ride by ID | None |
| `PUT` | `/api/rides/{id}` | Update ride | See below |
| `DELETE` | `/api/rides/{id}` | Delete ride | None |

**Create Ride Request:**
```json
{
  "userId": 1,
  "driverId": 2,
  "pickupLocation": "123 Main St, City",
  "dropoffLocation": "456 Oak Ave, City",
  "status": "PENDING",
  "fare": 25.50
}
```

**Update Ride Request:**
```json
{
  "status": "IN_PROGRESS",
  "fare": 30.00
}
```

**Ride Status Values:**
- `PENDING` - Ride requested, waiting for driver
- `ACCEPTED` - Driver accepted the ride
- `IN_PROGRESS` - Ride in progress
- `COMPLETED` - Ride completed
- `CANCELLED` - Ride cancelled

---

### **4. Tracking Service**

**Base Path:** `/api/tracking` and `/api/emergency-contacts`

#### **Tracking Endpoints**

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| `GET` | `/api/tracking/health` | Health check | No |
| `GET` | `/api/tracking/rides/{rideId}/location` | Get last known location | Yes |

#### **Emergency Contact Endpoints (All Require JWT)**

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `POST` | `/api/emergency-contacts` | Create contact | See below |
| `GET` | `/api/emergency-contacts` | Get all contacts | None |
| `GET` | `/api/emergency-contacts/{id}` | Get contact by ID | None |
| `PUT` | `/api/emergency-contacts/{id}` | Update contact | See below |
| `DELETE` | `/api/emergency-contacts/{id}` | Delete contact | None |

**Create Emergency Contact Request:**
```json
{
  "userId": 1,
  "name": "Emergency Contact",
  "phone": "+1234567890",
  "relationship": "Family"
}
```

**Update Emergency Contact Request:**
```json
{
  "name": "Updated Contact",
  "phone": "+9876543210"
}
```

---

## 🔐 Authentication Flow

### **Step 1: Register a User**

```http
POST http://localhost:9095/api/users
Content-Type: application/json

{
  "name": "Test User",
  "email": "test@example.com",
  "password": "password123",
  "phone": "+1234567890",
  "role": "USER"
}
```

**Response:** User object with ID

### **Step 2: Login to Get JWT Token**

```http
POST http://localhost:9095/api/users/auth/login
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### **Step 3: Use Token in Protected Requests**

```http
GET http://localhost:9095/api/users
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 🧪 Testing with Postman

### **Setup Postman Environment**

1. **Open Postman**
2. **Click "Environments"** in left sidebar
3. **Click "+"** to create new environment
4. **Name it:** `Local Development`
5. **Add Variables:**
   - `baseUrl`: `http://localhost:9095`
   - `token`: (leave empty, will be set after login - auto-saved)
   - `eurekaUrl`: `http://localhost:8761`
   - `configServerUrl`: `http://localhost:8889`
   - `userServiceUrl`: `http://localhost:9081`
   - `rideServiceUrl`: `http://localhost:9082`
   - `trackingServiceUrl`: `http://localhost:8084`
6. **Click "Save"**
7. **Select "Local Development"** from dropdown (top right)

### **Testing Workflow**

#### **1. Test Infrastructure Services**

1. **Eureka Dashboard:**
   - Collection → "0. Infrastructure" → "Eureka Dashboard"
   - Click "Send"
   - Should return: HTML page showing registered services

2. **Config Server Health:**
   - Collection → "0. Infrastructure" → "Config Server Health"
   - Click "Send"
   - Should return: `{"status":"UP"}`

3. **API Gateway Health:**
   - Collection → "0. Infrastructure" → "API Gateway Health"
   - **Note:** Uses Basic Auth (admin/admin123) - pre-configured
   - Click "Send"
   - Should return: `{"status":"UP"}`

#### **2. Test Authentication**

1. **Register User:**
   - Collection → "1. Authentication" → "User Registration (via Gateway)"
   - Update body with your test data
   - Click "Send"
   - Should return: User object with ID

2. **Login (Auto-saves Token):**
   - Collection → "1. Authentication" → "User Login"
   - Update body with registered email/password
   - Click "Send"
   - Should return: `{"token":"..."}`
   - **Token is automatically saved!** (Check Environment → `token` variable)

#### **3. Test Protected Endpoints**

All requests with `{{token}}` in headers will now use your token automatically.

1. **Get All Users:**
   - Request: `Get All Users (Auth Required)`
   - Should return: Array of users

2. **Create Ride:**
   - Request: `Create Ride (Auth Required)`
   - Update `userId` and `driverId` in body
   - Should return: Created ride object

3. **Get All Rides:**
   - Request: `Get All Rides (Auth Required)`
   - Should return: Array of rides

#### **4. Test Direct Service Access**

To test services directly (bypassing Gateway):

1. **User Service Direct:**
   - Request: `User Service Direct - Get All Users`
   - **Note:** Direct access may bypass JWT validation

2. **Ride Management Direct:**
   - Request: `Ride Management Direct - Get All Rides`
   - **Note:** Direct access may bypass JWT validation

---

## 🔗 Service Interconnections

### **How Services Communicate**

#### **1. Service Discovery (Eureka)**

All services register with Eureka and discover each other:

```
User Service → Registers → Eureka Server
Ride Service → Registers → Eureka Server
Tracking Service → Registers → Eureka Server
API Gateway → Discovers → All Services via Eureka
```

**Verify Registration:**
```http
GET http://localhost:8761/eureka/apps
Accept: application/json
```

#### **2. Configuration Management (Config Server)**

All services load configuration from Config Server:

```
User Service → Loads Config → Config Server
Ride Service → Loads Config → Config Server
Tracking Service → Loads Config → Config Server
```

**Verify Config:**
```http
GET http://localhost:8889/user-service/docker
```

#### **3. API Gateway Routing**

API Gateway routes requests to appropriate services:

```
Client Request → API Gateway (Port 9095)
                ├─ /api/users/** → User Service (Port 8081)
                ├─ /api/rides/** → Ride Management (Port 8083)
                └─ /api/tracking/** → Tracking Service (Port 8084)
```

**Gateway Features:**
- JWT Token Validation
- Rate Limiting (Redis-based)
- Load Balancing (via Eureka)
- Request Routing

#### **4. Database Connections**

All services connect to MySQL:

```
User Service → MySQL (ruberoo_user_db)
Ride Service → MySQL (ruberoo_ride_db)
Tracking Service → MySQL (ruberoo_tracking_db)
```

**Database Connection:**
- Host: `mysql-db` (Docker network) or `localhost:3307` (external)
- User: `root`
- Password: `rootmitali`

#### **5. Redis (Rate Limiting)**

API Gateway uses Redis for rate limiting:

```
API Gateway → Redis (Port 6379)
Rate Limit: 5 requests/second per IP
Burst Capacity: 10 requests
```

---

## 🐛 Common Issues & Solutions

### **Issue 1: 401 Unauthorized**

**Symptoms:**
- All protected endpoints return 401
- "Invalid token" or "Unauthorized" error

**Solutions:**
1. **Check if token is set:**
   - Verify token in Thunder Client environment
   - Token should start with `eyJ...`

2. **Re-login to get fresh token:**
   - Tokens expire after 24 hours (default)
   - Use `User Login` request to get new token

3. **Check token format:**
   - Header should be: `Authorization: Bearer {token}`
   - No extra spaces or quotes

### **Issue 2: 503 Service Unavailable**

**Symptoms:**
- Gateway returns 503
- "Service unavailable" error

**Solutions:**
1. **Check if service is registered in Eureka:**
   ```http
   GET http://localhost:8761/eureka/apps
   ```
   - Verify service appears in response

2. **Check service health:**
   ```http
   GET http://localhost:9081/actuator/health
   GET http://localhost:9082/actuator/health
   GET http://localhost:8084/actuator/health
   ```

3. **Restart service:**
   ```bash
   docker compose restart user-service
   docker compose restart ride-management-service
   docker compose restart tracking-service
   ```

### **Issue 3: Connection Refused**

**Symptoms:**
- Cannot connect to service
- "Connection refused" error

**Solutions:**
1. **Check if service is running:**
   ```bash
   docker ps
   ```
   - Verify service container is UP

2. **Check port mapping:**
   - Verify external port matches expected port
   - Check `docker-compose.yml` for port mappings

3. **Check service logs:**
   ```bash
   docker logs ruberoo-user-service
   docker logs ruberoo-ride-management-service
   docker logs ruberoo-tracking-service
   ```

### **Issue 4: Rate Limit Exceeded**

**Symptoms:**
- 429 Too Many Requests
- "Rate limit exceeded" error

**Solutions:**
1. **Wait before retrying:**
   - Rate limit: 5 requests/second
   - Burst capacity: 10 requests
   - Wait 1-2 seconds between requests

2. **Check Redis connection:**
   ```bash
   docker logs ruberoo-redis
   docker logs ruberoo-gateway
   ```

### **Issue 5: Database Connection Error**

**Symptoms:**
- 500 Internal Server Error
- "Database connection failed" error

**Solutions:**
1. **Check MySQL is running:**
   ```bash
   docker ps | grep mysql
   ```

2. **Check MySQL health:**
   ```bash
   docker exec ruberoo-mysql mysqladmin ping -h localhost -u root -prootmitali
   ```

3. **Verify database exists:**
   ```bash
   docker exec -it ruberoo-mysql mysql -u root -prootmitali -e "SHOW DATABASES;"
   ```

---

## 📊 Testing Checklist

### **Infrastructure Tests**

- [ ] Eureka Dashboard accessible
- [ ] Config Server health check passes
- [ ] API Gateway health check passes
- [ ] All services registered in Eureka
- [ ] MySQL connection successful
- [ ] Redis connection successful

### **Authentication Tests**

- [ ] User registration successful
- [ ] User login returns JWT token
- [ ] Token can be used in protected endpoints
- [ ] Invalid token returns 401
- [ ] Expired token returns 401

### **User Service Tests**

- [ ] Get all users (requires auth)
- [ ] Get user by ID (requires auth)
- [ ] Create user (public)
- [ ] Update user (requires auth)
- [ ] Delete user (requires auth)

### **Ride Management Tests**

- [ ] Create ride (requires auth)
- [ ] Get all rides (requires auth)
- [ ] Get ride by ID (requires auth)
- [ ] Update ride (requires auth)
- [ ] Delete ride (requires auth)

### **Tracking Service Tests**

- [ ] Tracking health check (public)
- [ ] Get last known location (requires auth)
- [ ] Create emergency contact (requires auth)
- [ ] Get all emergency contacts (requires auth)
- [ ] Update emergency contact (requires auth)
- [ ] Delete emergency contact (requires auth)

### **Integration Tests**

- [ ] Create user → Create ride → Track ride flow
- [ ] Gateway routes requests correctly
- [ ] Rate limiting works
- [ ] Service discovery works
- [ ] Config refresh works

---

## 🎯 Quick Reference

### **Common cURL Commands**

**Login:**
```bash
curl -X POST http://localhost:9095/api/users/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

**Get Users (with token):**
```bash
curl -X GET http://localhost:9095/api/users \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Create Ride (with token):**
```bash
curl -X POST http://localhost:9095/api/rides \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"driverId":2,"pickupLocation":"123 Main St","dropoffLocation":"456 Oak Ave","status":"PENDING","fare":25.50}'
```

---

## 📝 Notes

1. **Always use Gateway for production:** Direct service access bypasses security
2. **Token expires:** JWT tokens expire after 24 hours (configurable)
3. **Rate limiting:** API Gateway limits 5 req/sec per IP
4. **Service discovery:** Services auto-discover via Eureka
5. **Configuration:** All config loaded from Config Server

---

**Last Updated:** November 4, 2025  
**Version:** 1.0.0  
**Status:** ✅ Backend-Only Mode Active

