package org.teavm.common;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * utility methods for working with sets and lists. The main purpose of this class is to provide a convenient way to create new sets and lists by adding elements to existing ones without modifying the original collections.
 * 
 */
public class Sets {
  private Sets() {}

  @SafeVarargs
  private static <E> List<E> appended0(Collection<? extends E> s0, E... addends ) {
    var l = new ArrayList<E>(s0);
    for (var addend : addends ) { l.add(addend) ; }
    return l ;
  }

  /**
   * an immutable list containing all elements of the provided list and the additional elements. The original list is not modified.
   */
  @SafeVarargs
  public static <E> List<E> appended(List<? extends E> s0, E... addends ) {
    return List.copyOf(appended0(s0, addends) ) ;
  }

  /**
   * an immutable collection containing all elements of the provided collection and the additional elements. The original collection is not modified.
   */
  @SafeVarargs
  public static <E> Collection<E> supplemented(Collection<? extends E> s0, E... addends ) {
    return List.copyOf(appended0(s0, addends) ) ;
  }

  /**
   * an immutable set containing all elements of the provided set and the additional elements. The original collection is not modified.
   */
  @SafeVarargs
  public static <E> Set<E> supplementedSet(Set<? extends E> s0, E... addends ) {
    return Set.copyOf(appended0(s0, addends) ) ;
  }

}

