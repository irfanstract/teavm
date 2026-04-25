
package org.teavm

package native
package os









object Path :

  locally { }

type Path
>: PathOrUrl
<: PathOrUrl






object PathOrUrl :

  extension (path: PathOrUrl)
    // def resolve(other: PathOrUrl): PathOrUrl = ???
    // def relativize(other: PathOrUrl): PathOrUrl = ???
    // def normalize(): PathOrUrl = ???
    // def toAbsolutePath(): PathOrUrl = ???
    // def toRealPath(): PathOrUrl = ???

    def toURI(): java.net.URI =
      path match
        case AbsFilePathlikeString(p) => java.io.File(p).toPath().toUri()
        case s: String => new java.net.URI(s)
        case u: java.net.URI => u
        case _ => throw new IllegalArgumentException("Unsupported path type")

type PathOrUrl
>: String | java.net.URI
<: String | java.net.URI

/**
 * defines `unapply` method taking a `String` and returning an `Option[String]` that is non-empty if the string matches the pattern of an absolute file path, and empty otherwise.
 */
val AbsFilePathlikeString =
  "\\A(?:/|)((?:[A-Za-z]):.*)\\z".r
  .pipe { r => (r.unapplySeq(_: String) ).unlift }
  .pipe { _.andThen({ case p +: etc => p }) }

/**
 * defines `unapply` method taking a `PathOrUrl` and returning an `Option[String]` that is non-empty if the path matches the pattern of an absolute file path, and empty otherwise.
 */
val AbsFilePathlikeS =
  AbsFilePathlikeString.compose(Some[PathOrUrl].unlift.andThen(_.toString() ) )










