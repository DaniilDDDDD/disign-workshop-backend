kubectl delete -f https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml
kubectl delete -f rabbitmq-storage.yaml
kubectl delete -f rabbitmq-secret.yaml
kubectl delete -f rabbitmq-cluster.yaml