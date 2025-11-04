#!/bin/bash

# Ruberoo Microservices Health Check Script
# This script verifies that all services are running and healthy

echo "========================================="
echo "🚀 RUBEROO MICROSERVICES HEALTH CHECK"
echo "========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to check service health
check_service() {
    local service_name=$1
    local port=$2
    local endpoint=$3
    
    echo -n "Checking $service_name... "
    
    response=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost:$port$endpoint" 2>/dev/null)
    
    if [ "$response" = "200" ]; then
        status=$(curl -s "http://localhost:$port$endpoint" | jq -r '.status' 2>/dev/null)
        if [ "$status" = "UP" ]; then
            echo -e "${GREEN}✅ UP${NC}"
            return 0
        else
            echo -e "${YELLOW}⚠️  RESPONDING (Status: $status)${NC}"
            return 1
        fi
    else
        echo -e "${RED}❌ DOWN (HTTP: $response)${NC}"
        return 1
    fi
}

# Check Docker containers
echo "📦 Docker Containers:"
echo "-------------------"
docker ps --format "table {{.Names}}\t{{.Status}}" | grep -E "ruberoo|NAME"
echo ""

# Check individual services
echo "🏥 Service Health Checks:"
echo "------------------------"

all_up=true

check_service "User Service       " "9081" "/actuator/health" || all_up=false
check_service "Ride Management    " "9082" "/actuator/health" || all_up=false
check_service "Tracking Service   " "8084" "/actuator/health" || all_up=false
check_service "API Gateway        " "9095" "/actuator/health" || all_up=false

echo ""
echo "🔍 Infrastructure Services:"
echo "--------------------------"
check_service "Eureka Server      " "8761" "/actuator/health" || all_up=false
check_service "Config Server      " "8889" "/actuator/health" || all_up=false

echo ""
echo "📊 Service Registration (Eureka):"
echo "---------------------------------"
services=$(curl -s http://localhost:9095/actuator/health | jq -r '.components.discoveryComposite.components.eureka.details.applications | to_entries[] | "\(.key): \(.value)"' 2>/dev/null)
if [ -n "$services" ]; then
    echo "$services"
else
    echo "Could not retrieve service registration info"
fi

echo ""
echo "🔗 Redis Connection:"
echo "-------------------"
redis_status=$(curl -s http://localhost:9095/actuator/health | jq -r '.components.redis.status' 2>/dev/null)
redis_version=$(curl -s http://localhost:9095/actuator/health | jq -r '.components.redis.details.version' 2>/dev/null)
echo "Status: $redis_status (Version: $redis_version)"

echo ""
echo "========================================="
if [ "$all_up" = true ]; then
    echo -e "${GREEN}✅ ALL SERVICES ARE HEALTHY!${NC}"
else
    echo -e "${YELLOW}⚠️  SOME SERVICES NEED ATTENTION${NC}"
fi
echo "========================================="
echo ""
echo "📝 Service Endpoints:"
echo "  - API Gateway:        http://localhost:9095"
echo "  - User Service:       http://localhost:9081"
echo "  - Ride Management:    http://localhost:9082"
echo "  - Tracking Service:   http://localhost:8084"
echo "  - Eureka Dashboard:   http://localhost:8761"
echo "  - Config Server:      http://localhost:8889"
echo ""
