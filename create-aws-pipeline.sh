#!/bin/bash
# Complete script to set up AWS CI/CD Pipeline with S3 source

set -e

PROFILE="ruberoo-deployment"
REGION="us-east-1"
S3_BUCKET="ruberoo-codepipeline-artifacts-008041186656"
SERVICE_ROLE="arn:aws:iam::008041186656:role/ruberoo-codepipeline-service-role"

echo "=========================================="
echo "🚀 AWS CI/CD Pipeline Setup (S3 Source)"
echo "=========================================="
echo ""

# Step 1: Upload source to S3
echo "📤 Step 1: Uploading source code to S3..."
./upload-to-s3.sh

echo ""
echo "📋 Step 2: Creating CodePipeline..."
echo ""

# Step 2: Create CodePipeline
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
              "provider": "S3",
              "version": "1"
            },
            "outputArtifacts": [
              {
                "name": "SourceOutput"
              }
            ],
            "configuration": {
              "S3Bucket": "$S3_BUCKET",
              "S3ObjectKey": "source/ruberoo-microservices.zip"
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
echo ""
echo "🎯 Next Steps:"
echo "1. Go to CodePipeline console to see your pipeline"
echo "2. To update source code, run: ./upload-to-s3.sh"
echo "3. Then release the pipeline manually or it will auto-run"
echo ""


