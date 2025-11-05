# 🚗 Ruberoo Microservices - Backend Only

**A comprehensive microservices-based ride-sharing platform built with Spring Boot, Spring Cloud, and Docker.**

---

## 📋 Project Overview

Ruberoo is a backend-only microservices architecture demonstrating:
- **Service Discovery** (Eureka)
- **Configuration Management** (Spring Cloud Config)
- **API Gateway** (Spring Cloud Gateway with JWT authentication)
- **Distributed Services** (User, Ride Management, Tracking)
- **Message Broker** (Redis for rate limiting)
- **Database** (MySQL)

---

## 🏗️ Architecture

```
┌─────────────────┐
│   API Gateway    │ (Port 9095) - JWT Auth, Rate Limiting
└────────┬─────────┘
         │
    ┌────┴────┬─────────────┬──────────────┐
    │         │             │              │
┌───▼───┐ ┌──▼───┐ ┌───────▼────┐ ┌───────▼────┐
│ User  │ │ Ride │ │  Tracking  │ │  Eureka    │
│Service│ │Mgmt  │ │  Service    │ │  Server    │
└───┬───┘ └──┬───┘ └───────┬────┘ └──────┬─────┘
    │        │             │             │
    └────────┴─────────────┴─────────────┘
              │
         ┌────▼────┐
         │  MySQL  │
         └─────────┘
```

---

## 🚀 Quick Start

### Prerequisites

- Docker & Docker Compose
- Java 21 (for local development)
- Maven 3.8+
- Postman (for API testing)

### Start All Services

```bash
# Clone repository
git clone https://github.com/mitali246/ruberoo-microservices.git
cd ruberoo-microservices

# Start all services
docker compose up -d

# Verify services are running
docker ps
```

Wait 30-60 seconds for all services to start.

---

## 📡 Service Endpoints

| Service | Port | Health Check | Status |
|---------|------|--------------|--------|
| **API Gateway** | 9095 | `http://localhost:9095/actuator/health` | ✅ |
| **User Service** | 9081 | `http://localhost:9081/actuator/health` | ✅ |
| **Ride Management** | 9082 | `http://localhost:9082/actuator/health` | ✅ |
| **Tracking Service** | 8084 | `http://localhost:8084/actuator/health` | ✅ |
| **Eureka Server** | 8761 | `http://localhost:8761` | ✅ |
| **Config Server** | 8889 | `http://localhost:8889/actuator/health` | ✅ |

---

## 🧪 Testing with Postman

### 1. Import Collection

1. Open Postman
2. Click "Import" → Select `Ruberoo-Microservices.postman_collection.json`
3. Collection imported: **"Ruberoo Microservices API"**

### 2. Create Environment

1. Click "Environments" → "+" (create new)
2. Name: `Local Development`
3. Add variables:
   - `baseUrl`: `http://localhost:9095`
   - `token`: (leave empty - auto-saved after login)
   - `userServiceUrl`: `http://localhost:9081`
   - `rideServiceUrl`: `http://localhost:9082`
   - `trackingServiceUrl`: `http://localhost:8084`
4. Save and select environment (top right dropdown)

### 3. Test Flow

1. **Infrastructure** → Test all health checks
2. **Authentication** → Register user → Login (token auto-saves)
3. **User Service** → Test CRUD operations
4. **Ride Management** → Test CRUD operations
5. **Tracking Service** → Test CRUD operations

**Complete guide:** See `POSTMAN_SETUP_GUIDE.md`

---

## 📚 API Endpoints

### Public Endpoints (No Auth)

- `POST /api/users` - Register user
- `POST /api/users/auth/login` - Login (returns JWT token)
- `GET /actuator/health` - Health checks (all services)

### Protected Endpoints (JWT Required)

**User Service:**
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

**Ride Management:**
- `POST /api/rides` - Create ride
- `GET /api/rides` - Get all rides
- `GET /api/rides/{id}` - Get ride by ID
- `PUT /api/rides/{id}` - Update ride
- `DELETE /api/rides/{id}` - Delete ride

**Tracking Service:**
- `GET /api/tracking/health` - Health check
- `GET /api/tracking/rides/{rideId}/location` - Get location
- `POST /api/emergency-contacts` - Create contact
- `GET /api/emergency-contacts` - Get all contacts
- `GET /api/emergency-contacts/{id}` - Get contact by ID
- `PUT /api/emergency-contacts/{id}` - Update contact
- `DELETE /api/emergency-contacts/{id}` - Delete contact

