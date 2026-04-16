package org.teavm







/**
 * an alternative to `locally`,
 * letting the precise type of the actual argument through.
 * 
 * ```diff
 * - locally :
 * + lca:
 *     thisContainer.internal.modeller
 * ```
 * 
 */
inline def lca[R] (x: R) : x.type = x

// export util.chaining.{scalaUtilChainingOps}
extension [A] (self: A)
  inline def pipe[B](inline f: A => B): B = f(self)
  inline def tap[U](inline f: A => U): A = try self finally { f(self) }

extension [A] (self: A | Null)
  inline def pipeNonNull[B](inline f: A => B): B | Null
  =
    { Option(self).map(_.nn ) }
    .fold(null) { self => f(self) }






