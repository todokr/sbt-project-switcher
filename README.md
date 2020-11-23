# sbt-project-switcher [![Latest version](https://img.shields.io/badge/sbt--project--switcher-0.1.4-green.svg?ver=0.1.4)](https://index.scala-lang.org/todokr/sbt-project-switcher/sbt-project-switcher)

A sbt plugin to switch project in a snappy way⚡️

![demo](https://raw.githubusercontent.com/todokr/sbt-project-switcher/master/pjs.gif)


## Usage

```console

$ sbt

# Select project
> pjs # Then, choose project name you want to switch.

# Select project from history
> pjs h # or pjs history

# Switch to previously selected project
> pjs -
```

## Requirement
sbt-project-switcher uses [fzf](https://github.com/junegunn/fzf) to filter project.  
Install fzf and make sure that the command is in PATH and available.

If you want to use a filter other than `fzf`, you can set `pjsFilterCommand` key.

## Setup
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.todokr/sbt-project-switcher/badge.svg)](https://search.maven.org/artifact/io.github.todokr/sbt-project-switcher)


### Globally

Recommended. Just add sbt-project-switcher to `~/.sbt/1.0/plugins/sbt-project-switcher.sbt`.

```scala
addSbtPlugin("io.github.todokr" % "sbt-project-switcher" % "(version)")
```


### Per project

Add sbt-project-switcher to `project/plugins.sbt`.

```scala
addSbtPlugin("io.github.todokr" % "sbt-project-switcher" % "(version)")
```


## License
MIT
