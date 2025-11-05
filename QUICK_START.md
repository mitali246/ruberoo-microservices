# ⚡ QUICKEST AWS Deployment - 3 Steps!

## 🎯 What This Does

**Automatically builds and deploys your microservices to AWS EKS when you push to GitHub!**

---

## ✅ Step 1: Add GitHub Secrets (2 minutes)

1. Go to: https://github.com/mitali246/ruberoo-microservices/settings/secrets/actions

2. Click **"New repository secret"** and add:

   **Secret 1:**
   - Name: `AWS_ACCESS_KEY_ID`
   - Value: (Your `ruberoo-deployment-user` access key)

   **Secret 2:**
   - Name: `AWS_SECRET_ACCESS_KEY`
   - Value: (Your `ruberoo-deployment-user` secret key)

---

## ✅ Step 2: Deploy to EKS (5 minutes)

Run this command:

```bash
chmod +x deploy-to-eks.sh
./deploy-to-eks.sh
```

This will:
- ✅ Create Kubernetes namespace
- ✅ Apply RDS secret (connects to your AWS RDS)
- ✅ Deploy all 6 microservices
- ✅ Use ECR images (automatically built by GitHub Actions)

---

## ✅ Step 3: Push to GitHub (1 minute)

```bash
git add .
git commit -m "Add CI/CD and AWS deployment"
git push origin main
```

**That's it!** 🎉

GitHub Actions will automatically:
1. Build Docker images
2. Push to ECR
3. Deploy to EKS (on next push)

---

## 📊 Check Deployment Status

```bash
# Check all pods
kubectl get pods -n ruberoo

# Check services
kubectl get services -n ruberoo

# View logs
kubectl logs -f <pod-name> -n ruberoo
```

---

## 🎯 What's Already Done

✅ EKS Cluster: `ruberoo-cluster` (ready)  
✅ RDS Database: `ruberoo-mysql.cq382ua6uclq.us-east-1.rds.amazonaws.com`  
✅ ECR Repositories: All 6 services  
✅ Kubernetes Manifests: Updated for RDS  
✅ GitHub Actions: CI/CD workflow created  

---

## 🚀 Next Push = Auto Deploy!

Every time you push to `main` branch:
- ✅ Images build automatically
- ✅ Push to ECR
- ✅ Ready to deploy (manual step for now, or add auto-deploy)

---

**That's it! Much simpler!** 🎉

