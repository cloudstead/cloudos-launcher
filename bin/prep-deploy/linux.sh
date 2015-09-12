#!/bin/bash

function die {
  echo "${1}" >&2 && exit 1
}

SCRIPT_DIR="$(cd $(dirname ${0}) && pwd)"
BASE_DIR="$(cd ${SCRIPT_DIR}/../.. && pwd)"

JRE_DIR="${BASE_DIR}/jre/unrolled"
if [ ! -d ${JRE_DIR} ] ; then
  die "No JREs found in ${JRE_DIR}. Put JRE tarballs in jre/ directory, then run jre/unroll.sh"
fi

NUM_JRES=$(find ${JRE_DIR} -mindepth 1 -maxdepth 1 -type d | wc -l | tr -d ' ')
if [ ${NUM_JRES} -ne 1 ] ; then
  die "Expected one JRE version in ${JRE_DIR}, found ${NUM_JRES}"
fi

JAR="$(find ${BASE_DIR}/target -maxdepth 1 -type f -name cloudos-launcher-*.jar | head -1)"
if [ -z "${JAR}" ] ; then
  MAVEN="mvn -DskipTests=true -Dcheckstyle.skip=true"
  ${MAVEN} package || die "Error building cloudos-launcher jar"
  JAR="$(find ${BASE_DIR}/target -maxdepth 1 -type f -name cloudos-launcher-*.jar | head -1)"
  if [ -z "${JAR}" ] ; then
    die "Error building cloudos-launcher jar"
  fi
fi

# Linux -- create one zipfile per arch
ARCH_DIRS="$(find $(find ${JRE_DIR} -maxdepth 2 -type d -name linux) -mindepth 1 -maxdepth 1 -type d)"
for arch_dir in ${ARCH_DIRS} ; do
  cd ${BASE_DIR}
  arch="$(basename ${arch_dir})"
  archive_dir="cloudos_launcher_linux_${arch}"
  build_dir="target/${archive_dir}"
  jre_dir="$(find ${arch_dir} -mindepth 1 -maxdepth 1 -type d)"

  rm -rf ${build_dir}
  mkdir -p ${build_dir}/lib
  cp "${JAR}" ${build_dir}/lib
  cp -R ${jre_dir} ${build_dir}/jre
  cp ${BASE_DIR}/lib/linux-launcher.sh ${build_dir}/cloudstead-launcher.sh
  ARTIFACT="${BASE_DIR}/target/${archive_dir}.tar.gz"

  cd $(dirname ${build_dir})
  tar czf ${ARTIFACT} ${archive_dir} || die "Error creating tarball: ${ARTIFACT} from ${archive_dir}"
  echo "ARTIFACT: ${ARTIFACT}"
done
