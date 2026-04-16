/* DO NOT USE BLOOP ! */














import sbtcrossproject.CrossProject



// System.err.println("SBT scalac version: " + scala)

import Build.mainly._





/**
 * hopefully this package could be (re)used for future unrelated projects too.
 */
lazy val avcTxcExtLibsLhyFastparseV03
=
  (
    crossProject(
      suggestedTargetPlatforms
      .+:(JVMPlatform )
      .+:(JSPlatform )
      : _* )
    .withSuggestedSettings()
    in (extLibsParentDir / "ComLihaoyiFastparse3" )
  )
  .asLeafProjectWithNecessarySettings(skipPlatforms = false )
  .settings(libraryDependencies += ("com.lihaoyi" %%% "fastparse" % "3.1.1"))
  .settings(scalacOptions += "-explain" )
  .settings(scalacOptions += "-explain-cyclic" )

lazy val avcTxcExtLibsIltotoreIronV03
=
  (
    crossProject(
      suggestedTargetPlatforms
      .+:(JVMPlatform )
      .+:(JSPlatform )
      : _* )
    .withSuggestedSettings()
    in (extLibsParentDir / "IltotoreIron" )
  )
  .asLeafProjectWithNecessarySettings(skipPlatforms = false )
  .settings(libraryDependencies += ("io.github.iltotore" %%% "iron" % "3.2.1" ) )
  .settings(scalacOptions += "-explain" )
  .settings(scalacOptions += "-explain-cyclic" )

/**
 * Objectweb's ASM
 * 
 * @see https://asm.ow2.io/ 
 * @see https://central.sonatype.com/search?q=g:org.ow2.asm%20%20a:asm&smo=true 
 * @see https://asm.ow2.io/asm4-guide.pdf 
 * 
 */
lazy val avcTxcExtLibsObjectwebAsmV09
=
  (
    crossProject(
      suggestedTargetPlatforms
      .+:(JVMPlatform )
      : _* )
    .withSuggestedSettings()
    in (extLibsParentDir / "ObjectwebAsm9" )
  )
  .asLeafProjectWithNecessarySettings(skipPlatforms = true )
  .settings(libraryDependencies += ("org.ow2.asm" % "asm-commons" % "9.8" ))
  .settings(libraryDependencies += ("org.ow2.asm" % "asm-util" % "9.8" ))
  .settings(libraryDependencies += ("org.ow2.asm" % "asm" % "9.8" ))
  .settings(libraryDependencies += ("org.ow2.asm" % "asm-tree" % "9.8" ))
  .settings(libraryDependencies += ("org.ow2.asm" % "asm-analysis" % "9.8" ))
  .settings(scalacOptions += "-explain" )
  .settings(scalacOptions += "-explain-cyclic" )

lazy val avcTxcExtLibsJLineV03
=
  (
    crossProject(
      suggestedTargetPlatforms
      .+:(JVMPlatform )
      : _* )
    .withSuggestedSettings()
    in (extLibsParentDir / "JLine3" )
  )
  .asLeafProjectWithNecessarySettings(skipPlatforms = true )
  .settings(libraryDependencies += ("org.jline" % "jline"              % "3.30.0" ))
  .settings(libraryDependencies += ("org.jline" % "jline-terminal-jni" % "3.30.0" ))
  .settings(libraryDependencies += ("org.jline" % "jline-terminal-ffm" % "3.30.0" ))
  .settings(scalacOptions += "-explain" )
  .settings(scalacOptions += "-explain-cyclic" )








// lazy val avcTxcFncPlLibsCoreProject
// =
//   (
//     crossProject(
//       suggestedTargetPlatforms
//       .+:(JVMPlatform )
//       // .+:(JSPlatform )
//       : _* )
//     .withSuggestedSettings()
//     in (packagesParentDir / "avcframewrk" / "txc" / "fncpl-core" )
//   )
//   .asLeafProjectWithNecessarySettings(skipPlatforms = true )
//   // .dependsOn(avcTxcExtLibsLhyFastparseV03 )
//   // .settings(libraryDependencies += Build.externalLibraryVersions.comMonix )
//   .settings(scalaVersion := suggestedScala3Dot07VersionV)
//   .settings(scalacOptions += "-explain" )
//   .settings(scalacOptions += "-explain-cyclic" )
//   // .settings(scalacOptions += "-Xcheck-macros" )

