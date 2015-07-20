#!/bin/bash

function die {
  echo "${1}" >&2 && exit 1
}

BASE_DIR="$(cd $(dirname ${0})/.. && pwd)"

JAR=$(find target -maxdepth 1 -type f -name "cloudos-launcher-*.jar")
if [ -z ${JAR} ] ; then
  cd ${BASE_DIR} && mvn package
fi

cd ${BASE_DIR} && ASSETS_DIR=${BASE_DIR}/csapp/dist java -jar ${JAR}
