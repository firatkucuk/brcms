# BRCMS

BRCMS is a sample content-repository and CMS implementation. It's more like a showcase of how to
implement a basic content repository and an example service which uses the content repository.

![](docs/images/brcms-architecture.png)

Project architecture contains 2 stand-alone server. Content repository and Content management system.
Content repository server acts like a node store, and it provides CRUD operations regarding nodes
over REST API. Content management backend also provides REST API but more domain specific. As an example
it uses a test data of products.

Project also uses PostgreSQL database for persisting the nodes. In running section, more detailed
explanation can be found regarding how to run all connected components.

# 1. Project Structure

Project is a maven multimodule project. Maven is used because gradle sometimes too complex for
Github dependabot system. This project is actively scanning by Github's dependabot dependency
checking mechanism. There are 3 submodule of the project.

1. content-repository
2. cms-backend
3. client

Project uses maven for dependency management, docker and docker-compose for container support.
Checkstyle for advanced Java linting and code quality. Code rules are pretty strict, they also
ensure a certain amount of cylomatic complexity.

Project uses spotify code formatting library which uses Google java code format under the hood.
JDK 17 is needed for compiling the project.

## 1.1. Content-Repository

Content-repository is a very basic Apache Jackrabbit like implementation. It provides some REST
operations for node creation.

```
NODE OPERATIONS
================================================================================
POST   /nodes                        : Creates a parent node
POST   /nodes/{parent-node-id}       : Creates a child node under parent node
GET    /nodes                        : Lists all nodes
GET    /nodes?by-path={path}         : Traverse node by path
GET    /nodes/{id}                   : Gets specific node
DELETE /nodes/{id}                   : Deletes specific node
PUT    /nodes/{id}                   : Updates a node
```

## 1.2. Content management system

Provided content management system server exposes a REST API for product operations.

## 1.3. Client

Client library provides a programmatic way to interact with content repository. Content management
system uses that client library under the hood for interacting with content repository.

# 2. Running Project

BRCMS project needs a postgresql 14 database for persisting the data. Provided `docker-compose.yml`
can easily boot up a database server. Be sure that you have docker and docker-compose installed correctly.

## 2.1. Booting up the database

A standalone database can be used but for local development purposes it's easy to boot up the `docker-compose.yml`
definition. In the project root directory type the following for running the database server:

```shell
docker-compose up -d
```

`-d` option will run in daemon mode. If you want to see all output and shuting down the server via Ctrl-C then ignore the `-d` flag.

## 2.1. Running content repository server

by default content repository is a standalone server which runs on 9090 port. For running this stand-alone server you can use maven spring-boot plug-in.

```shell
./mvnw install -DskipTests && ./mvnw -pl content-repository spring-boot:run
```

or after compilation you can just run the jar file.

```shell
./mvnw clean compile package -DskipTests
cd content-repository/target/
java -jar content-repository.jar
```

It's possible to create a docker image and running it via docker image

```shell
cd content-repository/
docker build -t bloomreach/content-repository:1.0 .
```

Then running and connecting to actively running postgres server:

```shell
docker run --name brcms-content-repository \
    -p 127.0.0.1:9090:8080 \
    -e POSTGRES_URL='jdbc:postgresql://brcms-postgres:5432/brcms' \
    -e POSTGRES_USERNAME='brcms' \
    -e POSTGRES_PASSWORD='brcms' \
    --network brcms \
    -d \
    bloomreach/content-repository:1.0
```

Or you can just run uploaded version from docker hub:

```shell
docker run --name brcms-content-repository \
    -p 127.0.0.1:9090:8080 \
    -e POSTGRES_URL='jdbc:postgresql://brcms-postgres:5432/brcms' \
    -e POSTGRES_USERNAME='brcms' \
    -e POSTGRES_PASSWORD='brcms' \
    --network brcms \
    -d \
    firatkucuk/brcms-content-repository:1.0
```

## 2.2. Running content management system

Content management system is the API facade of all brcms system. By default it's running on 8080 port.

you can run directly using spring-boot maven plug-in.

```shell
./mvnw install -DskipTests && ./mvnw -pl cms-backend spring-boot:run
```

or after compilation you can just run the jar file.

```shell
./mvnw clean compile package -DskipTests
cd cms-backend/target/
java -jar cms-backend.jar
```

It's possible to create a docker image and running it via docker image

```shell
cd cms-backend/
docker build -t bloomreach/cms-backend:1.0 .
```

Then running and connecting to actively running postgres server:

```shell
docker run --name brcms-cms-backend \
    -p 127.0.0.1:8080:8080 \
    -e CONTENT_REPOSITORY_HOST='http://brcms-content-repository:8080' \
    -e CMS_LOAD_TEST_DATA='true' \
    --network brcms \
    -d \
    bloomreach/cms-backend:1.0
```

Or you can just run uploaded version from docker hub:

```shell
docker run --name brcms-cms-backend \
    -p 127.0.0.1:9090:8080 \
    -e POSTGRES_URL='jdbc:postgresql://brcms-postgres:5432/brcms' \
    -e POSTGRES_USERNAME='brcms' \
    -e POSTGRES_PASSWORD='brcms' \
    --network brcms \
    -d \
    firatkucuk/brcms-cms-backend:1.0
```
