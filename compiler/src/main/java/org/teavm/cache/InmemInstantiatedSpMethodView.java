package org.teavm.cache;

import java.lang.ref.WeakReference;
import java.util.Map;

import org.teavm.model.MethodDescriptor;
import org.teavm.model.MethodReader;
import org.teavm.model.MethodReference;
import org.teavm.model.ProgramReader;

/**
 * <p> simulation of instantiations of signature-polymorphic methods, which are not actually present in bytecode, but are needed for type checking and method resolution. this is a view over the original signature-polymorphic method, which delegates all calls to it, except for {@link MethodReader#getReference()}, which returns a new MethodReference with the same class and name as the original method, but with the signature taken from the provided MethodDescriptor.
 * 
 */
public class InmemInstantiatedSpMethodView {
  private InmemInstantiatedSpMethodView() {}

  /**
   * 
   * @param newTypes the descriptor the view MethodReader shall have
   * 
   * @apiNote {@code newTypes.name()} will be ignored
   * 
   * @param rawM the original method reader, which must be signature-polymorphic. the view will delegate all calls to this method reader, except for {@link MethodReader#getReference()}, which will return a new MethodReference with the same class and name as the original method, but with the signature taken from {@code newTypes}.
   */
  public static MethodReader wrap(MethodReader rawM, MethodDescriptor newTypes) {
    if (!rawM.isSignaturePolymorphic()) {
      throw new IllegalArgumentException(String.format("method %s is not signature-polymorphic", rawM.getReference()));
    }

    return (
      new CachedMethod() {

        {
          beingSignaturePolymorphic = false;
          reference = new MethodReference(rawM.getReference().className(), newTypes);
          program = new WeakReference<ProgramReader>(rawM.getProgram() );
          annotations = new CachedAnnotations(Map.of());
          annotationDefault = rawM.getAnnotationDefault();
          level = rawM.getLevel();
          modifiers = rawM.readModifiers();
        }

      }
    ) ;
  }

}
