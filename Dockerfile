FROM maven:3-alpine

LABEL maintainer = "Bkav SonCD - Shadow Good Kid"

WORKDIR /search/

VOLUME /var/log

COPY /pom.xml /search

COPY /src/ /search/src

COPY /wait-for-container.sh /wait-for-container.sh

RUN mvn clean install -Dmaven.test.skip=true

EXPOSE 8080 9200 9300

ENTRYPOINT ["sh", "/wait-for-container.sh"]