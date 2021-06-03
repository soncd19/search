#!/usr/bin/env bash

imageName=search
docker build -t $imageName -f Dockerfile  .

#mvn clean install -Dmaven.test.skip=true
docker-compose -f docker-compose.yml up --build
