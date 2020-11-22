package io.github.todokr

import scala.sys.process._
import scala.util.{Failure, Success, Try}

import sbt.Keys._
import sbt._

trait PluginInterface {
  val pjs: TaskKey[Unit] = taskKey[Unit]("switch projects snappy")
  val pjsFilterCommand: SettingKey[String] =
    settingKey[String]("cli filtering tool you want to use")
}

object ProjectSwitcher extends AutoPlugin {

  override def requires: Plugins = empty
  override def trigger: PluginTrigger = allRequirements

  val DefaultFilterCommand: String = "fzf"

  object autoImport extends PluginInterface

  import autoImport._

  override def projectSettings: Seq[Setting[_]] =
    Seq(
      pjsFilterCommand := pjsFilterCommand.?.value
        .getOrElse(DefaultFilterCommand),
      commands ++= Seq( //
        Command.args("pjs", "pjs") { (state, args) =>
          args.headOption
            .map(path => s"; project $path" :: state)
            .getOrElse {
              val projects = buildDependencies.value.classpath.keys
                .map(
                  projectRef => (projectRef.project, projectRef.build.toString)
                )
              Try {
                val echoProjectNames = Seq(
                  "echo",
                  projects
                    .map { case (projectName, _) => projectName }
                    .mkString(System.lineSeparator)
                )
                echoProjectNames.#|(pjsFilterCommand.value).!!.trim
              } match {
                case Success(selectedProjectName) =>
                  projects.find {
                    case (projectName, _) => projectName == selectedProjectName
                  } match {
                    case Some((projectName, projectPath)) =>
                      s"; project {$projectPath}; project $projectName" :: state
                    case None =>
                      state.handleError(
                        new MessageOnlyException(
                          s"The selected project named $selectedProjectName does not exist."
                        )
                      )
                  }
                case Failure(_) =>
                  state
              }
            }
        }
      )
    )
}
