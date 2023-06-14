kubectl delete -f application-background-deployment.yaml --force --grace-period=0
kubectl delete -f application-background-service.yaml --force --grace-period=0
kubectl delete -f application-background-storage.yaml --force --grace-period=0
kubectl delete -f application-background-config.yaml --force --grace-period=0