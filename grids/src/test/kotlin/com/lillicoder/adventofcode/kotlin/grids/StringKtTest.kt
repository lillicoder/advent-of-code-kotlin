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

package com.lillicoder.adventofcode.kotlin.grids

import com.lillicoder.adventofcode.kotlin.math.Vertex
import kotlin.test.Test
import kotlin.test.assertContentEquals

/**
 * Unit tests for [String] extensions for grids.
 */
internal class StringKtTest {
    private val raw =
        """
        123
        456
        789
        """.trimIndent()

    @Test
    fun `toGrid creates vertices in order`() {
        val expected =
            listOf(
                Vertex(0L, "1"),
                Vertex(1L, "2"),
                Vertex(2L, "3"),
                Vertex(3L, "4"),
                Vertex(4L, "5"),
                Vertex(5L, "6"),
                Vertex(6L, "7"),
                Vertex(7L, "8"),
                Vertex(8L, "9"),
            )
        val actual = raw.toGrid()
        assertContentEquals(expected, actual)
    }
}
