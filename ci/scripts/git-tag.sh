#!/bin/bash

# $1 : version
# $2 : message

echo "creating git tag ${1}..."

if [ ! -z "$(git tag -l $1)" ]; then
	echo "git tag ${1} already exists!"
	exit 1
fi

echo "executing: 'git tag -m \"${2}\" \"${1}\"'..."
git tag -m "${2}" "${1}"

if [ $? -eq 0 ]; then
  echo "created git tag ${1} successfully!"
else
  echo "cannot create tag ${1}!"
  exit 1
fi

echo "executing: 'git push origin ${1}'..."
git push origin "${1}"

if [ $? -eq 0 ]; then
  echo "pushed tag ${1} successfully to remote!"
  exit 0
else
  echo "cannot push git tag ${1} to remote!"
  exit 1
fi
