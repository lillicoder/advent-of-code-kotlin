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

import com.lillicoder.adventofcode.kotlin.text.toList
import kotlin.test.Test
import kotlin.test.assertContentEquals

/**
 * Unit tests for [List] extensions.
 */
internal class ListKtTest {
    @Test
    fun `Split by predicate creates sub-lists in order`() {
        val input = "123..321.565.........test...1".toList()
        val expected = listOf(
            "123".toList(),
            "321".toList(),
            "565".toList(),
            "1".toList(),
        )

        val actual = input.split { it.toIntOrNull() == null }
        assertContentEquals(expected, actual)
    }
}
