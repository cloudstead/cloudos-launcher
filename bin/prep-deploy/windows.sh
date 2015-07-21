#!/bin/bash

function die {
  echo "${1}" >&2 && exit 1
}

# script assumes that ../.. is the cloudos-launcher base directory
SCRIPT_DIR="$(cd $(dirname ${0}) && pwd)"
BASE_DIR="$(cd ${SCRIPT_DIR}/../.. && pwd)"

LAUNCHER_EXE="${BASE_DIR}/target/cloudos-launcher.exe"
if [ ! -f "${LAUNCHER_EXE}" ] ; then
   mvn -DskipTests=true package || die "Error building cloudos-launcher.exe"
  if [ ! -f "${LAUNCHER_EXE}" ] ; then
    die "Error building cloudos-launcher.exe"
  fi
fi

JRE_DIR="${BASE_DIR}/jre/unrolled"
if [ ! -d ${JRE_DIR} ] ; then
  die "No JREs found in ${JRE_DIR}. Put JRE tarballs in jre/ directory, then run jre/unroll.sh"
fi

NUM_JRES=$(find ${JRE_DIR} -mindepth 1 -maxdepth 1 -type d | wc -l | tr -d ' ')
if [ ${NUM_JRES} -ne 1 ] ; then
  die "Expected one JRE version in ${JRE_DIR}, found ${NUM_JRES}"
fi

# Windows -- create one zipfile per arch
ARCH_DIRS="$(find $(find ${JRE_DIR} -maxdepth 2 -type d -name windows) -mindepth 1 -maxdepth 1 -type d)"
for arch_dir in ${ARCH_DIRS} ; do
  cd ${BASE_DIR}
  arch="$(basename ${arch_dir})"
  archive_dir="cloudos_launcher_win_${arch}"
  build_dir="target/${archive_dir}"
  jre_dir="$(find ${arch_dir} -mindepth 1 -maxdepth 1 -type d)"

  rm -rf ${build_dir}
  mkdir -p ${build_dir}
  cp target/cloudos-launcher.exe ${build_dir}
  cp -R ${jre_dir} ${build_dir}/jre
  ARTIFACT="${BASE_DIR}/target/${archive_dir}.zip"

  cd $(dirname ${build_dir})
  zip -qr ${ARTIFACT} ${archive_dir} || die "Error creating zipfile: ${ARTIFACT} from ${archive_dir}"
  echo "ARTIFACT: ${ARTIFACT}"
done
