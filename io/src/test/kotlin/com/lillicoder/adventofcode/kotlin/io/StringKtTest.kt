/*
 * Copyright 2024 Scott Weeden-Moody
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this project except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lillicoder.adventofcode.kotlin.io

import kotlin.test.Test
import kotlin.test.assertContentEquals

/**
 * Unit tests for [String] extensions for resources.
 */
internal class StringKtTest {
    @Test
    fun `Split map maps correctly`() {
        val input = "1 2 3 4 5"
        val expected =
            listOf(
                1,
                2,
                3,
                4,
                5,
            )
        val actual = input.splitMap(" ") { it.toInt() }
        assertContentEquals(expected, actual)
    }

    @Test
    fun `Split map not empty removes empty substrings and maps correctly`() {
        val input = "     1 2 3 4 5      "
        val expected =
            listOf(
                1,
                2,
                3,
                4,
                5,
            )
        val actual = input.splitMapNotEmpty(" ") { it.toInt() }
        assertContentEquals(expected, actual)
    }

    @Test
    fun `Split not empty removes empty substrings`() {
        val input = "    The quick brown fox     "
        val expected =
            listOf(
                "The",
                "quick",
                "brown",
                "fox",
            )
        val actual = input.splitNotEmpty(" ")
        assertContentEquals(expected, actual)
    }
}
