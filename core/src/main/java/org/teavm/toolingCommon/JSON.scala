package org.teavm

package toolingCommon





import com.github.plokhotnyuk.{jsoniter_scala}





/**
 * intended to mimic/replicate TC39's `JSON` namespc.
 * 
 */
transparent inline def JSON
: Json.type
= Json

/**
 * intended to mimic/replicate TC39's `JSON` namespc.
 * 
 */
object Json
{

    import JsonInternal.{XValueCodec}

    def stringify
        [T: XValueCodec]
        (x: T, replacer: Null = null, indentation: Int = 0 )
        : String
    =
        jsoniter_scala.core.writeToStringReentrant { x }

    def parse
        [T: XValueCodec]
        (s: String, rvv: Null = null )
        : T
    =
        jsoniter_scala.core.readFromStringReentrant[T]  (s)

} // Json.$

/**
 * INTERNAL ONLY.
 * 
 */
object JsonInternal {

    object XValueCodec :

        inline given `jsoniter_scala.core.JsonValueCodec[E]`
        : [E] =>      jsoniter_scala.core.JsonValueCodec[E]
        = jsoniter_scala.macros.JsonCodecMaker.make

    type XValueCodec[E]
    >: jsoniter_scala.core.JsonValueCodec[E]
    <: jsoniter_scala.core.JsonValueCodec[E]

} // JsonInternal.$












