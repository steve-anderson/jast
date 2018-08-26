#!/bin/bash
docker run -d -h middle-server -p 8082:8080 -p 5002:5000 -p 9992:9990 --add-host "activemq:10.0.2.20" --add-host "postgres:10.0.2.20" --name back-server dregistry.swahome.net:7443/research/jast/back-server:1.0 /opt/jboss/wildfly/bin/standalone.sh --server-config=standalone-full.xml -Djboss.node.name=back-server --debug 5000 -b 0.0.0.0 -bmanagement 0.0.0.0
