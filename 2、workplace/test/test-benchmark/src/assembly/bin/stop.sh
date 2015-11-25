#!/bin/bash

SERVICE_NAME="${project.artifactId}"

PRO_BIN_DIR=`dirname "$0"`
PRO_PID_FILE=$PRO_BIN_DIR/running.pid

if [ -f "$PRO_PID_FILE" ]; then
    if [ `ps -ef | grep $(cat "$PRO_PID_FILE") | grep $SERVICE_NAME | wc -l` -gt 0 ]
    then
        PID=`cat $PRO_PID_FILE`
        kill $PID
        echo "STOPPED."
    else
        echo "No instance running."
    fi
fi