// lazy val avcTxcConvoToolkitProject
// =
//   (
//     crossProject(
//       suggestedTargetPlatforms
//       .+:(JVMPlatform )
//       // .+:(JSPlatform )
//       : _* )
//     .withSuggestedSettings()
//     in (packagesParentDir / "avcframewrk" / "txc-project" / "convotoolkit" )
//   )
//   .asLeafProjectWithNecessarySettings(skipPlatforms = true )
//   .dependsOn(avcTxcFncPlLibsCoreProject )
//   .dependsOn(avcTxcExtLibsIltotoreIronV03 )
//   .dependsOn(avcTxcExtLibsLhyFastparseV03 )
//   .settings(scalaVersion := suggestedScala3Dot07VersionV)
//   .settings(scalacOptions += "-explain" )
//   .settings(scalacOptions += "-explain-cyclic" )

// lazy val avcTxcConvoIoCoreToolkitProject
// =
//   (
//     crossProject(
//       suggestedTargetPlatforms
//       .+:(JVMPlatform )
//       // .+:(JSPlatform )
//       : _* )
//     .withSuggestedSettings()
//     in (packagesParentDir / "avcframewrk" / "txc-project" / "ConvoIoLibCoreToolkit" )
//   )
//   .asLeafProjectWithNecessarySettings(skipPlatforms = true )
//   // .dependsOn(avcTxcFncPlLibsCoreProject )
//   .settings(libraryDependencies += ("org.apache.pekko" %%% "pekko-actor-typed" % "1.1.5" ) )
//   .settings(libraryDependencies += ("org.apache.pekko" %%% "pekko-stream"      % "1.1.5" ) )
//   .settings(libraryDependencies += ("dev.zio" %%% "zio"         % "2.1.24") )
//   .settings(libraryDependencies += ("dev.zio" %%% "zio-streams" % "2.1.24") )
//   // .settings(libraryDependencies += ("dev.zio" %%% "zio-json"    % "2.1.24") )
//   .settings   (libraryDependencies += ("dev.zio" %%% "zio-logging"     % "2.5.2") )
//   .jvmSettings(libraryDependencies += ("dev.zio" %%% "zio-logging-jpl" % "2.5.2") )
//   .settings(scalaVersion := "3.7.4")
//   .settings(scalacOptions += "-explain" )
//   .settings(scalacOptions += "-explain-cyclic" )

// lazy val avcTxcConvoIoToolkitProject
// =
//   (
//     crossProject(
//       suggestedTargetPlatforms
//       .+:(JVMPlatform )
//       // .+:(JSPlatform )
//       : _* )
//     .withSuggestedSettings()
//     in (packagesParentDir / "avcframewrk" / "txc-project" / "ConvoIoLibToolkit" )
//   )
//   .asLeafProjectWithNecessarySettings(skipPlatforms = true )
//   .dependsOn(avcTxcFncPlLibsCoreProject )
//   .dependsOn(avcTxcConvoIoCoreToolkitProject )
//   .settings(libraryDependencies += ("org.apache.pekko" %%% "pekko-http"         % "1.3.0" ) )
//   .settings(libraryDependencies += ("dev.zio" %%% "zio-http"       % "3.3.3") )
//   .settings(scalaVersion := suggestedScala3Dot07VersionV)
//   .settings(scalacOptions += "-explain" )
//   .settings(scalacOptions += "-explain-cyclic" )

// // lazy val avcTxcRpxProject
// // =
// //   (
// //     crossProject(
// //       suggestedTargetPlatforms
// //       .+:(JVMPlatform )
// //       // .+:(JSPlatform )
// //       : _* )
// //     .withSuggestedSettings()
// //     in (packagesParentDir / "avcframewrk" / "txcrpx" )
// //   )
// //   .asLeafProjectWithNecessarySettings(skipPlatforms = true )
// //   .dependsOn(avcTxcFncPlLibsCoreProject )
// //   .dependsOn(avcTxcExtLibsLhyFastparseV03 )
// //   .settings(scalaVersion := suggestedScala3Dot07VersionV)
// //   .settings(scalacOptions += "-explain" )
// //   .settings(scalacOptions += "-explain-cyclic" )

