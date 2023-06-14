kubectl delete -f https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml --force --grace-period=0
kubectl delete -f rabbitmq-cluster.yaml --force --grace-period=0
kubectl delete rabbitmq-storage-class --force --grace-period=0
kubectl delete rabbitmq-persistent-volume-claim --force --grace-period=0
kubectl delete rabbitmq-persistent-volume --force --grace-period=0
kubectl delete -f rabbitmq-secret.yaml --force --grace-period=0

kubectl delete statefulset.apps rabbitmq-cluster-server --force --grace-period=0
kubectl delete service rabbitmq-cluster --force --grace-period=0
kubectl delete service rabbitmq-cluster-nodes --force --grace-period=0
kubectl delete rabbitmqcluster.rabbitmq.com rabbitmq-cluster --force --grace-period=0
kubectl delete pod rabbitmq-cluster-server-0 --force --grace-period=0
