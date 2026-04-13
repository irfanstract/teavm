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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.teavm.model.*;

public abstract class Expr implements Cloneable {
    private TextLocation location;
    private int variableIndex = -1;

    public abstract void acceptVisitor(ExprVisitor visitor);

    /**
     * returns the index of the variable represented by this expression, or -1 if this expression does not represent a variable.
     * 
     * <p>
     * this method is used to determine whether this expression represents a variable, and if so, which variable it represents.
     * the variable index is an integer that uniquely identifies a variable within a method. It is assigned by the compiler during the variable allocation phase, and is used to refer to the variable in various parts of the compiler, such as in variable expressions and in variable declarations.
     * if this expression does not represent a variable, this method returns -1. This is the default value of the variable index, and it indicates that this expression is not a variable expression.
     * if this expression represents a variable, this method returns the index of that variable. The variable index is a non-negative integer that uniquely identifies the variable within the method. It is assigned by the compiler during the variable allocation phase, and it is used to refer to the variable in various parts of the compiler, such as in variable expressions and in variable declarations.
     * <br/> see `OptimizingVisitor.java`. in that class, the variable index is used to determine whether a variable expression represents a specific variable that is being optimized, and to replace it with a constant expression if it does.
     * <br/> see `VariableExpr.java`. in that class, the variable index is set to the index of the variable that the expression represents, and it is used to determine which variable is being referred to by the expression.
     * <br/> see `Expr.java`. in that class, the variable index is defined as a field of the `Expr` class, and it is used to store the index of the variable that the expression represents. The `getVariableIndex()` method is used to retrieve this index, and the `setVariableIndex(int variableIndex)` method is used to set it.
     * 
     * @see #setVariableIndex(int)
     * @see VariableExpr
     */
    public int getVariableIndex() {
        return variableIndex;
    }

    /**
     * assigns the value for {@link #getVariableIndex()} to return.
     * 
     */
    public void setVariableIndex(int variableIndex) {
        this.variableIndex = variableIndex;
    }

    /**
     * creates and returns a copy of this object.
     * 
     * <p>
     * calls {@link #clone(Map) the one-arg `clone(Map)` method} internally, passing it a new empty map to keep track of already cloned expressions.
     * 
     * @see java.lang.Object#clone
     * @see java.lang.Cloneable
     */
    @Override
    public Expr clone() {
        return clone(new HashMap<>());
    }

    /**
     * called internally by {@link #clone() the no-arg `clone()` method}.
     * <br/>
     * {@code cache} will contain mappings from original expressions to their clones. This is used to avoid infinite recursion when cloning cyclic structures.
     * 
     * <p>
     * by convention, the returned object should be obtained by calling
     * {@code super.clone}.  If a class and all of its superclasses (except
     * {@code Object}) obey this convention, it will be the case that
     * {@code x.clone().getClass() == x.getClass()}.
     * <p>
     * by convention, the object returned by this method should be independent of this object (which is being cloned).
     * to achieve this independence,
     * it may be necessary to modify one or more fields of the object returned by {@code super.clone} before returning it.
     * typically, this means
     * copying any mutable objects that comprise the internal "deep structure"
     * of the object being cloned and replacing the references to these
     * objects with references to the copies. 
     * if a class contains only primitive fields or references to immutable objects, then
     * it is usually the case that
     * no fields in the object returned by {@code super.clone} need to be modified.
     * 
     * @param cache a map of already cloned expressions, used to avoid infinite recursion when cloning cyclic structures
     * 
     * @return a clone of this expression
     */
    protected abstract Expr clone(Map<Expr, Expr> cache);

    public static Expr constant(Object value) {
        ConstantExpr expr = new ConstantExpr();
        expr.setValue(value);
        return expr;
    }

    public static Expr var(int index) {
        VariableExpr expr = new VariableExpr();
        expr.setIndex(index);
        expr.setVariableIndex(index);
        return expr;
    }

    public static Expr binary(BinaryOperation op, OperationType type, Expr first, Expr second) {
        BinaryExpr expr = new BinaryExpr();
        expr.setFirstOperand(first);
        expr.setSecondOperand(second);
        expr.setOperation(op);
        expr.setType(type);
        return expr;
    }

