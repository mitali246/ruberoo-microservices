# 🚀 Step 5: CI/CD Pipeline Setup - Quick Start

## ✅ Completed So Far:

- ✅ S3 bucket created: `ruberoo-codepipeline-artifacts-008041186656`
- ✅ CodeBuild service role created: `ruberoo-codebuild-service-role`
- ✅ Buildspec files created for all 6 services
- ✅ Policies attached to CodeBuild role

---

## 🔧 Step 5.3: Create CodeBuild Projects

We'll create CodeBuild projects for each service. Let's start with a few key services.

### Create User Service CodeBuild Project:

**Run this command:**

```bash
cd /Users/mitali/Desktop/MSA/ruberoo-microservices
aws codebuild create-project \
  --cli-input-json file://aws/codebuild/user-service-project.json \
  --profile ruberoo-deployment \
  --region us-east-1
```

---

## 📋 Alternative: Create All Projects via Script

I'll create a script to create all CodeBuild projects at once. But first, let's test with one service.

---

## 🎯 Recommended Approach:

**For now, let's create a simplified pipeline:**

1. **Create ONE CodeBuild project** (User Service as test)
2. **Test it manually** to verify it works
3. **Create remaining projects** once we confirm the workflow
4. **Create CodePipeline** to orchestrate everything

---

## ⚠️ Note About GitHub Source:

CodeBuild needs GitHub access. You have two options:

1. **GitHub Personal Access Token** (recommended)
   - Create a token in GitHub Settings → Developer settings → Personal access tokens
   - Store it securely (we'll use it in CodeBuild)

2. **GitHub OAuth** (for CodePipeline)
   - Connect AWS to GitHub in CodePipeline console

---

**Ready to create the first CodeBuild project?** 

Tell me: **"Create CodeBuild project"** and I'll guide you through it, or if you prefer, I can create all the necessary files and scripts first.