// lazy val avcTxcJxcProjectMain
// =
//   (
//     crossProject(
//       suggestedTargetPlatforms
//       .+:(JVMPlatform )
//       // .+:(JSPlatform )
//       : _* )
//     .withSuggestedSettings()
//     in (packagesParentDir / "avcframewrk" / "TxcJxcMain" )
//   )
//   .asLeafProjectWithNecessarySettings(skipPlatforms = true )
//   .dependsOn(avcTxcFncPlLibsCoreProject )
//   .dependsOn(avcTxcConvoToolkitProject )
//   .dependsOn(avcTxcConvoIoCoreToolkitProject )
//   .dependsOn(avcTxcExtLibsObjectwebAsmV09 )
//   .settings(scalaVersion := suggestedScala3Dot07VersionV)
//   .settings(scalacOptions += "-explain" )
//   .settings(scalacOptions += "-explain-cyclic" )

// // lazy val avcTxcFncPlCoreProject
// // =
// //   (
// //     crossProject(
// //       suggestedTargetPlatforms
// //       .+:(JVMPlatform )
// //       // .+:(JSPlatform )
// //       : _* )
// //     .withSuggestedSettings()
// //     in (packagesParentDir / "avcframewrk" / "txc" / "fncplang-core" )
// //   )
// //   .asLeafProjectWithNecessarySettings(skipPlatforms = true )
// //   .dependsOn(avcTxcFncPlLibsCoreProject )
// //   .dependsOn(avcTxcRpxProject )
// //   .dependsOn(avcTxcExtLibsLhyFastparseV03 )
// //   .settings(scalaVersion := suggestedScala3Dot07VersionV)
// //   .settings(scalacOptions += "-explain" )
// //   .settings(scalacOptions += "-explain-cyclic" )

// // // lazy val avcFncPlProject
// // // =
// // //   (
// // //     crossProject(
// // //       suggestedTargetPlatforms
// // //       .+:(JVMPlatform )
// // //       // .+:(JSPlatform )
// // //       : _* )
// // //     .withSuggestedSettings()
// // //     in (packagesParentDir / "avcframewrk" / "txc-fncpl" / "main" )
// // //   )
// // //   .asLeafProjectWithNecessarySettings(skipPlatforms = true )
// // //   .dependsOn(avcTxcCommonsLibProject )
// // //   .dependsOn(avcTxcCommonsExtrasLibProject )
// // //   .dependsOn(avcTxcFncPlCoreProject )
// // //   // .settings(libraryDependencies += Build.externalLibraryVersions.comMonix )
// // //   .settings(scalacOptions += "-explain" )
// // //   .settings(scalacOptions += "-explain-cyclic" )

// lazy val avcTxcCompilersSubTwoProject
// =
//   (
//     crossProject(
//       suggestedTargetPlatforms
//       .+:(JVMPlatform )
//       // .+:(JSPlatform )
//       : _* )
//     .withSuggestedSettings()
//     in (packagesParentDir / "avcframewrk" / "TxcCompilersSubTwo" )
//   )
//   .asLeafProjectWithNecessarySettings(skipPlatforms = true )
//   .dependsOn(avcTxcFncPlLibsCoreProject )
//   .dependsOn(avcTxcExtLibsIltotoreIronV03 )
//   .dependsOn(avcTxcConvoToolkitProject )
//   // .dependsOn(avcTxcExtLibsLhyFastparseV03 )
//   // .dependsOn(avcTxcPrecompiledCodeRunnerProject )
//   .dependsOn(avcTxcExtLibsObjectwebAsmV09 )
//   // .dependsOn(avcFncPlProject )
//   .settings(scalaVersion := suggestedScala3Dot07VersionV)
//   .settings(scalacOptions += "-explain" )
//   .settings(scalacOptions += "-explain-cyclic" )

// lazy val avcTxcCompilersSubTwoProjectMain
// =
//   (
//     crossProject(
//       suggestedTargetPlatforms
//       .+:(JVMPlatform )
//       // .+:(JSPlatform )
//       : _* )
//     .withSuggestedSettings()
//     in (packagesParentDir / "avcframewrk" / "TxcCompilersSubTwoMain" )
//   )
//   .asLeafProjectWithNecessarySettings(skipPlatforms = true )
//   .dependsOn(avcTxcFncPlLibsCoreProject )
//   .dependsOn(avcTxcConvoToolkitProject )
//   .dependsOn(avcTxcConvoIoCoreToolkitProject )
//   .dependsOn(avcTxcConvoIoToolkitProject )
//   .dependsOn(avcTxcExtLibsJLineV03 )
//   // .dependsOn(avcTxcPrecompiledCodeRunnerProject )
//   .dependsOn(avcTxcJxcProjectMain )
//   .dependsOn(avcTxcCompilersSubTwoProject )
//   .settings(scalaVersion := suggestedScala3Dot07VersionV)
//   .settings(scalacOptions += "-explain" )
//   .settings(scalacOptions += "-explain-cyclic" )



