#!/bin/bash
# Simple deployment script for EKS

set -e

echo "🚀 Deploying Ruberoo Microservices to EKS..."
echo ""

# Check if kubectl is configured
if ! kubectl cluster-info &> /dev/null; then
    echo "❌ kubectl not configured or cluster not accessible"
    exit 1
fi

# Create namespace if it doesn't exist
echo "📦 Creating namespace..."
kubectl create namespace ruberoo --dry-run=client -o yaml | kubectl apply -f -

# Apply RDS secret
echo "🔐 Applying RDS secret..."
kubectl apply -f k8s/rds-secret.yaml

# Apply JWT secret if it doesn't exist
if ! kubectl get secret jwt-secret -n ruberoo &> /dev/null; then
    echo "🔑 Creating JWT secret..."
    kubectl create secret generic jwt-secret \
        --from-literal=secret-key="dev-secret-change-me-in-production" \
        -n ruberoo
fi

# Apply all services
echo "📋 Deploying services..."
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/jwt-secret.yaml
kubectl apply -f k8s/redis.yaml
kubectl apply -f k8s/eureka.yaml
kubectl apply -f k8s/config-server.yaml
kubectl apply -f k8s/user-service.yaml
kubectl apply -f k8s/ride-management-service.yaml
kubectl apply -f k8s/tracking-service.yaml
kubectl apply -f k8s/api-gateway.yaml

echo ""
echo "✅ Deployment complete!"
echo ""
echo "📊 Check status:"
echo "   kubectl get pods -n ruberoo"
echo ""
echo "📝 View logs:"
echo "   kubectl logs -f <pod-name> -n ruberoo"
echo ""

