#!/bin/bash

SERVICE_NAME="${project.artifactId}"
VERSION="${project.version}"
SERVICE_LIB_NAME=${SERVICE_NAME}-${VERSION}.jar

PRO_BIN_DIR=`dirname "$0"`
PRO_PID_FILE=$PRO_BIN_DIR/running.pid

if [ -f "$PRO_PID_FILE" ]; then
    if [ `ps -ef | grep $(cat "$PRO_PID_FILE") | grep $SERVICE_NAME | wc -l` -gt 0 ]
    then
        echo "$SERVICE_NAME is running..."
        exit 0;
    fi
fi

echo "no ${SERVICE_NAME} service is running."