# JAST
Parts of this project are tied to infrastructure that I run using VirtualBox VMs and/or an kubernetes cluster inside LXD nodes.

## Overview
The purpose of the java application server test (JAST) is to have three Java EE 7 applications in separate Docker containers doing remote EJB calls and MDB processing. These application process data in the order of `front-server` to `middle-server` to `back-server`.

The first container, `front-server`, contains a JAX-RS, Stateless EJB, `MessageController`, providing two REST calls dealing with the message data. This EJB calls a remote EJB, `MessageService`, in `middle-server` for all processing.

The second container, `middle-server`, contains a Stateless EJB, `MessageServiceBean`. For saving the message, this EJB transforms `CreateMessage` to JSON and sends the JSON to an external ActiveMQ queue, `createMessageQueue`. The [ActiveMQ](http://activemq.apache.org/) container is not ActiveMQ Artemis. For reading all the saved messages, `MessageServiceBean` does a remote EJB call to `MessageDao` on `back-server`. 

The last container, `back-server`, contains a MDB, `MessageSaver`, and a remote Stateless EJB, `MessageDaoBean`. `MessageSaver` processes JSON for `CreateMessage` and saves it using a local EJB call to `MessageDaoBean`. `MessageDaoBean` uses JPA to read and write to a [PostgreSQL](https://www.postgresql.org/).

## Building
Run `gradlew buildDockerImage` for local Docker images. 

I have a Docker repo running in a VM with Gitlab at `dregistry.swahome.net:7443`

Run `gradlew pushDockerImage` to push the image to the repo.

## Top Project Structure
The top directories, `back`, `front` and `middle` contain the `Gradle` subprojects that build the containers, internal JAR and test remote EJB clients.

The `devops` directory contains ansible scripts for building up the Docker containers on a remote VM, `lce.swahome.net`, that runs Atomic CentOS. The setup of this VM is beyond this write up. The local directory contains shell scripts for running docker images on my development Linux VM that hits `lce.swahome.net`

## Implementation
The current implementation uses Wildfly. Later, I hope to port this project to Wildfly Swarm, TomEE, etc.

`back-server` and `middle-server` run using standalone-full.xml.
`front-server` runs using standalone.xml

The `front-server` runs on `lce.swahome.net`, port 8080. 

See the ansible scripts for the other ports. 

The account for the Wildfly console is `admin`, password `ChangeM3=`. The remote EJB account is `ejbclient`, password `ChangeM3=`. 

I used ActiveMQ since it can be replaced with AmazonMQ when trying out AWS.

### Example REST
```text
GET http://lce.swahome.net:8080/front-server/api/messages
Accept: */*
Cache-Control: no-cache

###
PUT http://lce.swahome.net:8080/front-server/api/messages
Content-Type: application/json

{
  "sender": "steve",
  "receiver": "tom",
  "body": "I hope this works"
}

###
```
