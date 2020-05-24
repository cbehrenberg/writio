FROM jenkins/jenkins:jdk11

# disable jenkins authentication
ENV JENKINS_USER="admin" JENKINS_PASS="admin" JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

USER root

# install docker
RUN set -x \
    && apt-get -y update \
    && apt-get -y install apt-transport-https ca-certificates curl gnupg-agent software-properties-common \
    && curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add - \
    && apt-key fingerprint 0EBFCD88 \
    && add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable" \
    && apt-get -y update \
    && apt-get -y install docker-ce docker-ce-cli containerd.io \
	&& rm -rf /var/lib/apt/lists/* \
    && docker --version

# install git
RUN set -x \
    && apt-get -y update \
    && apt-get -y install git openssh-client \
	&& rm -rf /var/lib/apt/lists/* \
	&& git --version \
	&& ssh -V

# install maven
RUN set -x \
    && apt-get -y update \
    && apt-get -y install maven \
	&& rm -rf /var/lib/apt/lists/* \
    && mvn --version

# copy writio jenkins theme
COPY userContent /var/jenkins_home/userContent

# install jenkins plugins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

USER jenkins