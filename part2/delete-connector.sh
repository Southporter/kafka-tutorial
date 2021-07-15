#!/bin/bash

if (( $# == 0 ))
then
  echo "usage: delete-connector [config file path]"
  exit 1
fi

CONNECTOR_NAME=$(./get-config-name.sh $1);
if [ $? -ne 0 ] ; then
    exit 1
fi

echo -n "Deleting Connector ${CONNECTOR_NAME} ... "
response=$(curl -ksS -f -XDELETE http://localhost:8083/connectors/${CONNECTOR_NAME} 2>&1)
status=$?
if [ "${status}" -eq 0 ] ; then
  echo "SUCCESS"
  exit 0
else
  echo "FAILED"
  echo ${response}
  exit 1
fi
