kubectl create configmap mongo-content-init-config --from-file=mongo-content-init.js
kubectl apply -f mongo-content-config.yaml
kubectl apply -f mongo-content-storage.yaml
kubectl apply -f mongo-content-service.yaml
kubectl apply -f mongo-content-deployment.yaml