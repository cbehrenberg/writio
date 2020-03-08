<img src="https://raw.githubusercontent.com/cbehrenberg/writio/media/writio_logo.png" alt="writio logo" style="zoom: 50%;" />

Suite for indexing and searching literature to get inspiration, such as for your own writing!

This is a work in progress project - *everything can change!*

### Features

Writio is a Java 11 suite consisting of the following features:

- Parse epub as book (title, language, pages, sections and paragraphs)
- Store parsed books in a versioned protobuf or JSON representation

### Technical Guide

#### How to Clone

Latest work in progress is on branch "dev". Clone like this:

```
git clone --branch dev -v "https://github.com/cbehrenberg/writio.git"
```

#### How to build

##### Prerequisites

The following software must be installed in order to build and run writio:

- **Maven**, such as [*Apache Maven 3.6.3*](https://maven.apache.org/)
- **Java 11**, such as *[Amazon Corretto 11](https://aws.amazon.com/corretto/)*

##### Build

Run Maven build on parent directory:

```shell
mvn clean install
```

#### How to use

##### Prebuild Applications

The following applications are built as runnable JARs in subfolder "app" (see /target folders):

- **epub-json-file**: parses a epub file and writes out a JSON file with the content. Example:

  ```shell
  java -jar epub-json-file.jar --in-file <path-to-epub> --out-file <path-to-json>
  ```

