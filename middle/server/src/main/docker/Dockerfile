# Copyright 2018, Steven W. Anderson
FROM dregistry.swahome.net:7443/research/devops/wildfly:13.0.0.Final

USER jboss

COPY standalone-full.xml /opt/jboss/wildfly/standalone/configuration
COPY application-users.properties /opt/jboss/wildfly/standalone/configuration
COPY mgmt-users.properties /opt/jboss/wildfly/standalone/configuration

COPY activemq-rar.rar /opt/jboss/wildfly/standalone/deployments/

COPY libs/ /opt/jboss/wildfly/standalone/deployments/
