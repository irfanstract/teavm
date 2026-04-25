
package org.teavm
package parsing
package resource


import java.io.InputStream
import java.util.Date
import java.io.ByteArrayInputStream
import java.{util => ju}




import language.unsafeNulls

/**
 * 
 * @param data the whole data (bytes).
 * we can't store it as `IArray` here because that would give us broken `==`/`equals` behaviour for a `case class`.
 */
case class ConstResource(data: collection.immutable.ArraySeq.ofByte ) extends Resource :
  def this(data: IArray[Byte] ) = this(lca[collection.immutable.ArraySeq[Byte] ] { data } )

  override def open(): InputStream = new ByteArrayInputStream(data.unsafeArray )

  override def getModificationDate(): java.util.Date = null




