#!/bin/bash
# Step 3: RDS MySQL Setup Script
# This script creates the RDS MySQL instance for Ruberoo microservices

set -e

PROFILE="ruberoo-deployment"
REGION="us-east-1"
VPC_ID="vpc-00fdde9604a8b018a"
DEFAULT_SG="sg-08e305f797d3c3e1b"

# Subnets for DB subnet group (at least 2 in different AZs)
SUBNET_1="subnet-02cd5a1d25a3db602"  # us-east-1a
SUBNET_2="subnet-062aaa3fc859e5557"  # us-east-1b

echo "=========================================="
echo "Step 3: RDS MySQL Setup"
echo "=========================================="
echo ""

# Step 3.1: Create Security Group for RDS
echo "Step 3.1: Creating security group for RDS..."
RDS_SG=$(aws ec2 create-security-group \
  --group-name ruberoo-rds-sg \
  --description "Security group for Ruberoo RDS MySQL database" \
  --vpc-id $VPC_ID \
  --profile $PROFILE \
  --region $REGION \
  --query 'GroupId' \
  --output text)

echo "✅ Security group created: $RDS_SG"
echo ""

# Step 3.2: Add inbound rule for MySQL (port 3306)
echo "Step 3.2: Adding MySQL inbound rule..."
echo "⚠️  For now, we'll allow MySQL from default security group"
echo "   (You can restrict this later to specific IPs)"

aws ec2 authorize-security-group-ingress \
  --group-id $RDS_SG \
  --protocol tcp \
  --port 3306 \
  --source-group $DEFAULT_SG \
  --profile $PROFILE \
  --region $REGION > /dev/null

echo "✅ Security group rule added"
echo ""

# Step 3.3: Create DB Subnet Group
echo "Step 3.3: Creating DB subnet group..."
aws rds create-db-subnet-group \
  --db-subnet-group-name ruberoo-db-subnet-group \
  --db-subnet-group-description "Subnet group for Ruberoo RDS MySQL" \
  --subnet-ids $SUBNET_1 $SUBNET_2 \
  --profile $PROFILE \
  --region $REGION > /dev/null

echo "✅ DB subnet group created"
echo ""

# Step 3.4: Create RDS Instance
echo "Step 3.4: Creating RDS MySQL instance..."
echo "⚠️  IMPORTANT: You need to set a secure password!"
echo ""
read -sp "Enter MySQL master password (8-41 chars, mix of upper/lower/numbers/special): " DB_PASSWORD
echo ""
echo ""

if [ -z "$DB_PASSWORD" ]; then
  echo "❌ Error: Password cannot be empty!"
  exit 1
fi

echo "Creating RDS instance (this takes 10-15 minutes)..."
aws rds create-db-instance \
  --db-instance-identifier ruberoo-mysql \
  --db-instance-class db.t3.micro \
  --engine mysql \
  --engine-version 8.0.35 \
  --master-username admin \
  --master-user-password "$DB_PASSWORD" \
  --allocated-storage 20 \
  --storage-type gp2 \
  --vpc-security-group-ids $RDS_SG \
  --db-subnet-group-name ruberoo-db-subnet-group \
  --backup-retention-period 7 \
  --publicly-accessible \
  --storage-encrypted \
  --region $REGION \
  --profile $PROFILE > /dev/null

echo ""
echo "✅ RDS instance creation started!"
echo ""
echo "=========================================="
echo "RDS Instance Details:"
echo "=========================================="
echo "Instance ID: ruberoo-mysql"
echo "Instance Class: db.t3.micro (Free Tier)"
echo "Engine: MySQL 8.0.35"
echo "Storage: 20 GB"
echo "Status: Creating (will take 10-15 minutes)"
echo ""
echo "Security Group: $RDS_SG"
echo "DB Subnet Group: ruberoo-db-subnet-group"
echo ""
echo "=========================================="
echo "Next Steps:"
echo "=========================================="
echo "1. Wait 10-15 minutes for RDS to be available"
echo "2. Check status with:"
echo "   aws rds describe-db-instances --db-instance-identifier ruberoo-mysql --profile $PROFILE --region $REGION --query 'DBInstances[0].DBInstanceStatus' --output text"
echo ""
echo "3. Get endpoint with:"
echo "   aws rds describe-db-instances --db-instance-identifier ruberoo-mysql --profile $PROFILE --region $REGION --query 'DBInstances[0].Endpoint.Address' --output text"
echo ""
echo "4. Save your password securely!"
echo "   Password: [stored in variable - save it manually]"
echo ""
echo "✅ Step 3.4 Complete - RDS is provisioning!"
echo ""

