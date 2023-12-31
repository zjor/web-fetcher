#!/bin/bash

source .env
cat .env

NAMESPACE=app-get-that-page
APP=get-that-page-api

kubectl delete secret ${APP}-secrets -n ${NAMESPACE}

kubectl create secret generic ${APP}-secrets \
  --from-literal=SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL} \
  --from-literal=SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME} \
  --from-literal=SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD} \
  -n ${NAMESPACE}


