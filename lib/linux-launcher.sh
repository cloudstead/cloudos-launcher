#!/bin/bash
#
# This script starts the Cloudstead Launcher server API.
# When the API is up and running, if a windowing system is available it will opens a web browser
# otherwise it prints the API endpoint to stdout
#

function die {
  echo "${1}" >&2 && exit 1
}

THIS_DIR="$(cd $(dirname ${0}) && pwd)"

JRE_DIR="${THIS_DIR}/jre"
if [ ! -e "${JRE_DIR}/bin/java" ] ; then
  die "Bad JRE? No java executable found: ${JRE_DIR}/bin/java"
fi

JAR="$(find ${THIS_DIR}/lib -type f -name "cloudos-launcher-*.jar" | head -1)"
if [ -z "${JAR}" ] ; then
  die "No cloudos-launcher-*.jar found in ${THIS_DIR}/lib"
fi

if [ -z "${JAVA_OPTS}" ] ; then
  JAVA_OPTS="-Xms256m -Xmx1536m"
fi

${JRE_DIR}/bin/java -jar ${JAR} ${JAVA_OPTS}
