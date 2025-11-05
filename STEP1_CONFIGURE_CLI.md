# 🔧 Step 1.4: Configure AWS CLI

## Current Status:
✅ IAM User Created: `ruberoo-deployment-user`  
✅ Access Key ID: `AKIAQDX2KYVQJHCEWSOJ`  
✅ AWS CLI Installed: Version 2.31.29

---

## ⚠️ IMPORTANT: Do You Have the Secret Access Key?

The **Secret Access Key** is a long string that was shown **ONLY ONCE** when you created the access key.

**You need BOTH:**
- ✅ Access Key ID: `AKIAQDX2KYVQJHCEWSOJ` (you have this)
- ❓ Secret Access Key: `wJalrXUtn...` (long string - do you have this?)

---

## If You DON'T Have the Secret Access Key:

You need to create a new access key:

1. **On the AWS Console page you're on** (Security credentials tab)
2. **Scroll down to "Access keys" section**
3. **Look for "Access key 1"** - you should see your Access Key ID there
4. **Click "Create access key"** button (or "Add access key" if you see it)
5. **Select "CLI, SDK, & API access"** as the use case
6. **Click "Next"** then **"Create access key"**
7. **⚠️ CRITICAL:** Copy and save BOTH:
   - Access Key ID
   - Secret Access Key (shown only once!)
8. **Download the CSV file** (recommended)
9. **Click "Done"**

---

## If You DO Have the Secret Access Key:

Perfect! Let's configure AWS CLI now.

---

## Step 1.4: Configure AWS CLI

### Action Required:

Open your terminal and run this command:

```bash
cd /Users/mitali/Desktop/MSA/ruberoo-microservices
aws configure --profile ruberoo-deployment
```

### What to Enter When Prompted:

1. **AWS Access Key ID:** 
   ```
   AKIAQDX2KYVQJHCEWSOJ
   ```
   (Paste your Access Key ID)

2. **AWS Secret Access Key:** 
   ```
   [Paste your Secret Access Key here]
   ```
   (Paste the long Secret Access Key string)

3. **Default region name:** 
   ```
   us-east-1
   ```
   (Type exactly: `us-east-1`)

4. **Default output format:** 
   ```
   json
   ```
   (Type exactly: `json`)

---

## Step 1.5: Verify Configuration

After configuring, test it with:

```bash
aws sts get-caller-identity --profile ruberoo-deployment
```

**Expected Output:**
```json
{
    "UserId": "AIDA...",
    "Account": "008041186656",
    "Arn": "arn:aws:iam::008041186656:user/ruberoo-deployment-user"
}
```

✅ **If you see this, your configuration is correct!**

---

## 🎯 What's Next?

Once you've configured AWS CLI and verified it works, tell me:
- **"AWS CLI configured successfully"** or
- **"I need help creating a new access key"**

Then we'll proceed to:
- Step 1.6: Test Permissions
- Step 2: ECR Setup (Create container registries)

