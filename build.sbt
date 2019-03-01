import Dependencies._

version       := "0.1.0"
organization  := "io.github.todokr"
name          := "sbt-project-switcher"
sbtPlugin     := true
scalacOptions += "-feature"

lazy val root = (project in file("."))
  .settings(
    name := "sbt-project-switcher",
    libraryDependencies += scalaTest % Test
  )

 ThisBuild / description := "A sbt plugin to switch project in a snappy way⚡️"
 ThisBuild / licenses    := List("MIT" -> new URL("https://opensource.org/licenses/MIT"))
 ThisBuild / homepage    := Some(url("https://github.com/todokr/sbt-project-switcher"))
 ThisBuild / developers := List(
   Developer(
     id    = "todokr",
     name  = "Shunsuke Tadokoro",
     email = "s.tadokoro0317@gmail.com",
     url   = url("https://github.com/todokr")
   )
 )
 ThisBuild / pomIncludeRepository := { _ => false }
 ThisBuild / publishTo := {
   val nexus = "https://oss.sonatype.org/"
   if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
   else Some("releases" at nexus + "service/local/staging/deploy/maven2")
 }
 ThisBuild / publishMavenStyle := true
