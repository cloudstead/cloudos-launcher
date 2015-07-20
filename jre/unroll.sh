#!/bin/bash

THIS_DIR=$(cd $(dirname $0) && pwd)

jre_bundles=$(cd ${THIS_DIR} && ls -1 jre-*.tar.gz)
for jre_bundle in ${jre_bundles} ; do
  version=$(echo $jre_bundle | awk -F '-' '{print $2}')
  platform=$(echo $jre_bundle | awk -F '-' '{print $3}')
  arch=$(echo $jre_bundle | awk -F '-' '{print $4}' | awk -F '.' '{print $1}')
  jre_dir=${THIS_DIR}/unrolled/${version}/${platform}/${arch}
  mkdir -p ${jre_dir}
  cd ${jre_dir} && tar xzf ${THIS_DIR}/${jre_bundle}
done