#!/bin/bash

DOCKER_USER=zjor
IMAGE=get-that-page-api
VERSION=$(git rev-parse --short HEAD)
set -x

docker build -t ${IMAGE} .
docker tag ${IMAGE} ${DOCKER_USER}/${IMAGE}:${VERSION}
docker push ${DOCKER_USER}/${IMAGE}:${VERSION}
