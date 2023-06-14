kubectl delete -f ingress.yaml --force --grace-period=0

kubectl delete IngressClass nginx --force --grace-period=0
kubectl delete namespace ingress-nginx --force --grace-period=0
