#!/bin/sh

docker-compose exec kafka-1 kafka-topics \
  --create \
  --bootstrap-server localhost:29092 \
  --replication-factor 2 \
  --partitions 3 \
  --topic pinball.scores

docker-compose exec kafka-1 kafka-topics \
  --create \
  --bootstrap-server localhost:29092 \
  --replication-factor 2 \
  --partitions 2 \
  --topic pinball.users

docker-compose exec kafka-1 kafka-topics \
  --create \
  --bootstrap-server localhost:29092 \
  --replication-factor 2 \
  --partitions 2 \
  --topic pinball.highscores
