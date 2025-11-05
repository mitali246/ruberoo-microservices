# 🔐 Step 1: IAM User Setup - Detailed Instructions

**Current Status:** You are logged in as **root user**  
**Goal:** Create IAM user `ruberoo-deployment-user` with deployment permissions  

---

## 📋 Quick Checklist

Follow these steps in order. **Do not proceed to next step until current step is complete.**

- [ ] 1.1 - Create IAM User
- [ ] 1.2 - Attach Policies
- [ ] 1.3 - Save Credentials
- [ ] 1.4 - Configure AWS CLI
- [ ] 1.5 - Test Permissions
- [ ] Ready for Step 2 ✅

---

## 🔐 Step 1.1: Create IAM User in AWS Console

### Detailed Steps:

1. **Open AWS Console:**
   - URL: https://console.aws.amazon.com/iam/
   - Make sure you're logged in as **root user**
   - Verify region in top-right corner (choose `us-east-1` recommended)

2. **Navigate to Users:**
   - In left sidebar, click **"Users"**
   - Click orange button **"Create user"** (top right)

3. **Enter User Details:**
   - **User name:** Type exactly: `ruberoo-deployment-user`
   - **AWS access type:** 
     - ✅ Check **"Programmatic access"** (this enables Access Keys)
     - ❌ **Do NOT check** "AWS Management Console access" (we'll use CLI only)
   
4. **Click "Next: Permissions"** button (bottom right)

---

## 🔐 Step 1.2: Attach Required Policies

### Policy Selection:

You need to attach **7 AWS Managed Policies**. Search for each one and check the box:

#### Required Policies (attach all):

1. **AmazonEC2ContainerRegistryFullAccess**
   - Search: `ECR` or `Container Registry`
   - Check the box for: `AmazonEC2ContainerRegistryFullAccess`

2. **AmazonEKSClusterPolicy**
   - Search: `EKS Cluster`
   - Check the box for: `AmazonEKSClusterPolicy`

3. **AmazonEKSWorkerNodePolicy**
   - Search: `EKS Worker`
   - Check the box for: `AmazonEKSWorkerNodePolicy`

4. **AmazonRDSFullAccess**
   - Search: `RDS Full`
   - Check the box for: `AmazonRDSFullAccess`

5. **AWSCodeBuildDeveloperAccess**
   - Search: `CodeBuild`
   - Check the box for: `AWSCodeBuildDeveloperAccess`

6. **AWSCodePipelineFullAccess**
   - Search: `CodePipeline`
   - Check the box for: `AWSCodePipelineFullAccess`

7. **IAMFullAccess** ⚠️ (Temporary - needed for service roles)
   - Search: `IAM Full`
   - Check the box for: `IAMFullAccess`
   - **Note:** We'll restrict this later after creating service roles

### After Selecting All Policies:

- You should see **7 policies** checked
- Click **"Next: Tags"** button (bottom right)

---

## 🔐 Step 1.3: Skip Tags and Create User

1. **Tags Section:**
   - Click **"Next: Review"** (tags are optional, skip for now)

2. **Review Page:**
   - Verify:
     - User name: `ruberoo-deployment-user`
     - Access type: Programmatic access
     - Policies: 7 policies attached
   
3. **Click "Create user"** button (bottom right)

4. **⚠️ CRITICAL: Save Credentials IMMEDIATELY!**
   
   You'll see a **success page** with:
   
   **Access Key ID:** `AKIA...` (starts with AKIA)
   - Click the **copy icon** or manually copy
   - Save to secure location (password manager, secure file)
   
   **Secret Access Key:** `wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY` (long string)
   - Click **"Show"** to reveal
   - Click **copy icon** or manually copy
   - ⚠️ **THIS IS SHOWN ONLY ONCE!** Save immediately!
   
   **Download CSV:**
   - Click **"Download .csv"** button
   - Save file securely (do NOT commit to Git!)
   - File contains: Access Key ID, Secret Access Key, Console login link

5. **Click "Close"** after saving credentials

---

## 🔐 Step 1.4: Configure AWS CLI

### Prerequisites Check:

```bash
# Check if AWS CLI is installed
aws --version

# Should show: aws-cli/2.x.x or aws-cli/1.x.x
```

If not installed, install AWS CLI first:
- macOS: `brew install awscli`
- Linux: `sudo apt-get install awscli` or `sudo yum install awscli`
- Windows: Download from AWS website

### Configure Profile:

Open terminal in your project directory:

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

### Test ECR:
```bash
aws ecr describe-repositories --profile ruberoo-deployment
```
**Expected:** Empty list `{"repositories": []}` or no error ✅

### Test EKS:
```bash
aws eks list-clusters --profile ruberoo-deployment
```
**Expected:** Empty list `{"clusters": []}` or no error ✅

### Test RDS:
```bash
aws rds describe-db-instances --profile ruberoo-deployment
```
**Expected:** Empty list `{"DBInstances": []}` or no error ✅

### Test CodeBuild:
```bash
aws codebuild list-projects --profile ruberoo-deployment
```
**Expected:** Empty list `{"projects": []}` or no error ✅

### Test CodePipeline:
```bash
aws codepipeline list-pipelines --profile ruberoo-deployment
```
**Expected:** Empty list `{"pipelines": []}` or no error ✅

---

## ✅ Step 1 Complete

### Final Checklist:

- [ ] IAM user `ruberoo-deployment-user` created in AWS Console
- [ ] 7 policies attached (ECR, EKS Cluster, EKS Worker, RDS, CodeBuild, CodePipeline, IAM)
- [ ] Access Key ID saved securely
- [ ] Secret Access Key saved securely
- [ ] CSV file downloaded and saved
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
   - Add `.aws/credentials` to `.gitignore`
   - Never push CSV file to repository

2. **Use IAM Roles (not access keys) for:**
   - EC2 instances
   - EKS nodes
   - Lambda functions
   - CodeBuild projects

3. **Rotate credentials regularly:**
   - Change access keys every 90 days
   - Delete unused access keys

4. **Enable MFA for root user:**
   - Go to IAM → Users → root user → Security credentials
   - Enable MFA

---

## 🆘 Troubleshooting

### "Access Denied" Errors:
- Verify policies are attached correctly
- Check you're using correct profile: `--profile ruberoo-deployment`
- Ensure Access Key ID and Secret Access Key are correct

### AWS CLI Not Found:
- Install AWS CLI first
- Verify installation: `aws --version`

### Wrong Region:
- Always specify `us-east-1` for Free Tier
- Check region in AWS Console top-right corner

---

**Ready?** Complete all checklist items above, then type **"Step 1 complete"** or **"Ready for Step 2"** and I'll proceed!



