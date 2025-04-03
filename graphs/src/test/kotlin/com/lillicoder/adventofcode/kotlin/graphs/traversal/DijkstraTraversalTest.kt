package com.lillicoder.adventofcode.kotlin.graphs.traversal

import com.lillicoder.adventofcode.kotlin.graphs.graph
import com.lillicoder.adventofcode.kotlin.math.Vertex
import kotlin.test.Test
import kotlin.test.assertContentEquals

/**
 * Unit tests for [DijkstraTraversal].
 */
internal class DijkstraTraversalTest {
    private val graph =
        graph {
            vertex("1")
            vertex("2")
            vertex("3")
            vertex("4")
            vertex("5")
            vertex("6")
            vertex("7")

            edge("1", "2") {
                weight(7)
            }
            edge("1", "3") {
                weight(9)
            }
            edge("1", "6") {
                weight(14)
            }
            edge("2", "3") {
                weight(10)
            }
            edge("2", "4") {
                weight(15)
            }
            edge("3", "4") {
                weight(11)
            }
            edge("3", "6") {
                weight(2)
            }
            edge("4", "5") {
                weight(6)
            }
            edge("5", "6") {
                weight(9)
            }
        }
    private val traversal = DijkstraTraversal(graph)

    @Test
    fun `Path for connected vertices matches expected order`() {
        val expected = listOf("1", "3", "6", "5")

        val start = graph.find { it.value == "1" }!!
        val destination = graph.find { it.value == "5" }!!
        val actual = traversal.path(start, destination).map { it.value }

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Path with no destination gives all vertices in visited order`() {
        val start = graph.find { it.value == "1" }!!

        val expected = emptyList<String>()
        val actual = traversal.path(start).map { it.value }

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Path for unconnected vertices is empty`() {
        val expected = emptyList<Vertex<String>>()

        val start = graph.find { it.value == "1" }!!
        val destination = graph.find { it.value == "7" }!!
        val actual = traversal.path(start, destination)

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Path for invalid start is empty`() {
        val expected = emptyList<Vertex<String>>()

        val start = Vertex(100L, "123")
        val destination = graph.find { it.value == "5" }!!
        val actual = traversal.path(start, destination)

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Path for invalid destination is empty`() {
        val expected = emptyList<Vertex<String>>()

        val start = graph.find { it.value == "1" }!!
        val destination = Vertex(100L, "123")
        val actual = traversal.path(start, destination)

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Path for invalid vertices is empty`() {
        val expected = emptyList<Vertex<String>>()

        val start = Vertex(100L, "123")
        val destination = Vertex(101L, "321")
        val actual = traversal.path(start, destination)

        assertContentEquals(expected, actual)
    }
}
