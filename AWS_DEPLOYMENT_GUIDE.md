# 🚀 Ruberoo Microservices - AWS Deployment Guide

**Project:** Ruberoo Microservices Platform  
**Target:** AWS Free Tier Deployment with CI/CD  
**Date:** November 4, 2025  

---

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Step 1: IAM User Setup](#step-1-iam-user-setup)
3. [Step 2: ECR Setup](#step-2-ecr-setup) (Coming Next)
4. [Step 3: RDS Setup](#step-3-rds-setup) (Coming Next)
5. [Step 4: EKS Cluster Setup](#step-4-eks-cluster-setup) (Coming Next)
6. [Step 5: CI/CD Pipeline Setup](#step-5-cicd-pipeline-setup) (Coming Next)

---

## Prerequisites

✅ AWS Account (Free Tier eligible)  
✅ AWS CLI installed and configured  
✅ kubectl installed  
✅ Docker installed  
✅ Currently logged in as root user  

---

## Step 1: IAM User Setup

### 🎯 Objective
Create a dedicated IAM user with permissions for:
- ECR (Elastic Container Registry)
- EKS (Elastic Kubernetes Service)
- RDS (Relational Database Service)
- CodeBuild
- CodePipeline
- IAM (limited, for managing service roles)
- EC2 (for EKS node groups)
- VPC (for networking)

### ⚠️ Important Security Note
**Never use root user for daily operations!** Root user has full access and should only be used for:
- Creating the first IAM user
- Changing account settings
- Emergency access

---

## 🔐 Step 1.1: Create IAM User

### Actions Required:

1. **Open AWS Console:**
   - Go to: https://console.aws.amazon.com/iam/
   - Make sure you're in the correct AWS region (e.g., `us-east-1`)

2. **Navigate to Users:**
   - Click on "Users" in the left sidebar
   - Click "Create user" button

3. **User Details:**
   - **User name:** `ruberoo-deployment-user`
   - **AWS access type:** Select "Programmatic access" (for CLI/API)
   - ⚠️ **Do NOT select "AWS Management Console access"** (we'll use CLI/API only for security)

4. **Click "Next: Permissions"**

---

## 🔐 Step 1.2: Attach Policies

### Option A: Attach AWS Managed Policies (Recommended for Free Tier)

We'll attach these AWS managed policies:

**Required Policies:**
1. **AmazonEC2ContainerRegistryFullAccess** - For ECR
2. **AmazonEKSClusterPolicy** - For EKS cluster management
3. **AmazonEKSWorkerNodePolicy** - For EKS node groups
4. **AmazonRDSFullAccess** - For RDS (we'll restrict later)
5. **AWSCodeBuildDeveloperAccess** - For CodeBuild
6. **AWSCodePipelineFullAccess** - For CodePipeline
7. **IAMFullAccess** - For creating service roles (⚠️ we'll restrict this later)

**Steps:**
1. Click "Attach policies directly"
2. Search and select each policy above
3. ⚠️ **Note:** `IAMFullAccess` is needed temporarily to create service roles, but we'll create a more restricted policy later

### Option B: Create Custom Policy (More Secure - Recommended After Setup)

We'll create a custom policy with minimal permissions after initial setup.

**For now, proceed with Option A** (we'll refine permissions in Step 1.4)

---

## 🔐 Step 1.3: Review and Create

1. **Review:**
   - User name: `ruberoo-deployment-user`
   - Access type: Programmatic access
   - Policies: All listed above

2. **Click "Create user"**

3. **⚠️ CRITICAL: Save Credentials Immediately!**
   - **Access Key ID:** Copy and save securely
   - **Secret Access Key:** Copy and save securely (shown only once!)
   - ⚠️ **If you lose the Secret Access Key, you'll need to create a new one**

4. **Download CSV** (recommended) or store in password manager

---

## 🔐 Step 1.4: Configure AWS CLI with New User

### Actions Required:

1. **Open Terminal/Command Prompt**

2. **Configure AWS CLI:**
   ```bash
   aws configure --profile ruberoo-deployment
   ```

3. **Enter credentials when prompted:**
   ```
   AWS Access Key ID [None]: <paste-your-access-key-id>
   AWS Secret Access Key [None]: <paste-your-secret-access-key>
   Default region name [None]: us-east-1
   Default output format [None]: json
   ```

4. **Verify configuration:**
   ```bash
   aws sts get-caller-identity --profile ruberoo-deployment
   ```
   
   You should see:
   ```json
   {
       "UserId": "...",
       "Account": "...",
       "Arn": "arn:aws:iam::ACCOUNT:user/ruberoo-deployment-user"
   }
   ```

5. **Set as default profile (optional):**
   ```bash
   export AWS_PROFILE=ruberoo-deployment
   ```

---

## 🔐 Step 1.5: Test Permissions

### Test ECR Access:
```bash
aws ecr describe-repositories --profile ruberoo-deployment
```

### Test EKS Access:
```bash
aws eks list-clusters --profile ruberoo-deployment
```

### Test RDS Access:
```bash
aws rds describe-db-instances --profile ruberoo-deployment
```

**Expected:** All commands should work without errors (may return empty lists, which is fine)

---

## 🔐 Step 1.6: Create More Restricted IAM Policy (Optional - Do Later)

After initial deployment, we'll create a custom policy with minimal required permissions to follow least privilege principle.

**For now, proceed with the managed policies.**

---

## ✅ Step 1 Complete Checklist

- [ ] IAM user `ruberoo-deployment-user` created
- [ ] All required policies attached
- [ ] Access Key ID saved securely
- [ ] Secret Access Key saved securely
- [ ] AWS CLI configured with new profile
- [ ] Permissions tested successfully
- [ ] Can see account ARN matches new user

---

## 🎯 Next Steps (After Confirmation)

Once you confirm Step 1 is complete, we'll proceed to:
- **Step 2:** ECR Setup (Create container registries for each service)
- **Step 3:** RDS Setup (Create MySQL database)
- **Step 4:** EKS Cluster Setup (Create Kubernetes cluster)
- **Step 5:** CI/CD Pipeline Setup (CodeBuild + CodePipeline)

---

## 📝 Notes

- **Region:** Using `us-east-1` (N. Virginia) - Free tier eligible
- **Free Tier Limits:**
  - ECR: 500 MB storage, 0.5 GB/month data transfer
  - EKS: Not free tier eligible (we'll monitor costs)
  - RDS: 750 hours/month t2.micro instance
  - CodeBuild: 100 build minutes/month
  - CodePipeline: Free for first 30 days

- **Cost Optimization:**
  - EKS charges ~$0.10/hour (~$72/month) - Consider using ECS Fargate instead if budget is tight
  - We'll discuss alternatives if needed

---

## ⚠️ Important Reminders

1. **Never commit AWS credentials to Git**
2. **Use IAM roles for EC2/EKS instances** (not access keys)
3. **Enable MFA** for root user
4. **Review CloudTrail logs** regularly
5. **Set up billing alerts** in AWS Billing Dashboard

---

**Ready to proceed?** Complete Step 1 and let me know when you're ready for Step 2!



