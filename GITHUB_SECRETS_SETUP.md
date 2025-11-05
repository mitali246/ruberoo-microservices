# 🔐 GitHub Secrets Setup - Step by Step

## ⚠️ Important: You Need AWS Credentials, Not GitHub Token

For GitHub Actions to deploy to AWS, you need **AWS credentials**, not a GitHub token.

---

## ✅ Step-by-Step Instructions

### 1. Go to GitHub Secrets Page

Open: https://github.com/mitali246/ruberoo-microservices/settings/secrets/actions

### 2. Add First Secret: AWS_ACCESS_KEY_ID

1. Click **"New repository secret"**
2. **Name:** `AWS_ACCESS_KEY_ID`
3. **Value:** Your AWS access key (from `ruberoo-deployment-user`)
   - Format: `AKIA...` (starts with AKIA)
   - You can find it in: `~/.aws/credentials` or AWS Console → IAM → Users → ruberoo-deployment-user → Security credentials
4. Click **"Add secret"**

### 3. Add Second Secret: AWS_SECRET_ACCESS_KEY

1. Click **"New repository secret"** again
2. **Name:** `AWS_SECRET_ACCESS_KEY`
3. **Value:** Your AWS secret access key (from `ruberoo-deployment-user`)
   - Format: Long string of random characters
   - Same location as above
4. Click **"Add secret"**

---

## 🔍 How to Find Your AWS Credentials

### Option A: From AWS CLI Config

```bash
cat ~/.aws/credentials
```

Look for the `[ruberoo-deployment]` section:
```
[ruberoo-deployment]
aws_access_key_id = AKIA...
aws_secret_access_key = ...
```

### Option B: From AWS Console

1. Go to: https://console.aws.amazon.com/iam/
2. Click **Users** → **ruberoo-deployment-user**
3. Click **Security credentials** tab
4. Under **Access keys**, you'll see your access key ID
5. To see the secret key, you need to create a new one (or use the one you saved when creating the user)

---

## 📝 About GitHub Tokens

**Note:** GitHub tokens should never be committed to code repositories. They should only be stored in:
- GitHub Secrets (for GitHub Actions)
- AWS Secrets Manager (for AWS services)
- Environment variables (for local development)

For this workflow, we don't need a GitHub token - we only need AWS credentials.

---

## ✅ Verification

After adding both AWS secrets, you should see:
- ✅ `AWS_ACCESS_KEY_ID`
- ✅ `AWS_SECRET_ACCESS_KEY`

Then you're ready to push and deploy! 🚀

---

## 🚀 Next Steps

After adding secrets:
1. Run: `./deploy-to-eks.sh`
2. Push to GitHub: `git push origin main`
3. Watch GitHub Actions deploy automatically!

