#!/bin/bash

if (( $# == 0 ))
then
  echo "usage: get-config-name [config file path]"
  exit 1
fi

CONNECTOR_NAME=$(jq -e -r .name $1);
if [ $? -ne 0 ] ; then
  >&2 echo "Config $1 is missing the 'name' property but is required."
  exit 1
fi
echo ${CONNECTOR_NAME}
exit 0
