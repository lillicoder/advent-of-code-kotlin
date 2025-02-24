/*
 * Copyright 2025 Scott Weeden-Moody
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

package com.lillicoder.adventofcode.kotlin.collections

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith

/**
 * Unit tests for [MutableList] extensions.
 */
internal class MutableListKtTest {
    @Test
    fun `Swap moves elements correctly`() {
        val expected = listOf(1, 16, 4, 8, 2, 32)
        val actual = mutableListOf(1, 2, 4, 8, 16, 32).also { it.swap(1, 4) }
        assertContentEquals(expected, actual)
    }

    @Test
    fun `Swap with invalid index throws exception`() {
        assertFailsWith<IndexOutOfBoundsException> {
            mutableListOf(3, 2, 1).also { it.swap(0, 100) }
        }

        assertFailsWith<IndexOutOfBoundsException> {
            mutableListOf(3, 2, 1).also { it.swap(-100, 0) }
        }

        assertFailsWith<IndexOutOfBoundsException> {
            mutableListOf(3, 2, 1).also { it.swap(-100, 100) }
        }
    }
}
