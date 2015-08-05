#!/bin/bash

function die {
  echo "${1}" >&2 && exit 1
}

SCRIPT_DIR="$(cd $(dirname ${0}) && pwd)"
BASE_DIR="$(cd ${SCRIPT_DIR}/../.. && pwd)"

# MacOS -- create DMG file. Use different process when building on Linux vs Mac
ARTIFACT="${BASE_DIR}/target/cloudos_launcher.dmg"
APP_DIR="Cloudstead Launcher.app"
DMG_SRC_DIR="${BASE_DIR}/target/macosx/${APP_DIR}"
DMG_SIZE=$(expr $(du -kxs ${BASE_DIR}/target/macosx | awk '{print $1}') + 1024)
DMG_TITLE="Cloudstead Launcher"
TEMP_ARTIFACT=${ARTIFACT}.tmp.dmg
rm -f ${ARTIFACT} ${TEMP_ARTIFACT}

ARTIFACT_MOUNT="/Volumes/${DMG_TITLE}"
existing_device="$(mount | grep "${ARTIFACT_MOUNT}" | awk '{print $1}' | tr -d ' ')"
if [ ! -z "${existing_device}" ] ; then
  hdiutil detach "${existing_device}" 2> /dev/null && echo "Detached existing mount: ${ARTIFACT_MOUNT} (device ${existing_device})"
fi

# create blank DMG
hdiutil create -srcfolder "${DMG_SRC_DIR}" -volname "${DMG_TITLE}" -fs HFS+ \
  -fsargs "-c c=64,a=16,e=16" -format UDRW -size ${DMG_SIZE}k ${TEMP_ARTIFACT} \
  || die "Error creating DMG: ${TEMP_ARTIFACT}"

# mount it as R/W
hdiutil attach -readwrite -noverify -noautoopen "${TEMP_ARTIFACT}" | egrep '^/dev/' | sed 1q | awk '{print $1}' \
  || die "Error attaching DMG"

# Set background image and other stuffs
# todo...

# Finalize and unmount the DMG
chmod -Rf go-w "${ARTIFACT_MOUNT}/${APP_DIR}" || die "Error setting permissions on ${ARTIFACT_MOUNT}"
sync ; sync
hdiutil detach "${ARTIFACT_MOUNT}" || die "Error detaching ${ARTIFACT_MOUNT}"
hdiutil convert "/${TEMP_ARTIFACT}" -format UDZO -imagekey zlib-level=9 -o "${ARTIFACT}" || die "Error converting DMG"
rm -f "${TEMP_ARTIFACT}"
echo "ARTIFACT: ${ARTIFACT}"
