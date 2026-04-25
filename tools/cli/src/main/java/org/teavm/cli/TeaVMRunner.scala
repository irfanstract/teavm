
package org.teavm
package cli


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
// import java.util.List;
// import org.apache.commons.cli.CommandLine;
// import org.apache.commons.cli.CommandLineParser;
// import org.apache.commons.cli.DefaultParser;
// import org.apache.commons.cli.HelpFormatter;
// import org.apache.commons.cli.Option;
// import org.apache.commons.cli.Options;
// import org.apache.commons.cli.ParseException;
import org.teavm.toolingCommon.x.Cli.{Flag, FlagSeq, PositionalArg, AbsAssigningFlag, AddendingFlag, MaybeFlagOrArgString}
import org.teavm.toolingCommon.x.Cli.structures.FTN

import org.teavm.backend.modular.CodeSplittingMode
import org.teavm.backend.modular.CodeSplittingType
import org.teavm.backend.javascript.JSModuleType;
// import org.teavm.backend.wasm.render.WasmBinaryVersion;
import org.teavm.tooling.ConsoleTeaVMToolLog;
import org.teavm.tooling.TeaVMProblemRenderer;
import org.teavm.tooling.TeaVMTargetType;
import org.teavm.tooling.TeaVMTool;
import org.teavm.tooling.TeaVMToolException;
import org.teavm.tooling.util.FileSystemWatcher;
import org.teavm.vm.TeaVMOptimizationLevel;
import org.teavm.vm.TeaVMPhase;
import org.teavm.vm.TeaVMProgressFeedback;
import org.teavm.vm.TeaVMProgressListener;

@main
def TeaVMRunner1(args: String*) = TeaVMRunner.main(args*)

object TeaVMRunner :

    def main(args: String*): Unit =

        val opts = parseArgs(args)

        pprint.log(opts)

        locally { ??? }

    def parseArgs(s: Seq[MaybeFlagOrArgString] )
        : ToolchainOptions
    =
        s
        .pipe { s => FlagSeq.parseTokens(s).toSeq }
        .foldLeft[ToolchainOptions] (ToolchainOptions() ) :
            case (s0, e ) =>
                e.match
                case AbsAssigningFlag(FTN(k @ ("TARGET"  | "TARGET-PLATFORM" )), v) => s0.copy(targetPlatform = v.toString() )
                case AbsAssigningFlag(FTN(k @ ("OUTDIR"  | "OUTPUT-DIRECTORY")), v) => s0.copy(outputDirSpec  = v.toString() )
                case AbsAssigningFlag(FTN(k @ ("OUTFILE" | "OUTPUT-FILE"     )), v) => s0.copy(outputFileSpec = v.toString() )
                case AbsAssigningFlag(FTN(k @ ("MINIFY"                      )), v) => s0.copy(shallMinify = v.toString().matches("1|TRUE|true") )
                case AbsAssigningFlag(FTN(k @ ("DEBUG"                       )), v) => s0.copy(debugMode = v.toString() )
                case AbsAssigningFlag(FTN(k @ ("MAIN"                        )), v) => s0.copy(mainClassNames = Seq() :+ v.toString() )
                case AddendingFlag(FTN(k @ ("ENTRY-POINT"                    )), v) => s0.copy(mainClassNames = s0.mainClassNames :+ v.toString() )
                case AbsAssigningFlag(FTN(k @ ("OMIT-INCOMPLATIBLECLASSCHANGEERROR-CLASSES"  )), v) => ???
                case AbsAssigningFlag(FTN(k @ ("OMIT-VIRTUALMACHINEERROR-CLASSES"            )), v) => ???
                case AddendingFlag(FTN(k @ ("STDLIB-OPTION"                  )), v) => s0.copy(stdLibOptions = s0.stdLibOptions :+ v.toString() )
                case AddendingFlag(FTN(k @ ("LINKER-FLAG"                    )), v) => s0.copy(linkerOptions = s0.linkerOptions :+ v.toString() )
                case AddendingFlag(FTN(k @ ("LINKER-OPTION"                  )), v) => s0.copy(linkerOptions = s0.linkerOptions :+ v.toString() )
                case AbsAssigningFlag(FTN(k @ ("CODESPLITTING-RULE"          )), v) => s0.copy(codeSplittingRule = CodeSplittingMode.byType { CodeSplittingType.valueOf(v.toString() ) } )
                case f => throw new IllegalArgumentException(s"unsupported flag: $f" )

    case class ToolchainOptions(
        //
        targetPlatform: String = ARG_UNITIALIZED ,
        outputDirSpec : String = ARG_UNITIALIZED ,
        outputFileSpec: String = ARG_UNITIALIZED ,
        shallMinify: Boolean = ARG_UNITIALIZED ,
        debugMode       : String = ARG_UNITIALIZED ,
        mainClassNames   : Seq[String] = Seq() ,
        linkerOptions   : Seq[String] = Seq() ,
        stdLibOptions   : Seq[String] = Seq() ,
        codeSplittingRule: CodeSplittingMode = ARG_UNITIALIZED ,
    )

    def ARG_UNITIALIZED[A]: A
    = (null).asInstanceOf[Any].asInstanceOf[A]

class TeaVMRunner {

}











