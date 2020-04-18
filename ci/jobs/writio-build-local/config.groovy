def jobName = 'writio-build-local'

pipelineJob(jobName) {

    description("Builds the current writio working copy from local disk.")

    properties {

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

                filesystem {

                    path("/var/jenkins_home/jobs/writio-build-local/workspace")

                    clearWorkspace(false)
                    copyHidden(true)

                    filterSettings {
                        includeFilter(false)
                    }
                }
            }
        }
    }
}

