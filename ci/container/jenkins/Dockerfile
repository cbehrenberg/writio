FROM jenkins/jenkins:lts-alpine	

ENV JENKINS_USER="admin" JENKINS_PASS="admin" JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

USER root

RUN apk add --no-cache --update docker bash git openssh

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY userContent /var/jenkins_home/userContent

RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

USER jenkins