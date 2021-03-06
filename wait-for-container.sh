#!/usr/bin/env bash
while : ; do
    sleep 1;
    server="-X GET elasticserver:9200"
    curl -f $server && break || echo "Elasticsearch server isn't responding!"
done;
java -Dnetworkaddress.cache.ttl=60 -jar -Dspring.profiles.active=production ../search/target/search.jar
