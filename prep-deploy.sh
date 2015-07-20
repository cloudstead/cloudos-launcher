#!/bin/bash

function die {
  echo "${1}" >&2 && exit 1
}

BASE_DIR="$(cd $(dirname ${0}) && pwd)"
cd ${BASE_DIR}

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
  jre_dir="$(find ${arch_dir} -type d -mindepth 1 -maxdepth 1)"

  rm -rf ${build_dir}
  mkdir -p ${build_dir}
  cp target/cloudos-launcher.exe ${build_dir}
  cp -R ${jre_dir} ${build_dir}/jre
  ARTIFACT="${BASE_DIR}/target/${archive_dir}.zip"

  cd $(dirname ${build_dir})
  zip -qr ${ARTIFACT} ${archive_dir} || die "Error building: ${ARTIFACT}"
  echo "ARTIFACT: ${ARTIFACT}"
done

# MacOS -- create DMG file. Use different process when building on Linux vs Mac
ARTIFACT="${BASE_DIR}/target/cloudos_launcher.dmg"
DMG_SRC_DIR="target/macosx/Cloudstead Launcher.app"
DMG_SIZE=$(expr $(du -mxs ${BASE_DIR}/target/macosx | awk '{print $1}') + 2)
DMG_TITLE="Cloudstead Launcher"
TEMP_ARTIFACT=${ARTIFACT}.tmp.dmg
rm -f ${ARTIFACT} ${TEMP_ARTIFACT}

if [[ $(uname -a) =~ "Linux" ]] ; then
  # create blank DMG
  dd if=/dev/zero of=${TEMP_ARTIFACT} bs=1k count=${DMG_SIZE}

  # create and mount filesystem
  ARTIFACT_MOUNT=$(mktemp -d "/mnt/$(basename ${TEMP_ARTIFACT}).XXXXXXX")
  mkfs.hfsplus -v "${DMG_TITLE}" ${TEMP_ARTIFACT}
  mount -o loop ${TEMP_ARTIFACT} ${ARTIFACT_MOUNT}

  # Copy files to DMG
  cp -R ${DMG_SRC_DIR} ${ARTIFACT_MOUNT}

  # unmount it and move to target dir
  umount ${ARTIFACT_MOUNT}
  rmdir ${ARTIFACT_MOUNT}
  mv ${TEMP_ARTIFACT} ${ARTIFACT}
  echo "ARTIFACT: ${ARTIFACT}"

elif [[ $(uname -a) =~ "Darwin" ]] ; then

  ARTIFACT_MOUNT="/Volumes/${DMG_TITLE}"
  hdiutil detach "${ARTIFACT_MOUNT}" 2> /dev/null && echo "Detached existing mount: ${ARTIFACT_MOUNT}"

  # create blank DMG
  hdiutil create -srcfolder "${DMG_SRC_DIR}" -volname "${DMG_TITLE}" -fs HFS+ \
      -fsargs "-c c=64,a=16,e=16" -format UDRW -size ${DMG_SIZE}m ${TEMP_ARTIFACT}

  # mount it as R/W
  device=$(hdiutil attach -readwrite -noverify -noautoopen "${TEMP_ARTIFACT}" | egrep '^/dev/' | sed 1q | awk '{print $1}')

  # Set background image and other stuffs
  # todo...

  # Finalize and unmount the DMG
  chmod -Rf go-w ${ARTIFACT_MOUNT}
  sync ; sync
  hdiutil detach ${device}
  hdiutil convert "/${TEMP_ARTIFACT}" -format UDZO -imagekey zlib-level=9 -o "${ARTIFACT}"
  rm -f ${TEMP_ARTIFACT}
  echo "ARTIFACT: ${ARTIFACT}"

else
  die "Building MacOS bundles not supported on platform: $(uname -a)"
fi
