#!/bin/bash

if (( $# == 0 ))
then
  echo "usage: put-connector [config file path]"
  exit 1
fi


CONNECTOR_NAME=$(./get-config-name.sh $1);
if [ $? -ne 0 ] ; then
    exit 1
fi

echo -n "Creating/Updating Connector ${CONNECTOR_NAME} ... "
response=$(curl -s -k -XPUT http://localhost:8083/connectors/${CONNECTOR_NAME}/config -H "Content-Type: application/json" -d @${1})
status=$(echo ${response} | jq -r 'if (.error_code | length) > 0 then .error_code else 201 end')
if [ "${status}" -eq 201 ] ; then
  sleep 10
  echo "SUCCESS"
  ./get-connector-status.sh $1
  exit 0
else
  echo "FAILED"
  echo ${response} | jq '.'
  exit 1
fi
