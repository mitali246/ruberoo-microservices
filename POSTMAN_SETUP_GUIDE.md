# 🚀 Postman Setup Guide - Ruberoo Microservices

**Collection File:** `Ruberoo-Microservices.postman_collection.json`  
**Total Endpoints:** 33 pre-configured requests  
**Status:** ✅ Ready to Import

---

## 📋 Quick Start (5 Minutes)

### Step 1: Install Postman

1. **Download Postman:**
   - Go to: https://www.postman.com/downloads/
   - Download for your OS (Mac/Windows/Linux)
   - Install Postman

2. **Create Account (Free):**
   - Open Postman
   - Sign up for free account (or sign in if you have one)
   - Free account is sufficient for testing

### Step 2: Import Collection

1. **Open Postman**
2. **Click "Import" button** (top left, or press `Cmd+O` / `Ctrl+O`)
3. **Select "File" tab**
4. **Click "Upload Files"**
5. **Navigate to:** `/Users/mitali/Desktop/MSA/ruberoo-microservices`
6. **Select:** `Ruberoo-Microservices.postman_collection.json`
7. **Click "Import"**
8. **Collection imported!** You'll see "Ruberoo Microservices API" in Collections

### Step 3: Set Up Environment Variables

1. **Click "Environments"** in left sidebar (or `Cmd+E` / `Ctrl+E`)
2. **Click "+" button** to create new environment
3. **Name it:** `Local Development`
4. **Add these variables:**

| Variable | Initial Value | Current Value |
|----------|--------------|---------------|
| `baseUrl` | `http://localhost:9095` | `http://localhost:9095` |
| `token` | (leave empty) | (leave empty) |
| `eurekaUrl` | `http://localhost:8761` | `http://localhost:8761` |
| `configServerUrl` | `http://localhost:8889` | `http://localhost:8889` |
| `userServiceUrl` | `http://localhost:9081` | `http://localhost:9081` |
| `rideServiceUrl` | `http://localhost:9082` | `http://localhost:9082` |
| `trackingServiceUrl` | `http://localhost:8084` | `http://localhost:8084` |

5. **Click "Save"**
6. **Select "Local Development"** from dropdown (top right) to activate it

### Step 4: Start Services

```bash
cd /Users/mitali/Desktop/MSA/ruberoo-microservices
docker compose up -d
```

Wait 30 seconds, then verify:

```bash
docker ps
```

Should show 8 containers running.

---

## 🧪 Testing Workflow

### Step 1: Test Infrastructure

1. **Expand Collection:** "Ruberoo Microservices API"
2. **Open folder:** "0. Infrastructure"
3. **Click:** "Eureka Dashboard"
4. **Click "Send"** button
5. **Expected:** `200 OK` with HTML response

Test all health checks:
- ✅ Eureka Dashboard
- ✅ Eureka - Get Registered Services (JSON)
- ✅ Config Server Health
- ✅ API Gateway Health
- ✅ User Service Health
- ✅ Ride Management Health
- ✅ Tracking Service Health

### Step 2: Test Authentication

1. **Open folder:** "1. Authentication"
2. **Click:** "User Registration (via Gateway)"
3. **Update Body:**
   ```json
   {
     "name": "Test User",
     "email": "test@example.com",
     "password": "password123",
     "phone": "+1234567890",
     "role": "USER"
   }
   ```
4. **Click "Send"**
5. **Expected:** `200 OK` or `201 Created` with user object
6. **Save the user ID** from response

7. **Click:** "User Login"
8. **Update Body:**
   ```json
   {
     "email": "test@example.com",
     "password": "password123"
   }
   ```
