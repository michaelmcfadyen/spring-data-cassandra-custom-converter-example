### How To Run

```./start.sh```

#### What does it do?
 - Starts a cassandra docker container on port 9042
 - Creates a keyspace and columnfamily
 - Starts the spring boot application
 - Calls the POST endpoint twice to add data to db
 - Calls GET endpoint to retrieve all data
 
 ####Schema
 
 ```
CREATE KEYSPACE test WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};
CREATE TABLE test.test (pk text PRIMARY KEY,currency text,locale text)```