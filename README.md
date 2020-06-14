<img src="https://raw.githubusercontent.com/cbehrenberg/writio/media/writio_logo.png" alt="writio logo" style="zoom: 50%;" />

Writio is a suite of tools to generate ideas for your own writing. For example, it can be used to index literature and make it searchable for keywords.

At the moment, you can parse and tokenize digital ebooks for further processing.

This is a private project that should accompany me as part of my uncreative writing process for my own novel.

### Suite Contents

The suite consists of two parts: an [app](https://github.com/cbehrenberg/writio/tree/master/app) targetted for the end user and a [Continuous Integration (CI) chain](https://github.com/cbehrenberg/writio/tree/master/ci) for the developers.

The app is right now a Java 11 application to parse an epub file, tokenize it and store the contents as JSON or Google Protobuf file. This can and will be used later to feed a search engine. There will be more tools later.

The CI chain is based on a customized Jenkins server. It can be used to build branches or the local working copy and to create a release.

### Source Code Management

The [master branch](https://github.com/cbehrenberg/writio/tree/master) should be used to use the writio app, as this represents the last stable last [released version](https://github.com/cbehrenberg/writio/releases).

The source code will be developed further on the [dev branch](https://github.com/cbehrenberg/writio/tree/dev).

At the moment, I am working on an [MVP version](https://github.com/cbehrenberg/writio/projects/1). Hence, the current version is 0.0.x and is targetted to converge in version 0.1.0.

The release job in Jenkins creates a tag on GitHub in which the latest version is replaced by the tag version. Furthermore, the built container images (only Jenkins atm) are published as well on [Dockerhub](https://hub.docker.com/repository/docker/writio/jenkins).

### Quickstarts

#### How do I tokenize an ebook in epub format and save the result?

Get a digital copy in the form of an epub file for the book you want to tokenize.

Install Java 11 on your machine, for example [Amazon Coretto](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html).

Download the latest version of the epub-json-file JAR file from the [release page](https://github.com/cbehrenberg/writio/releases) or compile writio in the latest version (see below).

Run the app as follows and adjust the path of the input file (epub) and the output file (JSON) accordingly:

```shell
java -jar epub-json-file.jar --in-file <path-to-epub> --out-file <path-to-json>
```

#### How do I build a local working copy of writio?

Clone the master of writio to get the latest stable version.

Install Docker for your operating system. Make sure Docker can mount the content of the checked-out source code. Make sure that Docker compose is installed (e.g., with Docker Desktop for Windows, it is already included).

Open a command line in the *\ci* subfolder and start Jenkins with the following command:

```shell
docker-compose up -d
```

Open Jenkins Dashboard with accessing the URL http://localhost:48080

Select the job writio-build-local and click "Build Now."

After the compilation process you can find the built artifacts for download on the job page. Alternatively, you can find the compiles on the hard drive, for example, the app in subfolder *\app\epub-json-file\target*.