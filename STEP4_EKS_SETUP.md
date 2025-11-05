# ☸️ Step 4: EKS (Kubernetes) Cluster Setup

## 🎯 Objective
Create an EKS cluster to host all Ruberoo microservices with managed node groups.

---

## 📋 Prerequisites Check

Before we start:
- ✅ kubectl installed (v1.34.1)
- ⏳ eksctl needs to be installed
- ✅ VPC identified: `vpc-00fdde9604a8b018a`
- ✅ Subnets available

---

## 🔧 Step 4.1: Install eksctl

**eksctl** is the official CLI tool for creating EKS clusters.

### Install eksctl:

```bash
brew install eksctl
```

**Verify installation:**
```bash
eksctl version
```

---

## 🔧 Step 4.2: Create IAM Roles for EKS

Before creating the cluster, we need IAM roles for:
1. **EKS Cluster Role** - For the EKS control plane
2. **EKS Node Group Role** - For worker nodes

### Step 4.2.1: Create EKS Cluster Role

This role allows EKS to manage resources on your behalf.

**Create the role:**
```bash
aws iam create-role \
  --role-name ruberoo-eks-cluster-role \
  --assume-role-policy-document '{
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Principal": {
          "Service": "eks.amazonaws.com"
        },
        "Action": "sts:AssumeRole"
      }
    ]
  }' \
  --profile ruberoo-deployment \
  --region us-east-1
```

**Attach required policies:**
```bash
aws iam attach-role-policy \
  --role-name ruberoo-eks-cluster-role \
  --policy-arn arn:aws:iam::aws:policy/AmazonEKSClusterPolicy \
  --profile ruberoo-deployment \
  --region us-east-1
```

### Step 4.2.2: Create EKS Node Group Role

This role allows worker nodes to join the cluster.

**Create the role:**
```bash
aws iam create-role \
  --role-name ruberoo-eks-node-role \
  --assume-role-policy-document '{
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Principal": {
          "Service": "ec2.amazonaws.com"
        },
        "Action": "sts:AssumeRole"
      }
    ]
  }' \
  --profile ruberoo-deployment \
  --region us-east-1
```

**Attach required policies:**
```bash
# AmazonEKSWorkerNodePolicy
aws iam attach-role-policy \
  --role-name ruberoo-eks-node-role \
  --policy-arn arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy \
  --profile ruberoo-deployment \
  --region us-east-1

# AmazonEKS_CNI_Policy
aws iam attach-role-policy \
  --role-name ruberoo-eks-node-role \
  --policy-arn arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy \
  --profile ruberoo-deployment \
  --region us-east-1

# AmazonEC2ContainerRegistryReadOnly
aws iam attach-role-policy \
  --role-name ruberoo-eks-node-role \
  --policy-arn arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly \
  --profile ruberoo-deployment \
  --region us-east-1
```

---

## 🔧 Step 4.3: Create EKS Cluster

**⚠️ Important:** EKS cluster creation takes **20-30 minutes**. This is normal.

### Using eksctl (Recommended):

I've created a cluster configuration file: `aws/eks-cluster.yaml`

**Create the cluster:**
```bash
cd /Users/mitali/Desktop/MSA/ruberoo-microservices
eksctl create cluster -f aws/eks-cluster.yaml --profile ruberoo-deployment
```

**This will:**
- Create the EKS control plane
- Create worker node group (2 nodes)
- Configure networking
- Set up kubectl access
- Install add-ons

---

## ⏳ Step 4.4: Wait for Cluster Creation

**Cluster creation takes 20-30 minutes.**

**Check status:**
```bash
aws eks describe-cluster \
  --name ruberoo-cluster \
  --profile ruberoo-deployment \
  --region us-east-1 \
  --query 'cluster.status' \
  --output text
```

**Wait until status is:** `ACTIVE`

---

## ✅ Step 4.5: Configure kubectl

After cluster is created, configure kubectl:

```bash
aws eks update-kubeconfig \
  --name ruberoo-cluster \
  --region us-east-1 \
  --profile ruberoo-deployment
```

**Verify access:**
```bash
kubectl get nodes
kubectl get pods --all-namespaces
```

---

## 📝 Cluster Configuration Summary:

- **Cluster Name:** `ruberoo-cluster`
- **Region:** `us-east-1`
- **Kubernetes Version:** 1.28
- **Node Group:** `ruberoo-nodes`
- **Instance Type:** `t3.medium` (2 nodes)
- **VPC:** `vpc-00fdde9604a8b018a` (Default VPC)

---

## ⚠️ Important Notes:

1. **Cost:** 
   - EKS Control Plane: ~$0.10/hour (~$72/month)
   - EC2 Nodes (2x t3.medium): ~$0.0416/hour each (~$60/month for 2)
   - Total: ~$132/month (not in Free Tier)

2. **Time:** Cluster creation takes 20-30 minutes

3. **Free Tier:** EKS is NOT in Free Tier, but we're using minimal resources

---

## 🎯 What's Next After EKS is Ready:

1. ✅ Deploy microservices to EKS
2. ✅ Configure service discovery
3. ✅ Set up load balancer
4. ✅ Connect to RDS
5. ✅ Set up CI/CD pipeline

---

**Ready to start?** Let's first install eksctl, then create the IAM roles, then create the cluster!

