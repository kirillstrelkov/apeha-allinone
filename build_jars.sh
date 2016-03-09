#!/usr/bin/env bash
PREV_DIR=$(pwd)
SCRIPT_DIR="$( dirname "${BASH_SOURCE[0]}" )"
BUILD_DIR=${SCRIPT_DIR}/build
mkdir -p ${BUILD_DIR}
cd ${SCRIPT_DIR}
pwd
mvn clean
echo "Building Windows x32"
mvn package -P win32 -Dmaven.test.skip=true
echo "Building Windows x64"
mvn package -P win64 -Dmaven.test.skip=true
echo "Building Linux x32"
mvn package -P linux32 -Dmaven.test.skip=true
echo "Building Linux x64"
mvn package -P linux64 -Dmaven.test.skip=true
echo "Building Mac x64"
mvn package -P mac64 -Dmaven.test.skip=true
cd ${PREV_DIR}
pwd
cp ${SCRIPT_DIR}/target/allinone-*.jar ${BUILD_DIR}