
package org.teavm

package toolingCommon.x


val _ = { }




import util.chaining.{*, given}

// import avcfw.fncpl.Io.{JSON}
import org.teavm.toolingCommon.{JSON}





package Cli {
;


implicit object ccClwOps:
  extension [CC[E] <: collection.IterableOnceOps[E, CC, CC[E]] , E1 ] (x: CC[E1] )
    def collectWhileDefined
      [E2] (f: PartialFunction[E1, E2] )
      : CC[E2]
    =
      // import avcfw.txcCommons.whileRightIterableOp.{takeWhileRight as takeWhileRight1}
      x.map((f.lift).andThen(_.toRight { new Exception(s"ne") {} }.toTry ) ).takeWhileSuccess()

implicit object ccTfrOps:
  extension [CC[E] <: collection.IterableOnceOps[E, CC, CC[E]] , E1 ] (x: CC[E1] )
    def takeWhileSuccess
      [E2] (dummyArg1: Unit = {}) (using cf: E1 <:< util.Try[E2])
      : CC[E2]
    = x.takeWhile { _.isSuccess }.map { _.get }


export structures.{TogglingFlag, AssigningFlag, AbsAssigningFlag, AddendingFlag, PositionalArg}


object ValidWrittenFlagKey {
  def unapply(x: String)
  // : Option[String]
  =
    Some { x }
    .filter { v => v.matches("(?![01-9])(?=\\w)[a-zA-Z0-9\\-]+") }
    .map(identity[String] )
}

/**
 * having everything managed in a single place is important because
 * - the parsing logic is somewhat complex and needs to be consistent across different parts of the codebase, and
 * - if we change the parser then we'll also likely need to change the formatting logic, and having them together makes it easier to maintain consistency.
 * 
 * **the parser**
 * 
 * all this is essentially what's adopted by [[https://esbuild.github.io/api/ `esbuild`]].
 * 
 * `args` will be interpreted as a sequence of named-argument(s) followed by a sequence of positional-argument(s).
 * initially, the parser is in a state which accepts named-argument(s).
 * as-soon-as we encounter something like an `--` (PositionalArgsStart),
 * we switch the parsing mode to treat all subsequent tokens as positional arguments, even if they look like flags.
 * 
 * ```sh
 * # FOR ILLUSTRATION ONLY -- DON'T BLINDLY TRY ANY OF THESE!!
 * 
 * ixtvm-npx --yes --package:esbuild --package:esbuild-minify-plugin -- esbuild --help
 * #   named     : --yes --package:esbuild --package:esbuild-minify-plugin
 * #   positional: esbuild --help
 * ````
 * 
 * like `clang` and `esbuild`, and unlike `fftools` (`ffmpeg`, `ffprobe`, etc) and `scalac`,
 * here every named-argument is held to just be a single token.
 * 
 * there are three flavours of named-arguments:
 * - toggling: `--flag` (enables the flag)
 * - absolute assigning: `--flag=value` (sets the flag to the specified value)
 * - addending: `--flag:value` (adds the specified value to the existing value of the flag, if applicable)
 * 
 */
object ArgsFormat {

  // export structures.{Flag}
  // export structures.{FlagOrArg}

  ;

  /* PARSING */

  def parseTokens(x: Seq[MaybeFlagOrArgString] )
    : FlagSeq
  = psx.parseTokens0(using eofp = SWITCH_OR_MAIN ) (x)

  object psx :

    /**
     * a recursive function for that.
     * 
     * initially, the parser is in a state which accepts named-argument(s).
     * as-soon-as we encounter a PositionalArgsStart,
     * we switch the parsing mode to treat all subsequent tokens as positional arguments, even if they look like flags.
     * 
     */
    def parseTokens0
      (using eofp: Eofp )
      (x: Seq[MaybeFlagOrArgString] )
      : FlagSeq
    =
      x.match

        case Seq() =>
          Seq()

        case x0 +: xRest =>

          { Flag.parse(x0) }.match

            case (f: structures.Flag) if (eofp < MAIN_ARGS_STARTING) =>
              f +: parseTokens0(xRest)

            case (x0 : structures.PositionalArgsStart) if (eofp <= MAIN_ARGS_STARTING) =>
              parseTokens0(using eofp = MAIN_ARGS_STARTING ) (xRest)
              .toSeq
            // case x0 : structures.PositionalArg =>
            //   x0 +: parseTokens0(using eofp = MAIN_ARGS_ALREADY_STARTED ) (xRest)
            case _ =>
              structures.PositionalArgsStart() +: x.map { structures.PositionalArg(_) }

      .pipe { FlagSeq(_) }

    opaque type Eofp
    >: Int
    =  Int

  inline val SWITCH_OR_MAIN = 0
  inline val MAIN_ARGS_STARTING = 2
  inline val MAIN_ARGS_ALREADY_STARTED = 3

  /**
   * parses ONLY A SINGLE Flag Token str.
   * 
   * there are three flavours of named-arguments:
   * - toggling: `--flag` (enables the flag)
   * - absolute assigning: `--flag=value` (sets the flag to the specified value)
   * - addending: `--flag:value` (adds the specified value to the existing value of the flag, if applicable)
   * 
   */
  def parseSingleToken(x: MaybeFlagOrArgString)
    : Flag
  =
      x.match

        case "" =>
          throw new IllegalArgumentException(s"empty string is not a valid flag or arg")

        case s"-h"      => parseSingleToken("--help")
        case s"-help"   => parseSingleToken("--help")
        case s"-n"      => parseSingleToken("--no")
        case s"-y"      => parseSingleToken("--yes")
        case s"-i"      => parseSingleToken("--interactive")

        case s"--help" =>
          TogglingFlag("help")

        case s"--${k @ ValidWrittenFlagKey(k1) }:${v }" => AddendingFlag   (k, v)
        case s"--${k @ ValidWrittenFlagKey(k1) }=${v }" => AbsAssigningFlag(k, v)
        case s"--${k @ ValidWrittenFlagKey(k1) }"               => TogglingFlag    (k)

        case s if s.matches("-{2,}") =>
          structures.PositionalArgsStart()

        /* TODO decide whether we should handle more cases */
        case v if !(
          v startsWith "-"
        ) =>
          PositionalArg(rawValue = v )

        case v =>
          throw new IllegalArgumentException(s"for ${JSON.stringify(v) }")

  ;

  /* PRINTING */

  def formatKEnabled            [K   ] (k: K      ): String = s"--$k"
  def formatKResetToV           [K, V] (k: K, v: V): String = s"--$k=$v"
  def formatKAddedWithV         [K, V] (k: K, v: V): String = s"--$k:$v"

  def formatPositionalArgsStart() = "--"

  /**
   * value, without key.
   * positional-argument(s) are examples of that.
   */
  def formatAnonymousV         [V] (v: V): String = s"$v"

} // ArgsFormat


/**
 * maybe the string constitutes valid flag string, maybe not;
 */
type MaybeFlagOrArgString
>: String
<: String

type FlagOrArgString
>: String
<: String

object FlagSeq {

  // export structures.{Flag}
  // export structures.{FlagOrArg}

  def parseTokens(x: Seq[MaybeFlagOrArgString] )
    : FlagSeq
  = ArgsFormat.parseTokens(x)

  @deprecated
  def apply(x: Seq[structures.FlagOrArg] )
  : FlagSeq
  = x

  extension (x: FlagSeq)

    def flagsTcdn =
      x.flags
      .foldLeft[(Map[String, String] )] (Map() ) :
        case (m0, entr1 ) =>
          m0.updatedWith(entr1.k.toString() ) :
            _.getOrElse("")
            .pipe : v0 =>
              entr1.match
                case entr1: AddendingFlag => s"$v0;${entr1.v.toString()}"
                case entr1: AbsAssigningFlag => s"${entr1.v.toString()}"
                case entr1: TogglingFlag => "1"
              .pipe { Some(_) }

    def flags: Seq[structures.Flag] = x.flagsAndArgs._1
    def pArgs = x.flagsAndArgs._2

    def flagsAndArgs
      : (Seq[structures.Flag], Seq[PositionalArg])
    =
      val e1 @ (flgs) = x.collectWhileDefined { case e: structures.Flag => e }
      val e2 @ (poslArgs ) = (x.drop(flgs.length) ).dropWhile( _.isInstanceOf[structures.PositionalArgsStart ] ).map { case e: PositionalArg => e }
      (flgs, poslArgs)

  given `FlagSeq as Seq[Flag]`
  : Conversion[FlagSeq, (Seq[Flag])]
  = _.asInstanceOf

} // FlagSeq

opaque type FlagSeq
<: Any
= Seq[structures.FlagOrArg]

object Flag {

  given      `CommandLineParser.FromString[Flag]`
  :      util.CommandLineParser.FromString[Flag]
  = { case s => parse(s) }

  /**
   * parses ONLY A SINGLE Flag Token str.
   * 
   * this entry-point
   * is important to avoid direct usage of the `Cli.structure` API(s) likely to change.
   */
  def parse(x: MaybeFlagOrArgString): Flag
  =
      ArgsFormat.parseSingleToken(x)

  type TargetName
  = structures.FlagTargetName

  extension (x: Flag )
    def key: Flag.TargetName
    = x.k

} // Flag

type Flag
>: structures.FlagOrArg
<: structures.FlagOrArg



/**
 * the data-structure(s).
 * 
 * avoid using API(s) in this `package` directly; these are subject to change.
 * to parse SINGLE Flag(s), use `Flag.parse(...)`.
 * 
 */
package structures
{

  private val formatFlagComponent
  = JSON.stringify[String] (_)

  opaque type FlagTargetName
  = String
  object FlagTargetName {
    // @deprecated
    given Conversion[String, FlagTargetName]
    = (identity[String] andThen { _.toUpperCase(java.util.Locale.ROOT) } ).apply
  }

  @deprecated
  object FTN :
    def unapply(x: FlagTargetName )
    = Some(x.asInstanceOf[String] )

  type FlagOrArgValue
  >: String
  <: String

  /**
   * this base class has three direct subclasses: `Flag`, `PositionalArg`, and `PositionalArgsStart`.
   * 
   */
  trait FlagOrArg private[Cli] ()
  { val k: FlagTargetName }

  trait Flag
  extends
    FlagOrArg
  { val k: FlagTargetName }

  case class TogglingFlag(k: FlagTargetName)
  extends Flag
  { override def toString(): String = quoteFlagOrArgString { ArgsFormat.formatKEnabled(k) } }

  trait AssigningFlag
  extends
    Flag
  { }

  case class AbsAssigningFlag(k: FlagTargetName, v: FlagOrArgValue)
  extends
    Flag
    with AssigningFlag
  { override def toString(): String = quoteFlagOrArgString { ArgsFormat.formatKResetToV(k, v) } }

  case class AddendingFlag(k: FlagTargetName, v: FlagOrArgValue)
  extends
    Flag
    with AssigningFlag
  { override def toString(): String = quoteFlagOrArgString { ArgsFormat.formatKAddedWithV(k, v) } }

  case class PositionalArgsStart()
  extends
    FlagOrArg
  {
    override final val k = ""
    override def toString(): String = ArgsFormat.formatPositionalArgsStart()
  }

  case class PositionalArg(rawValue: FlagOrArgValue )
  extends
    FlagOrArg
  {
    override final val k = ""
    override def toString(): String = quoteFlagOrArgString(ArgsFormat.formatAnonymousV(rawValue) )
  }

  /**
   * the primary intent of this function is to quote the flag or arg string in a way that it can be safely passed to a command-line parser, especially when the string contains spaces or special characters. By using JSON.stringify, we can ensure that the string is properly escaped and quoted according to JSON rules, which are generally compatible with command-line parsing conventions.
   */
  def quoteFlagOrArgString (x: FlagOrArgString ): String = JSON.stringify[String] { x }

} // structures





} // Cli





