
package org.teavm.classlib.impl




enum ExtensionPhase extends java.lang.Enum[ExtensionPhase] {

  /**
   * the class-or-interface *totally replace* the existing one.
   */
  case BEGINNING

  /**
   * definitions in the class replace their coresponding definitions in the existing class, but new definitions are added to the end of the class.
   */
  case COMPLEMENTING

}






