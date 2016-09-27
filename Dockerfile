FROM java:8-jre-alpine

RUN mkdir -p /opt/target && mkdir -p /opt/src/main

COPY target/yoAlert-0.0.1.jar /opt/target/yoAlert-0.0.1.jar
#COPY src/main/webapp /opt/src/main/webapp
#COPY mysqlSchema.sh /opt/mysqlSchema.sh
COPY init.sh /opt/init.sh

RUN chmod a+x /opt/init.sh

MAINTAINER Francisco Giana <gianafrancisco@gmail.com>

EXPOSE 8080

ENTRYPOINT /opt/init.sh