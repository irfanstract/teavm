
/* the `addSbtPlugin` calls */















/* see also [https://github.com/portable-scala/sbt-crossproject](`sbt-crossproject`) */

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject"      % "1.2.0")

addSbtPlugin("org.scala-js"       % "sbt-scalajs"                   % "1.13.2")

/* ScalablyTyped, as demonstrated at https://www.scala-js.org/doc/tutorial/scalablytyped.html */
addSbtPlugin("org.scalablytyped.converter" % "sbt-converter" % "1.0.0-beta42")

/* https://scalacenter.github.io/scalajs-bundler/getting-started.html . */
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % (
   // "0.21.1+22-3d7106a1-SNAPSHOT"
   "0.21.1"
))

























