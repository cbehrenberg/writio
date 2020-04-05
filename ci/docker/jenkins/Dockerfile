FROM maven:amazoncorretto as maven

# initialize repo
RUN mvn archetype:generate -DgroupId=com.writio -DartifactId=writio -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false


FROM jenkins/jenkins:lts-alpine	
COPY --from=maven /root/.m2 /root/.m2

ENV JENKINS_USER admin
ENV JENKINS_PASS admin

ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false

COPY plugins.txt /usr/share/jenkins/plugins.txt

RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/plugins.txt

USER root

RUN apk add --no-cache --update docker py-pip python-dev libffi-dev openssl-dev gcc libc-dev make ca-certificates mailcap bash htop; \
    pip install docker-compose

USER jenkins