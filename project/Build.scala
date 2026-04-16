













object Build {

   // TODO
   private[Build]
   implicit class AnyReceiverChainingOps[P] (x: P) {
     def pipe[R] (fnc: Function[P, R] )
     = { fnc.apply(x) }
   }

   /**
    * 
    * config which needs to be manually sync-ed against the correspondents in other file(s)
    * 
    */
   object vars {

   } /* object vars */

   // final
   // lazy val pre
   // : PreBuild.type
   // = PreBuild

   /** 
    * 
    * to implement `platform.config.version`
    * Bloop stuck to
    * looping over `libraryDependencies` and
    * filtering to ones whose, in addition to being in/of the expected `organization`, `configuration` "matches" `CompilerPluginConfig`.
    * this means that
    * there *needs to* be the relevant SN-or-SJS artifact(s) (with)in `libraryDependencies`, having that `Configuration`
    * 
    */
   final
   lazy val pbgConfig
   = {

      sbt.Configuration.of("XCompilerPluginBc", "plugin->default(compile)")
   }

   /** 
    * 
    * to avoid spurious "not found" complaints,
    * we needed to move the affected things away from `/build.sbt` into here,
    * which means we needed to bring most the other things along.
    * 
    * to do both things at once -- developing for JVM and developing for JS, the so-called "cross-compilation" --
    * we needed to put `sbt-crossproject` into play here.
    * - https://www.scala-js.org/doc/project/cross-build.html
    * - https://github.com/portable-scala/sbt-crossproject
    * 
    * we disabled the line `import sbt.Keys._` since
    * the ident(s) of the defs within `sbt.Keys$`
    * conflicts with the fact that the values (of the defs) are `sbt.YyyKey`s rather than their values.
    * 
    */
   object mainly
   extends
   AnyRef
   with StdLibBuild
   // with ScalablyTypedBuildUtil
   with SpecialBspConfigs
   with SpecialSjsBuildConfigs
   {
      //

      import sbt._
      // import sbt.Keys.{compile => _, sourceGenerators => _, _} /* has naming issues */
      import sbt.Keys.{libraryDependencies }

      import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

      import sbtcrossproject.CrossProject
      import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType, _}

      import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

      import scalajscrossproject.ScalaJSCrossPlugin.autoImport._

      //

      /**
       * inspired by typical layout of JS/TS/Node/Vite Project(s)
       */
      lazy val packagesParentDir
      = file(".")

      /**
       * inspired by typical layout of JS/TS/Node/Vite Project(s)
       */
      lazy val extLibsParentDir
      = file("lxtvmExtLibs")

      /**
       * suggested version for Sc `3.7` (up to including patch ver)
       */
      lazy val suggestedScala3Dot07VersionV: String
      = "3.7.4"

      /**
       * suggested version for Sc `3.8` (up to including patch ver)
       */
      lazy val suggestedScala3Dot08VersionV: String
      = "3.8.4-RC1"

      // /**
      //  * suggested version for Sc `3.9` (up to including patch ver)
      //  */
      // lazy val suggestedScala3Dot09VersionV: String
      // = "3.9.0"

      lazy val suggestedScalaVersionV: String
      = suggestedScala3Dot07VersionV

      ThisBuild / Keys.scalaVersion := suggestedScalaVersionV

      def computeNecessaryProjectSrcCodeManifest()
      = {
        import sbt.Keys._

        Seq(
          //

          /* TITLE */

          name := { s"${name.value } for ${ crossProjectPlatform.value.identifier }" } ,

          /* SCALAC OPTIONS */

          scalaVersion := suggestedScalaVersionV ,

          scalacOptions += "-Yexplicit-nulls" ,
          scalacOptions += "-language:implicitConversions" ,

          scalacOptions += "-Wsafe-init" ,
          // scalacOptions += "-Xcheck-macros" ,

          // scalacOptions += "-explain" ,
          scalacOptions += "-feature" ,
          scalacOptions += "-deprecation" ,
          scalacOptions += "-unchecked" ,

          /* RESOURCE PATHS */

          Compile / resourceDirectories += (
            baseDirectory.value / "src" / "main" / "resources"
          )
          ,

          /* 
           * THE STD LIB DEPENDENCY SPEC
           * HAS BEEN MOVED BELOW, UNDER `withXMinimumNecessaryBoilerplate`,
           * CALLED BY `asLeafProjectWithNecessarySettings`
           */

        )
      }

