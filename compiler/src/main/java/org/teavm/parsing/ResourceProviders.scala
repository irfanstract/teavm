
package org.teavm

package parsing

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import org.teavm.parsing.resource.Resource;
import org.teavm.parsing.resource.ResourceProvider;




import scala.jdk.CollectionConverters.{given}

trait LwResourceProvider extends ResourceProvider with java.io.Closeable :

    override def close(): Unit
    = {}

object TransformedResourceProvider1 :

    /**
     * the callback `tx2` operates on the whole collection of resources at once.
     * 
     * the callback is expected to return a collection of resources that is the same size as the input collection, and the resources in the returned collection are expected to correspond to the input collection in order (i.e. the first resource in the returned collection corresponds to the first resource in the input collection, etc.).
     */
    def getBulkProcInstance(resolver1: ResourceProvider ) (tx2: (String, IterableOnce[Resource] ) => IterableOnce[Resource] ) =
      locally[ResourceProvider] :
        ResourceProvider.fromFunction(p => {
            val r1 = p.pipe { resolver1.getResources(_) }.asScala
            val r2 = tx2(p, r1 ).iterator

            r2
        }.asJava )

    /**
     * the callback `tx2` operates on each item individually.
     */
    def getSingleProcInstance(resolver1: ResourceProvider ) (tx2: (String, Resource ) => Resource ) =
        getBulkProcInstance(resolver1) {
            case (p *: r1 *: etc1) =>
                for
                    rElem1 <- r1.iterator
                yield tx2(p, rElem1)
        }







