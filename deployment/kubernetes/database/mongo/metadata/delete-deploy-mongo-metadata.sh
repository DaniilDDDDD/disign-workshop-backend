kubectl delete -f mongo-metadata-deployment.yaml --force --grace-period=0
kubectl delete -f mongo-metadata-service.yaml --force --grace-period=0
kubectl delete -f mongo-metadata-storage.yaml --force --grace-period=0
kubectl delete -f mongo-metadata-config.yaml --force --grace-period=0
kubectl delete configmap mongo-metadata-init-config --force --grace-period=0