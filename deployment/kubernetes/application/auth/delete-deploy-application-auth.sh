kubectl delete -f application-auth-deployment.yaml --force --grace-period=0
kubectl delete -f application-auth-service.yaml --force --grace-period=0
kubectl delete -f application-auth-storage.yaml --force --grace-period=0
kubectl delete -f application-auth-config.yaml --force --grace-period=0