9. **Click "Send"**
10. **Expected:** `200 OK` with token:
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    }
    ```

11. **Copy the token** (long string starting with `eyJ`)

12. **Set Token in Environment:**
    - Go to "Environments" → "Local Development"
    - Find `token` variable
    - Paste token in "Current Value" column
    - Click "Save"
    - **Select "Local Development"** from dropdown (top right) to activate

**✅ Token is now set!** All requests with `{{token}}` will automatically use it.

### Step 3: Test Protected Endpoints

Now test endpoints that require authentication:

**User Service:**
1. **Open folder:** "2. User Service"
2. **Click:** "Get All Users (Auth Required)"
3. **Click "Send"**
4. **Expected:** `200 OK` with array of users

**Ride Management:**
1. **Open folder:** "3. Ride Management Service"
2. **Click:** "Create Ride (Auth Required)"
3. **Update Body** with your user ID
4. **Click "Send"**
5. **Expected:** `200 OK` or `201 Created` with ride object

**Tracking Service:**
1. **Open folder:** "4. Tracking Service"
2. **Click:** "Create Emergency Contact (Auth Required)"
3. **Update Body** with your user ID
4. **Click "Send"**
5. **Expected:** `200 OK` or `201 Created` with contact object

---

## 🎯 Auto-Save Token Feature

The **"User Login"** request has a **Test Script** that automatically saves the token!

**How it works:**
1. Run "User Login" request
2. If response is `200 OK` and contains `token`
3. Token is automatically saved to `{{token}}` variable
4. No manual copy-paste needed!

**To verify:**
- After login, check Environment → `token` variable
- Should show your token automatically

---

## 📁 Collection Structure

```
Ruberoo Microservices API
├── 0. Infrastructure (7 requests)
│   ├── Eureka Dashboard
│   ├── Eureka - Get Registered Services (JSON)
│   ├── Config Server Health
│   ├── API Gateway Health
│   ├── User Service Health
│   ├── Ride Management Health
│   └── Tracking Service Health
│
├── 1. Authentication (2 requests)
│   ├── User Login (auto-saves token)
│   └── User Registration (via Gateway)
│
├── 2. User Service (4 requests)
│   ├── Get All Users (Auth Required)
│   ├── Get User by ID (Auth Required)
│   ├── Update User (Auth Required)
│   └── Delete User (Auth Required)
│
├── 3. Ride Management Service (5 requests)
│   ├── Create Ride (Auth Required)
│   ├── Get All Rides (Auth Required)
│   ├── Get Ride by ID (Auth Required)
│   ├── Update Ride (Auth Required)
│   └── Delete Ride (Auth Required)
│
├── 4. Tracking Service (7 requests)
│   ├── Tracking Service Health
│   ├── Get Last Known Location
│   ├── Create Emergency Contact (Auth Required)
│   ├── Get All Emergency Contacts (Auth Required)
│   ├── Get Emergency Contact by ID (Auth Required)
│   ├── Update Emergency Contact (Auth Required)
│   └── Delete Emergency Contact (Auth Required)
│
└── 5. Direct Service Access (8 requests)
    ├── User Service Direct - Health
    ├── User Service Direct - Get All Users
    ├── User Service Direct - Login
    ├── Ride Management Direct - Health
    ├── Ride Management Direct - Get All Rides
    ├── Ride Management Direct - Create Ride
    ├── Tracking Service Direct - Health
    └── Tracking Service Direct - Get All Emergency Contacts
```

**Total: 33 endpoints**

---

## 🔧 Using Environment Variables

The collection uses environment variables for easy switching:

- `{{baseUrl}}` - API Gateway URL (`http://localhost:9095`)
- `{{token}}` - JWT token (auto-saved after login)
- `{{userServiceUrl}}` - Direct User Service URL
- `{{rideServiceUrl}}` - Direct Ride Service URL
- `{{trackingServiceUrl}}` - Direct Tracking Service URL

**To update variables:**
1. Go to "Environments" → "Local Development"
2. Update "Current Value" column
3. Click "Save"
4. Make sure environment is selected (top right dropdown)

---

## ✅ Testing Checklist

### Infrastructure
- [ ] Eureka Dashboard accessible
- [ ] Eureka shows all registered services (JSON)
- [ ] Config Server health check passes
- [ ] API Gateway health check passes
- [ ] All service health checks pass

### Authentication
- [ ] User registration successful
- [ ] User login returns token
- [ ] Token auto-saved to environment (or manually set)
- [ ] Protected endpoints work with token
- [ ] Protected endpoints fail without token

