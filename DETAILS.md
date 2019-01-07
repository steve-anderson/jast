# back

## back-api
* `CreateMessage` - JSON message sent over the JMS Queue, `createMessageQueue`.
* `Message` - A message sent to someone. Used in Remote EJB calls.
* `MessageDao` - Remote EJB interface.

## back-server

### Java Classes
* `EMessage` - JPA Entity for storing a `Message`.
* `MessageDaoBean` - Stateless EJB with local and remote interface.
* `MessageSaver` - MDB listening to the JMS Queue, `createMessageQueue`.

### Docker Image
* `standalone-full.xml`
  * `activemq-remote` resource adapter for JMS provider. `activemq-rar.rar` is put into the wildfly `deployment` directory.
  * `java:jboss/datasources/JastDs` postgres datasource. We can use environment variables in the file, like `${env.DB_PASSWORD}`. The database driver, `postgresql-42.2.4.jar`, is put into the wildfly `deployment` directory.

# middle

## middle-api
* `MessageService` - Remote EJB interface.

## middle-server

### Java Classes
* `MessageServiceBean` - Stateless EJB with remote interface
  * Calls the remote EJB, `MessageDaoBean` on the `back-server` for `get()`
  * Produces the `CreateMessage` to the JMS Queue, `createMessageQueue`.

### Webapp
* `jboss-ejb-client.xml` - sets the outbound connection to `remote-ejb-connection`.

### Docker Image
* `standalone-full.xml`
  * `activemq-remote` resource adapter for JMS provider. `activemq-rar.rar` is put into the wildfly `deployment` directory.
  * `remote-ejb-connection` sets the outbound socket to `remote-ejb` along with the security stuff.
  * `remote-ejb` defines the host and port which is the service, `back-server`, on port 8080. Wildfly allows Remote EJB calls over the HTTP port and does a protocol upgrade to handle it. This protocol upgrade fails on the istio setup BTW. (Same issue for http/2 upgrades. There is a ticket on it in the istio project.)

# front

## front-server

### Java Classes
* `GetMessagesResponse` - JSON response to the GET call.
* `MessageController` - JAX-RS Stateless EJB providing the REST API.
* `StatefulServlet` - A simple servlet for testing sessions.

### Webapp
* `jboss-ejb-client.xml` - sets the outbound connection to `remote-ejb-connection`.

### Docker Image
* `standalone.xml`
  * `remote-ejb-connection` sets the outbound socket to `remote-ejb` along with the security stuff.
  * `remote-ejb` defines the host and port which is the service, `middle-server`, on port 8080.

# devops

## k8s
* `postgres.yml` - Create a Service, a ConfigMap with the SQL for initializing the database, and a StatefulSet for running postgres. It uses a HostOnly volume mount.
* `activemq.yml` - Create a Service and a StatefulSet for running activemq.
* `back-server.yml` - Create a Service and a Deployment for running the `back-server` image.
* `middle-server.yml` - Create a Service and a Deployment for running the `middle-server` image.
* `front-server.yml` - Create a Service and a Deployment for running the `front-server` image.
* `front-server-ingress.yml` - Create a nginx sticky session ingress mapping to the `front-server`
