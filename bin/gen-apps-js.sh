#!/bin/bash
#
# Generate apps.js file and copy metadata files so they are visible to the EmberJS app
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

CLOUDOS_APPS="${CLOUDOS_DIR}/cloudos-apps"

# Cloudstead launch helper tool
MANIFESTS="$(find ${CLOUDOS_APPS}/apps -maxdepth 2 -type f -name cloudos-manifest.json)"
APPS_JS="${BASE_DIR}/target/classes/web/js/apps.js"

# Build apps.js if it does not exist
if [ ! -f ${APPS_JS} ] ; then

  JSON="java -cp $(find ${CLOUDOS_DIR}/cloudos-server/target -maxdepth 1 -type f -name "cloudos-server-*.jar") org.cobbzilla.util.json.main.JsonEditor"
  apps=""
  for manifest in ${MANIFESTS} ; do
    app=$(basename $(dirname ${manifest}))
    level=$(cat ${manifest} | ${JSON} --path level --operation read | tr -d '"') || die "Error determining level: ${manifest}"
    if [ -z "${level}" ] ; then
      level="app"
    fi
    apps="${apps}
  APPS.${level}.$(echo ${app} | tr '-' '_')=\"${app}\";"
  done

    cat > ${APPS_JS} <<EOF
  /* generated by ${0} on $(date) */
  APPS = {
    init: {}, system: {}, cloudos: {}, app: {}
  };
  ${apps}
EOF

fi

# Copy per-app manifest, config-metadata and translations, if found
for manifest in ${MANIFESTS} ; do
  app_dir="$(dirname ${manifest})"
  app="$(basename ${app_dir})"
  app_js_dir="$(dirname ${APPS_JS})/apps/${app}"

  mkdir -p "${app_js_dir}" || die "Error creating directory: ${app_js_dir}"

  app_databags="${app_dir}/dist/build/chef/data_bags/${app}/*.json"
  if [ ! -z "$(ls -1 ${app_databags} 2> /dev/null)" ] ; then
    cp ${app_databags} ${app_js_dir} || die "Error copying ${app_databags} -> ${app_js_dir}"
  fi
done