### User Service
- [ ] Get all users (requires auth)
- [ ] Get user by ID (requires auth)
- [ ] Update user (requires auth)
- [ ] Delete user (requires auth)

### Ride Management
- [ ] Create ride (requires auth)
- [ ] Get all rides (requires auth)
- [ ] Get ride by ID (requires auth)
- [ ] Update ride (requires auth)
- [ ] Delete ride (requires auth)

### Tracking Service
- [ ] Tracking health check (public)
- [ ] Get last known location (requires auth)
- [ ] Create emergency contact (requires auth)
- [ ] Get all emergency contacts (requires auth)
- [ ] Update emergency contact (requires auth)
- [ ] Delete emergency contact (requires auth)

### Service Interconnections
- [ ] Gateway routes requests correctly
- [ ] Direct service access works
- [ ] Services registered in Eureka
- [ ] Create user → Create ride flow works
- [ ] Create user → Create emergency contact flow works

---

## 🐛 Troubleshooting

### Issue: "Collection Not Importing"

**Solution:**
1. Make sure file is `Ruberoo-Microservices.postman_collection.json`
2. Check JSON syntax is valid
3. Try importing via File → Import → Upload Files
4. Restart Postman if needed

### Issue: "Variables Not Working"

**Solution:**
1. Make sure "Local Development" environment is selected (top right dropdown)
2. Check variable names match exactly (`{{baseUrl}}`, not `{baseUrl}`)
3. Verify variables are saved in environment
4. Re-import collection if needed

### Issue: "401 Unauthorized"

**Solution:**
1. Make sure you logged in and got a token
2. Check token is set in environment variables
3. Verify token hasn't expired (re-login if needed)
4. Check environment is selected (top right dropdown)

### Issue: "Connection Refused"

**Solution:**
1. Check services are running: `docker ps`
2. Verify ports are correct in environment variables
3. Check service logs: `docker logs <service-name>`

---

## 💡 Postman Tips

1. **Save Responses:**
   - Right-click response → "Save Response" → "Save as Example"
   - Useful for documentation

2. **Duplicate Requests:**
   - Right-click request → "Duplicate"
   - Modify for different test cases

3. **Test Scripts:**
   - Use "Tests" tab to add assertions
   - Example: `pm.test("Status code is 200", function () { pm.response.to.have.status(200); });`

4. **Pre-request Scripts:**
   - Use "Pre-request Script" tab to set variables
   - Example: Generate timestamps, random IDs

5. **Collection Runner:**
   - Run multiple requests in sequence
   - Useful for integration testing

6. **Export Collection:**
   - Share collection with team
   - File → Export → Collection v2.1

---

## 🎯 Success Criteria

You've successfully set up Postman when:

- ✅ Collection imported successfully
- ✅ Environment variables configured
- ✅ Services running
- ✅ Infrastructure health checks pass
- ✅ Authentication flow works (register → login → token)
- ✅ Protected endpoints work with token
- ✅ All CRUD operations work
- ✅ Service interconnections verified

---

## 📝 Quick Reference

| Action | Postman Command |
|--------|----------------|
| **Import Collection** | File → Import → Upload Files |
| **Create Environment** | Environments → + → Add variables |
| **Select Environment** | Top right dropdown |
| **Send Request** | Click "Send" button |
| **View Response** | Bottom panel shows response |
| **Save Token** | Environments → Update `token` variable |
| **Use Variables** | `{{variableName}}` in URLs/headers |

---

## 🚀 Next Steps

1. ✅ Import collection
2. ✅ Set up environment
3. ✅ Start services
4. ✅ Test all endpoints
5. ✅ Verify service interconnections
6. ⏭️ Prepare for AWS deployment

---

**🎉 You're Ready!** Import the collection and start testing all your microservices!

**Files:**
- `Ruberoo-Microservices.postman_collection.json` - Postman collection
- `POSTMAN_SETUP_GUIDE.md` - This guide

**Need Help?** Check:
- `BACKEND_API_TESTING_GUIDE.md` - Complete API documentation
- `ENDPOINT_VERIFICATION_REPORT.md` - Endpoint verification details

