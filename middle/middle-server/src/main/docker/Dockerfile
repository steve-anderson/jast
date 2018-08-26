# Copyright 2018, Steven W. Anderson
FROM dregistry.swahome.net:7443/research/devops/wildfly:13.0.0.Final

USER jboss

COPY configuration/ /opt/jboss/wildfly/standalone/configuration/

COPY libs/ /opt/jboss/wildfly/standalone/deployments/
