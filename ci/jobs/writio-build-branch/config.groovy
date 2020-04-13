pipelineJob('writio-build-branch') {

    def repo = 'https://github.com/cbehrenberg/writio.git'

    description("<p>Builds a writio branch from remote GitHub repository.</p><p>When executed the first time, the dev branch will be built (default). Afterwards, you can select from a combo box all available other branches.</p>")

    properties {

        githubProjectUrl(repo)
        
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

            scriptPath 'ci/jobs/writio-build-branch/Jenkinsfile'

            scm {
                git {
                
                    remote {
                        url(repo)
                    }
                
                    branch '*/dev'
                    lightweight(true)

                    extensions {}
                }
            }
        }
    }
}