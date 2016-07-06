#!/bin/bash

# ./start.sh <method> <operationcount> <threadcount>         ex: ./start.sh checkVaBalance 100000 10

SERVICE_NAME="${project.artifactId}"
VERSION="${project.version}"
SERVICE_FULL_NAME=$SERVICE_NAME-$VERSION
SERVICE_LIB_NAME=$SERVICE_FULL_NAME.jar

PRO_BIN_DIR=$(cd `dirname $0`; pwd)
PRO_BASE_DIR=$PRO_BIN_DIR/..
PRO_CONF_DIR=$PRO_BASE_DIR/conf
PRO_LIB_DIR=$PRO_BASE_DIR/lib
PRO_LOGS_DIR=$PRO_BASE_DIR/logs

JAVA_OPTS="-server -Xms2048m -Xmx2048m -XX:PermSize=128m -XX:MaxPermSize=128m"	# PRD JVM CONFIG 
#JAVA_OPTS="-server -Xms128m -Xmx256m -XX:PermSize=32m -XX:MaxPermSize=32m"		# UAT JVM CONFIG 

java ${JAVA_OPTS} -jar ${PRO_LIB_DIR}/${SERVICE_LIB_NAME} -P ${PRO_CONF_DIR}/workload-vaservice -X ${PRO_CONF_DIR}/requests.xml -s -p method=$1 -p operationcount=$2 -p threadcount=$3


