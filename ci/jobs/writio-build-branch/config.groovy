def repositoryUrl = 'https://github.com/cbehrenberg/writio.git'
def jobName = 'writio-build-branch'
def defaultBranch = 'dev'

pipelineJob(jobName) {

    description("<p>Builds a writio branch from remote GitHub repository '${repositoryUrl}'.</p><p>When executed the first time, the ${defaultBranch}-branch will be built (default). Afterwards, you can select from a combo box all available other branches.</p>")

    properties {

        githubProjectUrl(repositoryUrl)
        
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

            scriptPath "ci/jobs/${jobName}/Jenkinsfile"

            scm {
                git {
                
                    remote {
                        url(repo)
                    }
                
                    branch "*/${defaultBranch}"
                    lightweight(true)

                    extensions {}
                }
            }
        }
    }
}

// execute immediately when fresh to initialize parameters
if (!jenkins.model.Jenkins.instance.getItemByFullName(jobName)) {
    queue(jobName)
}