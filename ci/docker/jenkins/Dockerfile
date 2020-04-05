FROM jenkins/jenkins:lts-alpine	

ENV JENKINS_USER admin
ENV JENKINS_PASS admin

ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false

COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/plugins.txt

USER root

RUN apk add --no-cache --update docker py-pip python-dev libffi-dev openssl-dev gcc libc-dev make ca-certificates mailcap bash htop; \
    pip install docker-compose

# local maven repo in layer
ARG MAVEN_REPO_LOCAL_PATH=/usr/share/maven/ref/repository
ENV MAVEN_REPO_LOCAL $MAVEN_REPO_LOCAL_PATH
RUN mkdir -p $MAVEN_REPO_LOCAL_PATH; \
    chown jenkins: $MAVEN_REPO_LOCAL_PATH

USER jenkins