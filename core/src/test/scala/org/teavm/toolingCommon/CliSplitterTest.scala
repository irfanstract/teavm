package org.teavm

package toolingCommon.x.Cli





class FlagParseTest
extends
  org.scalatest.funsuite.AnyFunSuite
{

  val (parse, tryParse) =
    implicitly[(util.CommandLineParser.FromString[Flag] )].pipe { f => (f.fromString, f.fromStringOption) }

  // transparent inline def togglingFlagTestTitle() = "`verbose` = true"

  test("'--%'") :
    assert(tryParse("--%").isEmpty )

  test("'-verbose'") :
    assert(tryParse("-verbose").isEmpty )

  test("'--verbose'" ) :
    assert(parse("--verbose"     ) == TogglingFlag("verbose") )

  test("'--verbose-streams=1'") :
    assert(parse("--verbose-streams=1") == AbsAssigningFlag("verbose-streams", "1") )
  test("'--verbose-streams:1'") :
    assert(parse("--verbose-streams:1") == AddendingFlag("verbose-streams", "1") )

  test("'--verbose-streams =1'") :
    assert(tryParse("--verbose-streams =1").isEmpty )
  // test("'--verbose-streams= 1'") :
  //   assert(tryParse("--verbose-streams= 1").isEmpty )

  test("'-h'" ) :
    assert(parse("-h"     ) == TogglingFlag("help") )

  test("'-i'" ) :
    assert(parse("-i"     ) == TogglingFlag("interactive") )

  test("DoubleHyphen" ) :
    assert { parse("--"     ) == structures.PositionalArgsStart() }

  test("TripleHyphen" ) :
    assert { parse("---"     ) == structures.PositionalArgsStart() }

  test("QuadrupleHyphen" ) :
    assert { parse("----"     ) == structures.PositionalArgsStart() }

  test("SevenHyphen" ) :
    assert { parse("-------"     ) == structures.PositionalArgsStart() }

  test("'jenum'" ) :
    assert { parse("jenum"     ) == PositionalArg("jenum") }

  test("'scala-contrib'" ) :
    assert { parse("scala-contrib"     ) == PositionalArg("scala-contrib") }

}

