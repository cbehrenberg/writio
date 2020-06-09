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
            description("writio branch to release from")
            branchFilter("origin/(.*)")
            tagFilter("*")
            sortMode("NONE")
            selectedValue("NONE")
            quickFilterEnabled(false)
            useRepository(repositoryUrl)
        }

		stringParam("version", "", "writio release version")

		credentialsParam('git_credentials') {
            type('com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl')
            description('writio github credential')
			defaultValue('writio-github-credential')
			required()
        }

		credentialsParam('docker_credentials') {
            type('com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl')
            description('writio dockerhub credential')
			defaultValue('writio-dockerhub-credential')
			required()
        }

		booleanParam("do_build", true, "If checked, container images are pushed to docker.io")
		
		booleanParam("do_deploy_tag", true, "If checked, release branch is deleted")
		
		booleanParam("do_delete_branch", true, "If checked, a dry run is performed to initialize parameters")

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

