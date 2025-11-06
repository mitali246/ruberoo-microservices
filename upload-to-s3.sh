#!/bin/bash
# Script to upload source code to S3 for CodePipeline

set -e

PROFILE="ruberoo-deployment"
REGION="us-east-1"
S3_BUCKET="ruberoo-codepipeline-artifacts-008041186656"
S3_KEY="source/ruberoo-microservices.zip"

echo "📦 Preparing source code for S3 upload..."
echo ""

# Create a temporary directory
TEMP_DIR=$(mktemp -d)
ZIP_FILE="$TEMP_DIR/ruberoo-microservices.zip"

echo "📁 Creating zip file..."
cd /Users/mitali/Desktop/MSA/ruberoo-microservices

# Create zip excluding unnecessary files
zip -r "$ZIP_FILE" . \
  -x "*.git/*" \
  -x "node_modules/*" \
  -x "target/*" \
  -x "*.class" \
  -x ".DS_Store" \
  -x "*.iml" \
  -x ".idea/*" \
  -x "*.log" \
  > /dev/null

echo "✅ Zip file created: $ZIP_FILE"
echo ""

echo "📤 Uploading to S3..."
aws s3 cp "$ZIP_FILE" "s3://$S3_BUCKET/$S3_KEY" \
  --profile "$PROFILE" \
  --region "$REGION"

echo ""
echo "✅ Source code uploaded to S3!"
echo "📍 Location: s3://$S3_BUCKET/$S3_KEY"
echo ""

# Cleanup
rm -rf "$TEMP_DIR"

echo "🎯 Next: Create CodePipeline with S3 source!"


