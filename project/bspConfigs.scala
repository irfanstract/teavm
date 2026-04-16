














trait SpecialBspConfigs
{
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

  /**
  * 
  * runs `println(value)` or `Keys.streams.value.log.info(value)`.
  * 
  * */
  def kPrintln(value: Any)
  = println(value)

  /*  
  * https://github.com/sbt/sbt/blob/ecfb0624e911798a6d2bdf1f6a7c45acb1c59b1e/main/src/main/scala/sbt/internal/server/BuildServerProtocol.scala
  * .
  */
  object bspConfigs
  {

    //

    /** 
    * 
    * `bspBuildTargetCompile`.
    * scoped at `Global`.
    * 
    * an `InputKey` rather than a `TaskKey` !!!
    * 
    */
    lazy val forBuildTargetCompile
    = {
      //

      Global / Keys.bspBuildTargetCompile
    }

    /** 
    * 
    * `bspBuildTargetCompileItem`.
    * scoped at `phase`, which will need to be one of `Compile`, `Test`, `IntegrationTest`.
    * 
    */
    def forBuildTargetCompileItemAt(phase: Configuration)
    = {
      //

      phase / Keys.bspBuildTargetCompileItem
    }

  }

  ;

  ;
}
















