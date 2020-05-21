def repositoryUrl = 'https://github.com/cbehrenberg/writio.git'
def jobName = 'writio-build-branch'
def defaultBranch = 'dev'

pipelineJob(jobName) {

    description("Builds a writio branch.")

    parameters {

        gitParameter {
            name("branch")
            type("PT_BRANCH")
            defaultValue(defaultBranch)
            branch(defaultBranch)
            description("writio branch to build")
            branchFilter("origin/(.*)")
            tagFilter("*")
            sortMode("NONE")
            selectedValue("NONE")
            quickFilterEnabled(false)
            useRepository(repositoryUrl)
        }

		booleanParam("parameterization", false, "If checked, a dry run is performed to initialize parameters")
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