FROM jenkins/jenkins:lts-alpine	

ENV JENKINS_USER="admin" JENKINS_PASS="admin" JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

USER root

RUN set -x \
    && apk update && apk upgrade \
    && apk add --no-cache docker bash git openssh \
    && git --version && bash --version && ssh -V

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY userContent /var/jenkins_home/userContent

RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

USER jenkins