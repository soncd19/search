FROM maven:3-alpine

LABEL maintainer = "Bkav SonCD - Shadow Good Kid"

WORKDIR /search/

VOLUME /var/log

COPY /pom.xml /search

COPY /src/ /search/src

COPY /wait-for-container.sh /wait-for-container.sh

RUN mvn clean install -Dmaven.test.skip=true

EXPOSE 8092

ENTRYPOINT ["sh", "/wait-for-container.sh"]