lazy val tvmCommonsProject
=
  (
    crossProject(
      suggestedTargetPlatforms
      .+:(JVMPlatform )
      // .+:(JSPlatform )
      : _* )
    .withSuggestedSettings()
    in (packagesParentDir / "core" )
  )
  .asLeafProjectWithNecessarySettings(skipPlatforms = true )
  // .settings(libraryDependencies += Build.externalLibraryVersions.comMonix )
  .settings(scalaVersion := suggestedScala3Dot07VersionV)
  .settings(scalacOptions ++= Seq("-source", "3.7") )
  .settings(scalacOptions += "-preview" )
  .settings(scalacOptions += "-explain" )
  .settings(scalacOptions += "-explain-cyclic" )
  // .settings(scalacOptions += "-Xcheck-macros" )
  .settings(libraryDependencies += ("com.carrotsearch" % "hppc" % "0.8.1" ))

lazy val tvmInteropCoreProject
=
  (
    crossProject(
      suggestedTargetPlatforms
      .+:(JVMPlatform )
      // .+:(JSPlatform )
      : _* )
    .withSuggestedSettings()
    in (packagesParentDir / "interopCore" )
  )
  .asLeafProjectWithNecessarySettings(skipPlatforms = true )
  // .settings(libraryDependencies += Build.externalLibraryVersions.comMonix )
  .settings(scalaVersion := suggestedScala3Dot07VersionV)
  .settings(scalacOptions ++= Seq("-source", "3.7") )
  .settings(scalacOptions += "-preview" )
  .settings(scalacOptions += "-explain" )
  .settings(scalacOptions += "-explain-cyclic" )
  // .settings(scalacOptions += "-Xcheck-macros" )

lazy val tvmCompilerProject
=
  (
    crossProject(
      suggestedTargetPlatforms
      .+:(JVMPlatform )
      // .+:(JSPlatform )
      : _* )
    .withSuggestedSettings()
    in (packagesParentDir / "compiler" )
  )
  .asLeafProjectWithNecessarySettings(skipPlatforms = true )
  .dependsOn(tvmCommonsProject )
  .dependsOn(tvmInteropCoreProject )
  .dependsOn(avcTxcExtLibsObjectwebAsmV09 )
  // .settings(libraryDependencies += Build.externalLibraryVersions.comMonix )
  .settings(scalaVersion := suggestedScala3Dot07VersionV)
  .settings(scalacOptions ++= Seq("-source", "3.7") )
  .settings(scalacOptions += "-preview" )
  .settings(scalacOptions += "-explain" )
  .settings(scalacOptions += "-explain-cyclic" )
  // .settings(scalacOptions += "-Xcheck-macros" )
  .settings(libraryDependencies += ("com.fasterxml.jackson.core" % "jackson-annotations" % "2.18.2" ))
  .settings(libraryDependencies += ("com.fasterxml.jackson.core" % "jackson-databind"    % "2.18.2" ))
  .settings(libraryDependencies += ("org.mozilla" % "rhino" % "1.7.15" ))

lazy val tvmBuildServerProject
=
  (
    crossProject(
      suggestedTargetPlatforms
      .+:(JVMPlatform )
      // .+:(JSPlatform )
      : _* )
    .withSuggestedSettings()
    in (packagesParentDir / "tools" / "devserver" )
  )
  .asLeafProjectWithNecessarySettings(skipPlatforms = true )
  .dependsOn(tvmCommonsProject )
  .dependsOn(tvmInteropCoreProject )
  .dependsOn(tvmToolchainCoreProject )
  .dependsOn(tvmCompilerProject )
  .dependsOn(tvmClasslibCoreProject )
  .dependsOn(tvmWapigenProject )
  .dependsOn(avcTxcExtLibsObjectwebAsmV09 )
  .settings(scalaVersion := suggestedScala3Dot07VersionV)
  .settings(scalacOptions ++= Seq("-source", "3.7") )
  .settings(scalacOptions += "-preview" )
  .settings(scalacOptions += "-explain" )
  .settings(scalacOptions += "-explain-cyclic" )

