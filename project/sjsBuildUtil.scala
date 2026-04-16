













trait SpecialSjsBuildConfigs
extends
AnyRef
with SpecialSjsBuildUtil
{
  this : (
    AnyRef
    with SpecialBspConfigs
    // with ScalablyTypedBuildUtil
    with SpecialSjsBuildUtil
  ) =>
  ;

  ;

  import sbt._
  import sbt.Keys.{libraryDependencies }

  import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

  import sbtcrossproject.CrossProject
  import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType, _}

  import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

  import scalajscrossproject.ScalaJSCrossPlugin.autoImport._

  ;

  ;

  ;

  object sJsTasks
  {

    //

    def fastLinkDuring(Phase1 : Configuration)
    : Def.Initialize[Task[Seq[File] ] ]
    = {
      //

      Def.task[Seq[File] ] ({
        Keys.streams.value.log.info(s"invoking FastLinkJs")
        val fjsv = (Phase1 / fastLinkJS).value
        Keys.streams.value.log.info(s"fjsv: ${fjsv}")
        Seq()
      })
    }

    def bundleWithWebpackTaskDuring(Phase1: Configuration, which1: (
      // (
      //   org.scalajs.sbtplugin.ScalaJSPlugin.autoImport.fullOptJS.type |
      //   org.scalajs.sbtplugin.ScalaJSPlugin.autoImport.fastOptJS.type
      // )
      sbt.TaskKey[?]
    ))
    = {
      (Phase1 / which1 / scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport.webpack )
    }

  }

  @deprecated("renamed into 'sJsTasks.fastLinkDuring(Phase1 = Phase1 )'.")
  def fjsHighLevelTaskIn(Phase1 : Configuration)
  : Def.Initialize[Task[Seq[File] ] ]
  = {
    //

    sJsTasks.fastLinkDuring(Phase1 = Phase1)
  }

  @deprecated("renamed into 'fjsHighLevelTaskIn(Phase1 = Compile )'.")
  def fjsHighLevelTask
  = fjsHighLevelTaskIn(Phase1 = Compile )

  //
}

