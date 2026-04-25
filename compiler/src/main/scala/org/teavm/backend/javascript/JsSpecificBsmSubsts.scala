
package org.teavm.backend.javascript




import language.unsafeNulls

import org.teavm.dependency.{BootstrapMethodSubstitutor}

object JsSpecificBsmSubst {

  def apply(): BootstrapMethodSubstitutor
  =
    locally[BootstrapMethodSubstitutor] :
        case (c, pe) =>
            val nameAndType = c.getCalledMethod()
            pe.constantNull(nameAndType.getResultType() )
    Class.forName("org.teavm.classlib.impl.LambdaMetafactorySubstitutor").newInstance().asInstanceOf[BootstrapMethodSubstitutor]

} // JsSpecificBsmSubst




