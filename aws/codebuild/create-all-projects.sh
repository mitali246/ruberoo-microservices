#!/bin/bash
# Script to create all CodeBuild projects for Ruberoo microservices

set -e

PROFILE="ruberoo-deployment"
REGION="us-east-1"
SERVICE_ROLE="arn:aws:iam::008041186656:role/ruberoo-codebuild-service-role"
GITHUB_REPO="https://github.com/mitali246/ruberoo-microservices.git"

# Array of services: name, buildspec path
SERVICES=(
  "ruberoo-user-service-build:aws/buildspecs/buildspec-user-service.yml:ruberoo-user"
  "ruberoo-api-gateway-build:aws/buildspecs/buildspec-api-gateway.yml:ruberoo-gateway"
  "ruberoo-ride-service-build:aws/buildspecs/buildspec-ride-service.yml:ruberoo-ride"
  "ruberoo-tracking-service-build:aws/buildspecs/buildspec-tracking-service.yml:ruberoo-tracking"
  "ruberoo-eureka-build:aws/buildspecs/buildspec-eureka.yml:ruberoo-eureka"
  "ruberoo-config-server-build:aws/buildspecs/buildspec-config-server.yml:ruberoo-config"
)

echo "=========================================="
echo "Creating CodeBuild Projects"
echo "=========================================="
echo ""

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
HELPER_SCRIPT="$SCRIPT_DIR/create-project-helper.sh"

for service_config in "${SERVICES[@]}"; do
  IFS=':' read -r project_name buildspec_path image_repo <<< "$service_config"
  
  echo "Creating project: $project_name..."
  bash "$HELPER_SCRIPT" "$project_name" "$buildspec_path" "$image_repo"
  echo ""
done

echo "=========================================="
echo "All CodeBuild projects created!"
echo "=========================================="

