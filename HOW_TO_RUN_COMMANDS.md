# 💻 How to Run AWS Commands

## Where to Run Commands:

**All commands should be run in your Terminal/Command Line.**

---

## 🖥️ Step-by-Step Instructions:

### On macOS (which you're using):

1. **Open Terminal:**
   - Press `Command + Space` to open Spotlight
   - Type: `Terminal`
   - Press Enter
   - OR
   - Go to Applications → Utilities → Terminal

2. **Navigate to Your Project Directory:**
   ```bash
   cd /Users/mitali/Desktop/MSA/ruberoo-microservices
   ```

3. **Verify You're in the Right Directory:**
   ```bash
   pwd
   ```
   Should show: `/Users/mitali/Desktop/MSA/ruberoo-microservices`

4. **Run the AWS Command:**
   ```bash
   aws rds create-db-instance \
     --db-instance-identifier ruberoo-mysql \
     --db-instance-class db.t3.micro \
     --engine mysql \
     --engine-version 8.0.35 \
     --master-username admin \
     --master-user-password "YOUR_SECURE_PASSWORD" \
     --allocated-storage 20 \
     --storage-type gp2 \
     --vpc-security-group-ids sg-0ad197107d8b310a0 \
     --db-subnet-group-name ruberoo-db-subnet-group \
     --backup-retention-period 7 \
     --publicly-accessible \
     --storage-encrypted \
     --region us-east-1 \
     --profile ruberoo-deployment
   ```

---

## 📝 Quick Copy-Paste Guide:

1. **Open Terminal** (Command + Space → "Terminal")

2. **Copy and paste this to navigate:**
   ```bash
   cd /Users/mitali/Desktop/MSA/ruberoo-microservices
   ```

3. **Copy and paste the RDS creation command** (replace `YOUR_SECURE_PASSWORD` with your actual password)

4. **Press Enter** to run the command

---

## ✅ What You Should See:

After running the command, you should see output like:
```json
{
    "DBInstance": {
        "DBInstanceIdentifier": "ruberoo-mysql",
        "DBInstanceStatus": "creating",
        ...
    }
}
```

This means RDS is starting to create!

---

## 🆘 Troubleshooting:

### "Command not found: aws"
- Make sure AWS CLI is installed
- Check with: `aws --version`

### "Profile not found"
- Make sure you configured AWS CLI with: `aws configure --profile ruberoo-deployment`
- Check: `aws sts get-caller-identity --profile ruberoo-deployment`

### "Permission denied"
- Make sure you're using the correct profile: `--profile ruberoo-deployment`

---

## 💡 Pro Tip:

You can also use VS Code's integrated terminal:
1. Open VS Code
2. Press `` Ctrl + ` `` (backtick) or go to Terminal → New Terminal
3. The terminal will open in your project directory
4. Run commands there

---

**Ready?** Open Terminal and run the command!

