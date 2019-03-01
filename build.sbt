import Dependencies._

sbtPlugin := true

version          := "0.1.0"
organization     := "io.github.todokr"
organizationName := "todokr"
name             := "sbt-project-switcher"
scalacOptions    += "-feature"

lazy val root = (project in file("."))
  .settings(
    name := "sbt-project-switcher",
    libraryDependencies += scalaTest % Test
  )

description := "A sbt plugin to switch project in a snappy way⚡️"
licenses    := List("MIT" -> new URL("https://opensource.org/licenses/MIT"))
homepage    := Some(url("https://github.com/todokr/sbt-project-switcher"))
developers := List(
  Developer(
    id    = "todokr",
    name  = "Shunsuke Tadokoro",
    email = "s.tadokoro0317@gmail.com",
    url   = url("https://github.com/todokr")
  )
)
scmInfo := Some(
  ScmInfo(
    url("https://github.com/todokr/sbt-project-switcher"),
    "scm:git@github.com:todokr/sbt-project-switcher.git"
  )
)
pomIncludeRepository := { _ => false }
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
publishMavenStyle := true
//credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credential")