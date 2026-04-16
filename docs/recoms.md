


#





## [`sbt-crossproject`](https://github.com/portable-scala/sbt-crossproject)




## JUnit

### JUnit `6.*`

https://docs.junit.org/6.0.3/writing-tests/parameterized-classes-and-tests.html

### [`sbt-junit`]()

usage:

> # JUnit Interface
> 
> JUnit Interface is an implementation of [sbt's test interface](https://github.com/sbt/test-interface) for [JUnit 4](https://junit.org/junit4/). This allows you to run JUnit tests from [sbt](http://www.scala-sbt.org/). For JUnit 5, check out [maichler/sbt-jupiter-interface](https://github.com/maichler/sbt-jupiter-interface).
> 
> Unlike Scala testing frameworks like ScalaTest (which can also run JUnit test cases), both JUnit and this adapter are pure Java, so you can run JUnit tests with any Scala version supported by sbt without having to build a binary-compatible test framework first.
> 
> ### Setup
> 
> Add the following dependency to your `build.sbt`:
> 
> ```scala
> libraryDependencies += "com.github.sbt" % "junit-interface" % "0.13.2" % Test
> ```
> 
> **Note**: Organization name has changed to **`"com.github.sbt"`** in 0.12.
> 
> JUnit itself is automatically pulled in as a transitive dependency.
> 
>  junit-interface version  | JUnit version
> :-------------------------|:--------------
>  0.13.2                   | 4.13.2
>  0.13.1                   | 4.13.1
>  0.13                     | 4.13
>  0.12                     | 4.12
> 
> sbt already knows about junit-interface so the dependency alone is enough. You do not have to add it to the list of test frameworks.
> 

### [`sbt-jupiter-interface`](https://github.com/sbt/sbt-jupiter-interface/blob/main/README.md)

https://github.com/sbt/sbt-jupiter-interface/blob/main/README.md

usage:

> [![Maven Central](https://img.shields.io/maven-central/v/com.github.sbt.junit/jupiter-interface)](https://central.sonatype.com/artifact/com.github.sbt.junit/jupiter-interface)
> 
> Add the following line to `./project/plugins.sbt`:
> 
> ```scala
> addSbtPlugin("com.github.sbt.junit" % "sbt-jupiter-interface" % "x.y.z")
> ```
> 
> **Note**: We changed the organization from `"net.aichler"` to `"com.github.sbt.junit"` starting 0.11.3.
> 
> Additionally a `Test` dependency to the runtime library `jupiter-interface` is required for every module which wants to run JUnit 5 tests:
> 
> ```scala
> libraryDependencies ++= Seq(
>   "com.github.sbt.junit" % "jupiter-interface" % JupiterKeys.jupiterVersion.value % Test
> )
> ```
> 
> Note: If you want to restore default behaviour like in versions before `0.8.0` you can globally activate this plugin by adding the runtime dependency to `ThisBuild / libraryDependencies`:
> 
> ```scala
> ThisBuild / libraryDependencies += "com.github.sbt.junit" % "jupiter-interface" % JupiterKeys.jupiterVersion.value % Test
> ```
> 






