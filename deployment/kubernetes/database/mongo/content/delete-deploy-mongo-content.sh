kubectl delete -f mongo-content-deployment.yaml --force --grace-period=0
kubectl delete -f mongo-content-service.yaml --force --grace-period=0
kubectl delete -f mongo-content-storage.yaml --force --grace-period=0
kubectl delete -f mongo-content-config.yaml --force --grace-period=0
kubectl delete configmap mongo-content-init-config --force --grace-period=0