#!/bin/bash

source ./scripts/ask.sh

echo "Asserting Docker being in swarm mode..."
if [ "$(docker info | grep Swarm | sed 's/Swarm: //g')" == "inactive" ]; then
	echo "Docker not in swarm mode, activating..."
	docker swarm init
else
	echo "Docker already in swarm mode"
fi

echo "Creating secrets..."

# Jenkins admin username and password

	jenkins_user_def="writioadmin"
	read -p "Enter username for Jenkins [${jenkins_user_def}]: " jenkins_user
	jenkins_user=${jenkins_user:-${jenkins_user_def}}
	echo "jenkins_user = ${jenkins_user}"
	
	while [[ -z "$jenkins_secret" ]]
	do
		read -p "Enter secret for Jenkins user ${jenkins_user}: " jenkins_secret
	done
	
	echo "${jenkins_user}" | docker secret create writio-ci-jenkins-username -
	echo "${jenkins_secret}" | docker secret create writio-ci-jenkins-secret -

if ask "Do you want to perform a release to GitHub and Docker Hub?"; then

	# GitHub username and password

		while [[ -z "$github_user" ]]
		do
			read -p "Enter GitHub user: " github_user
		done
		
		while [[ -z "$github_secret" ]]
		do
			read -p "Enter secret for GitHub user ${github_user}: " github_secret
		done
		
		echo "${github_user}" | docker secret create writio-ci-github-username -
		echo "${github_secret}" | docker secret create writio-ci-github-secret -
		
	# Docker Hub username and password

		while [[ -z "$dockerhub_user" ]]
		do
			read -p "Enter Docker Hub user: " dockerhub_user
		done
		
		while [[ -z "$dockerhub_secret" ]]
		do
			read -p "Enter secret for Docker Hub user ${dockerhub_user}: " dockerhub_secret
		done
		
		echo "${dockerhub_user}" | docker secret create writio-ci-dockerhub-username -
		echo "${dockerhub_secret}" | docker secret create writio-ci-dockerhub-secret -
	
else
    echo "Skipping creation of GitHub and DockerHub credentials (populated with dummy value)..."
	echo "UNDEFINED" | docker secret create writio-ci-github-username -
	echo "UNDEFINED" | docker secret create writio-ci-github-secret -
	echo "UNDEFINED" | docker secret create writio-ci-dockerhub-username -
	echo "UNDEFINED" | docker secret create writio-ci-dockerhub-secret -
fi

echo "done"
exit 0
