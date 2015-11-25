#!/bin/bash

SERVICE_NAME="test-dubbo"
VERSION="0.0.1-SNAPSHOT"
SERVICE_LIB_NAME=${SERVICE_NAME}-${VERSION}.jar

PRO_BASE_DIR=$(cd `dirname $0`; pwd)/..
cd ${PRO_BASE_DIR}
PRO_PID_FILE=bin/running.pid

if [ -f "$PRO_PID_FILE" ]; then
    if [ `ps -ef | grep $(cat "$PRO_PID_FILE") | grep $SERVICE_NAME | wc -l` -gt 0 ]
    then
        echo "$SERVICE_NAME is running..."
        exit 0;
    fi
fi

echo "no ${SERVICE_NAME} service is running."