FROM jenkins/jenkins:jdk11

# disable jenkins authentication
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

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

ENV MAVEN_VERSION 3.6.3
ENV MAVEN_HOME /usr/lib/mvn
ENV PATH $MAVEN_HOME/bin:$PATH

RUN wget http://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
	&& tar -zxvf apache-maven-$MAVEN_VERSION-bin.tar.gz \
	&& rm apache-maven-$MAVEN_VERSION-bin.tar.gz \
	&& mv apache-maven-$MAVEN_VERSION /usr/lib/mvn \
    && mvn --version

# copy writio jenkins theme
COPY userContent /var/jenkins_home/userContent

# install jenkins plugins

COPY plugins.core.txt /usr/share/jenkins/ref/plugins.core.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.core.txt

COPY plugins.ci.txt /usr/share/jenkins/ref/plugins.ci.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.ci.txt

USER jenkins