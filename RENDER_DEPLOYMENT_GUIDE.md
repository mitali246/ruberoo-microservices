# 🚀 Render Deployment Guide - Ruberoo Microservices

**Deployment Stack:** Render + Supabase + Upstash  
**CI/CD:** GitHub Actions → GHCR → Render  
**Status:** Manual Setup Required ⚠️

---

## 📋 Prerequisites Checklist

Before starting, ensure you have:
- ✅ GitHub account with repository access
- ✅ Render account (free tier): https://render.com
- ✅ Supabase account (free tier): https://supabase.com
- ✅ Upstash account (free tier): https://upstash.com
- ✅ Git repository pushed to GitHub

---

## 🛠️ STEP 1: Create External Services

### 1.1 Setup Supabase PostgreSQL Database

1. **Go to Supabase Dashboard:** https://app.supabase.com
2. **Create New Project:**
   - Project Name: `ruberoo-production`
   - Database Password: (Generate secure password - save it!)
   - Region: Choose closest to your users
   - Pricing Plan: Free

3. **Wait for Database Provisioning** (2-3 minutes)

4. **Get Connection Details:**
   - Go to Project Settings → Database
   - Copy these values:
     - **Host:** `db.xxxxxxxxxxxxx.supabase.co`
     - **Database name:** `postgres`
     - **Port:** `5432`
     - **User:** `postgres`
     - **Password:** (Your password from step 2)

5. **Format JDBC URL:**
   ```
   jdbc:postgresql://db.xxxxxxxxxxxxx.supabase.co:5432/postgres
   ```

6. **Initialize Database Schema:**
   - Go to SQL Editor in Supabase
   - Run this script:
   ```sql
   -- Create users table
   CREATE TABLE IF NOT EXISTS users (
       id BIGSERIAL PRIMARY KEY,
       name VARCHAR(255),
       email VARCHAR(255) UNIQUE NOT NULL,
       password VARCHAR(255) NOT NULL,
       role VARCHAR(50),
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );

   -- Create rides table
   CREATE TABLE IF NOT EXISTS rides (
       id BIGSERIAL PRIMARY KEY,
       origin VARCHAR(255),
       destination VARCHAR(255),
       scheduled_time VARCHAR(255),
       user_id BIGINT,
       status VARCHAR(50),
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (user_id) REFERENCES users(id)
   );

   -- Create emergency_contacts table
   CREATE TABLE IF NOT EXISTS emergency_contacts (
       id BIGSERIAL PRIMARY KEY,
       user_id BIGINT,
       contact_name VARCHAR(255),
       contact_number VARCHAR(255),
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (user_id) REFERENCES users(id)
   );
   ```

### 1.2 Setup Upstash Redis

1. **Go to Upstash Console:** https://console.upstash.com
2. **Create Redis Database:**
   - Name: `ruberoo-redis`
   - Type: Regional
   - Region: Choose closest to your Render region
   - Pricing: Free

3. **Get Connection Details:**
   - Go to database details page
   - Copy these values:
     - **Endpoint:** `xxxxx.upstash.io`
     - **Port:** `6379`
     - **Password:** (displayed on the page)

4. **Test Connection (Optional):**
   ```bash
   redis-cli -h xxxxx.upstash.io -p 6379 -a YOUR_PASSWORD PING
   ```

### 1.3 Generate JWT Secret Key

Run this command to generate a secure 512-bit key:
```bash
openssl rand -base64 64
```

**Save this key securely** - you'll need it for all services!

---

## 🌐 STEP 2: Deploy Services to Render

Deploy services **in this order** (dependencies matter!):

### 2.1 Deploy Eureka Server (Service Discovery)

1. **Go to Render Dashboard:** https://dashboard.render.com
2. **Click:** `New` → `Web Service`
3. **Connect Repository:**
   - Select "Build and deploy from a Git repository"
   - Connect your GitHub account
   - Select `ruberoo-microservices` repository
   - Click `Connect`

4. **Service Configuration:**
   ```
   Name:              ruberoo-eureka-server
   Environment:       Docker
   Region:            Oregon (or closest to you)
   Branch:            main
   Root Directory:    ruberoo-eureka-server
   Instance Type:     Free
   ```

5. **Docker Configuration:**
   ```
   Dockerfile Path:   Dockerfile (auto-detected)
   Docker Context:    ruberoo-eureka-server
   Port:              8761
   ```

