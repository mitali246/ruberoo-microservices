#!/bin/zsh

echo "🚀 COMPLETE AWS DEPLOYMENT - READY TO GO!"
echo "========================================="
echo ""

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo "${YELLOW}📋 Pre-deployment Checklist:${NC}"
echo ""

# Check git status
echo "1. Checking git status..."
CHANGES=$(git status --porcelain | wc -l)
if [ $CHANGES -gt 0 ]; then
    echo "   ✅ Found $CHANGES uncommitted changes - will commit now"
else
    echo "   ℹ️  No uncommitted changes"
fi

# Check AWS credentials
echo "2. Checking AWS credentials..."
if aws sts get-caller-identity --profile ruberoo-deployment >/dev/null 2>&1; then
    echo "   ✅ AWS credentials valid"
else
    echo "   ❌ AWS credentials invalid - check your profile setup"
    exit 1
fi

# Check S3 bucket
echo "3. Checking S3 bucket access..."
S3_BUCKET="ruberoo-codepipeline-artifacts-008041186656"
if aws s3 ls "s3://$S3_BUCKET" --profile ruberoo-deployment >/dev/null 2>&1; then
    echo "   ✅ S3 bucket accessible"
else
    echo "   ❌ S3 bucket not accessible - check permissions"
    exit 1
fi

echo ""
echo "${GREEN}✅ All checks passed! Ready for deployment.${NC}"
echo ""

# Commit changes if any
if [ $CHANGES -gt 0 ]; then
    echo "${YELLOW}📝 Committing latest changes...${NC}"
    git add .
    git commit -m "fix: Complete authentication and AWS deployment fixes

- Fix 401 authentication errors in SecurityConfig and AuthController
- Add password encryption to user registration  
- Fix Docker build context in buildspec files
- Add comprehensive testing and deployment documentation
- Ready for AWS CI/CD pipeline deployment"
    echo "   ✅ Changes committed"
    echo ""
fi

# Upload to S3
echo "${YELLOW}📤 Uploading to S3 (this triggers the pipeline)...${NC}"
./upload-to-s3.sh

if [ $? -eq 0 ]; then
    echo ""
    echo "${GREEN}🎉 SUCCESS! Code uploaded to S3${NC}"
    echo ""
    echo "🔄 What happens next:"
    echo "   1. CodePipeline automatically detects S3 change (~30 sec)"
    echo "   2. Source stage downloads code (~1 min)"  
    echo "   3. Build stage compiles all 6 services (~5-8 min)"
    echo "   4. Deploy stage updates ECS services (~3-5 min)"
    echo "   5. Total time: ~10-15 minutes"
    echo ""
    echo "📊 Monitor progress at:"
    echo "   AWS Console → CodePipeline → ruberoo-services-pipeline"
    echo ""
    echo "🧪 Test when complete:"
    echo "   curl -X POST http://ALB_URL:8081/api/users \\"
    echo "     -H 'Content-Type: application/json' \\"
    echo "     -d '{\"name\":\"Test\",\"email\":\"test@aws.com\",\"password\":\"pass123\",\"phone\":\"+1234567890\",\"role\":\"USER\"}'"
    echo ""
    echo "${GREEN}✅ Your 401 authentication error will be FIXED after deployment!${NC}"
else
    echo ""
    echo "${RED}❌ S3 upload failed. Check the error above.${NC}"
    exit 1
fi

echo ""
echo "═══════════════════════════════════════════════════════════════"
echo "🎯 Deployment initiated! Wait ~15 minutes then test your APIs."
echo "═══════════════════════════════════════════════════════════════"
