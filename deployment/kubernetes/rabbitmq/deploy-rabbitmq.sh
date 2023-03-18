kubectl apply -f https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml
kubectl apply -f rabbitmq-storage.yaml
kubestl apply -f rabbitmq-secret.yaml
kubectl apply -f rabbitmq-cluster.yaml