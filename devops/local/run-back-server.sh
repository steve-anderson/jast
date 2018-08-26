#!/bin/bash
docker run -d -h middle-server -p 8083:8080 -p 5002:5001 -p 9991:9990 --add-host "activemq:10.0.2.20" --add-host "postgres:10.0.2.20" --name back-server dregistry.swahome.net:7443/research/jast/back-server:1.0 /opt/jboss/wildfly/bin/standalone.sh --server-config=standalone-full.xml -Djboss.node.name=back-server --debug 5002 -b 0.0.0.0 -bmanagement 0.0.0.0
