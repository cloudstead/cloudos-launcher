#!/bin/bash

function die {
  echo "${1}" >&2 && exit 1
}

THIS_DIR="$(cd $(dirname ${0}) && pwd)"

JRE_DIR="${THIS_DIR}/jre"
if [ ! -e "${JRE_DIR}/bin/java" ] ; then
  die "Bad JRE? No java executable found: ${JRE_DIR}/bin/java"
fi

JAR="$(find ${THIS_DIR}/lib -type f -name cloudos-launcher-*.jar | head -1)"
if [ -z "${JAR}" ] ; then
  die "No cloudos-launcher-*.jar found in ${THIS_DIR}/lib"
fi

${JRE_DIR}/bin/java -jar ${JAR}
