/*
 *  Copyright 2016 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.ast;

import org.teavm.ast.analysis.LocationGraphBuilder;

/**
 * <p> subclassed by classes that represent statements with an identifier, such as labeled statements and blocks. the identifier can be used to refer to the statement from other statements, such as break and continue statements.
 * for example, {@link BlockStatement} and {@link WhileStatement} are subclasses of {@link IdentifiedStatement}, so they can have an identifier that can be used to refer to them from break and continue statements.
 * {@link ContinueStatement} and {@link BreakStatement} can refer to an {@link IdentifiedStatement} as their target, which allows to identify the statement that they are breaking or continuing to.
 * This can be useful for debugging and optimization purposes, such as identifying loops and blocks in the control flow graph.
 * 
 * <p> {@link IdentifiedStatement} is specifically designed to be used in the control flow graph of a method, where statements can have identifiers that can be used to refer to them from other statements. It is not intended to be used for all statements in the AST, but only for those that need to be identified and referred to from other statements.
 * used in classes like {@link LocationGraphBuilder} to build the control flow graph of a method, where statements can have identifiers that can be used to refer to them from other statements.
 * used in {@link org.teavm.ast.optimization.AllBlocksCountVisitor AllBlocksCountVisitor} to count the number of blocks in a method, where statements can have identifiers that can be used to refer to them from other statements.
 * <del> used in classes like {@link LocationCounter} and {@link BlockCountVisitor} to count the number of times a block is entered or exited, which can be useful for optimization purposes. It is also used in classes like {@link ControlFlowGraphBuilder} to build the control flow graph of a method, where statements can have identifiers that can be used to refer to them from other statements. </del>
 * used for building control flow graph of a method, where statements can have identifiers that can be used to refer to them from other statements. It is not intended to be used for all statements in the AST, but only for those that need to be identified and referred to from other statements.
 * 
 * <p> provides a field for storing the identifier and methods to get and set it.
 * 
 * <p> the identifier is a string that can be assigned to the statement. It can be used to refer to the statement from other statements, such as break and continue statements. The identifier can be null, which means that the statement does not have an identifier. The identifier should be unique within the method, so it can be used to identify the statement unambiguously. The identifier can be used for debugging and optimization purposes, such as identifying loops and blocks in the control flow graph.
 * 
 */
public abstract class IdentifiedStatement extends Statement {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
