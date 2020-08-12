#!/bin/bash
docker rm -f demo-cassandra
docker run --name demo-cassandra -d -p 9042:9042 cassandra

until docker exec -it demo-cassandra cqlsh -e "describe keyspaces"
do
  echo "Waiting for cassandra to start..."
  sleep 5
done

docker exec -it demo-cassandra cqlsh -e \
"CREATE KEYSPACE test WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};\
 CREATE TABLE test.test (pk text PRIMARY KEY,currency text,locale text)"

./mvnw clean :spring-boot:run &
pid=$!

until nc -vz localhost 8080
do
  echo "Waiting for app to start..."
  sleep 5
done

echo "App started up"
echo "Creating example entries..."
curl -XPOST localhost:8080/test
curl -XPOST localhost:8080/test
echo "Fetching all rows..."
curl -XGET localhost:8080/test

trap "trap - SIGTERM && kill -- -$$" SIGINT SIGTERM EXIT
