kubectl delete -f redis-deployment.yaml --force --grace-period=0
kubectl delete -f redis-service.yaml --force --grace-period=0
kubectl delete -f redis-storage.yaml --force --grace-period=0
kubectl delete -f redis-config.yaml --force --grace-period=0