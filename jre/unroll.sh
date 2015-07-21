#!/bin/bash

THIS_DIR=$(cd $(dirname $0) && pwd)
JRE_HOME="${THIS_DIR}/unrolled"
JRE_PROPS="${JRE_HOME}/jre.properties"

rm -f ${JRE_PROPS}

jre_bundles=$(cd ${THIS_DIR} && ls -1 jre-*.tar.gz)
for jre_bundle in ${jre_bundles} ; do
  version=$(echo $jre_bundle | awk -F '-' '{print $2}')
  platform=$(echo $jre_bundle | awk -F '-' '{print $3}')
  arch=$(echo $jre_bundle | awk -F '-' '{print $4}' | awk -F '.' '{print $1}')
  jre_dir=${JRE_HOME}/${version}/${platform}/${arch}
  mkdir -p ${jre_dir}
  cd ${jre_dir} && tar xzf ${THIS_DIR}/${jre_bundle}

  # Find java executable, JAVA_HOME is one directory above that
  if [ ${platform} = "windows" ] ; then
    JAVA=$(find $(find ${jre_dir} -type d -name bin) -type f -name java.exe)
  else
    JAVA=$(find $(find ${jre_dir} -type d -name bin) -type f -name java)
  fi
  if [ -z "${JAVA}" ] ; then
    echo "Error finding java in $(pwd)"
    exit 1
  fi
  JAVA_HOME=$(dirname $(dirname ${JAVA}))
  echo "jre.${platform}.${arch}.home=${JAVA_HOME}" >> ${JRE_PROPS}
done
