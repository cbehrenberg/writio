FROM maven:amazoncorretto

# initialize repo
RUN mvn archetype:generate -DgroupId=com.writio -DartifactId=writio -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

ENTRYPOINT ["/usr/local/bin/mvn-entrypoint.sh"]
CMD ["mvn"]