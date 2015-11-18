#!/bin/bash

SERVICE_NAME="${project.artifactId}"
VERSION="${project.version}"
SERVICE_LIB_NAME=${SERVICE_NAME}-${VERSION}.jar

PRO_BASE_DIR=$(cd `dirname $0`; pwd)/..
cd ${PRO_BASE_DIR}
PRO_CONF_DIR=conf
PRO_LIB_DIR=lib
PRO_LOGS_DIR=logs
PRO_PID_FILE=bin/running.pid

JOLOKIA_FILE=${PRO_LIB_DIR}/jolokia-jvm-1.3.1-agent.jar
JOLOKIA_OPTS="host=0.0.0.0,port=8778"

JAVA_OPTS="-server -Xms1024m -Xmx2048m -XX:PermSize=64m -XX:MaxPermSize=64m"

if [ -f "$PRO_PID_FILE" ]; then
    if [ `ps -ef | grep $(cat "$PRO_PID_FILE") | grep $SERVICE_NAME | wc -l` -gt 0 ]
    then
        echo "$SERVICE_NAME is running..."
        exit 0;
    fi
fi

nohup java ${JAVA_OPTS} \
    -javaagent:${JOLOKIA_FILE}=${JOLOKIA_OPTS} \
    -Dbase.dir=${PRO_BASE_DIR} -Dconf.dir=${PRO_CONF_DIR} \
    -jar ${PRO_LIB_DIR}/${SERVICE_LIB_NAME} \
    > ${PRO_LOGS_DIR}/nohup.out 2>&1 &

if [ $? -eq 0 ]; then
    if /bin/echo -n $! > "$PRO_PID_FILE"
    then
        sleep 1
        echo STARTED
    else
        echo FAILED TO WRITE PID
        exit 1
    fi
else
    echo SERVER DID NOT START
    exit 1
fi