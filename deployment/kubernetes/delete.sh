cd rabbitmq
bash delete-deploy-rabbitmq.sh

cd ../database/mongo/content
bash delete-deploy-mongo-content.sh
cd ../metadata
bash delete-deploy-mongo-metadata.sh
cd ../../postgres
bash delete-deploy-postgres.sh
cd ../redis
bash delete-deploy-redis.sh

cd ../../application/auth/
bash delete-deploy-application-auth.sh
cd ../background
bash delete-deploy-application-background.sh
cd ../content
bash delete-deploy-application-content.sh
cd ../metadata
bash delete-deploy-application-metadata.sh

cd ../..