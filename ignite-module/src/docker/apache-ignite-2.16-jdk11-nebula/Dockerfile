FROM apacheignite/ignite:2.16.0-jdk11

RUN apk update && apk add wget unzip

RUN wget https://www.gridgain.com/media/control-center-agent/control-center-agent-2.16.0.0.zip -P /tmp/
RUN unzip /tmp/control-center-agent-2.16.0.0.zip -d /opt/ignite/apache-ignite