class FlagSeqParseTest
extends
  org.scalatest.funsuite.AnyFunSuite
{

  import System.err.{println, print}

  val tryParse =

    { (s: Seq[String]) => for { r <- util.Try { println(s) ; FlagSeq.parseTokens(s) } } yield { println(r) ; r } }
    .compose :
      (_: String)
      .pipe { _.stripLeading().stripTrailing() }
      .pipe { _.split("\\s+").toIndexedSeq }

  test(         "'--verbose --verbose-streams=1 -i jenum jxenum --timeout=50'") :
    locally :
      tryParse(s"--verbose --verbose-streams=1 -i jenum jxenum --timeout=50")
      .tap { println(_) }
      .pipe :
        case util.Success(v) =>
          assert :
            v
            .`==`:
              Seq(
                TogglingFlag("verbose") ,
                AbsAssigningFlag("verbose-streams", "1") ,
                TogglingFlag("interactive") ,
                structures.PositionalArgsStart() ,
                PositionalArg("jenum") ,
                PositionalArg("jxenum") ,
                PositionalArg("--timeout=50") ,
              )
    ()

  test(         "'--verbose --verbose-streams=1 -i --- jenum jxenum --retry --timeout=50 scala-contrib jenum-package'") :
    locally :
      tryParse(s"--verbose --verbose-streams=1 -i --- jenum jxenum --retry --timeout=50 scala-contrib jenum-package")
      .tap { println(_) }
      .pipe :
        case util.Success(v) =>
          assert :
            v
            .`==`:
              Seq(
                TogglingFlag("verbose") ,
                AbsAssigningFlag("verbose-streams", "1") ,
                TogglingFlag("interactive") ,
                structures.PositionalArgsStart() ,
                PositionalArg("jenum") ,
                PositionalArg("jxenum") ,
                PositionalArg("--retry") ,
                PositionalArg("--timeout=50") ,
                PositionalArg("scala-contrib") ,
                PositionalArg("jenum-package") ,
              )
          assert :
            v.flags.`==` :
              Seq(
                TogglingFlag("verbose") ,
                AbsAssigningFlag("verbose-streams", "1") ,
                TogglingFlag("interactive") ,
              )
          assert :
            v.pArgs.`==` :
              Seq(
                PositionalArg("jenum") ,
                PositionalArg("jxenum") ,
                PositionalArg("--retry") ,
                PositionalArg("--timeout=50") ,
                PositionalArg("scala-contrib") ,
                PositionalArg("jenum-package") ,
              )
    ()

  test(         "'--verbose --include:a --include:b -i --cont=5 --- jenum jxenum --retry --timeout=50 wasmbind-contrib blinkie --- jenum-package'") :
    locally :
      tryParse(s"--verbose --include:a --include:b -i --cont=5 --- jenum jxenum --retry --timeout=50 wasmbind-contrib blinkie --- jenum-package")
      .tap { println(_) }
      .pipe :
        case util.Success(v) =>
          assert :
            v
            .`==`:
              Seq(
                TogglingFlag("verbose") ,
                AddendingFlag("include", "a") ,
                AddendingFlag("include", "b") ,
                TogglingFlag("interactive") ,
                AbsAssigningFlag("cont", "5") ,
                structures.PositionalArgsStart() ,
                PositionalArg("jenum") ,
                PositionalArg("jxenum") ,
                PositionalArg("--retry") ,
                PositionalArg("--timeout=50") ,
                PositionalArg("wasmbind-contrib") ,
                PositionalArg("blinkie") ,
                PositionalArg("---") ,
                PositionalArg("jenum-package") ,
              )
    ()

  test(         "'--verbose --include:a --include:b -i --cont=5 --- --retry --timeout=50 wasmbind-contribs blinkie jenum-package'") :
    locally :
      tryParse(s"--verbose --include:a --include:b -i --cont=5 --- --retry --timeout=50 wasmbind-contribs blinkie jenum-package")
      .tap { println(_) }
      .pipe :
        case util.Success(v) =>
          assert :
            v
            .`==`:
              Seq(
                TogglingFlag("verbose") ,
                AddendingFlag("include", "a") ,
                AddendingFlag("include", "b") ,
                TogglingFlag("interactive") ,
                AbsAssigningFlag("cont", "5") ,
                structures.PositionalArgsStart() ,
                PositionalArg("--retry") ,
                PositionalArg("--timeout=50") ,
                PositionalArg("wasmbind-contribs") ,
                PositionalArg("blinkie") ,
                PositionalArg("jenum-package") ,
              )
          assert :
            v.flags.`==` :
              Seq(
                TogglingFlag("verbose") ,
                AddendingFlag("include", "a") ,
                AddendingFlag("include", "b") ,
                TogglingFlag("interactive") ,
                AbsAssigningFlag("cont", "5") ,
              )
          assert :
            v.pArgs.`==` :
              Seq(
                PositionalArg("--retry") ,
                PositionalArg("--timeout=50") ,
                PositionalArg("wasmbind-contribs") ,
                PositionalArg("blinkie") ,
                PositionalArg("jenum-package") ,
              )
    ()

  test(         "'--verbose --verbose-streams=1 -i --- --- --- jenum jxenum --timeout=50'") :
    locally :
      tryParse(s"--verbose --verbose-streams=1 -i --- --- --- jenum jxenum --timeout=50")
      .tap { println(_) }
      .pipe :
        case util.Success(v) =>
          assert :
            v
            .`==`:
              Seq(
                TogglingFlag("verbose") ,
                AbsAssigningFlag("verbose-streams", "1") ,
                TogglingFlag("interactive") ,
                structures.PositionalArgsStart() ,
                PositionalArg("jenum") ,
                PositionalArg("jxenum") ,
                PositionalArg("--timeout=50") ,
              )
    ()

  test("'--% -verbose --verbose-streams=1 -i --- jenum jxenum scala-contrib jenum-package'") :
    // assert :
    //   tryParse(s"--% -verbose --verbose-streams=1 -i --- jenum jxenum scala-contrib jenum-package")
    //   .`==`:
    //     Seq(A )
    ()

} // FlagSeqParseTest







