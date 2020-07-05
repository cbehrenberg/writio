<img src="https://raw.githubusercontent.com/cbehrenberg/writio/media/writio_logo.png" alt="writio logo" style="zoom: 50%;" />

## Overview

Writio is a suite of tools to generate ideas for your own writing. For example, it can be used to index literature and make it searchable for keywords.

At the moment, users can parse and tokenize digital ebooks for further processing.

This is a private project that should accompany me as part of my uncreative writing process for my own novel.

## Suite Contents

The suite consists of two parts: an [app](https://github.com/cbehrenberg/writio/tree/master/app) targetted for the end user and a [Continuous Integration (CI) chain](https://github.com/cbehrenberg/writio/tree/master/ci) for the developers.

The app is right now a Java 11 application to parse an epub file, tokenize it and store the contents as JSON or Google Protobuf file. This can and will be used later to feed a search engine. There will be more tools later.

The CI chain is based on a custom Jenkins server. It can be used to build branches or the local working copy and to create a release. It is intended to run Ubuntu 18.04 LTS and consumes roughly 2 vCPUs and 2 GB RAM. You can easily deploy the CI stack with provided scripts to set it all up.

## Source Code Management

The [master branch](https://github.com/cbehrenberg/writio/tree/master) should be used to use the writio app, as this represents the last stable last [released version](https://github.com/cbehrenberg/writio/releases).

The source code will be developed further on the [dev branch](https://github.com/cbehrenberg/writio/tree/dev).

At the moment, I am working on an [MVP version](https://github.com/cbehrenberg/writio/projects/1). Hence, the current version is 0.0.x and is targetted to converge in version 0.1.0 in the future.

The release job in Jenkins creates a tag on GitHub in which the latest version is replaced by the tag version. Furthermore, the built container images (only Jenkins atm) are published as well on [Dockerhub](https://hub.docker.com/repository/docker/writio/jenkins).

## Quickstarts

### For Authors and Users

#### How to tokenize an ebook in epub format and save the results

Get a digital copy in the form of an epub file for the book you want to tokenize.

Install Java 11 on your machine, for example [Amazon Coretto](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html).

Download the latest version of the epub-json-file JAR file from the [release page](https://github.com/cbehrenberg/writio/releases) or compile writio in the latest version (see below).

Run the app as follows and adjust the path of the input file (epub) and the output file (JSON) accordingly:

```shell
java -jar epub-json-file.jar --in-file <path-to-epub> --out-file <path-to-json>
```

### For Developers and Enthusiasts

#### How to setup an Linux environment (on Azure) for development and CI

The following explains how to setup a small Azure VM for development and CI. With the scripts, however, you can do this also on a local Linux host. 

> Deployment on Windows 10 is possible with [Docker Desktop](https://www.docker.com/products/docker-desktop), but not supported.

[Create an Azure B2s VM with Ubuntu 18.04 LTS installed](https://azure.microsoft.com/en-in/resources/templates/101-vm-simple-linux/). Enable SSH inbound port 22 and use an Azure-generated private key pair with username = writioadmin. For cost efficiency, use a HDD disk and turn off boot diagnostics. Store the generated pem file securely. On Windows, copy it to a folder where only you have permissions for (e.g. "Documents").

SSH with the pem file as writioadmin to the VM (use your favorite SSH tool, such as [MobaXterm](https://mobaxterm.mobatek.net/)) and execute:

```
bash <(curl -s https://raw.githubusercontent.com/cbehrenberg/writio/master/env/azure/writio_ubuntu_18.04_setup.sh)
```

Clone the master (or any other branch), e.g. to `~/git`.

> You can also use for convenience the provided script. Replace `master` with any other branch, if needed. It checks out to `~/git/writio-<branch>`:
>
> ```
> bash <(curl -s https://raw.githubusercontent.com/cbehrenberg/writio/master/ci/scripts/clone_writio_branch.sh) master
> ```

The setup scripts installs all prerequisites to make builds with Jenkins, but also locally using Maven. Test it by building the repo as follows:

```
mvn clean install
```

To setup the CI stack, switch to the /ci directory of the cloned repo and execute the script `writio_ci_setup.sh` with following arguments. Please note that the Jenkins credentials are mandatory, the GitHub/DockerHub credentials are optional - but must be both present, if you want to use the release job:

- `--jenkins-username`, `--jenkins-secret` : Jenkins credentials (mandatory). Used to log into Jenkins Dashboard.
- `--github-username`, `--github-secret` : GitHub credentials (optional). Necessary for the release job to create a GitHub tag.
- `--dockerhub-username`, `--dockerhub-secret` : DockerHub credentials (optional). Necessary for the release job to push the built container images to DockerHub.

Example:

```
bash writio_ci_setup.sh --jenkins-username=writioadmin --jenkins-secret=... \
                        --github-username=cbehrenberg --github-secret=... \
						--dockerhub-username=writio --dockerhub-secret=...
```

> If you do not like to use the script `writio_ci_setup.sh`, you can also manually edit the file docker-compose.yml accordingly.

Start the CI stack as follows in the /ci directory:

```
docker-compose up -d
```

> To stop it at a later time, use `docker-compose down`

If you are deploying on Azure, create now a SSH tunnel with forwarding of port 48080 (not necessary for local deployments). For Windows, a helper script `open_ssh_tunnel.cmd` is located in */env*. Create a new terminal window (`cmd.exe`) and execute the script with the IP of your VM and the path to the pem file, for example:

```
open_ssh_tunnel.cmd <ip> C:\Users\Christian\Documents\writio_key.pem
```

> As long as terminal window is open, you can access Jenkins through port 48080. If you close, you can't do it.

Open a browser and navigate to the following URL to open Jenkins. Use the Jenkins credentials you entered above: http://localhost:48080/login

#### How to build a local working copy with Jenkins

Clone the branch you want to work on and set up the writio CI stack as described above.

Access the Jenkins dashboard and select the job *writio-build-local* and click *"Build Now."*

After the compilation process you can find the built artifacts for download on the job page. Alternatively, you can find the compiles on the hard drive, for example, the app in subfolder *\app\epub-json-file\target*.

> It is also possible to compile with `mvn clean install` without Jenkins. Make sure it is latest Maven and Java 11 (automatically installed with the `writio_ubuntu_18.04_setup.sh` script, s.a.)

### For Project Owners

#### How to release writio

Make sure that the software version to be released is on the dev branch, including update of the README.md (this file).

Clone the dev branch and replace your credentials for the GitHub and DockerHub project with `writio_ci_setup.sh`. Start a fresh instance of Jenkins with it.

Run the job *writio-release-branch* on the dev branch and assign a meaningful new [semantic version number](https://semver.org/) with no preceeding "v". As long as the MVP is still being developed, the tiny version 0.0.x will be pulled up.

After the release is published on https://github.com/cbehrenberg/writio/releases, download the built artifacts from the Jenkins job and upload them to the release version (to be automated later). Adjust the release notes. As long as the MVP is being developed, mark the release as "pre-release".
