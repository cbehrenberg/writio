#!/bin/bash

# Create Azure VM Ubuntu 18.04 LTS with
# - instance type = B2s
# - use SSH public key pair, username = writioadmin
# - allowed inbound ports: 22 (ssh)
# - standard HDD disk
# - turn off boot diagnostics
#
# Store pem file securely - not recoverable!
#
# Copy script to host and run:
# bash writio_ubuntu_18.04_setup.sh
#
# Create SSH tunnel for Jenkins access:
# ssh -i /path/to/your.pem writioadmin@<ip> -L 48080:127.0.0.1:48080

# docker

	set +e
	sudo apt-get -y update
	sudo apt-get -y remove docker docker-engine docker.io containerd runc

	set +e # if this is a fresh install, this will fail, hence we allow errors
	sudo apt-get -y remove docker-ce docker-ce-cli containerd.io

	set -e
	curl -fsSL https://get.docker.com -o get-docker.sh
	sh get-docker.sh
	sudo usermod -aG docker writioadmin
	sudo systemctl enable docker
	sudo docker images && sudo docker ps && sudo docker info

# docker compose
sudo apt-get -y update
sudo curl -L "https://github.com/docker/compose/releases/download/1.26.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# git
sudo apt-get -y update
sudo apt-get -y install git

# java 11
sudo apt-get -y update
sudo apt-get -y install openjdk-11-jre openjdk-11-jdk
java -version
echo "JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/" | sudo tee -a /etc/environment
source /etc/environment

# maven
sudo apt-get -y update
wget https://downloads.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz -P /tmp
sudo  mkdir -p /opt/maven
sudo tar xf /tmp/apache-maven-*.tar.gz --strip-components=1 -C /opt/maven
echo "M3_HOME=/opt/maven" | sudo tee -a /etc/environment
echo "MAVEN_HOME=/opt/maven" | sudo tee -a /etc/environment
echo "PATH=/opt/maven/bin:${PATH}" | sudo tee -a /etc/environment
source /etc/environment