trait SpecialSjsBuildUtil
extends
AnyRef
{
  this : (
    AnyRef
    // with ScalablyTypedBuildUtil
  ) =>
  ;

  ;

  import sbt._
  import sbt.Keys.{libraryDependencies }

  import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

  import sbtcrossproject.CrossProject
  import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType, _}

  import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

  import scalajscrossproject.ScalaJSCrossPlugin.autoImport._

  ;

  ;

  ;

  implicit class XCrossProjectSuggestedJsSpecificSettingsOps(receiver: CrossProject ) {
    //

    def toSwitchLinkerToEsm()
    : CrossProject
    = withSjsLinkerSpecificModuleType(ModuleKind.ESModule )

    def withSjsLinkerSpecificModuleType
    (t: org.scalajs.linker.interface.ModuleKind )
    : CrossProject
    = {
      ;
      receiver
      .jsSettings(
        //

        scalaJSLinkerConfig ~= (s0 => {

          s0
          .withModuleKind(t)
        })
        ,

        scalaJSLinkerConfig ~= (c => {
          // TODO
          c
          .withOutputPatterns(org.scalajs.linker.interface.OutputPatterns.fromJSFile(s"%s.${
            t match {
              case ModuleKind.ESModule => "mjs"
              case ModuleKind.CommonJSModule => "cjs"
              case _ => "js"
            }
          }") )
        })
        ,

        /* can't do this checking since Metals won't put the SBT Bloop Plugin outside usage of it */
        // /**
        //  * Bloop
        //  * refuses to fully evaluate the std fields, citing the issues with side-effects, and instead
        //  * evaluates the "proxy" fields, in this case `bloopScalaJSModuleKind`
        //  */
        // bloopScalaJSModuleKind := {
        //   throw new IllegalStateException(s"unsupported Bloop-import ; please switch to SBT-BSP")
        // }
        // ,

      )
    }

    def toSwitchLinkerToCommonJs()
    : CrossProject
    = {
      withSjsLinkerSpecificModuleType(ModuleKind.CommonJSModule )
    }

  }

  implicit class ScpScalaJsBundlerSpecificOps(receiver: CrossProject ) {
    //

    def withRelevantScJsBundlerSpecificSetup(Compile : Configuration, which1: (
      // (
      //   org.scalajs.sbtplugin.ScalaJSPlugin.autoImport.fullOptJS.type |
      //   org.scalajs.sbtplugin.ScalaJSPlugin.autoImport.fastOptJS.type
      // )
      TaskKey[Attributed[File ] ]
    ) )
    : CrossProject
    = {
      ;
      import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin
      receiver
      .jsEnablePlugins(ScalaJSBundlerPlugin )
      .jsSettings(
        //

        /* https://scalacenter.github.io/scalajs-bundler/reference.html#bundling-mode . */
        (Compile / ScalaJSBundlerPlugin.autoImport.webpackBundlingMode )
        := ScalaJSBundlerPlugin.autoImport.BundlingMode.LibraryAndApplication()
        ,

        /* https://scalacenter.github.io/scalajs-bundler/reference.html#bundling-mode-library-and-application */
        (Compile / ScalaJSBundlerPlugin.autoImport.webpackEmitSourceMaps )
        := true
        ,

        // (Compile / Keys.compileIncremental) := {
        //   val result0 = (Compile / Keys.compileIncremental).value
        //   fjsHighLevelTaskIn(Compile ).value
        //   ({ (Compile / fastLinkJS / ScalaJSBundlerPlugin.autoImport.webpack ).value })
        //   result0
        // }

      )
      /* see the relevant, still-abandoned open bug-ticket */
      .toSwitchLinkerToCommonJs()
      .withWpTask(Compile, which1 )
    }

    // TODO
    def withWpTask
      //
      //
      (Compile: Configuration, stage: (
        TaskKey[Attributed[File ] ]
      ) )
    : CrossProject
    = {
      ;

      import org.scalajs.sbtplugin.ScalaJSPlugin
      import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin

      receiver

      .jsSettings(
        //

        (Compile / ScalaJSBundlerPlugin.autoImport.npmDependencies) ++= (
          Nil

          /* the core of this subsystem - ESBuild or Vite or Webpack! */
          :+ ("esbuild" -> "0.18.16")
          // :+ ("vite" -> {
          //   ???
          // } )

        )
        ,

      )
      .jsSettings(
        //

        (Compile / stage / ScalaJSBundlerPlugin.autoImport.webpack ) := {
          // val v0 = (Compile / stage / ScalaJSBundlerPlugin.autoImport.webpack ).value
          // val files = v0.map(_.data )
          ;
          val logger
          = Keys.streams.value.log
          ({  })
          ;
          logger
          .info("a customised version of 'webpack', instead relying on ESBuild or Vite " )
          ;
          implEsbTask(Compile = Compile, stage = stage ).value
        }
        ,

      )
    }

    //
  }

  private
  def implEsbTask
    //
    (Compile: Configuration, stage: (
      TaskKey[Attributed[File ] ]
    ) )
  = Def.task {
    ;

    val logger
    = Keys.streams.value.log
    ({  })

    import org.scalajs.sbtplugin.ScalaJSPlugin
    import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin

    ;

    ;

    // val BUNDLEFILE =     "(?i)bundle\\.[cm]?[jt]s\\z".r.unanchored
    // val LIBRARFILE =    "(?i)library\\.[cm]?[jt]s\\z".r.unanchored
    // val ENTRYPFILE = "(?i)entrypoint\\.[cm]?[jt]s\\z".r.unanchored

    val taskBaseDir : File
    = { (Compile / ScalaJSBundlerPlugin.autoImport.npmUpdate / Keys.crossTarget ).value }
    logger
    .info("taskBaseDir: " + taskBaseDir )

    assert(taskBaseDir.toString.contains("scalajs-bundler"), "taskBaseDir points to unexpected directory: " + taskBaseDir )

    val fOptFile
    = {
      ({
        ;
        (Compile / stage)
        .value
        .data
      } )
    } : File
    logger
    .info("fOptFile: " + fOptFile )

    val fOptFileNameIncludingFnmExt
    = {
      ({
        ;
        fOptFile
        .name
      } : String )
    }
    val fOptFileNameExcludingFnmExt
    = {
      fOptFileNameIncludingFnmExt
      // TODO
      .replaceFirst(".[cm]?[jt]sx?\\z", "")
    }
    logger
    .info("fOptFileNameIncludingFnmExt: " + fOptFileNameIncludingFnmExt )
    logger
    .info("fOptFileNameExcludingFnmExt: " + fOptFileNameExcludingFnmExt )

    ;

    logger
    .info("runnng the task 'npmUpdate' " )
    (Compile / ScalaJSBundlerPlugin.autoImport.npmUpdate).value
    logger
    .info("done the task 'npmUpdate' " )

    ;
    ;

    val appOnlyCodeFilePath
    = { {
      import java.nio.file._
      val f1 = (taskBaseDir / fOptFileNameExcludingFnmExt.++(".js") )
      Files.copy(fOptFile.toPath(), f1.toPath(), StandardCopyOption.REPLACE_EXISTING )
      f1
    } : File }
    val fullBundleFilePath
    = { {
      (taskBaseDir / fOptFileNameExcludingFnmExt.++("-bundle.js") )
    } : File }
    logger
    .info("appOnlyCodeFilePath: " + appOnlyCodeFilePath )
    logger
    .info("fullBundleFilePath: " + fullBundleFilePath )

    ;

    ({
      import java.nio.file.*
      // ((taskBaseDir / "vite.config.mjs" ) : File )
      Files.writeString((
        ((taskBaseDir / "vite.config.mjs" ) : File )
        .toPath()
      ), "import { defineConfig } from \"vite\"; export default defineConfig({ }) ;", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING )
    })

    ;
    ;
    val cmd = (
      "npx.cmd" :: "esbuild" :: ({
        // appOnlyCodeFilePath.absolutePath
        appOnlyCodeFilePath.name
      } : String)
      :: "--bundle" :: "--sourcemap" ::
        // "--target=chrome58,firefox57,safari11,edge16" ::
        "--outfile=".++(fullBundleFilePath.absolutePath ) ::
        Nil
    )
    logger
    .info("running the main EBT " )
    logger
    .info("full cmd: " + cmd )
    val r = {
      import sbt.*
      sys.process.Process(cmd , taskBaseDir )
      .!(logger )
      match {
        case v =>
          if (!(v == 0 ) ) {
            throw new RuntimeException("failed with non-zero exit-code " + v )
          }
      }
    }
    logger
    .info("done the main EBT " )

    ;

    Nil :+ (
      //
      Attributed.apply(fullBundleFilePath : File )(AttributeMap({
        AttributeEntry(scalajsbundler.sbtplugin.SBTBundlerFile.ProjectNameAttr, fOptFileNameExcludingFnmExt)
      } ) )
    )

  }

  ;

  ;

  ;
}