6. **Environment Variables:**
   ```
   SPRING_PROFILES_ACTIVE = docker
   ```

7. **Click:** `Create Web Service`

8. **Wait for Deployment** (5-10 minutes for first build)

9. **Record Critical Information:**
   - **Service ID:** Find in URL (e.g., `srv-abcd1234efgh5678`)
   - **Public URL:** `https://ruberoo-eureka-server.onrender.com`
   - **Health Check:** Visit `https://ruberoo-eureka-server.onrender.com/`

### 2.2 Deploy Config Server

1. **Create New Web Service**
2. **Configuration:**
   ```
   Name:              ruberoo-config-server
   Environment:       Docker
   Branch:            main
   Root Directory:    ruberoo-config-server
   Instance Type:     Free
   Port:              8888
   ```

3. **Environment Variables:**
   ```
   SPRING_PROFILES_ACTIVE = docker
   EUREKA_CLIENT_SERVICEURL_DEFAULTZONE = https://ruberoo-eureka-server.onrender.com/eureka
   ```

4. **Record:** Service ID and Public URL

### 2.3 Deploy API Gateway

1. **Create New Web Service**
2. **Configuration:**
   ```
   Name:              ruberoo-api-gateway
   Environment:       Docker
   Branch:            main
   Root Directory:    ruberoo-api-gateway
   Instance Type:     Free
   Port:              8085
   ```

3. **Environment Variables:**
   ```
   SPRING_PROFILES_ACTIVE = docker
   EUREKA_CLIENT_SERVICEURL_DEFAULTZONE = https://ruberoo-eureka-server.onrender.com/eureka
   SPRING_REDIS_HOST = [Your Upstash Host]
   SPRING_REDIS_PORT = 6379
   SPRING_REDIS_PASSWORD = [Your Upstash Password]
   JWT_SECRET_KEY = [Your Generated JWT Secret]
   ```

4. **Record:** Service ID and Public URL

### 2.4 Deploy User Service

1. **Create New Web Service**
2. **Configuration:**
   ```
   Name:              ruberoo-user-service
   Environment:       Docker
   Branch:            main
   Root Directory:    ruberoo-user-service
   Instance Type:     Free
   Port:              8081
   ```

3. **Environment Variables:**
   ```
   SPRING_PROFILES_ACTIVE = docker
   EUREKA_CLIENT_SERVICEURL_DEFAULTZONE = https://ruberoo-eureka-server.onrender.com/eureka
   SPRING_DATASOURCE_URL = jdbc:postgresql://db.xxxxxxxxxxxxx.supabase.co:5432/postgres
   SPRING_DATASOURCE_USERNAME = postgres
   SPRING_DATASOURCE_PASSWORD = [Your Supabase Password]
   SPRING_JPA_HIBERNATE_DDL_AUTO = update
   JWT_SECRET_KEY = [Your Generated JWT Secret]
   ```

4. **Record:** Service ID and Public URL

### 2.5 Deploy Ride Management Service

1. **Create New Web Service**
2. **Configuration:**
   ```
   Name:              ruberoo-ride-management-service
   Environment:       Docker
   Branch:            main
   Root Directory:    ruberoo-ride-management-service
   Instance Type:     Free
   Port:              8083
   ```

3. **Environment Variables:**
   ```
   SPRING_PROFILES_ACTIVE = docker
   EUREKA_CLIENT_SERVICEURL_DEFAULTZONE = https://ruberoo-eureka-server.onrender.com/eureka
   SPRING_DATASOURCE_URL = jdbc:postgresql://db.xxxxxxxxxxxxx.supabase.co:5432/postgres
   SPRING_DATASOURCE_USERNAME = postgres
   SPRING_DATASOURCE_PASSWORD = [Your Supabase Password]
   SPRING_JPA_HIBERNATE_DDL_AUTO = update
   ```

4. **Record:** Service ID and Public URL

### 2.6 Deploy Tracking Service

1. **Create New Web Service**
2. **Configuration:**
   ```
   Name:              ruberoo-tracking-service
   Environment:       Docker
   Branch:            main
   Root Directory:    ruberoo-tracking-service
   Instance Type:     Free
   Port:              8084
   ```

