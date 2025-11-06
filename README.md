# 🚗 Ruberoo Microservices - Complete AWS Deployment

**A production-ready microservices-based ride-sharing platform deployed on AWS EKS with full CI/CD pipeline.**

---

## 🏗️ Architecture

```
┌─────────────────┐
│   API Gateway   │ (Port 8085) - JWT Auth, Rate Limiting
└────────┬────────┘
         │
    ┌────┴────┬─────────────┬──────────────┐
    │         │             │              │
┌───▼───┐ ┌──▼───┐ ┌───────▼────┐ ┌───────▼────┐
│ User  │ │ Ride │ │  Tracking  │ │  Eureka    │
│Service│ │Mgmt  │ │  Service    │ │  Server    │
└───┬───┘ └──┬───┘ └───────┬────┘ └────┬─────┘
    │        │             │             │
    └────────┴─────────────┴─────────────┘
              │
         ┌────▼────┐
         │  RDS    │
         │  MySQL  │
         └─────────┘
```

---

## ✅ Deployment Status

### **Infrastructure:**
- ✅ **EKS Cluster**: `ruberoo-cluster` (us-east-1)
- ✅ **RDS MySQL**: 3 databases (user, ride, tracking)
- ✅ **ECR**: 6 Docker image repositories
- ✅ **CodePipeline**: Fully automated CI/CD

### **Services:**
- ✅ **Eureka Server** - Service Discovery (Port 8761)
- ✅ **Config Server** - Configuration Management (Port 8888)
- ✅ **API Gateway** - API Routing & Gateway (Port 8085)
- ✅ **Redis Cache** - Caching & Rate Limiting (Port 6379)
- ✅ **User Service** - User Management & Authentication (Port 8081)
- ✅ **Ride Management Service** - Ride Operations (Port 8083)
- ✅ **Tracking Service** - Location Tracking (Port 8084)

---

## 🚀 Quick Start

### **Check Service Status:**
```bash
kubectl get pods -n ruberoo
kubectl get services -n ruberoo
```

### **View Logs:**
```bash
kubectl logs -f <pod-name> -n ruberoo
```

### **Access Eureka Dashboard:**
```bash
kubectl port-forward -n ruberoo service/eureka-server 8761:8761
# Open: http://localhost:8761
```

### **Deploy Updates:**
```bash
./upload-to-s3.sh
# Then go to CodePipeline console and click "Release change"
```

---

## 🔧 Local Development

```bash
# Start all services locally
docker compose up -d

# Check status
docker ps

# View logs
docker compose logs -f <service-name>
```

---

## 📦 CI/CD Pipeline

### **Automated Workflow:**
1. **Source**: Code uploaded to S3
2. **Build**: CodeBuild builds 6 Docker images
3. **Push**: Images pushed to ECR
4. **Deploy**: Manual deployment via `./deploy-to-eks.sh`

### **Trigger Pipeline:**
```bash
./upload-to-s3.sh
# Then go to AWS CodePipeline console
# Click "Release change" button
```

---

## 📁 Project Structure

```
ruberoo-microservices/
├── ruberoo-api-gateway/          # API Gateway Service
├── ruberoo-user-service/         # User Service
├── ruberoo-ride-management-service/  # Ride Management Service
├── ruberoo-tracking-service/     # Tracking Service
├── ruberoo-eureka-server/        # Eureka Server
├── ruberoo-config-server/        # Config Server
├── k8s/                          # Kubernetes manifests
├── aws/                          # AWS CI/CD configurations
│   ├── buildspecs/               # CodeBuild buildspecs
│   ├── codebuild/                # CodeBuild projects
│   └── codepipeline/             # CodePipeline config
├── deploy-to-eks.sh              # EKS deployment script
├── upload-to-s3.sh               # S3 upload script
├── create-aws-pipeline.sh        # Pipeline setup script
└── docker-compose.yml            # Local development
```

---

## 🔐 Configuration

### **RDS Database:**
- **Host**: `ruberoo-mysql.cq382ua6uclq.us-east-1.rds.amazonaws.com`
- **Port**: 3306
- **Databases**: `ruberoo_user_db`, `ruberoo_ride_db`, `ruberoo_tracking_db`

### **Secrets:**
- RDS credentials stored in Kubernetes secret: `rds-secret`
- JWT secret stored in Kubernetes secret: `jwt-secret`

---

## 🧪 Testing

### **Postman Collection:**
Import `Ruberoo-Microservices.postman_collection.json` for API testing.

### **Health Checks:**
```bash
# User Service
curl http://<user-service-ip>:8081/actuator/health

# API Gateway
curl http://<api-gateway-ip>:8085/actuator/health
```

---

## 📊 Monitoring

### **Eureka Dashboard:**
- URL: `http://<eureka-service-ip>:8761`
- Shows all registered services

### **Kubernetes:**
```bash
# View all pods
kubectl get pods -n ruberoo -o wide

# View service endpoints
kubectl get endpoints -n ruberoo

# View events
kubectl get events -n ruberoo --sort-by='.lastTimestamp'
```

---

## 🛠️ Troubleshooting

### **Pods Not Starting:**
```bash
# Check logs
kubectl logs <pod-name> -n ruberoo

# Describe pod
kubectl describe pod <pod-name> -n ruberoo

# Check events
kubectl get events -n ruberoo
```

### **Database Connection Issues:**
- Verify RDS security group allows EKS cluster security group
- Check RDS endpoint is correct
- Verify credentials in `rds-secret`

---

## 📝 Documentation

- **PROJECT_COMPLETE.md** - Complete project status
- **PROJECT_STATUS.md** - Current deployment status
- **ARCHITECTURE_DIAGRAMS.md** - Architecture documentation

---

## 🎯 Next Steps (Optional)

1. **Load Balancer**: Set up AWS ALB for external access
2. **Monitoring**: Configure CloudWatch or Prometheus
3. **Auto-scaling**: Set up HPA or cluster autoscaler
4. **SSL/TLS**: Configure certificates for HTTPS
5. **Backup**: Set up RDS automated backups

---

## 📄 License

This project is part of a microservices architecture demonstration.

---

**Status**: ✅ **Production Ready** | **Last Updated**: November 6, 2025
