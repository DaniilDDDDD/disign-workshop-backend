kubectl delete -f https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml
kubectl delete -f rabbitmq-cluster.yaml
kubectl delete -f rabbitmq-storage.yaml
kubectl delete -f rabbitmq-secret.yaml

kubectl delete rabbitmqcluster.rabbitmq.com rabbitmq-cluster
kubectl delete pod rabbitmq-cluster-server-0
kubectl delete service rabbitmq-cluster
kubectl delete service rabbitmq-cluster-nodes
kubectl delete statefulset.apps rabbitmq-cluster-server