kubectl delete -f application-metadata-deployment.yaml --force --grace-period=0
kubectl delete -f application-metadata-service.yaml --force --grace-period=0
kubectl delete -f application-metadata-storage.yaml --force --grace-period=0
kubectl delete -f application-metadata-config.yaml --force --grace-period=0