#!/bin/bash

# --jenkins-username, --jenkins-secret : Jenkins credentials
# --github-username, --github-secret : GitHub credentials
# --dockerhub-username, --dockerhub-secret : DockerHub credentials

# ./writio_ci_setup.sh --jenkins-username=ju --jenkins-secret=js --github-username=gu --github-secret=gs --dockerhub-username=du --dockerhub-secret=ds

while [ $# -gt 0 ]; do
  case "$1" in
    --jenkins-username*)
      if [[ "$1" != *=* ]]; then shift; fi
      JENKINS_USERNAME="${1#*=}"
      ;;
    --jenkins-secret*)
      if [[ "$1" != *=* ]]; then shift; fi
      JENKINS_SECRET="${1#*=}"
      ;;
    --github-username*)
      if [[ "$1" != *=* ]]; then shift; fi
      GITHUB_USERNAME="${1#*=}"
      ;;
    --github-secret*)
      if [[ "$1" != *=* ]]; then shift; fi
      GITHUB_SECRET="${1#*=}"
      ;;
    --dockerhub-username*)
      if [[ "$1" != *=* ]]; then shift; fi
      DOCKERHUB_USERNAME="${1#*=}"
      ;;
    --dockerhub-secret*)
      if [[ "$1" != *=* ]]; then shift; fi
      DOCKERHUB_SECRET="${1#*=}"
      ;;
    --help|-h)
      printf "Meaningful help message" # Flag argument
      exit 0
      ;;
    *)
      >&2 printf "Error: Invalid argument\n"
      exit 1
      ;;
  esac
  shift
done

echo "JENKINS_USERNAME = ${JENKINS_USERNAME}"
echo "JENKINS_SECRET = ${JENKINS_SECRET}"

echo "GITHUB_USERNAME = ${GITHUB_USERNAME}"
echo "GITHUB_SECRET = ${GITHUB_SECRET}"

echo "DOCKERHUB_USERNAME = ${DOCKERHUB_USERNAME}"
echo "DOCKERHUB_SECRET = ${DOCKERHUB_SECRET}"
