FROM maven:amazoncorretto

VOLUME /maven_repository
ENV MAVEN_REPO_LOCAL /maven_repository

ENTRYPOINT ["/usr/local/bin/mvn-entrypoint.sh"]
CMD ["mvn"]