pipelineJob('writio-build-branch') {

    def repo = 'https://github.com/cbehrenberg/writio.git'

    description("writio job for building a GitHub branch by cloning from remote")

    properties {

        githubProjectUrl (repo) 
        
        rebuild {
            autoRebuild(false) 
        }

        logRotator {
            artifactDaysToKeep(1)
            artifactNumToKeep(1)
            daysToKeep(1)
            numToKeep(1)
        }
    }

    definition {
        cpsScm {

            scriptPath 'ci/Jenkinsfile'

            scm {
                git {
                
                    remote {
                        url(repo)
                    }
                
                    branch '*/initial-jenkins-build'
                    lightweight(true)

                    extensions {}
                }
            }
        }
    }
}