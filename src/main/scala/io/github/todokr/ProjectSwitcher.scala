package io.github.todokr

import scala.sys.process._
import sbt.Keys._
import sbt._

import scala.util.{Failure, Success, Try}

object ProjectSwitcher extends AutoPlugin {
  override def requires = empty
  override def trigger = allRequirements

  object autoImport {
    val pjsFilterCommand = settingKey[String]("cli filtering tool you want to use")
  }

  import autoImport._

  override def globalSettings = Seq(
    pjsFilterCommand := "fzf"
  )

  override def projectSettings: Seq[Setting[_]] = Seq(
    commands += Command.command("pjs") { state =>
      val projects = buildDependencies.value.classpath.keys.map(_.project)
      val result = Try(Seq("echo", projects.mkString(System.lineSeparator)).#|(pjsFilterCommand.value).!!.trim)
      result match {
        case Success(selectedProject) => s"project $selectedProject" :: state
        case Failure(_) => state
      }

    }
  )
}
