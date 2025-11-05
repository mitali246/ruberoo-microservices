# 🚀 Step 4.3: Create EKS Cluster

## ✅ Prerequisites Complete:

- ✅ eksctl installed (v0.216.0)
- ✅ kubectl installed (v1.34.1)
- ✅ IAM Cluster Role created: `ruberoo-eks-cluster-role`
- ✅ IAM Node Role created: `ruberoo-eks-node-role`
- ✅ EKS cluster configuration file ready: `aws/eks-cluster.yaml`

---

## ⚠️ Important Before Starting:

1. **Time:** Cluster creation takes **20-30 minutes** (this is normal!)
2. **Cost:** EKS charges start when cluster is created (~$0.10/hour for control plane)
3. **Resources:** Creating EC2 instances for worker nodes

---

## 🔧 Step 4.3: Create EKS Cluster

### Action Required:

**Run this command to create the cluster:**

```bash
cd /Users/mitali/Desktop/MSA/ruberoo-microservices
eksctl create cluster -f aws/eks-cluster.yaml --profile ruberoo-deployment
```

**⚠️ This command will:**
- Create EKS control plane (20-30 minutes)
- Create 2 worker nodes (t3.medium instances)
- Configure networking
- Install add-ons (VPC CNI, CoreDNS, kube-proxy)
- Set up kubectl access automatically

**You'll see output like:**
```
[ℹ]  eksctl version 0.216.0
[ℹ]  using region us-east-1
[ℹ]  setting availability zones to [us-east-1a us-east-1b]
[ℹ]  subnets for us-east-1a - public:xxx/yy private:xxx/yy
[ℹ]  subnets for us-east-1b - public:xxx/yy private:xxx/yy
[ℹ]  nodegroup "ruberoo-nodes" will use "ami-xxx" [AmazonLinux2/1.28]
[ℹ]  using Kubernetes version 1.28
[ℹ]  creating EKS cluster "ruberoo-cluster" in "us-east-1" region
...
```

---

## ⏳ Step 4.4: Monitor Cluster Creation

**While the cluster is creating, you can monitor progress:**

**Check cluster status:**
```bash
aws eks describe-cluster \
  --name ruberoo-cluster \
  --profile ruberoo-deployment \
  --region us-east-1 \
  --query 'cluster.status' \
  --output text
```

**Status progression:**
- `CREATING` → Cluster is being created (current)
- `ACTIVE` → Cluster is ready ✅

**Check node group status:**
```bash
aws eks describe-nodegroup \
  --cluster-name ruberoo-cluster \
  --nodegroup-name ruberoo-nodes \
  --profile ruberoo-deployment \
  --region us-east-1 \
  --query 'nodegroup.status' \
  --output text
```

**Wait until:** Both cluster and node group status are `ACTIVE`

---

## ✅ Step 4.5: Verify Cluster is Ready

After cluster is created, verify everything is working:

```bash
# Configure kubectl (eksctl usually does this automatically)
aws eks update-kubeconfig \
  --name ruberoo-cluster \
  --region us-east-1 \
  --profile ruberoo-deployment

# Check nodes
kubectl get nodes

# Check all pods
kubectl get pods --all-namespaces
```

**Expected output:**
- 2 nodes showing `Ready` status
- CoreDNS pods running
- VPC CNI pods running

---

## 📝 Cluster Configuration:

- **Cluster Name:** `ruberoo-cluster`
- **Region:** `us-east-1`
- **Kubernetes Version:** 1.28
- **Node Group:** `ruberoo-nodes`
- **Instance Type:** `t3.medium`
- **Node Count:** 2 (min: 1, max: 3)
- **VPC:** `vpc-00fdde9604a8b018a` (Default VPC)

---

## 🎯 After Cluster is Ready:

1. ✅ Deploy microservices to EKS
2. ✅ Configure service discovery
3. ✅ Set up load balancer
4. ✅ Connect to RDS
5. ✅ Set up CI/CD pipeline

---

**Ready to create the cluster?** Run the command above and let me know when it starts!

**Tell me:** "Cluster creation started" and I'll help you monitor the progress!

