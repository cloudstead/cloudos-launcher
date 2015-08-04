#!/bin/bash
#
# Installs required packages for building cloudos-launcher binaries on Ubuntu
#

function die {
  echo "${1}" >&2 && exit 1
}

if [ $(whoami) != "root" ] ; then
  die "Must run as root"
fi

apt-get install -y pandoc zip wkhtmltopdf xorg xvfb hfsprogs libc6-i386 genisoimage
