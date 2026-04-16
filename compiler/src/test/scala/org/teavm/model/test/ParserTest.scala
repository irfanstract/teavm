/*
 *  Copyright 2016 Alexey Andreev and other contributors.
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
package org.teavm.model.text;

import org.scalatest.funsuite.{AnyFunSuite};
import org.junit.Assert.{assertEquals};
import java.io.IOException;
import org.junit.Test;
import org.teavm.model.BasicBlock;
import org.teavm.model.ListingParseUtils;
import org.teavm.model.Program;

class ParserTest1 extends AnyFunSuite {

    test("simple") {
        val program = runTest("simple");
        assertEquals(2, program.basicBlockCount());
        assertEquals(4, program.variableCount());
        assertEquals(4, program.basicBlockAt(0).instructionCount());
        assertEquals(1, program.basicBlockAt(1).instructionCount());
    }

    test("conditional") {
        val program = runTest("conditional");
        assertEquals(7, program.basicBlockCount());
        for (i <- Range(0, 7) ) do {
            assertEquals(1, program.basicBlockAt(i).instructionCount());
        }
    }

    test("phi") {
        val program = runTest("phi");
        assertEquals(4, program.basicBlockCount());
        assertEquals(2, program.basicBlockAt(3).getPhis().size());
    }

    test("constant") {
        val program = runTest("constant");
        assertEquals(1, program.basicBlockCount());

        val block = program.basicBlockAt(0);
        assertEquals(7, block.instructionCount());
    }

    test("invocation") {
        val program = runTest("invocation");
        assertEquals(1, program.basicBlockCount());
    }

    test("casting") {
        val program = runTest("casting");
        assertEquals(1, program.basicBlockCount());
    }

    test("operations") {
        runTest("operations");
    }

    test("create") {
        val program = runTest("create");
        assertEquals(1, program.basicBlockCount());
    }

    test("fields") {
        runTest("fields");
    }

    test("switchInsn") {
        runTest("switchInsn");
    }

    test("exceptions") {
        runTest("exceptions");
    }

    private def runTest(name: String): Program = {
        return ListingParseUtils.parseFromResource("model/text/" + name + ".txt");
    }
}
