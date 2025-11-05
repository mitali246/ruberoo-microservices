# 🐳 Step 3.5: Test ECR Image Push

## 🎯 Objective
Build a Docker image for User Service and push it to ECR to verify the workflow.

---

## 📋 Prerequisites Check

Before we start, we need:
- ✅ ECR repositories created (already done)
- ✅ Docker installed and running
- ✅ JAR file built for User Service
- ✅ AWS CLI configured with ECR access

---

## 🔧 Step 3.5.1: Build JAR File

First, let's build the User Service JAR:

```bash
cd /Users/mitali/Desktop/MSA/ruberoo-microservices
mvn clean package -DskipTests -pl ruberoo-user-service
```

This will create: `ruberoo-user-service/target/user-service-1.0.0-SNAPSHOT.jar`

---

## 🔧 Step 3.5.2: Login to ECR

Before pushing images, you need to authenticate Docker with ECR:

```bash
aws ecr get-login-password --region us-east-1 --profile ruberoo-deployment | \
  docker login --username AWS --password-stdin 008041186656.dkr.ecr.us-east-1.amazonaws.com
```

**Expected output:** `Login Succeeded`

---

## 🔧 Step 3.5.3: Build Docker Image

Build the Docker image for User Service:

```bash
cd /Users/mitali/Desktop/MSA/ruberoo-microservices
docker build -t ruberoo-user-service:latest ./ruberoo-user-service
```

---

## 🔧 Step 3.5.4: Tag Image for ECR

Tag the image with the ECR repository URI:

```bash
docker tag ruberoo-user-service:latest \
  008041186656.dkr.ecr.us-east-1.amazonaws.com/ruberoo-user:latest
```

---

## 🔧 Step 3.5.5: Push to ECR

Push the image to ECR:

```bash
docker push 008041186656.dkr.ecr.us-east-1.amazonaws.com/ruberoo-user:latest
```

---

## ✅ Step 3.5.6: Verify in ECR

Check that the image appears in ECR:

```bash
aws ecr describe-images \
  --repository-name ruberoo-user \
  --profile ruberoo-deployment \
  --region us-east-1
```

---

## 📝 Expected Results

After successful push:
- ✅ Image appears in ECR console
- ✅ Image URI: `008041186656.dkr.ecr.us-east-1.amazonaws.com/ruberoo-user:latest`
- ✅ Image can be pulled for EKS deployment

---

**Ready to start?** Let's begin with building the JAR file!

