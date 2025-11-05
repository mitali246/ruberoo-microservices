# 🚀 AWS Deployment - Step by Step Guide

**Current Status:** Starting AWS Deployment  
**Mode:** Slow and Methodical (one step at a time)  
**Target:** AWS Free Tier Compatible Deployment

---

## 📋 Step 1: IAM User Setup

Since you're logged in as **root user**, we need to create a dedicated IAM user for deployment.

### ⚠️ Why IAM User?
- **Security Best Practice:** Never use root user for daily operations
- **Audit Trail:** IAM users provide better logging
- **Permission Control:** Fine-grained access control
- **Free Tier:** No additional cost

---

## 🔐 Step 1.1: Create IAM User in AWS Console

### Action Required:

1. **Open AWS IAM Console:**
   - Go to: https://console.aws.amazon.com/iam/
   - Make sure you're logged in as **root user**
   - Verify region is set to **`us-east-1`** (top-right corner) - Best for Free Tier

2. **Navigate to Users:**
   - Click **"Users"** in the left sidebar
   - Click orange **"Create user"** button (top right)

3. **Enter User Details:**
   - **User name:** Type exactly: `ruberoo-deployment-user`
   - **AWS access type:** 
     - ✅ Check **"Programmatic access"** (enables Access Keys for CLI)
     - ❌ **Do NOT check** "AWS Management Console access"

4. **Click "Next: Permissions"** (bottom right)

---

## 🔐 Step 1.2: Attach Required Policies

### Action Required:

You need to attach **7 AWS Managed Policies**. Search for each one:

#### Policy List (attach all 7):

1. **AmazonEC2ContainerRegistryFullAccess**
   - Search: `ECR` or `Container Registry`
   - Check the box ✅

2. **AmazonEKSClusterPolicy**
   - Search: `EKS Cluster`
   - Check the box ✅

3. **AmazonEKSWorkerNodePolicy**
   - Search: `EKS Worker`
   - Check the box ✅

4. **AmazonRDSFullAccess**
   - Search: `RDS Full`
   - Check the box ✅

5. **AWSCodeBuildDeveloperAccess**
   - Search: `CodeBuild`
   - Check the box ✅

6. **AWSCodePipelineFullAccess**
   - Search: `CodePipeline`
   - Check the box ✅

7. **IAMFullAccess** ⚠️ (Temporary - needed for service roles)
   - Search: `IAM Full`
   - Check the box ✅
   - **Note:** We'll restrict this later after creating service roles

### After Selecting All 7 Policies:

- Verify you see **7 policies** checked
- Click **"Next: Tags"** (bottom right)
- On Tags page, click **"Next: Review"** (tags optional)
- On Review page, verify everything, then click **"Create user"**

---

## 🔐 Step 1.3: Save Credentials ⚠️ CRITICAL

### Action Required:

After clicking "Create user", you'll see a **success page** with credentials:

### ⚠️ THIS IS SHOWN ONLY ONCE! Save immediately!

1. **Access Key ID:** `AKIA...` (starts with AKIA)
   - Click the **copy icon** or manually copy
   - Save to secure location (password manager, secure file)

2. **Secret Access Key:** Long string (hidden by default)
   - Click **"Show"** to reveal
   - Click **copy icon** or manually copy
   - ⚠️ **THIS IS SHOWN ONLY ONCE!** Save immediately!

3. **Download CSV:**
   - Click **"Download .csv"** button
   - Save file securely (do NOT commit to Git!)
   - Contains: Access Key ID, Secret Access Key, Console login link

4. **Click "Close"** after saving credentials

---

## 🔐 Step 1.4: Configure AWS CLI

### Prerequisites Check:

```bash
# Check if AWS CLI is installed
aws --version
```

If not installed:
- **macOS:** `brew install awscli`
- **Linux:** `sudo apt-get install awscli` or `sudo yum install awscli`
- **Windows:** Download from AWS website

### Configure Profile:

```bash
cd /Users/mitali/Desktop/MSA/ruberoo-microservices

# Configure AWS CLI with new user
aws configure --profile ruberoo-deployment
```

**Enter when prompted:**

1. **AWS Access Key ID:** Paste your Access Key ID (from Step 1.3)
2. **AWS Secret Access Key:** Paste your Secret Access Key (from Step 1.3)
3. **Default region name:** Type `us-east-1`
4. **Default output format:** Type `json`

### Verify Configuration:

```bash
# Check configured identity
aws sts get-caller-identity --profile ruberoo-deployment
```

**Expected Output:**
```json
{
    "UserId": "AIDA...",
    "Account": "123456789012",
    "Arn": "arn:aws:iam::123456789012:user/ruberoo-deployment-user"
}
```

✅ **If you see this, configuration is correct!**

---

## 🔐 Step 1.5: Test Permissions

Run these commands to verify permissions:

```bash
# Test ECR
aws ecr describe-repositories --profile ruberoo-deployment

# Test EKS
aws eks list-clusters --profile ruberoo-deployment

# Test RDS
aws rds describe-db-instances --profile ruberoo-deployment

# Test CodeBuild
aws codebuild list-projects --profile ruberoo-deployment

# Test CodePipeline
aws codepipeline list-pipelines --profile ruberoo-deployment
```

**Expected:** Empty lists `{"repositories": []}` or no errors ✅

---

## ✅ Step 1 Complete Checklist

Before proceeding, verify:

- [ ] IAM user `ruberoo-deployment-user` created in AWS Console
- [ ] 7 policies attached (ECR, EKS Cluster, EKS Worker, RDS, CodeBuild, CodePipeline, IAM)
- [ ] Access Key ID saved securely
- [ ] Secret Access Key saved securely
- [ ] CSV file downloaded and saved securely
- [ ] AWS CLI configured with `ruberoo-deployment` profile
- [ ] Identity verified (`get-caller-identity` shows correct user)
- [ ] All permission tests passed (no errors)

---

## 🎯 What's Next?

Once you confirm Step 1 is complete, we'll proceed to:

**Step 2: ECR Setup**
- Create container registries for each microservice
- Configure Docker to push images to ECR
- Set up image naming conventions

---

## ⚠️ Important Security Notes

1. **Never commit credentials to Git:**
   - `.aws/credentials` is already in `.gitignore`
   - Never push CSV file to repository

2. **Use IAM Roles** (not access keys) for:
   - EC2 instances
   - EKS nodes
   - Lambda functions
   - CodeBuild projects

3. **Rotate credentials regularly:**
   - Change access keys every 90 days
   - Delete unused access keys

---

## 🆘 Troubleshooting

### "Access Denied" Errors:
- Verify policies are attached correctly in AWS Console
- Check you're using correct profile: `--profile ruberoo-deployment`
- Ensure Access Key ID and Secret Access Key are correct

### AWS CLI Not Found:
- Install AWS CLI first
- Verify installation: `aws --version`

### Wrong Region:
- Always specify `us-east-1` for Free Tier
- Check region in AWS Console top-right corner

---

**Ready?** Complete all checklist items above, then tell me **"Step 1 complete"** or **"Ready for Step 2"** and I'll proceed!

