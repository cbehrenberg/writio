FROM jenkins/jenkins:lts-alpine	

ENV JENKINS_USER="admin" JENKINS_PASS="admin" JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

USER root

RUN apk add --no-cache --update docker

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

USER jenkins