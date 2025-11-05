# 🔑 Step 5.1: GitHub Personal Access Token Setup

## 🎯 Why We Need This:

CodeBuild needs access to your GitHub repository to:
- Clone the repository
- Build from source code
- Access buildspec files

---

## 📋 Create GitHub Personal Access Token

### Step 1: Go to GitHub Settings

1. **Open GitHub:** https://github.com
2. **Click your profile picture** (top right)
3. **Click "Settings"**
4. **Scroll down** → Click **"Developer settings"** (left sidebar)
5. **Click "Personal access tokens"** → **"Tokens (classic)"**
6. **Click "Generate new token"** → **"Generate new token (classic)"**

### Step 2: Configure Token

**Token Settings:**
- **Note:** `Ruberoo AWS CodeBuild`
- **Expiration:** `90 days` (or your preference)
- **Scopes:** Check these:
  - ✅ `repo` (Full control of private repositories)
    - This includes: `repo:status`, `repo_deployment`, `public_repo`, `repo:invite`, `security_events`

### Step 3: Generate and Save

1. **Click "Generate token"** (bottom of page)
2. **⚠️ COPY THE TOKEN IMMEDIATELY!** (You won't see it again!)
3. **Save it securely** (password manager, secure file)

**Token format:** `ghp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx`

---

## 🔐 Step 2: Store Token in AWS Secrets Manager (Recommended)

**Store the token securely in AWS:**

```bash
aws secretsmanager create-secret \
  --name ruberoo/github/token \
  --secret-string "YOUR_GITHUB_TOKEN_HERE" \
  --profile ruberoo-deployment \
  --region us-east-1
```

**Replace `YOUR_GITHUB_TOKEN_HERE` with your actual token!**

---

## ✅ Alternative: Use Token Directly in CodeBuild

You can also pass the token directly when creating CodeBuild projects (less secure, but simpler for testing).

---

**After you create the token, tell me: "GitHub token created"** and I'll help you set it up in AWS!

---

## 🔍 Quick Checklist:

- [ ] GitHub token created
- [ ] Token saved securely
- [ ] Token has `repo` scope
- [ ] Ready to use in CodeBuild

