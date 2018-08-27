#!/bin/bash
docker run -d -h front-server -p 8080:8080 -p 5000:5000 --link middle-server --name front-server dregistry.swahome.net:7443/research/jast/front-server:1.0 /opt/jboss/wildfly/bin/standalone.sh --debug 5000 -b 0.0.0.0 -bmanagement 0.0.0.0
