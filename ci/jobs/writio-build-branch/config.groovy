def repositoryUrl = 'https://github.com/cbehrenberg/writio.git'
def jobName = 'writio-build-branch'
def defaultBranch = 'dev'

pipelineJob(jobName) {

    description("Builds a writio branch from remote GitHub repository '${repositoryUrl}'. When executed the first time, the ${defaultBranch}-branch will be built (default). Afterwards, you can select from a combo box all available other branches.")

    parameters {
        gitParameter {
            name("BRANCH")
            type("PT_BRANCH")
            defaultValue("dev")
            branch("dev")
            description("writio branch to build")
            branchFilter("origin/(.*)")
            tagFilter("*")
            sortMode("NONE")
            selectedValue("NONE")
            quickFilterEnabled(false)
            useRepository("https://github.com/cbehrenberg/writio.git")
        }
    }

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
                        url(repositoryUrl)
                    }
                
                    branch "*/${defaultBranch}"
                    lightweight(true)

                    extensions {}
                }
            }
        }
    }
}

queue(jobName)