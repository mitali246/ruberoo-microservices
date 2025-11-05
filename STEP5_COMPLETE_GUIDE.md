# 🚀 Step 5: Complete CI/CD Pipeline Setup Guide

## 📋 Overview

This guide will help you create a complete CI/CD pipeline using:
- **CodeBuild** - Build Docker images and push to ECR
- **CodePipeline** - Orchestrate the entire workflow
- **GitHub** - Source code repository

---

## ✅ Prerequisites Completed:

- ✅ S3 bucket: `ruberoo-codepipeline-artifacts-008041186656`
- ✅ CodeBuild service role: `ruberoo-codebuild-service-role`
- ✅ Buildspec files: Created for all 6 services
- ✅ ECR repositories: Ready
- ✅ EKS cluster: Ready

---

## 🔑 Step 5.1: GitHub Personal Access Token

**Action Required:** Create a GitHub token

1. Go to: https://github.com/settings/tokens
2. Click "Generate new token (classic)"
3. Name: `Ruberoo AWS CodeBuild`
4. Expiration: `90 days`
5. Scopes: Check `repo`
6. Generate and **SAVE THE TOKEN** (shown only once!)

**After you have the token, we'll store it in AWS Secrets Manager.**

---

## 🔧 Step 5.2: Store GitHub Token in AWS

Once you have the token, run:

```bash
aws secretsmanager create-secret \
  --name ruberoo/github/token \
  --secret-string "YOUR_GITHUB_TOKEN_HERE" \
  --profile ruberoo-deployment \
  --region us-east-1
```

**Replace `YOUR_GITHUB_TOKEN_HERE` with your actual token!**

---

## 🔧 Step 5.3: Create CodeBuild Projects

**Option A: Create All Projects (Automated)**

I've created a script that creates all 6 CodeBuild projects:

```bash
cd /Users/mitali/Desktop/MSA/ruberoo-microservices
./aws/codebuild/create-all-projects.sh
```

**Option B: Create One by One (Manual)**

Start with User Service:

```bash
aws codebuild create-project \
  --cli-input-json file://aws/codebuild/user-service-project.json \
  --profile ruberoo-deployment \
  --region us-east-1
```

---

## 🔧 Step 5.4: Create CodePipeline

After CodeBuild projects are created, we'll create the pipeline to orchestrate everything.

---

## 📝 Services to Create Projects For:

1. `ruberoo-user-service-build`
2. `ruberoo-api-gateway-build`
3. `ruberoo-ride-service-build`
4. `ruberoo-tracking-service-build`
5. `ruberoo-eureka-build`
6. `ruberoo-config-server-build`

---

**First, create your GitHub token, then tell me: "GitHub token ready"** and I'll help you set it up!

