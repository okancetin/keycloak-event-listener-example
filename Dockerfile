FROM maven:3.6.0-jdk-11-slim AS build
COPY /src /opt/jboss/keycloak/event-listener-example/src
COPY /src /opt/jboss/keycloak/event-listener-example/src
COPY /pom.xml /opt/jboss/keycloak/event-listener-example/pom.xml
RUN mvn dependency:go-offline -f /opt/jboss/keycloak/event-listener-example/pom.xml package
COPY --from=build /opt/jboss/keycloak/target/event-listener-example-*.jar /opt/jboss/keycloak/standalone/deployments/
