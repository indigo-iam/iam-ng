FROM mysql:5.7

ENV MYSQL_ROOT_PASSWORD=pwd
ENV MYSQL_DATABASE=iam

ADD ./00_init.sql /docker-entrypoint-initdb.d/
