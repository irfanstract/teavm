






import sbt._
import sbt.Keys.{libraryDependencies }

import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

import sbtcrossproject.CrossProject
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType, _}

import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

import scalajscrossproject.ScalaJSCrossPlugin.autoImport._








object ResourceFileBuild {
  ;

  ;

  ;

  def qtd
    (value: String)
  = "\"" + value + "\""

  def qtdPath
    (value: java.nio.file.Path)
  = {
    import scala.jdk.CollectionConverters.*
    /* convert to URI first. note also that the regular alt `toString` will leave illegal chars unescaped */
    "\"" + value.toString.replace("\\", "/") + "\""
  }

  ;

  // case class SpecialGlobs(flts: Seq[java.io.FileFilter] )

  val defaultSupportedFmts
  = (
    io.GlobFilter("*.txt")
    | io.GlobFilter("*.log")
    | io.GlobFilter("*.properties")
    | io.GlobFilter("*.csv")
    | io.GlobFilter("*.yaml")
    | io.GlobFilter("*.json")
    | io.GlobFilter("*.xml")
    | io.GlobFilter("*.css")
    | io.GlobFilter("*.scss")
    | io.GlobFilter("*.ttf")
    | io.GlobFilter("*.otf")
    | (io.GlobFilter("*.jpg") | io.GlobFilter("*.jpeg") ) // TODO
    | (io.GlobFilter("*.mjp") | io.GlobFilter("*.mjpg") | io.GlobFilter("*.mjpeg") ) // TODO
    | io.GlobFilter("*.jp2000") // TODO
    | io.GlobFilter("*.png")
    | io.GlobFilter("*.apng")
    | io.GlobFilter("*.svg")
    | io.GlobFilter("*.gif")
    | io.GlobFilter("*.avi")
    | io.GlobFilter("*.mp3")
    | io.GlobFilter("*.ogv")
    | io.GlobFilter("*.mp4")
    | io.GlobFilter("*.mkv")
    | io.GlobFilter("*.weba")
    | io.GlobFilter("*.webm")
    | io.GlobFilter("*.flac")
    | io.GlobFilter("*.dash") // TODO
    | io.GlobFilter("*.html")
    | io.GlobFilter("*.bin")
    | io.GlobFilter("*.wat")
    | io.GlobFilter("*.wasm")
    | io.GlobFilter("*.class")
    | io.GlobFilter("*.glsl")
    | io.GlobFilter("*.zip")
    | io.GlobFilter("*.gz")
    | io.GlobFilter("*.iso")
  ) : io.NameFilter

  ;

  ;

  // TODO
  /* no need to restrict to JS ; we're only doing logging here */
  def printAllResFilesIn
    (f: File )
    (logger : Logger )
  : Unit
  = {
    ;
    val fmts = defaultSupportedFmts
    ;
    //
    // val logger = Keys.streams.value.log
    logger.info("traversing, for resource files: " + f )
    import sbt.io.*
    for (itemPath <- PathFinder(f).descendantsExcept(fmts, ".ignore").get.take(15 ) ) {
      logger.info("resource file: " + itemPath )
    }
  }

  // TODO
  def toTranslateAllResFilesIn
    (srcRoot: File )
    (destDir : File )
  = Def.task {
    //
    ;
    val fmts = defaultSupportedFmts
    ;
    val logger
    = Keys.streams.value.log
    val platformLetter = crossProjectPlatform.value.identifier
    import sbt.io.*
    logger.debug(s"toTranslateAllResFilesIn: " )
    logger.debug(s"for src dir $srcRoot -> dest dir $destDir " )
    case class SpecialTranslateRelativePathIntoPkgAndClassName
      (itemRelativePath: File )
    {
      ;

      val sFullName
      = {
        itemRelativePath
        .toString()
        .replace("\\", "/")
        .replaceFirst("\\A\\.\\/", "")
        .split("\\/")
        .toIndexedSeq
        .map((s: String ) => s.replaceAll("\\W+", "_") )
      }

      val sPkgName :+ sLeafName
      = sFullName
    }
    for (itemRelativePath <- {
      PathFinder(srcRoot).descendantsExcept(fmts, ".ignore").get
      .map(itemAbsPath => {
        srcRoot
        .relativize(itemAbsPath ).get
      } )
    } )
    yield {
      // logger.info("resource file: " + itemPath )
      val prs = SpecialTranslateRelativePathIntoPkgAndClassName(itemRelativePath = itemRelativePath )
      import prs.{itemRelativePath => _ , sLeafName => sFileLeafName , _}
      // TODO
      (for (
        (generatedFileContent, destPath) <- Some( {
          //
          ;
          val finalLeafName
          = {
            // s"${sFileLeafName}_${platformLetter}Asset"
            s"${sFileLeafName}"
          }
          ;
          (
            Seq()
            .++({
              sPkgName
              .map("package " + _)
            })
            .:+("" )
            // .:+("import typings.{std as domItc } " )
            // .:+("import typings.std.{global as dom } " )
            .:+("" )
            .:+(s"/**" )
            .:+(s" * generated from" )
            .:+(s" * file `${itemRelativePath }` in src-tree `${srcRoot }` " )
            .:+(s" * for import/reference from Scala ." )
            .:+(s" */ " )
            // .:+(s"object $finalLeafName {" )
            // // .:+(s"  /* ... TODO code here ... */ " )
            // .:+(s"} // $finalLeafName " )
            .:+("final /* needs to be `final`, to work-around the `not a legal path` complaint */" )
            .:+(s"lazy val $finalLeafName " )
            .:+(s"= avcframewrk.forms.templating.assetsymbolism.`%%`( " )
            .:+(s"  symbolisedName = ${qtd(finalLeafName ) } , " )
            .:+(s"  itemRelativePath = ${qtdPath(itemRelativePath.toPath ) } , " )
            .:+(s"  srcRoot = ${qtdPath(srcRoot.toPath ) } , " )
            .:+(s") " )
            .:+("" )
            .mkString("\r\n")
          ) : String
        } , {
          Some(itemRelativePath)
          .map( { s => new File(s.toString() + ".scala") })
          // .map( { s => (new File("scala") ).toPath .resolve(s.toPath() ) .toFile })
          .map( { s => destDir.toPath .resolve(s.toPath() ) .toFile })
          .get
        } : File )
      )
      yield {
        ;
        logger.info(s"generating $destPath " )
        ;
        if (false) {
          logger.info("contents: " + generatedFileContent )
        }
        IO.write(destPath, generatedFileContent : String )
        ;
        (() , destPath)
      })
      .get._2
    }
  }

  ;

  ;
}

















