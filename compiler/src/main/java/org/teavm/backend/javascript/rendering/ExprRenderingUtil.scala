
package org.teavm

package backend.javascript
package rendering

import org.teavm.ast.Expr
import org.teavm.ast.ConstantExpr




import language.unsafeNulls

import scala.jdk.CollectionConverters.{*, given}

object ExprRenderingUtil {

    // public static boolean isSmallInteger(Expr expr) {
    //     if (!(expr instanceof ConstantExpr)) {
    //         return false;
    //     }

    //     Object constant = ((ConstantExpr) expr).getValue();
    //     if (!(constant instanceof Integer)) {
    //         return false;
    //     }

    //     int value = (Integer) constant;
    //     return Math.abs(value) < (1 << 18);
    // }
    def isSmallInteger(expr: Expr): Boolean =
        expr match
            case c: ConstantExpr =>
                c.getValue() match
                    case i: Int =>
                        math.abs(i) < (1 << 18)
                    case _ =>
                        false
            case _ =>
                false

}
