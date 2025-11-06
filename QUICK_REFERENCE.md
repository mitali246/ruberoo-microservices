# ⚡ Quick Reference Guide

## 🛑 **Shutdown Project**
```bash
kubectl scale deployment --all --replicas=0 -n ruberoo
```

## 🚀 **Start Project**
```bash
./deploy-to-eks.sh
kubectl get pods -n ruberoo -w  # Wait 2-3 minutes
```

## 🧪 **Test Project**
1. Get API Gateway URL:
   ```bash
   kubectl get nodes -o wide
   kubectl get service api-gateway -n ruberoo
   # URL: http://<EXTERNAL-IP>:<NODE_PORT>
   ```

2. Use Postman:
   - Import `Ruberoo-Microservices.postman_collection.json`
   - Set `baseUrl` variable
   - Test: Register → Login → Get token → Create ride

3. View Eureka:
   ```bash
   kubectl port-forward -n ruberoo service/eureka-server 8761:8761
   # Open: http://localhost:8761
   ```

---

## 📚 **Documentation Files:**
- `README.md` - Main documentation
- `COMPLETE_PRESENTATION_PACKAGE.md` - Full presentation guide
- `PROJECT_COMPLETE.md` - Project status
- `SHUTDOWN_START_TEST.md` - Quick reference

---

**For detailed information, see `COMPLETE_PRESENTATION_PACKAGE.md`**

