# Big Data Advanced

This project aim to implement the concepts of NoSQL on Redis database

## Description
**todo** : add the description of project

This project is also available for Mongo database in this repository
https://github.com/SlaveofChrist/Big-Data-Advanced

## Docker Compose Services

The docker-compose.yml file provided includes the following services:

### Redis
The Redis service is used to run the latest version of the Redis database server. It is configured with the following details:

- Image: redis:latest
- Ports: The Redis server is accessible on port 6379 of the host machine, mapped to the same port within the Redis container.
- Volumes: The Redis data is stored in the ./redis-data directory on the host machine, which is mounted as /data within the Redis container.

Ensure that Docker is installed and running on your machine before using the docker-compose.yml file or update the database
configuration in the project.

## Database Configuration

The RedisClient class manages the connection to the Redis database. It uses the Jedis Java client library to interact with Redis. Here are the key details:

- Class: org.unice.config.RedisClient
- Singleton: The RedisClient class follows the singleton design pattern, ensuring that only one instance of the Redis client is created.
- JedisPool: The Redis client is initialized with a JedisPool configured to connect to the Redis server running on localhost and port 6379.
- Accessing Resources: The getResource() method provides access to a Jedis instance from the connection pool, allowing interaction with the Redis database.

### Important!

Ensure that Redis is running and accessible on localhost with port 6379 or change the redis database config in the configuration class.
Note that we don't use application.[yml|properties] file.

## Running tests
### Run all the unit test classes.
$ mvn test

### Run a single test class.
$ mvn -Dtest=TestApp1 test

### Run multiple test classes.
$ mvn -Dtest=TestApp1,TestApp2 test

### Run a single test method from a test class.
$ mvn -Dtest=TestApp1#methodname test


## Authors

* Eliel WOTOBE
* Romeo David AMEDOMEY
* Tymoteusz Igor Cyryl CIESIELSKI
* Mohamed ELYSALEM
