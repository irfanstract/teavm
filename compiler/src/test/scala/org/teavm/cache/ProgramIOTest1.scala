/*
 *  Copyright 2014 Alexey Andreev.
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
package org.teavm.cache;

import org.scalatest.funsuite.AnyFunSuite
import org.hamcrest.CoreMatchers.instanceOf;
import org.hamcrest.CoreMatchers.is;
import org.junit.Assert.assertThat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;
import org.teavm.model.BasicBlock;
import org.teavm.model.Instruction;
import org.teavm.model.Program;
import org.teavm.model.ReferenceCache;
import org.teavm.model.ValueType;
import org.teavm.model.instructions.*;

class ProgramIOScTest extends AnyFunSuite {
    test("empty instruction") {
        var program = Program();
        var block = program.createBasicBlock();
        block.add(new EmptyInstruction());

        program = inputOutput(program);
        block = program.basicBlockAt(0);

        assertThat(block.instructionCount(), is(1));
        assertThat(block.getFirstInstruction(), instanceOf(classOf[EmptyInstruction] ));
    }

    test("constants") {
        var program = Program();
        var block = program.createBasicBlock();
        var classConstInsn = ClassConstantInstruction();
        classConstInsn.setConstant(ValueType.BYTE);
        classConstInsn.setReceiver(program.createVariable());
        block.add(classConstInsn);
        var nullConstInsn = NullConstantInstruction();
        nullConstInsn.setReceiver(program.createVariable());
        block.add(nullConstInsn);
        var intConsInsn = IntegerConstantInstruction();
        intConsInsn.setReceiver(program.createVariable());
        intConsInsn.setConstant(23);
        block.add(intConsInsn);
        var longConsInsn = LongConstantInstruction();
        longConsInsn.setReceiver(program.createVariable());
        longConsInsn.setConstant(234);
        block.add(longConsInsn);
        var floatConsInsn = FloatConstantInstruction();
        floatConsInsn.setReceiver(program.createVariable());
        floatConsInsn.setConstant(3.14f);
        block.add(floatConsInsn);
        var doubleConsInsn = DoubleConstantInstruction();
        doubleConsInsn.setReceiver(program.createVariable());
        doubleConsInsn.setConstant(3.14159);
        block.add(doubleConsInsn);
        var stringConsInsn = StringConstantInstruction();
        stringConsInsn.setReceiver(program.createVariable());
        stringConsInsn.setConstant("foo");
        block.add(stringConsInsn);

        program = inputOutput(program);
        block = program.basicBlockAt(0);

        assertThat(block.instructionCount(), is(7));
        var insn = block.getFirstInstruction();
        assertThat(insn, instanceOf(classOf[ClassConstantInstruction]));
        insn = insn.getNext();
        assertThat(insn, instanceOf(classOf[NullConstantInstruction]));
        insn = insn.getNext();
        assertThat(insn, instanceOf(classOf[IntegerConstantInstruction]));
        insn = insn.getNext();
        assertThat(insn, instanceOf(classOf[LongConstantInstruction]));
        insn = insn.getNext();
        // assertThat(insn, instanceOf(FloatConstantInstruction.class));
        insn = insn.getNext();
        // assertThat(insn, instanceOf(DoubleConstantInstruction.class));
        insn = insn.getNext();
        // assertThat(insn, instanceOf(StringConstantInstruction.class));

        insn = block.getFirstInstruction();
        // classConstInsn = (ClassConstantInstruction) insn;
        assertThat(classConstInsn.getReceiver().getIndex(), is(0));
        assertThat(classConstInsn.getConstant().toString(), is(ValueType.BYTE.toString()));

        insn = insn.getNext();
        // nullConstInsn = (NullConstantInstruction) insn;
        assertThat(nullConstInsn.getReceiver().getIndex(), is(1));

        insn = insn.getNext();
        // intConsInsn = (IntegerConstantInstruction) insn;
        assertThat(intConsInsn.getConstant(), is(23));
        assertThat(intConsInsn.getReceiver().getIndex(), is(2));

        insn = insn.getNext();
        // longConsInsn = (LongConstantInstruction) insn;
        assertThat(longConsInsn.getConstant(), is(234L));
        assertThat(longConsInsn.getReceiver().getIndex(), is(3));

        insn = insn.getNext();
        // floatConsInsn = (FloatConstantInstruction) insn;
        assertThat(floatConsInsn.getConstant(), is(3.14f));
        assertThat(floatConsInsn.getReceiver().getIndex(), is(4));

        insn = insn.getNext();
        // doubleConsInsn = (DoubleConstantInstruction) insn;
        assertThat(doubleConsInsn.getConstant(), is(3.14159));
        assertThat(doubleConsInsn.getReceiver().getIndex(), is(5));

        insn = insn.getNext();
        // stringConsInsn = (StringConstantInstruction) insn;
        assertThat(stringConsInsn.getConstant(), is("foo"));
        assertThat(stringConsInsn.getReceiver().getIndex(), is(6));
    }

    test("binary operation") {
        val program = Program();
        val block = program.createBasicBlock();
        var constInsn = IntegerConstantInstruction();
        constInsn.setConstant(3);
        constInsn.setReceiver(program.createVariable());
        block.add(constInsn);
        constInsn = new IntegerConstantInstruction();
        constInsn.setConstant(2);
        constInsn.setReceiver(program.createVariable());
        block.add(constInsn);
        val addInsn = BinaryInstruction(BinaryOperation.ADD, NumericOperandType.INT);
        addInsn.setFirstOperand(program.variableAt(0));
        addInsn.setSecondOperand(program.variableAt(1));
        addInsn.setReceiver(program.createVariable());
        block.add(addInsn);
        val subInsn = BinaryInstruction(BinaryOperation.SUBTRACT, NumericOperandType.INT);
        subInsn.setFirstOperand(program.variableAt(2));
        subInsn.setSecondOperand(program.variableAt(0));
        subInsn.setReceiver(program.createVariable());
        block.add(subInsn);

        assertThat(block.instructionCount(), is(4));
    }

    def inputOutput(program: Program) = {
        val symbolTable = InMemorySymbolTable();
        val fileTable = InMemorySymbolTable();
        val variableTable = InMemorySymbolTable();
        val programIO = new ProgramIO(new ReferenceCache(), symbolTable, fileTable, variableTable);
        util.Using.resource(ByteArrayOutputStream()) { output =>
            programIO.write(program, output);
            util.Using.resource(ByteArrayInputStream(output.toByteArray())) { input =>
                programIO.read(input)
            }
        }
    }
}
