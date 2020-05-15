#!/bin/bash

while getopts ":v:m:u:p:" opt; do
  case $opt in
    v) version="$OPTARG"
    ;;
    m) message="$OPTARG"
    ;;
    u) username="$OPTARG"
    ;;
    p) password="$OPTARG"
    ;;
    \?) echo "Invalid option -$OPTARG" >&2
    ;;
  esac
done

branch=${branch:-dev}
message=${message:-create release tag}

auth_url="https://${username}:${password}@github.com/cbehrenberg/writio.git"

tmp_dir=$(mktemp -d -t writio-XXXXXXXXXX)
release_branch="release/${version}"

echo "cloning branch '${release_branch}' into temp dir ${tmp_dir}..."
git clone -b ${release_branch} "${auth_url}" ${tmp_dir}

git -C ${tmp_dir} status

echo "creating git tag ${version}..."

if [ ! -z "$(git -C ${tmp_dir} tag -l ${version})" ]; then
	echo "git tag ${version} already exists in -C ${tmp_dir}!"
	exit 1
fi

echo "executing: 'git -C ${tmp_dir} tag -m \"${message}\" \"${version}\"'..."
git -C ${tmp_dir} tag -m "${message}" "${version}"

if [ $? -eq 0 ]; then
  echo "created git tag ${version} in ${tmp_dir} successfully!"
else
  echo "cannot create tag ${version} in ${tmp_dir}!"
  exit 1
fi

echo "executing: 'git push origin ${version}'..."
git -C ${tmp_dir} push origin "${version}"

if [ $? -eq 0 ]; then
  echo "pushed tag ${version} successfully to remote!"
  exit 0
else
  echo "cannot push git tag ${version} to remote!"
  exit 1
fi

rm -rf ${tmp_dir}