lazy val mainFrontendProject
=
  (
    crossProject(
      suggestedTargetPlatforms
      .+:(JVMPlatform )
      // .+:(JSPlatform )
      : _* )
    .withSuggestedSettings()
    in (packagesParentDir / "tools" / "cli" )
  )
  .asLeafProjectWithNecessarySettings(skipPlatforms = true )
  .dependsOn(tvmCommonsProject )
  .dependsOn(avcTxcExtLibsJLineV03 )
  .dependsOn(tvmToolchainCoreProject )
  .dependsOn(tvmBuildServerProject )
  .dependsOn(tvmWapigenProject )
  // .dependsOn(tvmJsWasmBackendProject )
  .settings(scalaVersion := suggestedScala3Dot07VersionV)
  .settings(scalacOptions ++= Seq("-source", "3.7") )
  .settings(scalacOptions += "-preview" )
  .settings(scalacOptions += "-explain" )
  .settings(scalacOptions += "-explain-cyclic" )

lazy val tvmToolchainCoreProject
=
  (
    crossProject(
      suggestedTargetPlatforms
      .+:(JVMPlatform )
      // .+:(JSPlatform )
      : _* )
    .withSuggestedSettings()
    in (packagesParentDir / "tools" / "core" )
  )
  .asLeafProjectWithNecessarySettings(skipPlatforms = true )
  .dependsOn(tvmCommonsProject )
  .dependsOn(tvmInteropCoreProject )
  .dependsOn(tvmCompilerProject )
  .dependsOn(tvmClasslibCoreProject )
  .settings(scalaVersion := suggestedScala3Dot07VersionV)
  .settings(scalacOptions ++= Seq("-source", "3.7") )
  .settings(scalacOptions += "-preview" )
  .settings(scalacOptions += "-explain" )
  .settings(scalacOptions += "-explain-cyclic" )

lazy val tvmWapigenProject
=
  (
    crossProject(
      suggestedTargetPlatforms
      .+:(JVMPlatform )
      // .+:(JSPlatform )
      : _* )
    .withSuggestedSettings()
    in (packagesParentDir / "wapigen" )
  )
  .asLeafProjectWithNecessarySettings(skipPlatforms = true )
  .dependsOn(tvmCommonsProject )
  .dependsOn(tvmInteropCoreProject )
  .dependsOn(tvmCompilerProject )
  .dependsOn(avcTxcExtLibsObjectwebAsmV09 )
  .settings(scalaVersion := suggestedScala3Dot07VersionV)
  .settings(scalacOptions ++= Seq("-source", "3.7") )
  .settings(scalacOptions += "-preview" )
  .settings(scalacOptions += "-explain" )
  .settings(scalacOptions += "-explain-cyclic" )

lazy val tvmClasslibCoreProject
=
  (
    crossProject(
      suggestedTargetPlatforms
      .+:(JVMPlatform )
      // .+:(JSPlatform )
      : _* )
    .withSuggestedSettings()
    in (packagesParentDir / "classlib" )
  )
  .asLeafProjectWithNecessarySettings(skipPlatforms = true )
  .dependsOn(tvmCommonsProject )
  .dependsOn(tvmInteropCoreProject )
  .dependsOn(tvmCompilerProject )
  // .settings(libraryDependencies += Build.externalLibraryVersions.comMonix )
  .settings(scalaVersion := suggestedScala3Dot07VersionV)
  .settings(scalacOptions ++= Seq("-source", "3.7") )
  .settings(scalacOptions += "-preview" )
  .settings(scalacOptions += "-explain" )
  .settings(scalacOptions += "-explain-cyclic" )
  // .settings(scalacOptions += "-Xcheck-macros" )

// lazy val tvmJsWasmBackendProject
// =
//   (
//     crossProject(
//       suggestedTargetPlatforms
//       .+:(JVMPlatform )
//       // .+:(JSPlatform )
//       : _* )
//     .withSuggestedSettings()
//     in (packagesParentDir / "compiler-jswasm-backend" )
//   )
//   .asLeafProjectWithNecessarySettings(skipPlatforms = true )
//   .dependsOn(tvmCommonsProject )
//   .dependsOn(tvmCompilerProject )
//   .dependsOn(avcTxcExtLibsObjectwebAsmV09 )
//   .settings(scalaVersion := suggestedScala3Dot07VersionV)
//   .settings(scalacOptions ++= Seq("-source", "3.7") )
//   .settings(scalacOptions += "-preview" )
//   .settings(scalacOptions += "-explain" )
//   .settings(scalacOptions += "-explain-cyclic" )