3. **Environment Variables:**
   ```
   SPRING_PROFILES_ACTIVE = docker
   EUREKA_CLIENT_SERVICEURL_DEFAULTZONE = https://ruberoo-eureka-server.onrender.com/eureka
   SPRING_DATASOURCE_URL = jdbc:postgresql://db.xxxxxxxxxxxxx.supabase.co:5432/postgres
   SPRING_DATASOURCE_USERNAME = postgres
   SPRING_DATASOURCE_PASSWORD = [Your Supabase Password]
   SPRING_JPA_HIBERNATE_DDL_AUTO = update
   ```

4. **Record:** Service ID and Public URL

### 2.7 Deploy Frontend

1. **Create New Web Service**
2. **Configuration:**
   ```
   Name:              ruberoo-frontend
   Environment:       Docker
   Branch:            main
   Root Directory:    ruberoo-frontend
   Instance Type:     Free
   Port:              80
   ```

3. **Environment Variables:**
   ```
   VITE_API_BASE_URL = https://ruberoo-api-gateway.onrender.com
   ```

4. **Record:** Service ID and Public URL

### 2.8 Get Render API Key

1. **Go to:** Account Settings → API Keys
2. **Click:** `Create API Key`
3. **Name:** `GitHub Actions CI/CD`
4. **Copy and save the key securely**

---

## 🔐 STEP 3: Configure GitHub Secrets

Now you need to add **18 secrets** to your GitHub repository:

1. **Go to GitHub Repository:**
   - Navigate to: `Settings` → `Secrets and variables` → `Actions`
   - Click: `New repository secret`

2. **Add All Required Secrets:**

| Secret Name | Value Source | Example |
|-------------|--------------|---------|
| `RENDER_API_KEY` | Render Account Settings → API Keys | `rnd_xxxxxxxxxxxxxxxxxxxxxx` |
| `RENDER_EUREKA_SERVICE_ID` | Eureka service page URL | `srv-abcd1234efgh5678` |
| `RENDER_CONFIG_SERVICE_ID` | Config service page URL | `srv-ijkl5678mnop9012` |
| `RENDER_GATEWAY_SERVICE_ID` | Gateway service page URL | `srv-qrst3456uvwx7890` |
| `RENDER_USER_SERVICE_ID` | User service page URL | `srv-yzab1234cdef5678` |
| `RENDER_RIDE_SERVICE_ID` | Ride service page URL | `srv-ghij9012klmn3456` |
| `RENDER_TRACKING_SERVICE_ID` | Tracking service page URL | `srv-opqr5678stuv9012` |
| `RENDER_FRONTEND_SERVICE_ID` | Frontend service page URL | `srv-wxyz1234abcd5678` |
| `EUREKA_URL` | Eureka public URL | `https://ruberoo-eureka-server.onrender.com` |
| `API_GATEWAY_URL` | Gateway public URL | `https://ruberoo-api-gateway.onrender.com` |
| `FRONTEND_URL` | Frontend public URL | `https://ruberoo-frontend.onrender.com` |
| `DB_URL` | Supabase JDBC URL | `jdbc:postgresql://db.xxx.supabase.co:5432/postgres` |
| `DB_USERNAME` | Supabase username | `postgres` |
| `DB_PASSWORD` | Supabase database password | `your_secure_password` |
| `REDIS_HOST` | Upstash Redis host | `xxxxx.upstash.io` |
| `REDIS_PORT` | Upstash Redis port | `6379` |
| `REDIS_PASSWORD` | Upstash Redis password | `your_upstash_password` |
| `JWT_SECRET_KEY` | Generated secret key | `your_512_bit_base64_key` |

### How to Find Service ID in Render:
1. Go to your service dashboard
2. Look at the URL: `https://dashboard.render.com/web/srv-XXXXXXXXXXXXXXXX`
3. The `srv-XXXXXXXXXXXXXXXX` part is your Service ID

---

## ✅ STEP 4: Verification Checklist

Before proceeding to automated deployment, verify:

### External Services
- [ ] Supabase database is online and accessible
- [ ] Database schema is initialized with all tables
- [ ] Upstash Redis is online
- [ ] JWT secret key is generated and saved

### Render Services
- [ ] Eureka Server is deployed and running (green status)
- [ ] Config Server is deployed and registered with Eureka
- [ ] API Gateway is deployed and accessible
- [ ] User Service is deployed and registered with Eureka
- [ ] Ride Management Service is deployed and registered with Eureka
- [ ] Tracking Service is deployed and registered with Eureka
- [ ] Frontend is deployed and serving the UI

