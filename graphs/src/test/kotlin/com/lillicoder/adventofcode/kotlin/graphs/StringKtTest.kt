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

package com.lillicoder.adventofcode.kotlin.graphs

import com.lillicoder.adventofcode.kotlin.math.Vertex
import com.lillicoder.adventofcode.kotlin.text.normalizeLineSeparators
import kotlin.math.exp
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Unit tests for [String] extensions for graphs.
 */
internal class StringKtTest {
    private val single =
        """
        123
        456
        789
        """.trimIndent()

    private val multiple =
        """
        123
        456
        789
        
        987
        654
        321
        """.trimIndent()

    @Test
    fun `Grid to graph creates vertices in order`() {
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
        val actual = single.gridToGraph()
        assertContentEquals(expected, actual)
    }

    @Test
    fun `Grid to graph creates edges between all neighboring vertices`() {
        val vertices =
            mapOf(
                "1" to Vertex(0L, "1"),
                "2" to Vertex(1L, "2"),
                "3" to Vertex(2L, "3"),
                "4" to Vertex(3L, "4"),
                "5" to Vertex(4L, "5"),
                "6" to Vertex(5L, "6"),
                "7" to Vertex(6L, "7"),
                "8" to Vertex(7L, "8"),
                "9" to Vertex(8L, "9"),
            )
        val graph = single.gridToGraph()

        // 1 is connected to 2, 4
        assertTrue(graph.adjacent(vertices["1"]!!, vertices["2"]!!))
        assertTrue(graph.adjacent(vertices["1"]!!, vertices["4"]!!))

        // 2 is connected to 1, 3, 5
        assertTrue(graph.adjacent(vertices["2"]!!, vertices["1"]!!))
        assertTrue(graph.adjacent(vertices["2"]!!, vertices["3"]!!))
        assertTrue(graph.adjacent(vertices["2"]!!, vertices["5"]!!))

        // 3 is connected to 2, 6
        assertTrue(graph.adjacent(vertices["3"]!!, vertices["2"]!!))
        assertTrue(graph.adjacent(vertices["3"]!!, vertices["6"]!!))

        // 4 is connected to 1, 5, 7
        assertTrue(graph.adjacent(vertices["4"]!!, vertices["1"]!!))
        assertTrue(graph.adjacent(vertices["4"]!!, vertices["5"]!!))
        assertTrue(graph.adjacent(vertices["4"]!!, vertices["7"]!!))

        // 5 is connected to 2, 4, 6, 8
        assertTrue(graph.adjacent(vertices["5"]!!, vertices["2"]!!))
        assertTrue(graph.adjacent(vertices["5"]!!, vertices["4"]!!))
        assertTrue(graph.adjacent(vertices["5"]!!, vertices["6"]!!))
        assertTrue(graph.adjacent(vertices["5"]!!, vertices["8"]!!))

        // 6 is connected to 3, 5, 9
        assertTrue(graph.adjacent(vertices["6"]!!, vertices["3"]!!))
        assertTrue(graph.adjacent(vertices["6"]!!, vertices["5"]!!))
        assertTrue(graph.adjacent(vertices["6"]!!, vertices["9"]!!))

        // 7 is connected to 4, 8
        assertTrue(graph.adjacent(vertices["7"]!!, vertices["4"]!!))
        assertTrue(graph.adjacent(vertices["7"]!!, vertices["8"]!!))

        // 8 is connected to 5, 7, 9
        assertTrue(graph.adjacent(vertices["8"]!!, vertices["5"]!!))
        assertTrue(graph.adjacent(vertices["8"]!!, vertices["7"]!!))
        assertTrue(graph.adjacent(vertices["8"]!!, vertices["9"]!!))

        // 9 is connected to 6, 8
        assertTrue(graph.adjacent(vertices["9"]!!, vertices["6"]!!))
        assertTrue(graph.adjacent(vertices["9"]!!, vertices["8"]!!))
    }

    @Test
    fun `Grids to graphs creates expected number of graphs`() {
        val expected =
            listOf(
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
                ),
                listOf(
                    Vertex(0L, "9"),
                    Vertex(1L, "8"),
                    Vertex(2L, "7"),
                    Vertex(3L, "6"),
                    Vertex(4L, "5"),
                    Vertex(5L, "4"),
                    Vertex(6L, "3"),
                    Vertex(7L, "2"),
                    Vertex(8L, "1"),
                )
            )

        val actual = multiple.normalizeLineSeparators().gridsToGraphs()
        assertEquals(expected.size, actual.size)
        assertContentEquals(expected.first(), actual.first())
        assertContentEquals(expected.last(), actual.last())
    }
}
