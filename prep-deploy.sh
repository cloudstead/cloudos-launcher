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

# Build Windows
# ${SCRIPT_BASE}/windows.sh || die "Error building Windows app"

# Build Linux
# ${SCRIPT_BASE}/linux.sh || die "Error building Linux app"

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