    public static Expr and(Expr first, Expr second) {
        return binary(BinaryOperation.AND, null, first, second);
    }

    public static Expr or(Expr first, Expr second) {
        return binary(BinaryOperation.OR, null, first, second);
    }

    public static Expr addInt(Expr first, Expr second) {
        return binary(BinaryOperation.ADD, OperationType.INT, first, second);
    }

    public static Expr divInt(Expr first, Expr second) {
        return binary(BinaryOperation.DIVIDE, OperationType.INT, first, second);
    }

    public static Expr less(Expr first, Expr second) {
        return binary(BinaryOperation.LESS, OperationType.INT, first, second);
    }

    public static Expr binary(BinaryOperation op, OperationType type, Expr first, Expr second, TextLocation loc) {
        Expr expr = binary(op, type, first, second);
        expr.setLocation(loc);
        return expr;
    }

    public static Expr unary(UnaryOperation op, OperationType type, Expr arg) {
        UnaryExpr expr = new UnaryExpr();
        expr.setOperand(arg);
        expr.setOperation(op);
        expr.setType(type);
        return expr;
    }

    public static Expr invert(Expr expr) {
        UnaryExpr result = new UnaryExpr();
        result.setOperand(expr);
        result.setOperation(UnaryOperation.NOT);
        result.setLocation(expr.getLocation());
        return result;
    }

    public static Expr subscript(Expr array, Expr index, ArrayType type) {
        SubscriptExpr expr = new SubscriptExpr();
        expr.setArray(array);
        expr.setIndex(index);
        expr.setType(type);
        return expr;
    }

    public static Expr createArray(ValueType type, Expr length) {
        NewArrayExpr expr = new NewArrayExpr();
        expr.setType(type);
        expr.setLength(length);
        return expr;
    }

    public static Expr createArray(ValueType type, Expr... dimensions) {
        NewMultiArrayExpr expr = new NewMultiArrayExpr();
        expr.setType(type);
        expr.getDimensions().addAll(Arrays.asList(dimensions));
        return expr;
    }

    public static Expr createObject(String type) {
        NewExpr expr = new NewExpr();
        expr.setConstructedClass(type);
        return expr;
    }

    public static InvocationExpr constructObject(MethodReference method, Expr[] arguments) {
        InvocationExpr expr = new InvocationExpr();
        expr.setMethod(method);
        expr.setType(InvocationType.CONSTRUCTOR);
        expr.getArguments().addAll(Arrays.asList(arguments));
        return expr;
    }

    public static Expr qualify(Expr target, FieldReference field) {
        QualificationExpr expr = new QualificationExpr();
        expr.setQualified(target);
        expr.setField(field);
        return expr;
    }

    public static InvocationExpr invoke(MethodReference method, Expr target, Expr[] arguments) {
        InvocationExpr expr = new InvocationExpr();
        expr.setMethod(method);
        expr.setType(InvocationType.DYNAMIC);
        expr.getArguments().add(target);
        expr.getArguments().addAll(Arrays.asList(arguments));
        return expr;
    }

    public static InvocationExpr invokeSpecial(MethodReference method, Expr target, Expr[] arguments) {
        InvocationExpr expr = new InvocationExpr();
        expr.setMethod(method);
        expr.setType(InvocationType.SPECIAL);
        expr.getArguments().add(target);
        expr.getArguments().addAll(Arrays.asList(arguments));
        return expr;
    }

    public static InvocationExpr invokeStatic(MethodReference method, Expr... arguments) {
        InvocationExpr expr = new InvocationExpr();
        expr.setMethod(method);
        expr.setType(InvocationType.STATIC);
        expr.getArguments().addAll(Arrays.asList(arguments));
        return expr;
    }

    public static Expr instanceOf(Expr target, ValueType className) {
        InstanceOfExpr expr = new InstanceOfExpr();
        expr.setExpr(target);
        expr.setType(className);
        return expr;
    }

    public TextLocation getLocation() {
        return location;
    }

    public void setLocation(TextLocation location) {
        this.location = location;
    }
}
