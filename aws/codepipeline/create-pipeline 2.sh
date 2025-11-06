#!/bin/bash
# Script to create CodePipeline for Ruberoo microservices

set -e

PROFILE="ruberoo-deployment"
REGION="us-east-1"
GITHUB_OWNER="mitali246"
GITHUB_REPO="ruberoo-microservices"
GITHUB_BRANCH="main"
# GITHUB_TOKEN - Set via environment variable or AWS Secrets Manager
# Do not hardcode tokens in source code!
S3_BUCKET="ruberoo-codepipeline-artifacts-008041186656"
SERVICE_ROLE="arn:aws:iam::008041186656:role/ruberoo-codepipeline-service-role"

echo "=========================================="
echo "Creating CodePipeline"
echo "=========================================="
echo ""

# Create pipeline JSON
TEMP_JSON=$(mktemp)
cat > "$TEMP_JSON" <<EOF
{
  "pipeline": {
    "name": "ruberoo-microservices-pipeline",
    "roleArn": "$SERVICE_ROLE",
    "artifactStore": {
      "type": "S3",
      "location": "$S3_BUCKET"
    },
    "stages": [
      {
        "name": "Source",
        "actions": [
          {
            "name": "SourceAction",
            "actionTypeId": {
              "category": "Source",
              "owner": "AWS",
              "provider": "CodeStarSourceConnection",
              "version": "1"
            },
            "outputArtifacts": [
              {
                "name": "SourceOutput"
              }
            ],
            "configuration": {
              "ConnectionArn": "arn:aws:codestar-connections:us-east-1:008041186656:connection/placeholder",
              "FullRepositoryId": "$GITHUB_OWNER/$GITHUB_REPO",
              "BranchName": "$GITHUB_BRANCH"
            }
          }
        ]
      },
      {
        "name": "Build",
        "actions": [
          {
            "name": "BuildUserService",
            "actionTypeId": {
              "category": "Build",
              "owner": "AWS",
              "provider": "CodeBuild",
              "version": "1"
            },
            "inputArtifacts": [
              {
                "name": "SourceOutput"
              }
            ],
            "outputArtifacts": [
              {
                "name": "UserServiceOutput"
              }
            ],
            "configuration": {
              "ProjectName": "ruberoo-user-service-build"
            }
          },
          {
            "name": "BuildApiGateway",
            "actionTypeId": {
              "category": "Build",
              "owner": "AWS",
              "provider": "CodeBuild",
              "version": "1"
            },
            "inputArtifacts": [
              {
                "name": "SourceOutput"
              }
            ],
            "outputArtifacts": [
              {
                "name": "ApiGatewayOutput"
              }
            ],
            "configuration": {
              "ProjectName": "ruberoo-api-gateway-build"
            }
          },
          {
            "name": "BuildRideService",
            "actionTypeId": {
              "category": "Build",
              "owner": "AWS",
              "provider": "CodeBuild",
              "version": "1"
            },
            "inputArtifacts": [
              {
                "name": "SourceOutput"
              }
            ],
            "outputArtifacts": [
              {
                "name": "RideServiceOutput"
              }
            ],
            "configuration": {
              "ProjectName": "ruberoo-ride-service-build"
            }
          },
          {
            "name": "BuildTrackingService",
            "actionTypeId": {
              "category": "Build",
              "owner": "AWS",
              "provider": "CodeBuild",
              "version": "1"
            },
            "inputArtifacts": [
              {
                "name": "SourceOutput"
              }
            ],
            "outputArtifacts": [
              {
                "name": "TrackingServiceOutput"
              }
            ],
            "configuration": {
              "ProjectName": "ruberoo-tracking-service-build"
            }
          },
          {
            "name": "BuildEureka",
            "actionTypeId": {
              "category": "Build",
              "owner": "AWS",
              "provider": "CodeBuild",
              "version": "1"
            },
            "inputArtifacts": [
              {
                "name": "SourceOutput"
              }
            ],
            "outputArtifacts": [
              {
                "name": "EurekaOutput"
              }
            ],
            "configuration": {
              "ProjectName": "ruberoo-eureka-build"
            }
          },
          {
            "name": "BuildConfigServer",
            "actionTypeId": {
              "category": "Build",
              "owner": "AWS",
              "provider": "CodeBuild",
              "version": "1"
            },
            "inputArtifacts": [
              {
                "name": "SourceOutput"
              }
            ],
            "outputArtifacts": [
              {
                "name": "ConfigServerOutput"
              }
            ],
            "configuration": {
              "ProjectName": "ruberoo-config-server-build"
            }
          }
        ]
      }
    ]
  }
}
EOF

aws codepipeline create-pipeline \
  --cli-input-json file://"$TEMP_JSON" \
  --profile "$PROFILE" \
  --region "$REGION"

rm "$TEMP_JSON"

echo ""
echo "=========================================="
echo "✅ CodePipeline created successfully!"
echo "=========================================="

