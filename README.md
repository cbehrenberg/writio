<img src="https://raw.githubusercontent.com/cbehrenberg/writio/media/writio_logo.png" alt="writio logo" style="zoom: 50%;" />

Suite for indexing and searching literature to get inspiration, such as for your own writing!

This is a work in progress project - *everything can change!*

### Features

With writio you can:

- parse epub files as books (title, language, pages, sections and paragraphs)

- store parsed books in a versioned protobuf- or JSON file

### Tech Stack

Writio is a suite consisting of:

- Compiled, runnable Java 11 JARs using [Apache Maven 3.6.3](https://maven.apache.org/) and [Amazon Corretto 11](https://aws.amazon.com/corretto/)
- Container-based Jenkins instance to build local working copy or branch from GitHub
- Developed and tested with Eclipse IDE 2020-03

### Technical Guide

#### How to Clone with Git

Latest work in progress is on branch "dev". Clone like this:

```
git clone --branch dev -v "https://github.com/cbehrenberg/writio.git"
```

#### How to build with Jenkins

Writio comes with a local CI chain based on Jenkins, based on [Docker Compose](https://docs.docker.com/compose/).

##### Build and start local Jenkins instance

Navigate to the */ci* subdirectory, open a terminal and execute:

```shell
docker-compose up -d
```

On the first run, a number of container images will be configured and built.

When the task is completed, navigate in your browser to http://localhost:48080/ to open the Jenkins Dashboard. 

##### Build local working copy

Open the pipeline job "writio-build-local" and click "Build Now" to build the local working copy. 

The generated artifacts - such as the runnable JARs, s.b. - are located in your local file system.

##### Build remote GitHub branch

You build any writio GitHub branch with the pipeline job "writio-build-branch". Click "Build with Parameters" and select the branch to build, such as "dev". Jenkins will checkout the branch from GitHub and build it.

The generated artifacts are not stored in the local file system. After the job is finished, you can click on the left pane on "Workspaces" and navigate through the checked out and -built branch inside Jenkins. You can download the entire workspace or single files individually.

#### How to use writio

##### Prebuild Applications

The following apps are built as runnable JARs in subfolder */app* (see */target* folders):

- **epub-json-file**: parses a epub file and writes out a JSON file with the content. Example:

  ```shell
  java -jar epub-json-file.jar --in-file <path-to-epub> --out-file <path-to-json>
  ```

