#!/bin/bash

while getopts ":v:m:u:p:b:" opt; do
  case $opt in
    v) version="$OPTARG"
    ;;
    m) message="$OPTARG"
    ;;
    u) username="$OPTARG"
    ;;
    p) password="$OPTARG"
    ;;
    b) branch="$OPTARG"
    ;;
    \?) echo "Invalid option -$OPTARG" >&2
    ;;
  esac
done

branch=${branch:-dev}
message=${message:-create release branch}

tmp_dir=$(mktemp -d -t writio-XXXXXXXXXX)

target_branch="release/${version}"

echo "cloning ${branch} into temp dir ${tmp_dir}..."
git clone -b ${branch} https://${username}:${password}@github.com/cbehrenberg/writio.git ${tmp_dir}

git -C ${tmp_dir} status

echo "switching to new branch '${target_branch}'..."
git -C ${tmp_dir} checkout -b ${target_branch} ${branch}

echo "switching maven version ${version}..."
mvn -f ${tmp_dir}/pom.xml versions:set -DnewVersion=${version} -DgenerateBackupPoms=false

echo "switching latest to specific version ${version} for container yamls..."
sed -i "s/latest/${version}/g" ${tmp_dir}/ci/.env

git -C ${tmp_dir} status

echo "comitting changes..."
git -C ${tmp_dir} commit -a -m "${message}"

echo "executing: 'git push origin ${target_branch}'..."
git -C ${tmp_dir} push origin "${target_branch}"

if [ $? -eq 0 ]; then
  echo "pushed changes on ${target_branch} successfully to remote!"
  exit 0
else
  echo "cannot push changes on ${target_branch} to remote!"
  exit 1
fi

rm -rf ${tmp_dir}
