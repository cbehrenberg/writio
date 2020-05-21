def repositoryUrl = 'https://github.com/cbehrenberg/writio.git'
def jobName = 'writio-release-branch'
def defaultBranch = 'dev'

pipelineJob(jobName) {

    description("Builds and releases a writio branch.")
	
    parameters {

        gitParameter {
            name("branch")
            type("PT_BRANCH")
            defaultValue(defaultBranch)
            branch(defaultBranch)
            description("writio branch to release from, default: ${defaultBranch}")
            branchFilter("origin/(.*)")
            tagFilter("*")
            sortMode("NONE")
            selectedValue("NONE")
            quickFilterEnabled(false)
            useRepository(repositoryUrl)
        }

		stringParam("version", "<version>", "writio release version")

		credentialsParam('credentials') {
            type('com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl')
            required()
            description('writio GitHub credentials')
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

