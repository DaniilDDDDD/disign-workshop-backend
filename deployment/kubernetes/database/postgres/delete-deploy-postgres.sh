kubectl delete -f postgres-deployment.yaml --force --grace-period=0
kubectl delete -f postgres-service.yaml --force --grace-period=0
kubectl delete -f postgres-storage.yaml --force --grace-period=0
kubectl delete -f postgres-config.yaml --force --grace-period=0