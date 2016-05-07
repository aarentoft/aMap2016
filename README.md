[![Build Status](https://travis-ci.org/aarentoft/aMap2016.svg?branch=master)](https://travis-ci.org/aarentoft/aMap2016)

Requirements
============
- Java 7 or newer
- [Gradle](https://gradle.org/gradle-download/) to build easily (on OSX, Gradle can also be installed using homebrew).

Getting Started
===============
### Application
This application uses [Gradle](https://gradle.org/) for the build process. Everything Gradle produces is put in a folder called `build` in the root of the project. The following commands can be executed in any order as Gradle handles prerequisite commands. Eg. `gradle run` will automatically compile the source code first.

For more information about the build process, refer to the [build.gradle](build.gradle) file.

- To compile the project, `cd` in to the root of the project and do
    ```
    $ gradle compileJava
    ```

- To run the compiled class files
    ```
    $ gradle run
    ```
    To bypass the map file browser and pass a map file directly
    ```
    $ gradle run -DmapPath=<path-to-osm-file>
    ```
    For example, to use the included `map-anholt-raw.osm` execute the following
    ```
    $ gradle run -DmapPath=data/map-anholt-raw.osm
    ```

- To build an executable jar file
    ```
    $ gradle jar
    ```
    Then run the jar file using
    ```
    $ java -jar build/libs/aMap2016.jar
    ```
    or simply browse to `build/libs` and double click `aMap2016.jar`.
    Alternatively, you can pass the map data path to the jar via the command line
    ```
    $ java -jar build/libs/aMap2016.jar data/map-anholt-raw.osm
    ```
    This omits the file browser which appears otherwise.
- (OSX) Build a .app bundle
    ```
    $ gradle createApp
    ```
    The app bundle is available in `build/macApp`

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
$ gradle test --rerun-tasks
```

Using custom maps
=================
It is possible to use custom maps exported directly from [OpenStreetMap](http://openstreetmap.org). The application supports map files in the [OSM XML format](http://wiki.openstreetmap.org/wiki/OSM_XML) (.osm file ending) which is the format which openstreetmap exports to per default. It also supports files in the binary and compressed [PBF format](http://wiki.openstreetmap.org/wiki/PBF_Format) (.pbf ending). The tool [Osmosis](http://wiki.openstreetmap.org/wiki/Osmosis) can convert between the two formats and save a lot of disk space. OSM can files get really large really fast. For more information on how to use Osmosis, refer to [this link](http://wiki.openstreetmap.org/wiki/Osmosis/Detailed_Usage_0.44).

**DO NOTE** that only map files of a certain size can be used. This is a result of the fact that _ALL_ the data from a map file is read into memory and not streamed dynamically from disk. Using too large map files will cause Java to throw a `java.lang.OutOfMemoryError: GC overhead limit exceeded`. However, it does not do that before the memory allocated to the JVM has been filled, so if the application seems to hang when loading a custom map, the map might be too big. If the map is only a little too big, one can attempt using different values for [`-Xmx`](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/java.html) JVM argument to modify the size of the memory allocation pool.



About
=====
This piece of software was originally written in the spring of 2011. We were lent a proprietary set of data containing the road data of Denmark for which we were to use to create an interactive map application for. The application had to draw the data and enable panning, zooming and path finding from one road to another. Unfortunately, we could not put the software anywhere public in its form at the time for a couple of reasons. For one, as the data was proprietary and not ours, we were not allowed to share it with anyone, so if we shared the application, it would not be able to show anything. Secondly, the code which loaded the proprietary road data into Java was not ours either so we could not share the application at all. All this code was removed from the project before the initial commit was made for this repository so all code from the initial commit to now (2016-05-05) is written by the three authors listed below.

Authors:
   [Jeppe Mariager-Lam](https://github.com/JeppeMariagerLam), [Nicolai Østby](https://github.com/shooka), [Asger Arentoft](https://github.com/aarentoft)
