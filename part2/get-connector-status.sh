#!/bin/bash

if (( $# == 0 ))
then
  echo "usage: get-connector-status [config file path]"
  exit 1
fi

CONNECTOR_NAME=$(./get-config-name.sh $1);
if [ $? -ne 0 ] ; then
    exit 1
fi

echo "Connector ${CONNECTOR_NAME} Status: "
curl -ks http://localhost:8083/connectors/${CONNECTOR_NAME}/status | jq '.'
