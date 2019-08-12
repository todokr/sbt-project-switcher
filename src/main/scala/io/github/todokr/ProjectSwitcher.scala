package io.github.todokr

import scala.sys.process._
import sbt.Keys._
import sbt._

import scala.util.{Failure, Success, Try}

object ProjectSwitcher extends AutoPlugin {
  override def requires: Plugins = empty
  override def trigger: PluginTrigger = allRequirements

  object autoImport {
    val pjsFilterCommand: SettingKey[String] = settingKey[String]("cli filtering tool you want to use")
  }

  import autoImport._

  override def projectSettings: Seq[Setting[_]] = Seq(
    pjsFilterCommand := "fzf",
    commands += Command.command("pjs") { state =>
      val projects = buildDependencies.value.classpath.keys.map(projectRef => (projectRef.project, projectRef.build.toString))
      Try {
        val echoProjectNames = Seq("echo", projects.map { case (projectName, _) => projectName }.mkString(System.lineSeparator))
        echoProjectNames.#|(pjsFilterCommand.value).!!.trim
      } match {
        case Success(selectedProjectName) =>
          projects.find { case (projectName, _) => projectName == selectedProjectName } match {
            case Some((projectName, projectPath)) =>
              s"; project {$projectPath}; project $projectName" :: state
            case None =>
              state.handleError(new MessageOnlyException(s"The selected project named $selectedProjectName does not exist."))
          }
        case Failure(_) =>
          state
      }

    }
  )
}