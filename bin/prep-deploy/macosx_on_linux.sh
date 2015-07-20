#!/bin/bash

function die {
  echo "${1}" >&2 && exit 1
}

SCRIPT_DIR="$(cd $(dirname ${0}) && pwd)"
BASE_DIR="$(cd ${SCRIPT_DIR}/../.. && pwd)"

# MacOS -- create DMG file. Use different process when building on Linux vs Mac
ARTIFACT="${BASE_DIR}/target/cloudos_launcher.dmg"
DMG_SRC_DIR="target/macosx/Cloudstead Launcher.app"
DMG_SIZE=$(expr $(du -mxs ${BASE_DIR}/target/macosx | awk '{print $1}') + 2)
DMG_TITLE="Cloudstead Launcher"
TEMP_ARTIFACT=${ARTIFACT}.tmp.dmg
rm -f ${ARTIFACT} ${TEMP_ARTIFACT}

# create blank DMG
dd if=/dev/zero of=${TEMP_ARTIFACT} bs=1k count=${DMG_SIZE} || die "Error creating DMG"

# create and mount filesystem
ARTIFACT_MOUNT=$(mktemp -d "/mnt/$(basename ${TEMP_ARTIFACT}).XXXXXXX")
mkfs.hfsplus -v "${DMG_TITLE}" ${TEMP_ARTIFACT} || die "Error creating filesystem on DMG: ${TEMP_ARTIFACT}"
mount -o loop ${TEMP_ARTIFACT} ${ARTIFACT_MOUNT} || die "Error mounting DMG ${TEMP_ARTIFACT} as ${ARTIFACT_MOUNT}"

# Copy files to DMG
cp -R ${DMG_SRC_DIR} ${ARTIFACT_MOUNT} || die "Error copying to DMG: ${ARTIFACT_MOUNT}"

# Set background image and other stuffs
# todo...

# unmount it and move to target dir
umount ${ARTIFACT_MOUNT} || die "Error unmounting DMG: ${ARTIFACT_MOUNT}"
rmdir ${ARTIFACT_MOUNT} || die "Error removing DMG mount dir: ${ARTIFACT_MOUNT}"
mv ${TEMP_ARTIFACT} ${ARTIFACT} || die "Error moving ${TEMP_ARTIFACT} -> ${ARTIFACT}"
echo "ARTIFACT: ${ARTIFACT}"
