#!/bin/bash

# $1 : version

while getopts ":v:" opt; do
  case $opt in
    v) version="$OPTARG"
    ;;
    \?) echo "Invalid option -$OPTARG" >&2
    ;;
  esac
done


target_branch="release/${version}"

echo "deleting remote branch ${target_branch}..."
git push origin --delete ${target_branch}
