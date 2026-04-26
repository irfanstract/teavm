
package org.teavm

package model






import language.unsafeNulls

object EffectiveElementModifier :

    import ElementModifier.{*}
    import ElementModifier.{asModifiersInfo}
    import ElementModifier.{ANNOTATION}
    import org.teavm.runtime.reflect.ModifiersInfo
  
    // public static int encodeModifiers(ClassReader cls) {
    //     var modifiers = asModifiersInfo(cls.readModifiers(), cls.getLevel());
    //     if (cls.hasModifier(ElementModifier.ANNOTATION)) {
    //         var retention = cls.getAnnotations().get(Retention.class.getName());
    //         if (retention != null && retention.getValue("value").getEnumValue().getFieldName().equals("RUNTIME")) {
    //             if (cls.getAnnotations().get(Inherited.class.getName()) != null) {
    //                 modifiers |= ModifiersInfo.INHERITED_ANNOTATION;
    //             }
    //         }
    //     }
    //     return modifiers;
    // }
    def encodeModifiers(cls: ClassReader): Int =
        var modifiers = asModifiersInfo(cls.readModifiers(), cls.getLevel())
        if (cls.hasModifier(ANNOTATION)) {
            var retention = cls.getAnnotations().get(classOf[java.lang.annotation.Retention].getName())
            if (retention != null && retention.getValue("value").getEnumValue().getFieldName().equals("RUNTIME")) {
                if (cls.getAnnotations().get(classOf[java.lang.annotation.Inherited].getName()) != null) {
                    modifiers |= ModifiersInfo.INHERITED_ANNOTATION
                }
            }
        }
        modifiers




