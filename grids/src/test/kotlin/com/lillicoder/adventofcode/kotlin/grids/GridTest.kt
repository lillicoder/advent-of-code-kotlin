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

import com.lillicoder.adventofcode.kotlin.math.Direction
import com.lillicoder.adventofcode.kotlin.math.Vertex
import com.lillicoder.adventofcode.kotlin.math.to
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Base unit tests for [Grid] implementers.
 */
internal abstract class GridTest(
    private val grid: Grid<String>,
    private val emptyGrid: Grid<Nothing> = grid {},
) {
    @Test
    fun `Columns are returned in correct order`() {
        val expected =
            listOf(
                "147",
                "258",
                "369",
            )
        val actual =
            grid.columns().map { column ->
                column.joinToString("") { it.value }
            }
        assertContentEquals(expected, actual)
    }

    @Test
    fun `Column by index returns correct column`() {
        val expected = listOf("1", "4", "7")
        val actual = grid.column(0)!!.map { it.value }
        assertContentEquals(expected, actual)
    }

    @Test
    fun `Column by invalid index returns null`() {
        val actual = grid.column(100)
        assertNull(actual)
    }

    @Test
    fun `Coordinates for vertex matches expected`() {
        val center = grid.find { it.value == "5" }!!
        val coordinates = grid.coordinates(center)
        assertEquals(1.to(1), coordinates)
    }

    @Test
    fun `Coordinates for invalid vertex are null`() {
        val vertex = Vertex(100L, "123")
        assertNull(grid.coordinates(vertex))
    }

    @Test
    fun `Default iterator is in top-left to bottom-right order`() {
        val expected = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
        val actual = grid.map { it.value }
        assertContentEquals(expected, actual)
    }

    @Test
    fun `Default iterator for empty grid is empty`() {
        val expected = emptyList<String>()
        val actual = emptyGrid.map { it.value }
        assertContentEquals(expected, actual)
    }

    @Test
    fun `Direction between neighbors matches expected direction`() {
        val expected = Direction.entries.filterNot { it == Direction.UNKNOWN }

        val center = grid.find { it.value == "5" }!!
        val neighbors =
            listOf(
                grid.find { it.value == "2" }!!,
                grid.find { it.value == "8" }!!,
                grid.find { it.value == "4" }!!,
                grid.find { it.value == "6" }!!,
                grid.find { it.value == "1" }!!,
                grid.find { it.value == "3" }!!,
                grid.find { it.value == "9" }!!,
                grid.find { it.value == "7" }!!,
            )
        val actual =
            neighbors.map {
                grid.direction(center, it)
            }

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Direction between invalid vertex and valid vertex is unknown`() {
        val first = grid.first()
        val vertex = Vertex(100L, "123")

        // Results should match regardless of parameter order
        assertEquals(
            Direction.UNKNOWN,
            grid.direction(
                first,
                vertex,
            ),
        )
        assertEquals(
            Direction.UNKNOWN,
            grid.direction(
                vertex,
                first,
            ),
        )
    }

    @Test
    fun `Direction between invalid vertices is unknown`() {
        val first = Vertex(100L, "123")
        val second = Vertex(101L, "987")
        val actual = grid.direction(first, second)
        assertEquals(Direction.UNKNOWN, actual)
    }

    @Test
    fun `Direction between non-neighbors is unknown`() {
        val first = grid.first()
        val last = grid.last()
        val actual = grid.direction(first, last)
        assertEquals(Direction.UNKNOWN, actual)
    }

    @Test
    fun `Distance between vertices matches expected`() {
        val expected = 4L

        val first = grid.first()
        val last = grid.last()
        val actual = grid.distance(first, last)

        assertEquals(expected, actual)
    }

    @Test
    fun `Distance between invalid vertex and valid vertex is unknown`() {
        val expected = -1L

        val first = grid.first()
        val vertex = Vertex(100L, "!23")

        // Results should match regardless of parameter order
        assertEquals(
            expected,
            grid.distance(
                first,
                vertex,
            ),
        )
        assertEquals(
            expected,
            grid.distance(
                vertex,
                first,
            ),
        )
    }

    @Test
    fun `Distance between invalid vertices is unknown`() {
        val expected = -1L

        val first = Vertex(100L, "123")
        val second = Vertex(101L, "987")
        val actual = grid.distance(first, second)

        assertEquals(expected, actual)
    }

    @Test
    fun `Neighbor for center is correct in all directions`() {
        val expected =
            listOf(
                "2",
                "8",
                "4",
                "6",
                "1",
                "3",
                "9",
                "7",
            )

        val center = grid.find { it.value == "5" }!!
        val actual =
            Direction.entries.mapNotNull {
                grid.neighbor(center, it)?.value
            }

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Neighbor for invalid vertex in any direction is null`() {
        val expected = List(9) { null }

        val vertex = Vertex(100L, "123")
        val actual =
            Direction.entries.map {
                grid.neighbor(vertex, it)
            }

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Neighbor in invalid direction is null`() {
        val first = grid.first()
        val actual = grid.neighbor(first, Direction.LEFT)
        assertNull(actual)
    }

    @Test
    fun `Neighbor in unknown direction is null`() {
        val first = grid.first()
        val actual = grid.neighbor(first, Direction.UNKNOWN)
        assertNull(actual)
    }

    @Test
    fun `Neighbors without diagonals for center matches are correct`() {
        val expected =
            listOf(
                grid.find { it.value == "4" },
                grid.find { it.value == "2" },
                grid.find { it.value == "8" },
                grid.find { it.value == "6" },
            )

        val center = grid.find { it.value == "5" }!!
        val actual = grid.neighbors(center)

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Neighbors with diagonals for center matches are correct`() {
        val expected =
            listOf(
                grid.find { it.value == "4" },
                grid.find { it.value == "2" },
                grid.find { it.value == "8" },
                grid.find { it.value == "6" },
                grid.find { it.value == "1" },
                grid.find { it.value == "3" },
                grid.find { it.value == "9" },
                grid.find { it.value == "7" },
            )

        val center = grid.find { it.value == "5" }!!
        val actual = grid.neighbors(center, true)

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Neighbors for invalid vertex are null`() {
        val expected = emptyList<Vertex<String>>()

        val vertex = Vertex(100L, "123")
        val actual = grid.neighbors(vertex)

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Rows are returned in correct order`() {
        val expected =
            listOf(
                "123",
                "456",
                "789",
            )
        val actual =
            grid.rows().map {
                it.joinToString("") { it.value }
            }
        assertContentEquals(expected, actual)
    }

    @Test
    fun `Row by index returns correct row`() {
        val expected = listOf("1", "2", "3")
        val actual = grid.row(0)!!.map { it.value }
        assertContentEquals(expected, actual)
    }

    @Test
    fun `Row by invalid index returns null`() {
        val actual = grid.row(100)
        assertNull(actual)
    }

    @Test
    fun `Height matches expected`() = assertEquals(3, grid.height)

    @Test
    fun `Width matches expected`() = assertEquals(3, grid.width)
}
