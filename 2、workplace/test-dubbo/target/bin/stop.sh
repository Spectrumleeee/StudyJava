#!/bin/bash

SERVICE_NAME="test-dubbo"

PRO_BASE_DIR=$(cd `dirname $0`; pwd)/..
cd ${PRO_BASE_DIR}
PRO_PID_FILE=bin/running.pid
MAX_STOP_TIME_S=20

if [ -f "$PRO_PID_FILE" ]; then
    if [ `ps -ef | grep $(cat "$PRO_PID_FILE") | grep $SERVICE_NAME | wc -l` -gt 0 ]
    then
        PID=`cat $PRO_PID_FILE`
        kill $PID
        COUNT=0
        while [ `ps -ef | grep $(cat "$PRO_PID_FILE") | grep $SERVICE_NAME | wc -l` -gt 0 ]; do
            sleep 1
            COUNT=`expr $COUNT + 1`
            echo "$COUNT second(s) elapsed..."
            if [ $COUNT -gt $MAX_STOP_TIME_S ]; then
                kill -9 $PID
                break;
            fi
        done
        echo "STOPPED."
    else
        echo "No instance running."
    fi
fi
