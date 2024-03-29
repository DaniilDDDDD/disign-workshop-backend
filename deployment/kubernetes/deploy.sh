cd rabbitmq
bash deploy-rabbitmq.sh

cd ../database/mongo/content
bash deploy-mongo-content.sh
cd ../metadata
bash deploy-mongo-metadata.sh
cd ../../postgres
bash deploy-postgres.sh
cd ../redis
bash deploy-redis.sh

cd ../../application/auth/
bash deploy-application-auth.sh
cd ../background
bash deploy-application-background.sh
cd ../content
bash deploy-application-content.sh
cd ../metadata
bash deploy-application-metadata.sh

cd ../../ingress
bash deploy-ingress.sh

cd ..