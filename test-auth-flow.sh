#!/bin/bash

# Ruberoo Authentication Flow Test Script

echo "========================================="
echo "🔐 RUBEROO AUTHENTICATION FLOW TEST"
echo "========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Generate a unique email for testing
TIMESTAMP=$(date +%s)
TEST_EMAIL="testuser${TIMESTAMP}@ruberoo.com"
TEST_PASSWORD="SecurePass123!"

echo -e "${BLUE}📝 Test Configuration:${NC}"
echo "  Email: $TEST_EMAIL"
echo "  Password: $TEST_PASSWORD"
echo ""

# Test 1: Register a new user
echo -e "${BLUE}1️⃣  Testing User Registration...${NC}"
REGISTER_RESPONSE=$(curl -s -X POST http://localhost:9095/api/users/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Test User\",\"email\":\"$TEST_EMAIL\",\"password\":\"$TEST_PASSWORD\",\"role\":\"PASSENGER\"}" 2>&1)

if echo "$REGISTER_RESPONSE" | grep -q "error\|Error\|401\|403\|500"; then
    echo -e "${RED}   ❌ Registration failed${NC}"
    echo "   Response: $REGISTER_RESPONSE"
else
    echo -e "${GREEN}   ✅ User registered successfully${NC}"
fi
echo ""

# Test 2: Login with the new user
echo -e "${BLUE}2️⃣  Testing User Login...${NC}"
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:9095/api/users/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$TEST_EMAIL\",\"password\":\"$TEST_PASSWORD\"}")

JWT_TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.token' 2>/dev/null)

if [ -n "$JWT_TOKEN" ] && [ "$JWT_TOKEN" != "null" ]; then
    echo -e "${GREEN}   ✅ Login successful${NC}"
    echo "   JWT Token: ${JWT_TOKEN:0:50}..."
else
    echo -e "${RED}   ❌ Login failed${NC}"
    echo "   Response: $LOGIN_RESPONSE"
fi
echo ""

# Test 3: Access protected endpoint with JWT
if [ -n "$JWT_TOKEN" ] && [ "$JWT_TOKEN" != "null" ]; then
    echo -e "${BLUE}3️⃣  Testing Protected Endpoint Access...${NC}"
    PROTECTED_RESPONSE=$(curl -s http://localhost:9095/api/users \
      -H "Authorization: Bearer $JWT_TOKEN")
    
    if echo "$PROTECTED_RESPONSE" | grep -q "error\|Error\|401\|403"; then
        echo -e "${RED}   ❌ Protected endpoint access failed${NC}"
        echo "   Response: $PROTECTED_RESPONSE"
    else
        echo -e "${GREEN}   ✅ Successfully accessed protected endpoint${NC}"
        echo "   Response: $(echo "$PROTECTED_RESPONSE" | jq -r '.' 2>/dev/null || echo "$PROTECTED_RESPONSE")"
    fi
    echo ""
    
    # Test 4: Access without JWT token (should fail)
    echo -e "${BLUE}4️⃣  Testing Unauthorized Access (should fail)...${NC}"
    UNAUTH_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:9095/api/users)
    
    if [ "$UNAUTH_RESPONSE" = "401" ] || [ "$UNAUTH_RESPONSE" = "403" ]; then
        echo -e "${GREEN}   ✅ Correctly rejected unauthorized request (HTTP $UNAUTH_RESPONSE)${NC}"
    else
        echo -e "${YELLOW}   ⚠️  Unexpected response: HTTP $UNAUTH_RESPONSE${NC}"
    fi
    echo ""
fi

# Test 5: Verify Rate Limiting (Redis)
echo -e "${BLUE}5️⃣  Testing Rate Limiting...${NC}"
RATE_LIMIT_COUNT=0
for i in {1..12}; do
    RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:9095/api/users/auth/login \
      -H "Content-Type: application/json" \
      -d "{\"email\":\"test@test.com\",\"password\":\"test\"}")
    
    if [ "$RESPONSE" = "429" ]; then
        RATE_LIMIT_COUNT=$((RATE_LIMIT_COUNT + 1))
    fi
done

if [ $RATE_LIMIT_COUNT -gt 0 ]; then
    echo -e "${GREEN}   ✅ Rate limiting is working (Got $RATE_LIMIT_COUNT 429 responses)${NC}"
else
    echo -e "${YELLOW}   ⚠️  Rate limiting not triggered yet (may need more requests)${NC}"
fi
echo ""

# Summary
echo "========================================="
echo -e "${GREEN}✅ AUTHENTICATION FLOW TEST COMPLETE${NC}"
echo "========================================="
echo ""
echo "📊 Test Summary:"
echo "  - User Registration: Check logs above"
echo "  - User Login: Check logs above"
echo "  - JWT Validation: Check logs above"
echo "  - Unauthorized Access Protection: Check logs above"
echo "  - Rate Limiting: Check logs above"
echo ""
echo "💡 Tips:"
echo "  - Check API Gateway logs: docker logs ruberoo-gateway"
echo "  - Check User Service logs: docker logs ruberoo-user-service"
echo "  - View Eureka dashboard: http://localhost:8761"
echo ""
