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

/**
 * <p> type of operand.
 * used in {@link BinaryExpr} to specify the type of binary operation, so it can be determined how to evaluate the expression and what type of result it will produce. used in {@link UnaryExpr} to specify the type of unary operation, so it can be determined how to evaluate the expression and what type of result it will produce.
 * <del> used in {@link CastExpr} to specify the type of cast operation, so it can be determined how to evaluate the expression and what type of result it will produce. used in {@link InstanceOfExpr} to specify the type of instance of operation, so it can be determined how to evaluate the expression and what type of result it will produce. used in {@link TernaryExpr} to specify the type of ternary operation, so it can be determined how to evaluate the expression and what type of result it will produce. used in {@link SwitchExpr} to specify the type of switch operation, so it can be determined how to evaluate the expression and what type of result it will produce. used in {@link ConditionalExpr} to specify the type of conditional operation, so it can be determined how to evaluate the expression and what type of result it will produce. used in {@link MethodCallExpr} to specify the type of method call operation, so it can be determined how to evaluate the expression and what type of result it will produce. used in {@link FieldAccessExpr} to specify the type of field access operation, so it can be determined how to evaluate the expression and what type of result it will produce. used in {@link ArrayAccessExpr} to specify the type of array access operation, so it can be determined how to evaluate the expression and what type of result it will produce.
 * 
 * <p> type of operand, which can be used to determine the type of operands and result of an operation. used in {@link Expr}(s) to specify the type of operation, so it can be determined how to evaluate the expression and what type of result it will produce.
 * for example, if the operation type is INT, then the operands and result of the operation will be of type int. if the operation type is LONG, then the operands and result of the operation will be of type long. if the operation type is FLOAT, then the operands and result of the operation will be of type float. if the operation type is DOUBLE, then the operands and result of the operation will be of type double.
 * <br/>
 * used in {@link BinaryExpr} to specify the type of binary operation, so it can be determined how to evaluate the expression and what type of result it will produce. used in {@link UnaryExpr} to specify the type of unary operation, so it can be determined how to evaluate the expression and what type of result it will produce.
 * <del>
 * used in {@link CastExpr} to specify the type of cast operation, so it can be determined how to evaluate the expression and what type of result it will produce. used in {@link InstanceOfExpr} to specify the type of instance of operation, so it can be determined how to evaluate the expression and what type of result it will produce.
 * used in {@link TernaryExpr} to specify the type of ternary operation, so it can be determined how to evaluate the expression and what type of result it will produce. used in {@link SwitchExpr} to specify the type of switch operation, so it can be determined how to evaluate the expression and what type of result it will produce.
 * used in {@link ConditionalExpr} to specify the type of conditional operation, so it can be determined how to evaluate the expression and what type of result it will produce. used in {@link MethodCallExpr} to specify the type of method call operation, so it can be determined how to evaluate the expression and what type of result it will produce.
 * used in {@link FieldAccessExpr} to specify the type of field access operation, so it can be determined how to evaluate the expression and what type of result it will produce. used in {@link ArrayAccessExpr} to specify the type of array access operation, so it can be determined how to evaluate the expression and what type of result it will produce.
 * 
 * <p> type of operation, which can be used to determine the type of operands and result of an operation. It is used in {@link Expr}(s) to specify the type of operation, so it can be determined how to evaluate the expression and what type of result it will produce.
 * 
 */
public enum OperationType {
    INT,
    LONG,
    FLOAT,
    DOUBLE,
}
