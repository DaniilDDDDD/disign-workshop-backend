kubectl create configmap mongo-metadata-init-config --from-file=mongo-metadata-init.js
kubectl apply -f mongo-metadata-config.yaml
kubectl apply -f mongo-metadata-storage.yaml
kubectl apply -f mongo-metadata-service.yaml
kubectl apply -f mongo-metadata-deployment.yaml