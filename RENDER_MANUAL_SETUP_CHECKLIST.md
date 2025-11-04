# 🛠️ Render Manual Setup Checklist

**Purpose:** Initial manual deployment to obtain Service IDs and URLs for CI/CD automation  
**Status:** ⏳ IN PROGRESS  
**Date Started:** November 4, 2025

---

## 📋 Pre-Deployment Checklist

Before starting, ensure you have:

- [ ] **Render Account** created and verified (https://render.com)
- [ ] **Supabase Account** with PostgreSQL database created
- [ ] **Upstash Account** with Redis instance created
- [ ] **GitHub Repository** pushed with all services
- [ ] **JWT Secret Key** generated (512-bit, base64-encoded)

### Generate JWT Secret Key (if not already done):
```bash
openssl rand -base64 64
```
**Store this securely** - you'll need it for multiple services.

---

## 🔄 Service Deployment Order

Deploy services in this exact order (dependencies matter):

1. ✅ MySQL Database (Supabase) - Deploy first
2. ✅ Redis Cache (Upstash) - Deploy second
3. ⏳ Eureka Server - Service Discovery (Deploy 3rd)
4. ⏳ Config Server - Centralized Configuration (Deploy 4th)
5. ⏳ API Gateway - Public Entry Point (Deploy 5th)
6. ⏳ User Service - Authentication (Deploy 6th)
7. ⏳ Ride Management Service - Ride booking (Deploy 7th)
8. ⏳ Tracking Service - Emergency contacts (Deploy 8th)
9. ⏳ Frontend - React UI (Deploy last)

---

## 1️⃣ Supabase PostgreSQL Database

### Setup Steps:
1. Go to https://supabase.com
2. Create new project: `ruberoo-production`
3. Choose region closest to you
4. Wait for database provisioning (~2 minutes)
5. Go to **Settings → Database**
6. Copy **Connection String** (JDBC format)

### Database Initialization:
Run this SQL in Supabase SQL Editor:

```sql
-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create rides table
CREATE TABLE rides (
    id BIGSERIAL PRIMARY KEY,
    origin VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    scheduled_time VARCHAR(255),
    user_id BIGINT REFERENCES users(id),
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create emergency contacts table
CREATE TABLE emergency_contacts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    contact_name VARCHAR(255) NOT NULL,
    contact_number VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_rides_user_id ON rides(user_id);
CREATE INDEX idx_rides_status ON rides(status);
CREATE INDEX idx_emergency_user_id ON emergency_contacts(user_id);
```

### ✅ Record These Values:
- [ ] **DB_URL**: `jdbc:postgresql://db.xxxxxx.supabase.co:5432/postgres`
- [ ] **DB_USERNAME**: `postgres`
- [ ] **DB_PASSWORD**: `[your-password]`

---

## 2️⃣ Upstash Redis Cache

### Setup Steps:
1. Go to https://upstash.com
2. Create new Redis database: `ruberoo-redis`
3. Choose region closest to you
4. Copy connection details

### ✅ Record These Values:
- [ ] **REDIS_HOST**: `[your-instance].upstash.io`
- [ ] **REDIS_PORT**: `6379`
- [ ] **REDIS_PASSWORD**: `[your-password]`

---

## 3️⃣ Eureka Server Deployment

### Render Configuration:

| Setting | Value |
|---------|-------|
| **Service Type** | Web Service |
| **Name** | `ruberoo-eureka-server` |
| **Repository** | `https://github.com/mitali246/ruberoo-microservices` |
| **Branch** | `main` |
| **Root Directory** | `ruberoo-eureka-server` |
| **Environment** | `Docker` |
| **Instance Type** | `Free` |
| **Internal Port** | `8761` |

### Environment Variables:
```
SPRING_PROFILES_ACTIVE=prod
```

### ✅ After Deployment, Record:
- [ ] **Service ID**: `srv-________________`
- [ ] **Public URL**: `https://ruberoo-eureka-server.onrender.com`
- [ ] **Status**: Deployed ✅

### Verification:
Open URL in browser - you should see Eureka Dashboard with 0 instances initially.

---

## 4️⃣ Config Server Deployment

### Render Configuration:

| Setting | Value |
|---------|-------|
| **Service Type** | Web Service |
| **Name** | `ruberoo-config-server` |
| **Repository** | `https://github.com/mitali246/ruberoo-microservices` |
| **Branch** | `main` |
| **Root Directory** | `ruberoo-config-server` |
| **Environment** | `Docker` |
| **Instance Type** | `Free` |
| **Internal Port** | `8888` |

### Environment Variables:
```
SPRING_PROFILES_ACTIVE=prod
SPRING_CLOUD_CONFIG_SERVER_GIT_URI=https://github.com/mitali246/ruberoo-microservices.git
SPRING_CLOUD_CONFIG_SERVER_GIT_DEFAULT_LABEL=main
SPRING_CLOUD_CONFIG_SERVER_GIT_SEARCH_PATHS=config-repo
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=https://ruberoo-eureka-server.onrender.com/eureka
```

### ✅ After Deployment, Record:
- [ ] **Service ID**: `srv-________________`
- [ ] **Public URL**: `https://ruberoo-config-server.onrender.com`
- [ ] **Status**: Deployed ✅

### Verification:
```bash
curl https://ruberoo-config-server.onrender.com/actuator/health
# Should return: {"status":"UP"}
```

---

## 5️⃣ API Gateway Deployment

### Render Configuration:

| Setting | Value |
|---------|-------|
| **Service Type** | Web Service |
| **Name** | `ruberoo-api-gateway` |
| **Repository** | `https://github.com/mitali246/ruberoo-microservices` |
| **Branch** | `main` |
| **Root Directory** | `ruberoo-api-gateway` |
| **Environment** | `Docker` |
| **Instance Type** | `Free` |
| **Internal Port** | `8085` |

### Environment Variables:
```
SPRING_PROFILES_ACTIVE=prod
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=https://ruberoo-eureka-server.onrender.com/eureka
SPRING_CLOUD_CONFIG_URI=https://ruberoo-config-server.onrender.com
REDIS_HOST=[your-upstash-host].upstash.io
REDIS_PORT=6379
REDIS_PASSWORD=[your-upstash-password]
RUBEROO_JWT_SECRET_KEY=[your-jwt-secret-key]
```

### ✅ After Deployment, Record:
- [ ] **Service ID**: `srv-________________`
- [ ] **Public URL**: `https://ruberoo-api-gateway.onrender.com`
- [ ] **Status**: Deployed ✅

### Verification:
```bash
curl https://ruberoo-api-gateway.onrender.com/actuator/health
# Should return: {"status":"UP"}
```

---

## 6️⃣ User Service Deployment

### Render Configuration:

| Setting | Value |
|---------|-------|
| **Service Type** | Web Service |
| **Name** | `ruberoo-user-service` |
| **Repository** | `https://github.com/mitali246/ruberoo-microservices` |
| **Branch** | `main` |
| **Root Directory** | `ruberoo-user-service` |
| **Environment** | `Docker` |
| **Instance Type** | `Free` |
| **Internal Port** | `8081` |

### Environment Variables:
```
SPRING_PROFILES_ACTIVE=prod
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=https://ruberoo-eureka-server.onrender.com/eureka
SPRING_CLOUD_CONFIG_URI=https://ruberoo-config-server.onrender.com
SPRING_DATASOURCE_URL=jdbc:postgresql://db.xxxxxx.supabase.co:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=[your-supabase-password]
RUBEROO_JWT_SECRET_KEY=[your-jwt-secret-key]
```

### ✅ After Deployment, Record:
- [ ] **Service ID**: `srv-________________`
- [ ] **Internal URL**: `http://ruberoo-user-service.onrender.com` (via Eureka)
- [ ] **Status**: Deployed ✅

### Verification:
Check Eureka Dashboard - should show `USER-SERVICE` registered.

---

## 7️⃣ Ride Management Service Deployment

### Render Configuration:

| Setting | Value |
|---------|-------|
| **Service Type** | Web Service |
| **Name** | `ruberoo-ride-management-service` |
| **Repository** | `https://github.com/mitali246/ruberoo-microservices` |
| **Branch** | `main` |
| **Root Directory** | `ruberoo-ride-management-service` |
| **Environment** | `Docker` |
| **Instance Type** | `Free` |
| **Internal Port** | `8083` |

### Environment Variables:
```
SPRING_PROFILES_ACTIVE=prod
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=https://ruberoo-eureka-server.onrender.com/eureka
SPRING_CLOUD_CONFIG_URI=https://ruberoo-config-server.onrender.com
SPRING_DATASOURCE_URL=jdbc:postgresql://db.xxxxxx.supabase.co:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=[your-supabase-password]
```

### ✅ After Deployment, Record:
- [ ] **Service ID**: `srv-________________`
- [ ] **Internal URL**: `http://ruberoo-ride-management-service.onrender.com`
- [ ] **Status**: Deployed ✅

---

## 8️⃣ Tracking Service Deployment

### Render Configuration:

| Setting | Value |
|---------|-------|
| **Service Type** | Web Service |
| **Name** | `ruberoo-tracking-service` |
| **Repository** | `https://github.com/mitali246/ruberoo-microservices` |
| **Branch** | `main` |
| **Root Directory** | `ruberoo-tracking-service` |
| **Environment** | `Docker` |
| **Instance Type** | `Free` |
| **Internal Port** | `8084` |

### Environment Variables:
```
SPRING_PROFILES_ACTIVE=prod
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=https://ruberoo-eureka-server.onrender.com/eureka
SPRING_CLOUD_CONFIG_URI=https://ruberoo-config-server.onrender.com
SPRING_DATASOURCE_URL=jdbc:postgresql://db.xxxxxx.supabase.co:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=[your-supabase-password]
```

### ✅ After Deployment, Record:
- [ ] **Service ID**: `srv-________________`
- [ ] **Internal URL**: `http://ruberoo-tracking-service.onrender.com`
- [ ] **Status**: Deployed ✅

---

## 9️⃣ Frontend Deployment

### Render Configuration:

| Setting | Value |
|---------|-------|
| **Service Type** | Static Site |
| **Name** | `ruberoo-frontend` |
| **Repository** | `https://github.com/mitali246/ruberoo-microservices` |
| **Branch** | `main` |
| **Root Directory** | `ruberoo-frontend` |
| **Build Command** | `npm install && npm run build` |
| **Publish Directory** | `dist` |

### Environment Variables:
```
VITE_API_GATEWAY_URL=https://ruberoo-api-gateway.onrender.com
```

### ✅ After Deployment, Record:
- [ ] **Service ID**: `srv-________________`
- [ ] **Public URL**: `https://ruberoo-frontend.onrender.com`
- [ ] **Status**: Deployed ✅

### Verification:
Open URL in browser - you should see the login page.

---

## 📝 GitHub Secrets Configuration

### Get Render API Key:
1. Go to Render Dashboard → Account Settings
2. Click **API Keys** → **Create API Key**
3. Copy the key (starts with `rnd_...`)

### Create All 18 Secrets:

Go to: `https://github.com/mitali246/ruberoo-microservices/settings/secrets/actions`

Click **New repository secret** and add each of these:

| Secret Name | Example Value | Status |
|-------------|---------------|--------|
| `RENDER_API_KEY` | `rnd_xxxxxxxxxxxxxx` | [ ] |
| `RENDER_EUREKA_SERVICE_ID` | `srv-xxxxxxxx` | [ ] |
| `RENDER_CONFIG_SERVICE_ID` | `srv-xxxxxxxx` | [ ] |
| `RENDER_GATEWAY_SERVICE_ID` | `srv-xxxxxxxx` | [ ] |
| `RENDER_USER_SERVICE_ID` | `srv-xxxxxxxx` | [ ] |
| `RENDER_RIDE_SERVICE_ID` | `srv-xxxxxxxx` | [ ] |
| `RENDER_TRACKING_SERVICE_ID` | `srv-xxxxxxxx` | [ ] |
| `RENDER_FRONTEND_SERVICE_ID` | `srv-xxxxxxxx` | [ ] |
| `EUREKA_URL` | `https://ruberoo-eureka-server.onrender.com` | [ ] |
| `API_GATEWAY_URL` | `https://ruberoo-api-gateway.onrender.com` | [ ] |
| `FRONTEND_URL` | `https://ruberoo-frontend.onrender.com` | [ ] |
| `DB_URL` | `jdbc:postgresql://db.xxx.supabase.co:5432/postgres` | [ ] |
| `DB_USERNAME` | `postgres` | [ ] |
| `DB_PASSWORD` | `[your-supabase-password]` | [ ] |
| `REDIS_HOST` | `[your-host].upstash.io` | [ ] |
| `REDIS_PORT` | `6379` | [ ] |
| `REDIS_PASSWORD` | `[your-upstash-password]` | [ ] |
| `JWT_SECRET_KEY` | `[your-512-bit-base64-key]` | [ ] |

**⚠️ CRITICAL:** Secret names are case-sensitive. Copy them exactly as shown.

---

## ✅ Final Verification Checklist

Once all services are deployed and secrets configured:

- [ ] All 9 services show "Live" status in Render Dashboard
- [ ] Eureka Dashboard shows all 6 microservices registered
- [ ] API Gateway health check returns `{"status":"UP"}`
- [ ] Frontend loads successfully in browser
- [ ] All 18 GitHub Secrets are created and verified
- [ ] Render API Key is valid and working

### Test Authentication Flow:
```bash
# Register a test user
curl -X POST https://ruberoo-api-gateway.onrender.com/api/users/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123",
    "role": "PASSENGER"
  }'

# Login and get JWT token
curl -X POST https://ruberoo-api-gateway.onrender.com/api/users/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

If you receive a JWT token, **your deployment is successful!** ✅

---

## 🚨 Troubleshooting

### Service Won't Start:
1. Check Render logs: Dashboard → Service → Logs
2. Verify environment variables are set correctly
3. Ensure Docker build succeeded
4. Check for port conflicts

### Eureka Registration Issues:
1. Verify `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` is correct
2. Check network connectivity between services
3. Wait 30-60 seconds for registration to complete

### Database Connection Errors:
1. Verify Supabase database is running
2. Check `SPRING_DATASOURCE_URL` format
3. Ensure credentials are correct
4. Verify Supabase allows external connections

### Redis Connection Errors:
1. Verify Upstash Redis is active
2. Check `REDIS_HOST` and `REDIS_PASSWORD`
3. Ensure TLS is enabled if required

---

## 📊 Deployment Status Summary

**Total Services:** 9  
**Completed:** 0/9  
**GitHub Secrets:** 0/18  
**Estimated Time:** 2-3 hours  
**Status:** ⏳ IN PROGRESS

---

## 🎯 Next Steps (After Completion)

Once this manual setup is complete:

1. ✅ Create `.github/workflows/deploy.yml` (CI/CD workflow)
2. ✅ Commit and push workflow to GitHub
3. ✅ Make a test code change to trigger automated deployment
4. ✅ Verify GitHub Actions runs successfully
5. ✅ Verify services update automatically on Render

---

**⚠️ DO NOT PROCEED to CI/CD setup until ALL checkboxes above are marked ✅**

**Report back with:** "Manual setup complete - all services deployed and secrets configured"

---

*Last Updated: November 4, 2025*
