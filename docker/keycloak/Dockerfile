FROM jboss/keycloak:9.0.3

USER root

ENV IAM_ADMIN_USER=admin
ENV IAM_ADMIN_PASSWORD=password

ENV KEYCLOAK_USER=admin
ENV KEYCLOAK_PASSWORD=password

ADD iam /opt/jboss/keycloak/iam
RUN chmod +x /opt/jboss/keycloak/iam/*.sh
RUN microdnf update && microdnf install java-11-openjdk-devel && microdnf clean all

USER jboss

ENTRYPOINT  [ "/opt/jboss/keycloak/iam/docker-entrypoint.sh" ]
CMD ["-b", "0.0.0.0"]
