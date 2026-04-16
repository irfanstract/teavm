/*
 *  Copyright 2023 Alexey Andreev and contributors.
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
package org.teavm.backend.javascript.rendering;

import org.scalatest.funsuite.{AnyFunSuite}
import org.junit.Assert.{assertFalse};
import org.junit.Assert.{assertTrue };
import java.util.HashSet;
import org.junit.Test;

class VariableRenderingTest extends AnyFunSuite {
    test("shortened names") {
        var generator = new VariableNameGenerator(true);
        var usedNames = HashSet[String]();
        for (i <- Range(0, 10000) ) do {
            var name = generator.variableName(i);
            assertTrue(usedNames.add(name));
            assertFalse(RenderingUtil.KEYWORDS.contains(name));
        }
    }
}
