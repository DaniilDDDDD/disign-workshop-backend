kubectl delete configmap mongo-content-init-config
kubectl delete -f mongo-content-config.yaml
kubectl delete -f mongo-content-storage.yaml
kubectl delete -f mongo-content-service.yaml
kubectl delete -f mongo-content-deployment.yaml