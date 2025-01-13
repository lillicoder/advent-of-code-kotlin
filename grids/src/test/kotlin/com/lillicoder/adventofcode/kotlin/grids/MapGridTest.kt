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
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Unit tests for [MapGrid].
 */
internal class MapGridTest {
    private val grid =
        mapGrid {
            row {
                vertex("1")
                vertex("2")
                vertex("3")
            }
            row {
                vertex("4")
                vertex("5")
                vertex("6")
            }
            row {
                vertex("7")
                vertex("8")
                vertex("9")
            }
        }

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
}
