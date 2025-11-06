╔══════════════════════════════════════════════════════════════════════╗
║                                                                      ║
║              🧪 COMPLETE TESTING WORKFLOW - START HERE              ║
║                                                                      ║
╚══════════════════════════════════════════════════════════════════════╝

## 🎯 Current Status Summary

### ✅ What's Fixed:
- **401 Authentication Error** - Added public registration endpoint
- **Security Configuration** - Registration no longer requires auth
- **Registration Endpoint** - Added `/api/users/auth/register` with password encryption
- **AWS Pipeline Fix** - Buildspec files corrected (ready to deploy)

### 🔧 What You Need to Test:

1. **Local Services** (if running locally)
2. **AWS Deployed Services** (once pipeline completes)
3. **Full API Workflow** (register → login → authenticated calls)

## 📋 Testing Options

### Option 1: Test Local Services 🏠

**Prerequisites:**
```bash
# Ensure services are running
docker-compose up -d
# OR
mvn spring-boot:run (in each service directory)
```

**Test Commands:**
```bash
# 1. Test User Service Health
curl http://localhost:8081/actuator/health

# 2. Test Registration (Fixed!)
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123",
    "phone": "+1234567890",
    "role": "USER"
  }'

# 3. Test Login
curl -X POST http://localhost:8081/api/users/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Option 2: Deploy to AWS First 🚀

**Deploy Pipeline:**
```bash
cd /Users/mitali/Desktop/MSA/ruberoo-microservices

# Run the buildspec fix (if not done)
chmod +x fix-buildspecs-complete.sh && ./fix-buildspecs-complete.sh