      // libraryDependencies ++= {

      //   println(s"PATH: '${java.lang.System.getenv("PATH") }'")

      //   Seq()
      // }

      // //

      /* see also [https://github.com/portable-scala/sbt-crossproject](`sbt-crossproject`) */
      val suggestedTargetPlatforms: Seq[sbtcrossproject.Platform ]
      // = Seq(JSPlatform)
      = Seq(JVMPlatform)

      implicit class XCrossProjectSuggestedSourceArtefactSettingsOps(receiver: CrossProject ) {

        def asLeafProjectWithNecessarySettings(
          skipPlatforms: Boolean = false ,
        )
        // : CrossProject
        = {
          receiver
          .pipe { _.settings( computeNecessaryProjectSrcCodeManifest() ) }
          .pipe { _.withXMinimumNecessaryBoilerplate() }
          .pipe { _.withSuggestedPlatformSpecifics(skipPlatforms = true ) }

          .pipe { _.withJsonIterLib() }

        }

      }

      implicit class XCrossProjectBuilderSuggestedSettingsOps(receiver: CrossProject.Builder ) {

        def withSuggestedSettings()
        // : CrossProject.Builder
        = {
          receiver

          .withoutSuffixFor(JVMPlatform )
          // .crossType(CrossType.Pure )
          .crossType {
              import sbtcrossproject._
              import java.io.{File}
              import sbt._

              new CrossType {
                  override def projectDir(crossBase: File, projectType: String): File =
                    projectType match {
                      case  "jvm" => crossBase
                      case  _     => crossBase / ("." + projectType)
                    }

                  override def projectDir(crossBase: File, platform: Platform): File =
                    projectDir(crossBase, platform.identifier )

                  override def sharedSrcDir(projectBase: File, conf: String): Option[File] =
                    Some(projectBase.getParentFile / "src" / conf / "cross-scala")

                  override def sharedResourcesDir(projectBase: File,
                                                  conf: String): Option[File] =
                    Some(projectBase.getParentFile / "src" / conf / "resources")
              }
          }

        }

      }

      implicit class XCrossProjectSuggestedPlatformSpecificSettingsOps(receiver: CrossProject ) {
         //

         def withSuggestedPlatformSpecifics(skipPlatforms: Boolean )
        //  : CrossProject
         = {
            val skipJs = skipPlatforms

            receiver
            .pipe { case s if (!skipJs ) => {
              s
              .withJavaUtilLocaleCQuiroz()
              .toSwitchLinkerToEsm()
              /* 
               * 
               * https://www.scala-js.org/doc/semantics.html .
               * otherwise, one'll get `Error` when it needs to be `RuntimeException` instead.
               * 
               */
              .withMaintainedClassCastExceptioning()
              // .asBrowserBasedApp()
            } ; case s => s }
            .pipe { case s if (!skipJs || !skipPlatforms ) => {
              s
              .withWebCtxStdLib()
              // TODO
              .withSuggestedResFilesOps()
            } ; case s => s }
         }

         def withMainClass
            //
            (mainClassNames: Option[String])
         : CrossProject
         = {
            ;
            receiver
            .settings(
              //

              (Compile / Keys.mainClass) := mainClassNames
              ,

            )
            .jsSettings(
              //

              // Tell Scala.js that this is an application with a main method
              // I'll keep it here for quite a while
              scalaJSUseMainModuleInitializer := true
              ,

              /* Configure Scala.js to emit modules in the optimal way to
               * connect to Vite's incremental reload.
               * - emit as many small modules as possible for classes in the "livechart" package
               * - emit as few (large) modules as possible for all other classes
               *   (in particular, for the standard library)
               */
              scalaJSLinkerConfig ~= (s0 => {

                import org.scalajs.linker.interface.ModuleSplitStyle

                s0
                .withModuleSplitStyle(
                  ModuleSplitStyle.FewestModules )
              }),

            )
         }

         def asBrowserBasedApp
            //
            (mainClassNames: Option[String])
         : CrossProject
         = {
            ;
            receiver
            .withMainClass(mainClassNames )
            .withJsCorePolyfills()
            .withWebCtxStdLib()
         }

         /* 
          * 
          * https://www.scala-js.org/doc/semantics.html .
          * otherwise, one'll get `Error` when it needs to be `RuntimeException` instead.
          * 
          */
         def withMaintainedClassCastExceptioning()
         : CrossProject
         = {
            ;
            receiver
            .jsSettings(
              //

              scalaJSLinkerConfig ~= (c => (
                 c.withSemantics(s => (
                    s
                    .withAsInstanceOfs(org.scalajs.linker.interface.CheckedBehavior.Compliant )
                 ) )
              ) )
              ,

            )
         }

      }

