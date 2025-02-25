package com.lillicoder.adventofcode.kotlin.graphs

import com.lillicoder.adventofcode.kotlin.math.Direction
import com.lillicoder.adventofcode.kotlin.math.Vertex
import com.lillicoder.adventofcode.kotlin.math.to
import com.lillicoder.adventofcode.kotlin.text.normalizeLineSeparators
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Unit tests for [SquareLatticeGraph].
 */
internal class SquareLatticeGraphTest {
    private val graph =
        """
        123
        456
        789
        """.trimIndent().gridToGraph()

    @Test
    fun `Columns are returned in correct order`() {
        val expected =
            listOf(
                "147",
                "258",
                "369",
            )
        val actual =
            graph.columns().map { column ->
                column.joinToString("") { it.value }
            }
        assertContentEquals(expected, actual)
    }

    @Test
    fun `Column by index returns correct column`() {
        val expected = listOf("1", "4", "7")
        val actual = graph.column(0)!!.map { it.value }
        assertContentEquals(expected, actual)
    }

    @Test
    fun `Column by invalid index returns null`() {
        val actual = graph.column(100)
        assertNull(actual)
    }

    @Test
    fun `Coordinates for vertex matches expected`() {
        val center = graph.vertex(4L)!!
        val coordinates = graph.coordinates(center)
        assertEquals(1.to(1), coordinates)
    }

    @Test
    fun `Coordinates for invalid vertex are null`() {
        val vertex = Vertex(100L, "123")
        assertNull(graph.coordinates(vertex))
    }

    @Test
    fun `Direction between neighbors matches expected direction`() {
        val expected = Direction.entries.filterNot { it == Direction.UNKNOWN }

        val center = graph.vertex(4L)!!
        val neighbors =
            listOf(
                graph.vertex(1L)!!,
                graph.vertex(7L)!!,
                graph.vertex(3L)!!,
                graph.vertex(5L)!!,
                graph.vertex(0L)!!,
                graph.vertex(2L)!!,
                graph.vertex(8L)!!,
                graph.vertex(6L)!!,
            )
        val actual =
            neighbors.map {
                graph.direction(center, it)
            }

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Direction between invalid vertex and valid vertex is unknown`() {
        val first = graph.first()
        val vertex = Vertex(100L, "123")

        // Results should match regardless of parameter order
        assertEquals(
            Direction.UNKNOWN,
            graph.direction(
                first,
                vertex,
            ),
        )
        assertEquals(
            Direction.UNKNOWN,
            graph.direction(
                vertex,
                first,
            ),
        )
    }

    @Test
    fun `Direction between invalid vertices is unknown`() {
        val first = Vertex(100L, "123")
        val second = Vertex(101L, "987")
        val actual = graph.direction(first, second)
        assertEquals(Direction.UNKNOWN, actual)
    }

    @Test
    fun `Direction between non-neighbors is unknown`() {
        val first = graph.first()
        val last = graph.last()
        val actual = graph.direction(first, last) // Should not be connected
        assertEquals(Direction.UNKNOWN, actual)
    }

    @Test
    fun `Height matches expected`() = assertEquals(3, graph.height)

    @Test
    fun `Neighbors for center are correct in all directions`() {
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

        val center = graph.vertex(4L)!!
        val actual =
            Direction.entries.mapNotNull {
                graph.neighbor(center, it)?.value
            }

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Neighbor for invalid vertex in any direction is null`() {
        val expected = List(9) { null }

        val vertex = Vertex(100L, "123")
        val actual =
            Direction.entries.map {
                graph.neighbor(vertex, it)
            }

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Neighbor in invalid direction is null`() {
        val first = graph.first()
        val actual = graph.neighbor(first, Direction.LEFT)
        assertNull(actual)
    }

    @Test
    fun `Neighbor in unknown direction is null`() {
        val first = graph.first()
        val actual = graph.neighbor(first, Direction.UNKNOWN)
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
            graph.rows().map {
                it.joinToString("") { it.value }
            }
        assertContentEquals(expected, actual)
    }

    @Test
    fun `Row by index returns correct row`() {
        val expected = listOf("1", "2", "3")
        val actual = graph.row(0)!!.map { it.value }
        assertContentEquals(expected, actual)
    }

    @Test
    fun `Row by invalid index returns null`() {
        val actual = graph.row(100)
        assertNull(actual)
    }

    @Test
    fun `toString matches expected format`() {
        val expected =
            """
            123
            456
            789
            """.trimIndent().normalizeLineSeparators()
        val actual = graph.toString().normalizeLineSeparators()
        assertEquals(expected, actual)
    }

    @Test
    fun `Width matches expected`() = assertEquals(3, graph.width)
}
