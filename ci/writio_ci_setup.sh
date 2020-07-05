#!/bin/bash

# --jenkins-username, --jenkins-secret : Jenkins credentials
# --github-username, --github-secret : GitHub credentials
# --dockerhub-username, --dockerhub-secret : DockerHub credentials

# ./writio_ci_setup.sh --jenkins-username=ju --jenkins-secret=js --github-username=gu --github-secret=gs --dockerhub-username=du --dockerhub-secret=ds

file="docker-compose.yml"
file_orig="${file}.orig"

replace_jenkins_credentials="false"
replace_github_dockerhub_credentials="false"

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

if [[ -z ${JENKINS_USERNAME+x} ]] || [[ -z ${JENKINS_SECRET+x} ]]
then
	echo "Mandatory Jenkins credentials missing / incomplete, exiting..."
	exit 1
else
	if ! grep -q "JENKINS_USER=%%JENKINS_USER%%" "${file}" || ! grep -q "JENKINS_PASS=%%JENKINS_PASS%%" "${file}"
	then
		echo "${file} misses Jenkins credential placeholders, exiting..."
		exit 1
	else
		replace_jenkins_credentials="true"
	fi
fi

if [[ -z ${GITHUB_USERNAME+x} ]] && [[ -z ${GITHUB_SECRET+x} ]] && [[ -z ${DOCKERHUB_USERNAME+x} ]] && [[ -z ${DOCKERHUB_SECRET+x} ]]
then
	echo "No GitHub and DockerHub credentials given, hence you cannot perform a release!"
else
	if [[ -z ${GITHUB_USERNAME+x} ]] || [[ -z ${GITHUB_SECRET+x} ]] || [[ -z ${DOCKERHUB_USERNAME+x} ]] || [[ -z ${DOCKERHUB_SECRET+x} ]]
	then
		echo "GitHub or DockerHub credentials are incomplete, exiting..."
		exit 1
	else
		if ! grep -q "WRITIO_GITHUB_USER=%%WRITIO_GITHUB_USER%%" "${file}" || ! grep -q "WRITIO_GITHUB_SECRET=%%WRITIO_GITHUB_SECRET%%" "${file}" \
			|| ! grep -q "WRITIO_DOCKERHUB_USER=%%WRITIO_DOCKERHUB_USER%%" "${file}" || ! grep -q "WRITIO_DOCKERHUB_SECRET=%%WRITIO_DOCKERHUB_SECRET%%" "${file}"
		then
			echo "${file} misses GitHub or DockerHub credential placeholders, exiting..."
			exit 1
		else
			echo "GitHub and DockerHub credentials given, hence you can perform a release."
			replace_github_dockerhub_credentials="true"
		fi
	fi
fi

echo "creating a backup of original ${file} (${file_orig})..."
cp "${file}" "${file_orig}"

declare -A confs
confs=(
    [%%WRITIO_GITHUB_USER%%]="${GITHUB_USERNAME}"
    [%%WRITIO_GITHUB_SECRET%%]="${GITHUB_SECRET}"
    [%%WRITIO_DOCKERHUB_USER%%]="${DOCKERHUB_USERNAME}"
    [%%WRITIO_DOCKERHUB_SECRET%%]="${DOCKERHUB_SECRET}"
    [%%JENKINS_USER%%]="${JENKINS_USERNAME}"
    [%%JENKINS_PASS%%]="${JENKINS_SECRET}"
)

configurer() {
    for i in "${!confs[@]}"
    do
        search=$i
        replace=${confs_wp[$i]}
        sed -i "" "s/${search}/${replace}/g" ${file}
    done
}

configurer