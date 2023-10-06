#!/bin/bash

NS=app-get-that-page
APP=get-that-page-api
VERSION=$(git rev-parse --short HEAD)

set -x

helm upgrade --namespace ${NS} --create-namespace --install ${APP} --set version=${VERSION} ./ops/${APP}