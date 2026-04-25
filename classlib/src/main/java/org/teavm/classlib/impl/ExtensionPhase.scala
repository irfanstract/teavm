
package org.teavm.classlib.impl




enum ExtensionPhase extends java.lang.Enum[ExtensionPhase] {

  /**
   * the class-or-interface *totally replace* the existing one.
   * all their methods must be implemented from scratch, and all their fields must be defined in the class. The order of definitions in the class is not important.
   */
  case BEGINNING

  /**
   * definitions in the class replace their coresponding definitions in the existing classfile, but new definitions are added to the end of the class.
   * other methods present in the existing classfile will remain unchanged.
   */
  case COMPLEMENTING

}






