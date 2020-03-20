FROM openjdk:8

RUN mkdir /iam-api
WORKDIR /iam-api
COPY iam-api.jar /iam-api/

ENV IAM_JAVA_OPTS -Dspring.profiles.active=h2

CMD java -XX:MaxRAMFraction=1 -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap ${IAM_JAVA_OPTS} -jar iam-api.jar

# Embed TINI since compose v3 syntax do not support the init 
# option to run docker --init
#
ENV TINI_VERSION v0.18.0
ADD https://github.com/krallin/tini/releases/download/${TINI_VERSION}/tini /tini
RUN chmod +x /tini
ENTRYPOINT ["/tini", "--"]
