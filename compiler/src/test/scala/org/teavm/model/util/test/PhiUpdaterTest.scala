/*
 *  Copyright 2017 Alexey Andreev.
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
package org.teavm.model.util.test;

import org.scalatest.funsuite.{AnyFunSuite}
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.teavm.model.ListingParseUtils;
import org.teavm.model.Program;
import org.teavm.model.Variable;
import org.teavm.model.text.ListingBuilder;
import org.teavm.model.util.PhiUpdater;

class PhiUpdaterTest {
    private final val PREFIX = "model/util/phi-updater/";

    // val name = new TestName();

    @Test
    def exceptionPhi() = {
        doTest("exceptionPhi");
        doTest();
    }

    @Test
    def exceptionPhiMultiple() = {
        doTest("exceptionPhiMultiple");
        doTest();
    }

    @Test
    def exceptionPhiFromSinglePhi() = {
        doTest("exceptionPhiFromSinglePhi");
        doTest();
    }

    @Test
    def existingExceptionPhi() = {
        doTest("existingExceptionPhi");
        doTest();
    }

    @Test
    def phiIncoming() = {
        doTest("phiIncoming");
        doTest();
    }

    private def doTest(): Unit = {}

    private def doTest(nm: String): Unit = {
        val originalPath = PREFIX + nm + ".original.txt";
        val expectedPath = PREFIX + nm + ".expected.txt";
        val original = ListingParseUtils.parseFromResource(originalPath);
        val expected = ListingParseUtils.parseFromResource(expectedPath);

        new PhiUpdater().updatePhis(original, Array[Variable]() );

        val originalText = new ListingBuilder().buildListing(original, "");
        val expectedText = new ListingBuilder().buildListing(expected, "");
        Assert.assertEquals(expectedText, originalText);

        new PhiUpdater().updatePhis(original, Array[Variable]() );
        val repeatedText = new ListingBuilder().buildListing(original, "");
        Assert.assertEquals("Repeated update", originalText, repeatedText);
    }
}
