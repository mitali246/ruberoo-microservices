# 🔄 Complete Guide: Shutdown → Start → Test

## 🛑 **SHUTDOWN THE PROJECT**

### **Single Command:**
```bash
kubectl scale deployment --all --replicas=0 -n ruberoo
```

### **Verify:**
```bash
kubectl get pods -n ruberoo
# Should show: No pods running
```

---

## 🚀 **START THE PROJECT**

### **Step 1: Deploy**
```bash
./deploy-to-eks.sh
```

### **Step 2: Wait**
```bash
kubectl get pods -n ruberoo -w
# Wait until all 7 services show "1/1 Running" (2-3 minutes)
```

### **Step 3: Verify**
```bash
kubectl get pods -n ruberoo
# Should show 7 pods with "1/1 Running"
```

---

## 🧪 **TEST THE PROJECT**

### **Step 1: Get API Gateway URL**
```bash
# Get Node IP and Port
kubectl get nodes -o wide
kubectl get service api-gateway -n ruberoo

# URL: http://<EXTERNAL-IP>:<NODE_PORT>
# Example: http://3.236.36.113:31848
```

### **Step 2: Postman Setup**
1. Import `Ruberoo-Microservices.postman_collection.json`
2. Create environment: `baseUrl` = `http://<node-ip>:31848`
3. Set `token` variable after login

### **Step 3: Test Flow**
1. **Register User** → POST `/api/users/auth/register`
2. **Login** → POST `/api/users/auth/login` → Save token
3. **Get Profile** → GET `/api/users/profile` (use token)
4. **Create Ride** → POST `/api/rides` (use token)
5. **Get Rides** → GET `/api/rides` (use token)

### **Step 4: View Eureka**
```bash
kubectl port-forward -n ruberoo service/eureka-server 8761:8761
# Open: http://localhost:8761
```

---

**✅ Ready to present!**
