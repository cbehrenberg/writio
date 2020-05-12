#!/bin/bash

# $1 : version
# $2 : message

echo "INFO: trying to create git tag ${1}..."

if [ ! -z "$(git tag -l $1)" ]; then
	echo "ERROR: git tag ${1} already exists!"
	exit 1
fi

echo "INFO: executing: 'git tag -m \"${2}\" \"${1}\"'..."
git tag -m "${2}" "${1}"

if [ $? -eq 0 ]; then
  echo "INFO: created git tag ${1} successfully!"
else
  echo "ERROR: cannot create tag ${1}"
  exit 1
fi

echo "INFO: executing: 'git push origin ${1}'..."
git push origin "${1}"

if [ $? -eq 0 ]; then
  echo "INFO: pushed tag ${1} successfully to remote!"
  exit 0
else
  echo "ERROR: cannot push git tag ${1} to remote!"
  exit 1
fi
