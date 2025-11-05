#!/bin/bash
# Helper script to create a single CodeBuild project

set -e

PROJECT_NAME=$1
BUILDSPEC_PATH=$2
IMAGE_REPO=$3
SERVICE_ROLE="arn:aws:iam::008041186656:role/ruberoo-codebuild-service-role"
GITHUB_REPO="https://github.com/mitali246/ruberoo-microservices.git"
REGION="us-east-1"
PROFILE="ruberoo-deployment"

# Create temporary JSON file
TEMP_JSON=$(mktemp)
cat > "$TEMP_JSON" <<EOF
{
  "name": "$PROJECT_NAME",
  "description": "CodeBuild project for $PROJECT_NAME",
  "source": {
    "type": "GITHUB",
    "location": "$GITHUB_REPO",
    "buildspec": "$BUILDSPEC_PATH"
  },
  "artifacts": {
    "type": "NO_ARTIFACTS"
  },
  "environment": {
    "type": "LINUX_CONTAINER",
    "image": "aws/codebuild/standard:7.0",
    "computeType": "BUILD_GENERAL1_SMALL",
    "privilegedMode": true,
    "environmentVariables": [
      {
        "name": "AWS_REGION",
        "value": "$REGION"
      },
      {
        "name": "ECR_REGISTRY",
        "value": "008041186656.dkr.ecr.us-east-1.amazonaws.com"
      },
      {
        "name": "IMAGE_REPO_NAME",
        "value": "$IMAGE_REPO"
      }
    ]
  },
  "serviceRole": "$SERVICE_ROLE",
  "timeoutInMinutes": 60
}
EOF

aws codebuild create-project \
  --cli-input-json file://"$TEMP_JSON" \
  --profile "$PROFILE" \
  --region "$REGION" > /dev/null

rm "$TEMP_JSON"

echo "✅ Created: $PROJECT_NAME"