**Complete API documentation:** See `BACKEND_API_TESTING_GUIDE.md`

---

## 🔐 Authentication Flow

1. **Register:** `POST /api/users` (public)
2. **Login:** `POST /api/users/auth/login` (public)
   - Returns: `{"token": "eyJ..."}`
   - Token auto-saves to Postman environment
3. **Use Token:** All protected endpoints require `Authorization: Bearer {token}` header

---

## 🛠️ Development

### Build Project

```bash
mvn clean install -DskipTests
```

### Build Specific Service

```bash
mvn clean install -DskipTests -pl ruberoo-user-service
```

### Rebuild Docker Containers

```bash
docker compose build
docker compose up -d
```

### View Logs

```bash
docker logs ruberoo-user-service
docker logs ruberoo-gateway
docker logs eureka-server
```

---

## 📁 Project Structure

```
ruberoo-microservices/
├── ruberoo-api-gateway/          # API Gateway (JWT, Rate Limiting)
├── ruberoo-user-service/         # User Management & Auth
├── ruberoo-ride-management-service/  # Ride CRUD Operations
├── ruberoo-tracking-service/     # Location Tracking & Emergency Contacts
├── ruberoo-eureka-server/        # Service Discovery
├── ruberoo-config-server/        # Configuration Management
├── config-client-demo/           # Config Client Demo
├── config-server-demo/           # Config Server Demo
├── security-demo/                 # Security Demo
├── docker-compose.yml            # Docker Compose Configuration
├── k8s/                          # Kubernetes Manifests
├── Ruberoo-Microservices.postman_collection.json  # Postman Collection
└── README.md                     # This file
```

---

## 📖 Documentation

- **`POSTMAN_SETUP_GUIDE.md`** - Complete Postman setup and testing guide
- **`BACKEND_API_TESTING_GUIDE.md`** - Comprehensive API documentation
- **`ENDPOINT_VERIFICATION_REPORT.md`** - Endpoint verification details
- **`BACKEND_ONLY_SUMMARY.md`** - Quick reference guide
- **`AWS_DEPLOYMENT_GUIDE.md`** - AWS deployment guide
- **`STEP1_IAM_USER_SETUP.md`** - AWS IAM user setup

---

## 🔧 Configuration

### Environment Variables

Services are configured via:
- `application.properties` (default)
- `bootstrap.properties` (Config Server connection)
- Docker environment variables (see `docker-compose.yml`)

### Database

- **Host:** `mysql-db` (Docker network) or `localhost:3307` (external)
- **User:** `root`
- **Password:** `rootmitali`
- **Databases:** `ruberoo_user_db`, `ruberoo_ride_db`, `ruberoo_tracking_db`

### JWT Secret

Configured in `docker-compose.yml`:
```yaml
RUBEROO_JWT_SECRET_KEY: bXlTdXBlclNlY3JldEp3dFNlY3JldEtleVRoYXRJc0F0TGVhc3QyNTZCaXRzTG9uZw==
```

---

## ✅ Verification Checklist

- [x] All services running (`docker ps`)
- [x] Eureka shows all services registered
- [x] Health checks pass for all services
- [x] User registration works
- [x] User login returns JWT token
- [x] Protected endpoints require authentication
- [x] CRUD operations work for all services
- [x] Service interconnections verified

---

## 🐛 Troubleshooting

### Services Not Starting

```bash
# Check logs
docker logs <service-name>

# Restart specific service
docker compose restart <service-name>

# Rebuild and restart
docker compose build <service-name>
docker compose up -d <service-name>
```

### Health Checks Failing

- Verify services are registered in Eureka: `http://localhost:8761`
- Check database connectivity
- Verify environment variables are set correctly

### 401 Unauthorized

- Ensure token is set in Postman environment
- Verify token hasn't expired (re-login if needed)
- Check environment is selected (top right dropdown)

---

## 🚀 Next Steps

1. **Test all endpoints** using Postman collection
2. **Deploy to AWS** (see `AWS_DEPLOYMENT_GUIDE.md`)
3. **Set up CI/CD** pipeline
4. **Monitor services** (health checks, logs)

---

## 📝 License

This project is for educational/demonstration purposes.

---

## 👥 Contributors

- Mitali

---

**Last Updated:** November 5, 2025  
**Status:** ✅ Backend-Only Mode Active  
**Version:** 1.0.0

