#!/bin/bash

SERVICE_NAME="${project.artifactId}"
VERSION="${project.version}"
SERVICE_LIB_NAME=${SERVICE_NAME}-${VERSION}.jar

PRO_BIN_DIR=`dirname "$0"`
PRO_BASE_DIR=${PRO_BIN_DIR}/..
PRO_CONF_DIR=${PRO_BIN_DIR}/../conf
PRO_PID_FILE=$PRO_BIN_DIR/running.pid

JAVA_OPTS="-server -Xms1024m -Xmx1024m -XX:PermSize=64m -XX:MaxPermSize=64m"

if [ -f "$PRO_PID_FILE" ]; then
    if [ `ps -ef | grep $(cat "$PRO_PID_FILE") | grep $SERVICE_NAME | wc -l` -gt 0 ]
    then
        echo "$SERVICE_NAME is running..."
        exit 0;
    fi
fi

java ${JAVA_OPTS} \
    -Dbase.dir=${PRO_BASE_DIR} -Dconf.dir=${PRO_CONF_DIR} \
    -jar ${PRO_BASE_DIR}/lib/${SERVICE_LIB_NAME} $1 $2 $3 $4 $5
