
package org.teavm

package backend.modular




object CodeSplittingMode :

    /**
     * a [[CodeSplittingMode]] for the given [[CodeSplittingType]].
     * 
     * this is a simple wrapper around the enum value, but it allows us to add additional methods to the mode if needed in the future, without changing the enum definition.
     * 
     */
    def byType(x: CodeSplittingType)
        : CodeSplittingMode
    = x

trait CodeSplittingMode private[teavm] ()



enum CodeSplittingType extends java.lang.Enum[CodeSplittingType] with CodeSplittingMode :

    /**
     * only a single file, thus all-in, everything there
     */
    case SINGLE_COMPLETE

    /**
     * fewer files, each thus larger
     */
    case FEWER_AND_LARGER

    /**
     * smaller files, thus more of them
     */
    case MORE_AND_SMALLER


