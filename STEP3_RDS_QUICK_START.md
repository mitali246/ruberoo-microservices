# 🚀 Step 3: RDS Setup - Quick Start Guide

## ✅ Your AWS Environment:
- **VPC ID:** `vpc-00fdde9604a8b018a` (Default VPC)
- **Default Security Group:** `sg-08e305f797d3c3e1b`
- **Subnets:** 6 subnets across availability zones
- **Region:** `us-east-1`

---

## 🎯 Quick Start: Create RDS MySQL Instance

### Option A: Automated Script (Recommended)

I've created a script that does everything for you. **Run this command:**

```bash
cd /Users/mitali/Desktop/MSA/ruberoo-microservices
chmod +x STEP3_RDS_CREATE.sh
./STEP3_RDS_CREATE.sh
```

**The script will:**
1. ✅ Create security group for RDS
2. ✅ Configure security group rules
3. ✅ Create DB subnet group
4. ✅ Create RDS MySQL instance
5. ✅ Prompt you for a secure password

---

### Option B: Manual Step-by-Step Commands

If you prefer to run commands manually, follow these steps:

#### Step 3.1: Create Security Group

```bash
aws ec2 create-security-group \
  --group-name ruberoo-rds-sg \
  --description "Security group for Ruberoo RDS MySQL database" \
  --vpc-id vpc-00fdde9604a8b018a \
  --profile ruberoo-deployment \
  --region us-east-1
```

**Save the GroupId from the output!** (It will look like `sg-xxxxx`)

#### Step 3.2: Add Inbound Rule

```bash
# Replace sg-xxxxx with the GroupId from Step 3.1
aws ec2 authorize-security-group-ingress \
  --group-id sg-xxxxx \
  --protocol tcp \
  --port 3306 \
  --source-group sg-08e305f797d3c3e1b \
  --profile ruberoo-deployment \
  --region us-east-1
```

#### Step 3.3: Create DB Subnet Group

```bash
aws rds create-db-subnet-group \
  --db-subnet-group-name ruberoo-db-subnet-group \
  --db-subnet-group-description "Subnet group for Ruberoo RDS MySQL" \
  --subnet-ids subnet-02cd5a1d25a3db602 subnet-062aaa3fc859e5557 \
  --profile ruberoo-deployment \
  --region us-east-1
```

#### Step 3.4: Create RDS Instance

```bash
aws rds create-db-instance \
  --db-instance-identifier ruberoo-mysql \
  --db-instance-class db.t3.micro \
  --engine mysql \
  --engine-version 8.0.35 \
  --master-username admin \
  --master-user-password "YOUR_SECURE_PASSWORD_HERE" \
  --allocated-storage 20 \
  --storage-type gp2 \
  --vpc-security-group-ids sg-xxxxx \
  --db-subnet-group-name ruberoo-db-subnet-group \
  --backup-retention-period 7 \
  --publicly-accessible \
  --storage-encrypted \
  --region us-east-1 \
  --profile ruberoo-deployment
```

**⚠️ Important:**
- Replace `YOUR_SECURE_PASSWORD_HERE` with a strong password
- Replace `sg-xxxxx` with your security group ID from Step 3.1
- Password requirements: 8-41 characters, mix of uppercase, lowercase, numbers, and special characters
- **Save the password securely!**

---

## ⏳ Step 3.5: Wait and Monitor

**RDS takes 10-15 minutes to provision.**

**Check status:**
```bash
aws rds describe-db-instances \
  --db-instance-identifier ruberoo-mysql \
  --profile ruberoo-deployment \
  --region us-east-1 \
  --query 'DBInstances[0].[DBInstanceStatus,Endpoint.Address]' \
  --output table
```

**Wait until status is:** `available`

---

## ✅ Step 3.6: Get Connection Details

Once status is `available`, get the endpoint:

```bash
aws rds describe-db-instances \
  --db-instance-identifier ruberoo-mysql \
  --profile ruberoo-deployment \
  --region us-east-1 \
  --query 'DBInstances[0].Endpoint.Address' \
  --output text
```

**Save this endpoint!** Example: `ruberoo-mysql.xxxxx.us-east-1.rds.amazonaws.com`

---

## 🧪 Step 3.7: Test Connection

Test from your local machine:

```bash
mysql -h ruberoo-mysql.xxxxx.us-east-1.rds.amazonaws.com \
  -u admin \
  -p \
  -P 3306
```

Enter your password when prompted.

---

## 📝 Step 3.8: Create Databases

Once connected, create databases:

```sql
CREATE DATABASE ruberoo_user_db;
CREATE DATABASE ruberoo_ride_db;
CREATE DATABASE ruberoo_tracking_db;
SHOW DATABASES;
EXIT;
```

---

## 🎯 My Recommendation:

**Use Option A (Automated Script)** - It's faster and handles everything automatically!

**Ready to proceed?** Run the script:

```bash
cd /Users/mitali/Desktop/MSA/ruberoo-microservices
chmod +x STEP3_RDS_CREATE.sh
./STEP3_RDS_CREATE.sh
```

Tell me when you've run it, and I'll help you monitor the RDS creation!

