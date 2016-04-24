[![Build Status](https://travis-ci.org/aarentoft/aMap2016.svg?branch=master)](https://travis-ci.org/aarentoft/aMap2016)

Requirements
============
- Java 7 or newer
- [Gradle](https://gradle.org/gradle-download/) to build easily (on OSX, Gradle can also be installed using homebrew).

Getting Started
=========
## Application
This application uses [Gradle](https://gradle.org/) for the build process. Everything Gradle produces is put in a folder called `build` in the root of the project. The following commands can be executed in any order as Gradle handles prerequisite commands. Eg. `$ gradle run` will automatically compile the source code first.

- To compile the project, `cd` in to the root of the project. Then execute
    ```
    $ gradle compileJava
    ```

- To run the compiled class files
    ```
    $ gradle run -DmapPath=<path-to-osm-file>
    ```
    For example, to use the included `map-anholt.osm' execute the following
    ```
    $ gradle run -DmapPath=data/map-anholt.osm
    ```

- To build an executable jar file
    ```
    $ gradle jar
    ```
    Then run the jar file using
    ```
    $ java -jar build/libs/aMap2016.jar data/map-anholt.osm
    ```
    Running the `aMap2016.jar` file by simply clicking it, will result in the jar file complaining that it cannot find any map data.

- To clean all Gradle generated files
    ```
    $ gradle clean
    ```

## Tests

To run all the JUnit tests
```
$ gradle test
```

**NOTE:** After running some of the previous commands, Gradle may simply report 
```
[...]
:testClasses UP-TO-DATE
:test UP-TO-DATE

BUILD SUCCESSFUL
```
Without actually running the tests. This happens because the tests are being executed as a prerequisite for some of the tasks above. Since the tests have not changed, they are not run again. To force Gradle to run the tests again, simply add the `--rerun-tasks` flag like so

```
$ gradle test --rerun-tasks -DtestDataPath=data/map-anholt.osm
```

About
=====

Authors:
   [Jeppe Mariager-Lam](https://github.com/JeppeMariagerLam), [Nicolai Østby](https://github.com/shooka),  [Asger Arentoft](https://github.com/aarentoft)