      implicit class XCrossProjectSuggestedResFilesOps(receiver: CrossProject ) {
        //

        import ResourceFileBuild.*

        // TODO
        def withSuggestedResFilesOps()
        : CrossProject
        = {
          def main()
          : CrossProject
          = {
            ;

            receiver
            .settings(
              //
              (Compile / Keys.sourceGenerators) ++= {
                // (
                //   Nil
                //   :+ (Keys.unmanagedResourceDirectories )
                //   :+ (Keys.unmanagedSourceDirectories )
                // )
                // .map(key => {
                //   ((), bsk(srcDirsConfig = key))
                // })
                // .map(e => e._2.taskValue )
                (
                  Nil
                  :+ bsk(srcDirsConfig = Keys.unmanagedResourceDirectories ).taskValue
                  :+ bsk(srcDirsConfig = Keys.unmanagedSourceDirectories   ).taskValue
                )
              }
              ,
            )
          }

          // workaround to compiler failure due to exception
          def bsk
            (srcDirsConfig: SettingKey[Seq[File]], skipResourceFiles: Boolean = true )
          = Def.taskDyn {
            ;

            ({
              ;

              val logger
              = {
                (Keys.streams).value
                .log
              }

              val destDir
              = {
                (Compile / Keys.sourceManaged).value
              } : File

              (Compile / srcDirsConfig).value
              .map(rootDir => {
                // printAllResFilesIn(rootDir )(logger )
                // (() -> Seq() )
                ({
                  ;

                  toTranslateAllResFilesIn(srcRoot = rootDir )(destDir = destDir )
                })
              })
              match { case tasks => {
                // Def.sequential(tasks)
                tasks
                .foldLeft[Def.Initialize[Task[Seq[File] ] ] ](Def.task(Seq() ) )((t0, t1) => Def.task({ t0.value ++ t1.value }) )
              } }
            })
          }

          main()
        }

        //
      }

      /* (with)in-chain utils */

      /**
       * 
       * ```
       * new Exception(s"sourceGenerators evaluated")
       * .printStackTrace()
       * ```
       * 
       * */
      @deprecated("use 'new Exception(....).printStackTrace()' directly.")
      def runSge()
      : Unit
      = {
        //

        new Exception(s"sourceGenerators evaluated")
        .printStackTrace()
      }

      implicit class CrossProjectDbpOPs(receiver: CrossProject)
      {

        //

        def withDbp(mainClassNames: Option[String])
        : CrossProject
        = {
          //

          val fjslStage
          = org.scalajs.sbtplugin.ScalaJSPlugin.autoImport.fastOptJS
          
          receiver
          
          .asBrowserBasedApp(mainClassNames )

          .jsEnablePlugins(scalajsbundler.sbtplugin.ScalaJSBundlerPlugin )
          .withRelevantScJsBundlerSpecificSetup(Compile , fjslStage )
          .jsSettings(
            //

            bspConfigs.forBuildTargetCompile := {
              //

              val value = (bspConfigs.forBuildTargetCompile ).evaluated
              kPrintln(s"done Compile:${bspConfigs.forBuildTargetCompile.toString }")
              fjsHighLevelTask.value
              // ({ ; (Compile / fjslStage / scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport.webpack ).value })
              sJsTasks.bundleWithWebpackTaskDuring(Compile , fjslStage ).value
              value
            }
            ,

            (Compile / Keys.sourceGenerators) += {
              //

              Def.task[Seq[File] ] ({
                runSge()
                Seq()
              })
              .taskValue
              // .triggeredBy((Compile / fastLinkJS) )
            }
            , 

            /* etc */

          )
        }

      }

   }

}












