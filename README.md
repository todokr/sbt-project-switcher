# sbt-project-switcher

## Demo
![demo](https://raw.githubusercontent.com/todokr/sbt-project-switcher/master/pjs.gif)

## Requirement
CLI filter tool you like

- [fzf](https://github.com/junegunn/fzf) (default)
- [peco](https://github.com/peco/peco)
- [percol](https://github.com/mooz/percol)
- [fzy](https://github.com/jhawthorn/fzy)
- etc.

If you want to use filter tool other than default, you can set in `PjsFilterCommand` (See **Usage**). 

## Usage

### build.sbt
```scala
pjsFilterCommand := "fzf" // filter tool command you want to use
enablePlugins(ProjectSwitcher)

lazy val root = project in file(".")
lazy val lorem = project in file("lorem")
lazy val ipsum = project in file("ipsum")
...
```

## License
MIT
