#!/bin/bash

function die {
  echo "${1}" >&2 && exit 1
}

BASE_DIR="$(cd $(dirname ${0}) && pwd)"
SCRIPT_BASE="${BASE_DIR}/bin/prep-deploy"

JRE_DIR="${BASE_DIR}/jre/unrolled"
if [ ! -d ${JRE_DIR} ] ; then
  die "No JREs found in ${JRE_DIR}. Put JRE tarballs in jre/ directory, then run jre/unroll.sh"
fi

NUM_JRES=$(find ${JRE_DIR} -mindepth 1 -maxdepth 1 -type d | wc -l | tr -d ' ')
if [ ${NUM_JRES} -ne 1 ] ; then
  die "Expected one JRE version in ${JRE_DIR}, found ${NUM_JRES}"
fi

JAR="$(find ${BASE_DIR}/target -maxdepth 1 -type f -name "cloudos-launcher-*.jar" | head -1)"
if [ -z "${JAR}" ] ; then
  die "No cloudos-launcher-*.jar found in ${BASE_DIR}/target"
fi

# API stuff -- todo: move these to maven pom.xml

# generate API examples
if [ ! -d ${BASE_DIR}/target/api-examples ] ; then
  mvn verify || die "Error generating API examples"
fi
cd ${BASE_DIR}/target && mkdir -p classes/web && cp -R api-examples classes/web || die "Error copying api-examples to web dir"

# generate API docs
if [ ! -d ${BASE_DIR}/target/miredot ] ; then
  ${BASE_DIR}/gen-apidocs.sh || die "Error generating API docs"
fi
cd ${BASE_DIR}/target && mkdir -p classes/web && cp -R miredot classes/web/api-docs || die "Error copying api-docs to web dir"

# generate build stamps
BMETA="${BASE_DIR}/target/classes/build-meta"
mkdir -p ${BMETA}
echo "$(date +%Y%m%d-%H%M%S)" > ${BMETA}/timestamp
echo "$(uname -a)" > ${BMETA}/sysinfo

# re-roll jar file with API docs and build timestamp
MAVEN="mvn -DskipTests=true -Dcheckstyle.skip=true"
cd ${BASE_DIR} && ${MAVEN} package

# Build Windows
${SCRIPT_BASE}/windows.sh || die "Error building Windows app"

# Build Linux
${SCRIPT_BASE}/linux.sh || die "Error building Linux app"

# Build MacOS
if [[ $(uname -a) =~ "Linux" ]] ; then
  # Building on Linux
  ${SCRIPT_BASE}/macosx_on_linux.sh || die "Error building MacOS app"

elif [[ $(uname -a) =~ "Darwin" ]] ; then
  # Building on MacOS
  ${SCRIPT_BASE}/macosx_on_macosx.sh || die "Error building MacOS app"

else
  die "Building MacOS app only supported on Linux or Darwin (MacOS) platforms. Your platform was: $(uname -a)"
fi
