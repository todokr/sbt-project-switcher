# sbt-project-switcher [![Latest version](https://index.scala-lang.org/todokr/sbt-project-switcher/sbt-project-switcher/latest.svg)](https://index.scala-lang.org/todokr/sbt-project-switcher/sbt-project-switcher)

A sbt plugin to switch project in a snappy way⚡️


## Demo
![demo](https://raw.githubusercontent.com/todokr/sbt-project-switcher/master/pjs.gif)


## Usage

Enter `pjs` in sbt console.

```console
$ sbt
> pjs
```

Then, choose project name you want to switch.


## Requirement
sbt-project-switcher uses [fzf](https://github.com/junegunn/fzf) to filter project.  
Install fzf and make sure that the command is in PATH and available.


## Setup
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.todokr/sbt-project-switcher/badge.svg)](https://search.maven.org/artifact/io.github.todokr/sbt-project-switcher)


### Globally

Recommended. Just add sbt-project-switcher to `~/.sbt/1.0/plugins/sbt-project-switcher.sbt`.

```scala
addSbtPlugin("io.github.todokr" % "sbt-project-swithcer" % "(version)")
```


### Per project

Add sbt-project-switcher to `project/plugins.sbt`.

```scala
addSbtPlugin("io.github.todokr" % "sbt-project-swithcer" % "(version)")
```


## License
MIT
