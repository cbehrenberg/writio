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

                    path("/scm")

                    clearWorkspace(true)
                    copyHidden(true)

                    filterSettings {
                        includeFilter(false)
                    }
                }
            }
        }
    }
}

