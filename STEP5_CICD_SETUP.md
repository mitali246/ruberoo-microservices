# 🚀 Step 5: CI/CD Pipeline Setup

## 🎯 Objective
Create a complete CI/CD pipeline using CodeBuild and CodePipeline to automatically build and deploy microservices to EKS.

---

## 📋 Prerequisites

- ✅ GitHub repository: `https://github.com/mitali246/ruberoo-microservices.git`
- ✅ ECR repositories created (6 services)
- ✅ EKS cluster ready
- ✅ IAM user with permissions

---

## 🔧 Step 5.1: Create S3 Bucket for Pipeline Artifacts

CodePipeline needs an S3 bucket to store artifacts.

**Create the bucket:**
```bash
aws s3 mb s3://ruberoo-codepipeline-artifacts --region us-east-1 --profile ruberoo-deployment
```

---

## 🔧 Step 5.2: Create CodeBuild Service Role

CodeBuild needs a service role to access AWS resources.

**Create the role:**
```bash
aws iam create-role \
  --role-name ruberoo-codebuild-service-role \
  --assume-role-policy-document '{
    "Version": "2012-10-17",
    "Statement": [{
      "Effect": "Allow",
      "Principal": {"Service": "codebuild.amazonaws.com"},
      "Action": "sts:AssumeRole"
    }]
  }' \
  --profile ruberoo-deployment
```

**Attach policies:**
```bash
# ECR access
aws iam attach-role-policy \
  --role-name ruberoo-codebuild-service-role \
  --policy-arn arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryFullAccess \
  --profile ruberoo-deployment

# EKS access
aws iam attach-role-policy \
  --role-name ruberoo-codebuild-service-role \
  --policy-arn arn:aws:iam::008041186656:policy/ruberoo-eks-full-access \
  --profile ruberoo-deployment

# S3 access
aws iam attach-role-policy \
  --role-name ruberoo-codebuild-service-role \
  --policy-arn arn:aws:iam::aws:policy/AmazonS3FullAccess \
  --profile ruberoo-deployment

# CloudWatch Logs
aws iam attach-role-policy \
  --role-name ruberoo-codebuild-service-role \
  --policy-arn arn:aws:iam::aws:policy/CloudWatchLogsFullAccess \
  --profile ruberoo-deployment
```

---

## 🔧 Step 5.3: Create CodeBuild Projects

We'll create CodeBuild projects for each service. Let's start with User Service.

**Create User Service CodeBuild project:**
```bash
aws codebuild create-project \
  --name ruberoo-user-service-build \
  --source type=GITHUB,location=https://github.com/mitali246/ruberoo-microservices.git \
  --artifacts type=NO_ARTIFACTS \
  --environment type=LINUX_CONTAINER,image=aws/codebuild/standard:7.0,computeType=BUILD_GENERAL1_SMALL,privilegedMode=true \
  --service-role arn:aws:iam::008041186656:role/ruberoo-codebuild-service-role \
  --buildspec aws/buildspecs/buildspec-user-service.yml \
  --profile ruberoo-deployment \
  --region us-east-1
```

---

## 🔧 Step 5.4: Create CodePipeline

**Create the pipeline:**
```bash
aws codepipeline create-pipeline \
  --cli-input-json file://aws/pipeline-config.json \
  --profile ruberoo-deployment \
  --region us-east-1
```

---

## 📝 Next Steps:

1. Create S3 bucket for artifacts
2. Create CodeBuild service role
3. Create CodeBuild projects (one per service)
4. Create CodePipeline
5. Test the pipeline

---

**Ready to start?** Let's begin with creating the S3 bucket and service roles!