# Commit and push to trigger pipeline
git add aws/buildspecs/*.yml
git commit -m "fix: Update buildspecs for Docker context"  
git push origin main

# Watch pipeline: AWS Console → CodePipeline → ruberoo-services-pipeline
```

**Test Deployed Services:**
```bash
# Replace ALB_URL with your actual Application Load Balancer URL
ALB_URL="your-alb-url-here.us-east-1.elb.amazonaws.com"

# Test services through ALB
curl http://${ALB_URL}:8081/actuator/health
```

### Option 3: Test with Postman 📮

**Import Collection:**
1. Open Postman
2. Import: `Ruberoo-Postman-Collection.json`
3. Create Environment: "Ruberoo Local"
4. Set variable: `base_url = http://localhost:8081`

**Test Sequence:**
1. **Health Check** → `GET {{base_url}}/actuator/health`
2. **Register User** → `POST {{base_url}}/api/users`
3. **Login User** → `POST {{base_url}}/api/users/auth/login` (save token!)
4. **Get Profile** → `GET {{base_url}}/api/users/1` (use token in header)

## 🔄 Recommended Testing Workflow

### Phase 1: Local Development Testing

```bash
# Start services
cd /Users/mitali/Desktop/MSA/ruberoo-microservices
docker-compose up -d

# Wait for services to start (30-60 seconds)
sleep 60

# Test basic connectivity
curl http://localhost:8761  # Eureka
curl http://localhost:8081/actuator/health  # User Service
curl http://localhost:8080/actuator/health  # API Gateway

# Test registration (your fixed issue!)
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "password123",
    "phone": "+1234567890",
    "role": "USER"
  }'

# Expected: 200 OK with user object (no password returned)
```

### Phase 2: Authentication Flow Testing

```bash
# 1. Login to get JWT token
RESPONSE=$(curl -s -X POST http://localhost:8081/api/users/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "password123"
  }')

echo "Login Response: $RESPONSE"

# 2. Extract token (you'll need to do this manually)
TOKEN="paste-jwt-token-here"

# 3. Test authenticated endpoint
curl -X GET http://localhost:8081/api/users/1 \
  -H "Authorization: Bearer $TOKEN"

# Expected: User profile data
```

### Phase 3: Full Microservices Testing

```bash
# Test through API Gateway (if running)
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Gateway User",
    "email": "gateway@example.com",
    "password": "password123",
    "phone": "+1234567891",
    "role": "USER"
  }'

# Test ride booking (once user is registered/logged in)
curl -X POST http://localhost:8080/api/rides \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "pickupLocation": "123 Main St",
    "dropoffLocation": "456 Oak Ave",
    "rideType": "STANDARD"
  }'
```

## 🚨 Troubleshooting Common Issues

### Issue 1: Still Getting 401 
**Solution:** Make sure you rebuilt the user service after my fixes:
```bash
cd ruberoo-user-service
mvn clean package -DskipTests
mvn spring-boot:run
```

### Issue 2: Port Connection Refused
**Check what's running:**
```bash
lsof -i :8081  # User Service
lsof -i :8080  # API Gateway
lsof -i :8761  # Eureka
```

### Issue 3: Database Connection Error
**Start MySQL:**
```bash
docker-compose up -d mysql-db
# Wait for MySQL to fully start
sleep 30
```

### Issue 4: Service Registration Issues
**Check Eureka:**
```bash
curl http://localhost:8761/eureka/apps
# Should show registered services
```

## 📊 Service Port Reference

| Service | Port | URL |
|---------|------|-----|
| Eureka Server | 8761 | http://localhost:8761 |
| Config Server | 8888 | http://localhost:8888 |
| API Gateway | 8080 | http://localhost:8080 |
| User Service | 8081 | http://localhost:8081 |
| Ride Service | 8082 | http://localhost:8082 |
| Tracking Service | 8083 | http://localhost:8083 |

## ✅ Success Criteria Checklist

### Basic Connectivity:
- [ ] Eureka dashboard loads at http://localhost:8761
- [ ] User service health check returns `{"status":"UP"}`
- [ ] API Gateway health check returns `{"status":"UP"}`

### Authentication Flow:
- [ ] User registration returns 200 OK (not 401!)
- [ ] User login returns JWT token
- [ ] JWT token works for authenticated endpoints

### Microservices Integration:
- [ ] All services register with Eureka
- [ ] API Gateway routes requests to services
- [ ] Database operations work (create/read users)

### Full Application Flow:
- [ ] Register user → Login → Get profile → Book ride
- [ ] Frontend connects to backend APIs
- [ ] Real-time features work (WebSocket)

## 🎯 Next Steps Based on Results

### If Local Tests Pass ✅
1. Deploy to AWS using the fixed pipeline
2. Test deployed services with same workflow
3. Update frontend to use deployed backend URLs

### If Local Tests Fail ❌
1. Check specific error messages
2. Verify database connectivity
3. Ensure all services are running
4. Check application logs for details

### If AWS Deploy Fails ❌
1. Check CodeBuild logs in AWS Console
2. Verify ECR repositories exist
3. Check ECS service configurations
4. Review network/security group settings

## 📚 Reference Documentation

- **POSTMAN_401_FIX.md** - Detailed fix for your 401 error
- **AWS_PIPELINE_FIX_GUIDE.md** - Complete AWS deployment fix
- **MICROSERVICES_TESTING_GUIDE.md** - API testing reference
- **Ruberoo-Postman-Collection.json** - Ready-to-use Postman tests

═══════════════════════════════════════════════════════════════════════

## 🚀 Quick Start Commands

**Test locally right now:**
```bash
cd /Users/mitali/Desktop/MSA/ruberoo-microservices

# Start services
docker-compose up -d

# Test the fix (wait 60 seconds for startup)
sleep 60 && curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"password123","phone":"+1234567890","role":"USER"}'
```

**Deploy to AWS:**
```bash
# Fix buildspecs and deploy
./fix-buildspecs-complete.sh
git add aws/buildspecs/*.yml
git commit -m "fix buildspecs" && git push origin main
```

═══════════════════════════════════════════════════════════════════════

**Ready to test? Choose your path and let's validate everything works! 🎊**
