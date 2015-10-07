#!/bin/bash

BASE_DIR=$(cd $(dirname $0) && pwd)
BUILD_DIR=${BASE_DIR}/target/build-apidocs/
SRC_DIR=${BUILD_DIR}/src/main/

rm -rf ${BUILD_DIR}
mkdir -p ${SRC_DIR}

rsync -avzc ${BASE_DIR}/src/main/* ${SRC_DIR}
rsync -avzc ${BASE_DIR}/../cloudos-lib/src/main/* ${SRC_DIR}
rsync -avzc ${BASE_DIR}/../cloudos-appstore/appstore-common/src/main/* ${SRC_DIR}
rsync -avzc ${BASE_DIR}/../utils/cobbzilla-utils/src/main/* ${SRC_DIR}
rsync -avzc ${BASE_DIR}/../utils/cobbzilla-wizard/wizard-server/src/main/* ${SRC_DIR}
rsync -avzc ${BASE_DIR}/csapp ${BUILD_DIR}
rsync -avzc ${BASE_DIR}/bin ${BUILD_DIR}

cp ${BASE_DIR}/pom.xml ${BUILD_DIR}/pom.xml

cd ${BUILD_DIR}
mvn -P apidocs test && rsync -avzc target/miredot .. # && cd ${BASE_DIR} && rm -rf ${BUILD_DIR}
