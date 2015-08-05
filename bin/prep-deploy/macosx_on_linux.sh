#!/bin/bash

function die {
  echo "${1}" >&2 && exit 1
}

SCRIPT_DIR="$(cd $(dirname ${0}) && pwd)"
BASE_DIR="$(cd ${SCRIPT_DIR}/../.. && pwd)"

# MacOS -- create DMG file. Use different process when building on Linux vs Mac
ARTIFACT="${BASE_DIR}/target/cloudos_launcher.dmg"
DMG_SRC_DIR="${BASE_DIR}/target/macosx"
DMG_SIZE=$(expr $(du -kxs ${BASE_DIR}/target/macosx | awk '{print $1}') + 2)
DMG_TITLE="Cloudstead Launcher"
rm -f ${ARTIFACT}

# Set background image and other stuffs
# todo...

# create dmg. it will be uncompressed and unsigned. use MacOS to build a compressed/signed DMG.
genisoimage -A "${DMG_TITLE}" -V "${DMG_TITLE}" -apple -no-pad -hide-rr-moved -r -o ${ARTIFACT} "${DMG_SRC_DIR}" \
  || die "Error creating DMG: ${ARTIFACT}"

echo "ARTIFACT: ${ARTIFACT}"
