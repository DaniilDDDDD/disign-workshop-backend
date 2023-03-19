kubectl delete configmap mongo-metadata-init-config
kubectl delete -f mongo-metadata-config.yaml
kubectl delete -f mongo-metadata-storage.yaml
kubectl delete -f mongo-metadata-service.yaml
kubectl delete -f mongo-metadata-deployment.yaml