### GitHub Secrets
- [ ] All 18 secrets are added to GitHub repository
- [ ] No typos in secret names (case-sensitive!)
- [ ] All Service IDs are correct (srv-xxxxxxxx format)
- [ ] All URLs are HTTPS and publicly accessible

### Health Checks
Test each service manually:
```bash
# Eureka
curl https://ruberoo-eureka-server.onrender.com/

# Config Server
curl https://ruberoo-config-server.onrender.com/actuator/health

# API Gateway
curl https://ruberoo-api-gateway.onrender.com/actuator/health

# User Service
curl https://ruberoo-user-service.onrender.com/actuator/health

# Ride Management
curl https://ruberoo-ride-management-service.onrender.com/actuator/health

# Tracking Service
curl https://ruberoo-tracking-service.onrender.com/actuator/health

# Frontend
curl https://ruberoo-frontend.onrender.com/
```

---

## 🚀 STEP 5: Test Manual Deployment

Before automating, test the complete flow manually:

### 5.1 Test User Registration
```bash
curl -X POST https://ruberoo-api-gateway.onrender.com/api/users/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "test123",
    "role": "PASSENGER"
  }'
```

### 5.2 Test User Login
```bash
curl -X POST https://ruberoo-api-gateway.onrender.com/api/users/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "test123"
  }'
```

**Save the JWT token from the response!**

### 5.3 Test Authenticated Request
```bash
curl -X GET https://ruberoo-api-gateway.onrender.com/api/users \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 5.4 Test Ride Booking
```bash
curl -X POST https://ruberoo-api-gateway.onrender.com/api/rides \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "origin": "Times Square",
    "destination": "Central Park",
    "scheduledTime": "2025-11-05T14:00:00"
  }'
```

---

## 📊 Expected Results

After completing all steps:

### Render Dashboard
You should see 7 services all showing **"Live"** (green) status:
- ✅ ruberoo-eureka-server
- ✅ ruberoo-config-server
- ✅ ruberoo-api-gateway
- ✅ ruberoo-user-service
- ✅ ruberoo-ride-management-service
- ✅ ruberoo-tracking-service
- ✅ ruberoo-frontend

### Eureka Dashboard
Visit your Eureka URL and verify all services are registered:
- ✅ API-GATEWAY (1 instance)
- ✅ USER-SERVICE (1 instance)
- ✅ RIDE-MANAGEMENT-SERVICE (1 instance)
- ✅ TRACKING-SERVICE (1 instance)
- ✅ RUBEROO-CONFIG-SERVER (1 instance)

### Frontend
Visit your frontend URL and verify:
- ✅ Login page loads
- ✅ Registration page loads
- ✅ Can create an account
- ✅ Can log in
- ✅ Can book a ride

---

## ⚠️ Common Issues & Solutions

### Issue: Service won't start on Render
**Solution:**
- Check logs in Render dashboard
- Verify Dockerfile exists in service directory
- Ensure port matches in Dockerfile and Render config

### Issue: Service can't connect to Eureka
**Solution:**
- Verify `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` is correct
- Ensure Eureka Server is fully running before starting other services
- Check Eureka URL is HTTPS

### Issue: Database connection errors
**Solution:**
- Verify Supabase connection URL format
- Check username/password are correct
- Ensure database is not paused (Supabase free tier)

### Issue: JWT validation fails
**Solution:**
- Ensure `JWT_SECRET_KEY` is **identical** across all services
- Key must be Base64-encoded and at least 512 bits
- No extra spaces or line breaks in the secret

### Issue: Rate limiting errors
**Solution:**
- Verify Redis host, port, and password
- Check Upstash Redis is not paused
- Test Redis connection with redis-cli

---

## 🎯 Next Steps

Once all manual deployments are successful and verified:

1. ✅ All services are live on Render
2. ✅ All 18 GitHub Secrets are configured
3. ✅ Manual testing passes all API endpoints
4. ✅ Eureka shows all services registered

**You are ready to proceed with automated CI/CD!**

The next step will be to create and test the GitHub Actions workflow that automates builds and deployments.

---

## 📞 Support Resources

- **Render Documentation:** https://render.com/docs
- **Supabase Documentation:** https://supabase.com/docs
- **Upstash Documentation:** https://docs.upstash.com
- **Spring Cloud Netflix:** https://spring.io/projects/spring-cloud-netflix

---

**Status:** ⏳ Awaiting Manual Setup Completion  
**Created:** November 4, 2025  
**Next:** GitHub Actions Workflow Configuration
