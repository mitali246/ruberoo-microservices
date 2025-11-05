# 🗄️ Step 3: RDS MySQL Database Setup

## 🎯 Objective
Create a MySQL RDS instance for Ruberoo microservices with Free Tier configuration.

---

## 📋 Prerequisites Check

Before creating RDS, we need:
- ✅ Default VPC information
- ✅ Subnet IDs for database subnet group
- ✅ Security group for database access

---

## 🔧 Step 3.1: Create Security Group for RDS

First, we need to create a security group that allows:
- MySQL access (port 3306) from EKS cluster
- MySQL access from your local machine (for testing)

### Action Required:

**Run this command to create the security group:**

```bash
aws ec2 create-security-group \
  --group-name ruberoo-rds-sg \
  --description "Security group for Ruberoo RDS MySQL database" \
  --vpc-id vpc-xxxxx \
  --profile ruberoo-deployment \
  --region us-east-1
```

**Note:** Replace `vpc-xxxxx` with your actual default VPC ID (we'll get this in the next step).

---

## 🔧 Step 3.2: Configure Security Group Rules

After creating the security group, we need to add rules:

### Rule 1: Allow MySQL from EKS (we'll add this later when EKS is ready)
### Rule 2: Allow MySQL from your IP (for testing)

**Get your public IP first:**
```bash
curl -s https://checkip.amazonaws.com
```

**Then add inbound rule:**
```bash
aws ec2 authorize-security-group-ingress \
  --group-id sg-xxxxx \
  --protocol tcp \
  --port 3306 \
  --cidr YOUR_PUBLIC_IP/32 \
  --profile ruberoo-deployment \
  --region us-east-1
```

---

## 🔧 Step 3.3: Create DB Subnet Group

RDS needs a subnet group for high availability:

```bash
aws rds create-db-subnet-group \
  --db-subnet-group-name ruberoo-db-subnet-group \
  --db-subnet-group-description "Subnet group for Ruberoo RDS" \
  --subnet-ids subnet-xxx subnet-yyy subnet-zzz \
  --profile ruberoo-deployment \
  --region us-east-1
```

**Note:** Replace subnet IDs with your default VPC subnet IDs (we'll get these).

---

## 🔧 Step 3.4: Create RDS MySQL Instance

### Configuration Details:

- **Instance Identifier:** `ruberoo-mysql`
- **Instance Class:** `db.t3.micro` (Free Tier eligible)
- **Engine:** MySQL 8.0
- **Master Username:** `admin` (or your choice)
- **Master Password:** `[Choose a strong password]`
- **Storage:** 20 GB (Free Tier: 20 GB)
- **Backup Retention:** 7 days
- **Public Access:** Yes (for testing, can be restricted later)

### Action Required:

**Run this command** (replace placeholders with actual values):

```bash
aws rds create-db-instance \
  --db-instance-identifier ruberoo-mysql \
  --db-instance-class db.t3.micro \
  --engine mysql \
  --engine-version 8.0.35 \
  --master-username admin \
  --master-user-password YOUR_SECURE_PASSWORD \
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
- Replace `YOUR_SECURE_PASSWORD` with a strong password (save it securely!)
- Replace `sg-xxxxx` with your security group ID
- Password must be 8-41 characters, include uppercase, lowercase, numbers, and special characters

---

## ⏳ Step 3.5: Wait for RDS to be Available

**RDS takes 10-15 minutes to provision.**

**Check status:**
```bash
aws rds describe-db-instances \
  --db-instance-identifier ruberoo-mysql \
  --profile ruberoo-deployment \
  --region us-east-1 \
  --query 'DBInstances[0].[DBInstanceStatus,Endpoint.Address,Endpoint.Port]' \
  --output table
```

**Status should be:** `available` (not `creating`)

---

## ✅ Step 3.6: Get Connection Details

Once RDS is available, get the endpoint:

```bash
aws rds describe-db-instances \
  --db-instance-identifier ruberoo-mysql \
  --profile ruberoo-deployment \
  --region us-east-1 \
  --query 'DBInstances[0].Endpoint.Address' \
  --output text
```

**Save this endpoint!** It will look like: `ruberoo-mysql.xxxxx.us-east-1.rds.amazonaws.com`

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

Once connected, create databases for your services:

```sql
CREATE DATABASE ruberoo_user_db;
CREATE DATABASE ruberoo_ride_db;
CREATE DATABASE ruberoo_tracking_db;
SHOW DATABASES;
EXIT;
```

---

## ✅ Step 3 Complete Checklist:

- [ ] Security group created
- [ ] Security group rules configured
- [ ] DB subnet group created
- [ ] RDS instance created and provisioning
- [ ] RDS status is "available"
- [ ] Connection endpoint saved
- [ ] Connection tested successfully
- [ ] Databases created (user_db, ride_db, tracking_db)

---

## 🎯 What's Next?

After RDS is ready:
1. ✅ Test ECR image push (quick test)
2. ✅ Update application configs with RDS endpoint
3. ✅ Proceed to Step 4: EKS Setup

---

## ⚠️ Important Notes:

1. **Free Tier Limits:**
   - 750 hours/month of db.t3.micro
   - 20 GB storage
   - 20 GB backup storage

2. **Cost Monitoring:**
   - RDS charges only when instance is running
   - Stop instance when not in use to save costs
   - Monitor in AWS Billing Dashboard

3. **Security:**
   - Password stored securely (not in Git)
   - Security group restricts access
   - Can restrict to VPC-only later

---

**Ready to start?** Let me get your VPC information first, then we'll create the RDS instance step by step!

