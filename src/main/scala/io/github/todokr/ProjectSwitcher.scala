package io.github.todokr

import scala.sys.process._
import scala.util.{Failure, Success, Try}

import sbt.Keys._
import sbt._

trait PluginInterface {
  val pjs: TaskKey[Unit] = taskKey[Unit]("Switch projects snappy")
  val pjsFilterCommand: SettingKey[String] = {
    settingKey[String]("CLI filtering tool you want to use")
  }
  val pjsHistoryPath: SettingKey[File] =
    settingKey[File]("The location where pjs history is persisted.")

  val pjsMaxHistories: SettingKey[Int] =
    settingKey[Int]("Max size of pjs histories")
}

object ProjectSwitcher extends AutoPlugin {

  override def requires: Plugins = empty
  override def trigger: PluginTrigger = allRequirements

  val DefaultFilterCommand: String = "fzf"
  val DefaultPjsHistoryPath: String = "target/.pjshistory"
  val DefaultMaxHistories: Int = 10
  val HistoryColumnSeparator: String = "\t"

  object autoImport extends PluginInterface

  import autoImport._

  override def projectSettings: Seq[Setting[_]] =
    Seq(
      pjsFilterCommand := pjsFilterCommand.?.value
        .getOrElse(DefaultFilterCommand),
      pjsHistoryPath := pjsHistoryPath.?.value.getOrElse {
        val app = appConfiguration.value
        val base = app.baseDirectory.getCanonicalFile
        base / DefaultPjsHistoryPath
      },
      pjsMaxHistories := pjsMaxHistories.?.value.getOrElse(DefaultMaxHistories),
      commands ++= Seq( //
        Command.command("pjsh") { state =>
          state
        },
        Command.args("pjs", "pjs") { (state, args) =>
          def updateHistory(projectInfo: ProjectInfo): Unit = {
            val prevLines = IO
              .readLines(pjsHistoryPath.value)
              .take(pjsMaxHistories.value - 1)
            val newLine = projectInfo match {
              case ProjectInfo(pName, Some(pPath)) =>
                s"$pName$HistoryColumnSeparator$pPath"
              case ProjectInfo(pName, _) => s"$pName"
            }
            IO.writeLines(pjsHistoryPath.value, newLine :: prevLines)
          }
          def listProjects(): Seq[ProjectInfo] =
            buildDependencies.value.classpath.keys
              .map(ref => ProjectInfo(ref.project, Some(ref.build.toString)))
              .toSeq
          def listHistories(): Seq[ProjectInfo] =
            IO.readLines(pjsHistoryPath.value)
              .map(ProjectInfo.fromHistory)
          def findPrevProject(): Option[ProjectInfo] = {
            IO.readLines(pjsHistoryPath.value)
              .lift(1)
              .map(ProjectInfo.fromHistory)
          }
          def selectProject(
            projects: Seq[ProjectInfo]
          ): Either[String, ProjectInfo] = {
            Try {
              val echoProjectNames = Seq(
                "echo",
                projects
                  .map { case ProjectInfo(projectName, _) => projectName }
                  .mkString(System.lineSeparator)
              )
              echoProjectNames.#|(pjsFilterCommand.value).!!.trim
            } match {
              case Success(selected) =>
                projects
                  .find {
                    case ProjectInfo(projectName, _) => projectName == selected
                  }
                  .toRight(
                    s"The selected project named $selected does not exist."
                  )
              case Failure(e) => Left(e.getMessage)
            }
          }

          IO.touch(pjsHistoryPath.value, setModified = false)

          args match {
            case Seq("-") =>
              val prevProject = findPrevProject().getOrElse(ProjectInfo.root)
              updateHistory(prevProject)
              prevProject.toCommand :: state
            case Seq("h") | Seq("history") =>
              val histories = listHistories()
              selectProject(histories) match {
                case Left(msg) =>
                  state.handleError(new MessageOnlyException(msg))
                case Right(projectInfo) =>
                  updateHistory(projectInfo)
                  projectInfo.toCommand :: state
              }
            case Seq(path) =>
              val projectInfo = ProjectInfo(path, None)
              updateHistory(projectInfo)
              projectInfo.toCommand :: state
            case Seq() =>
              val projects = listProjects()
              selectProject(projects) match {
                case Left(msg) =>
                  state.handleError(new MessageOnlyException(msg))
                case Right(projectInfo) =>
                  updateHistory(projectInfo)
                  projectInfo.toCommand :: state
              }
          }
        }
      )
    )

  case class ProjectInfo(projectName: String, projectPath: Option[String]) {

    def toCommand: String = this match {
      case ProjectInfo(pName, Some(pPath)) =>
        s"; project {$pPath}; project $pName"
      case ProjectInfo(pName, None) =>
        s"; project $pName"
    }
  }

  object ProjectInfo {
    val root: ProjectInfo = ProjectInfo("root", None)
    def fromHistory(line: String): ProjectInfo =
      line.split(HistoryColumnSeparator) match {
        case Array(pName: String) => ProjectInfo(pName, None)
        case Array(pName: String, pPath: String) =>
          ProjectInfo(pName, Some(pPath))
        case _ => throw new Exception("Invalid pjs history")
      }
  }
}
