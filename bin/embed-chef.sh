#!/bin/bash
#
# Copy chef-related assets to target/classes, including app bundles and chef deploy scripts
# They will then be accessible from the launcher's server-side code
#

function die {
  echo "${1}" >&2 && exit 1
}

BASE_DIR="$(cd $(dirname ${0})/.. && pwd)"

# Determine CLOUDOS_DIR
if [ -z "${CLOUDOS_DIR}" ] ; then
  CLOUDOS_DIR=$(echo ${BASE_DIR} | egrep -o '.+/cloudos/')
  if [ -z "${CLOUDOS_DIR}" ] ; then
    die "No cloudos-dir provided"
  else
    echo "Detected CLOUDOS_DIR=${CLOUDOS_DIR}"
  fi
else
  # normalize possible relative paths, ensure it's actually a directory
  CLOUDOS_DIR=$(cd ${CLOUDOS_DIR} && pwd) || usage && die "Invalid CLOUDOS_DIR env var: ${CLOUDOS_DIR}"
fi

# Copy app bundles
CLOUDOS_APPS="${CLOUDOS_DIR}/cloudos-apps"
APPS_DEST="${BASE_DIR}/target/classes/app-bundles"
mkdir -p ${APPS_DEST}
for bundle in $(find $(find ${CLOUDOS_APPS} -maxdepth 3 -type d -name dist) -type f -name "*-bundle.tar.gz") ; do
  cp ${bundle} ${APPS_DEST} || die "Error copying app: ${bundle}"
done

# Copy chef scripts
CLOUDOS_LIB="${CLOUDOS_DIR}/cloudos-lib"
CLOUDOS_SERVER="${CLOUDOS_DIR}/cloudos-server"

CHEF_MASTER="${BASE_DIR}/target/classes/chef-master"
mkdir -p ${CHEF_MASTER}

CHEF_MASTER_MANIFEST="${BASE_DIR}/target/classes/chef-master/manifest.txt"
cat /dev/null > ${CHEF_MASTER_MANIFEST}

for f in JSON.sh solo.rb install.sh uninstall.sh deploy_lib.sh ; do
  cp ${CLOUDOS_LIB}/chef-repo/${f} ${CHEF_MASTER}/
  echo "${f}" >> ${CHEF_MASTER_MANIFEST}
done && \
cp ${CLOUDOS_SERVER}/chef-repo/deploy.sh ${CHEF_MASTER}/
echo "deploy.sh" >> ${CHEF_MASTER_MANIFEST}
