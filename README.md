[![Build Status](https://travis-ci.org/aarentoft/aMap2016.svg?branch=master)](https://travis-ci.org/aarentoft/aMap2016)

Requirements
============
- Java 7
- [JUnit](https://github.com/junit-team/junit4/wiki/Download-and-Install) for unit tests

Getting Started
=========
## Application

`cd` in to the root of the project where the [Makefile](Makefile) is located. Then execute
```
$ make
```
to compile the project. Compiled files are put in a folder called "bin". To run the compiled application, run
```
$ make ARGS="<path-to-osm-file>" run
```
For example, to use the included `map-anholt.osm' execute the following
```
$ make ARGS="map-anholt.osm" run
```
Execute the following to clean the project
```
$ make clean
```

For more information about the build process and the execution of the application, refer to the [Makefile](Makefile)

About
=====

Authors:
   [Jeppe Mariager-Lam](https://github.com/JeppeMariagerLam), [Nicolai Østby](https://github.com/shooka),  [Asger Arentoft](https://github.com/aarentoft)