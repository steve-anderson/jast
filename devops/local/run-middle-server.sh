#!/bin/bash
docker run -d -h middle-server -p 8081:8080 -p 5001:5001 -p 9990:9990 --add-host "activemq:10.0.2.20" --name middle-server dregistry.swahome.net:7443/research/jast/middle-server:1.0 /opt/jboss/wildfly/bin/standalone.sh --server-config=standalone-full.xml -Djboss.node.name=middle-server --debug 5001 -b 0.0.0.0 -bmanagement 0.0.0.0
