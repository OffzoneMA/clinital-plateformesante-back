docker-compose down

docker volume rm platformesante_db-data

docker-compose up -d --build

sleep 1m

docker network connect app clinital-plateformesante-back-backend-1

sleep 1m

docker restart nginx-proxy
docker restart nginx-proxy-letsencrypt
