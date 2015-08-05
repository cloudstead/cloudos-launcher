#!/bin/bash
#
# Run the cloudos-launcher directly from the source tree, without building the platform-specific apps
#
# Usage: run.sh [debug [debug-port]]
#
# Environment variables:
#
#  LAUNCHER_LISTEN_ADDR -- IP address to bind server's TCP endpoint. Default is 0.0.0.0 (all network interfaces)
#
#  LAUNCHER_PORT        -- port to listen on. Default is 18080
#
#  LAUNCHER_ASSETS_DIR  -- load static assets from here. Default is csapp/dist relative to cloudos-launcher base directory
#

function die {
  echo "${1}" >&2 && exit 1
}

BASE_DIR="$(cd $(dirname ${0})/.. && pwd)"

JAR=$(find target -maxdepth 1 -type f -name "cloudos-launcher-*.jar" | head -1)
if [ -z ${JAR} ] ; then
  cd ${BASE_DIR} && mvn package
fi

DEBUG="${1}"
DEBUG_OPTS=""
if [ ! -z ${DEBUG} ] && [ ${DEBUG} = "debug" ] ; then
  ARG_LEN=$(echo -n "${1}" | wc -c)
  ARG_NUMERIC_LEN=$(echo -n "${1}" | tr -dc [:digit:] | wc -c)  # strip all non-digits
  shift
  if [ ${ARG_LEN} -eq ${ARG_NUMERIC_LEN} ] ; then
    # Second arg is the debug port
    DEBUG_PORT="${1}"
    shift
  else
    DEBUG_PORT=5005
  fi
  DEBUG_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=${DEBUG_PORT}"
fi

if [ -z "${LAUNCHER_PORT}" ] ; then
  LAUNCHER_PORT="18080"
fi

if [ -z "${LAUNCHER_ASSETS_DIR}" ] ; then
  LAUNCHER_ASSETS_DIR="${BASE_DIR}/csapp/dist"
fi

if [ -z "${LAUNCHER_LISTEN_ADDR}" ] ; then
  LAUNCHER_LISTEN_ADDR="0.0.0.0"
fi

cd ${BASE_DIR} &&
  LAUNCHER_PORT=${LAUNCHER_PORT} \
  LAUNCHER_ASSETS_DIR=${LAUNCHER_ASSETS_DIR} \
  LAUNCHER_LISTEN_ADDR=${LAUNCHER_LISTEN_ADDR} \
  java ${DEBUG_OPTS} -jar ${JAR}
