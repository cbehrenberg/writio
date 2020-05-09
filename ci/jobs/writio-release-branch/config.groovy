def repositoryUrl = 'https://github.com/cbehrenberg/writio.git'
def jobName = 'writio-release-branch'
def defaultBranch = 'dev'

pipelineJob(jobName) {

    description("Builds and releases writio branch.")
	
    parameters {
        gitParameter {
            name("from_branch")
            type("PT_BRANCH")
            defaultValue("dev")
            branch("dev")
            description("writio branch to release from")
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

