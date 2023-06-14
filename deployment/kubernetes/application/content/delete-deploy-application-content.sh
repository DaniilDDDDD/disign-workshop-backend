kubectl delete -f application-content-deployment.yaml --force --grace-period=0
kubectl delete -f application-content-service.yaml --force --grace-period=0
kubectl delete -f application-content-storage.yaml --force --grace-period=0
kubectl delete -f application-content-config.yaml --force --grace-period=0