# 🔧 Fix: EKS IAM Permissions Issue

## ❌ Error:
```
AccessDeniedException: User: arn:aws:iam::008041186656:user/ruberoo-deployment-user 
is not authorized to perform: eks:DescribeClusterVersions
```

## 🔍 Problem:
The IAM user `ruberoo-deployment-user` doesn't have sufficient EKS permissions for eksctl to work.

## ✅ Solution:
We need to attach `AmazonEKSFullAccess` policy to the IAM user.

---

## 🔧 Fix: Add EKS Full Access Policy

**Run this command to add the missing permission:**

```bash
aws iam attach-user-policy \
  --user-name ruberoo-deployment-user \
  --policy-arn arn:aws:iam::aws:policy/AmazonEKSFullAccess \
  --profile ruberoo-deployment
```

**Note:** You might need to use root user credentials or the IAM user needs permission to attach policies to itself.

---

## ✅ Verify Permission Added:

```bash
aws iam list-attached-user-policies \
  --user-name ruberoo-deployment-user \
  --profile ruberoo-deployment \
  --query 'AttachedPolicies[*].PolicyArn' \
  --output table
```

You should see `AmazonEKSFullAccess` in the list.

---

## 🔄 After Adding Permission:

Try creating the cluster again:

```bash
eksctl create cluster -f aws/eks-cluster.yaml --profile ruberoo-deployment
```

---

## ⚠️ Alternative: Use Root User Temporarily

If the IAM user doesn't have permission to attach policies, you can:

1. **Switch to root user** in AWS Console
2. **Go to IAM → Users → ruberoo-deployment-user**
3. **Click "Add permissions" → "Attach policies directly"**
4. **Search for and select:** `AmazonEKSFullAccess`
5. **Click "Add permissions"**

Then try the eksctl command again.

---

**After adding the permission, try creating the cluster again!**

