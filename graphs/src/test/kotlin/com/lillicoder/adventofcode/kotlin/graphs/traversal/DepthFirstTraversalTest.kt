package com.lillicoder.adventofcode.kotlin.graphs.traversal

import com.lillicoder.adventofcode.kotlin.graphs.graph
import com.lillicoder.adventofcode.kotlin.math.Vertex
import kotlin.test.Test
import kotlin.test.assertContentEquals

/**
 * Unit tests for [DepthFirstTraversal].
 */
internal class DepthFirstTraversalTest {
    private val graph =
        graph {
            vertex("1")
            vertex("2")
            vertex("3")
            vertex("4")
            vertex("5")
            vertex("6")
            vertex("7")
            vertex("8")
            vertex("9") // Intentionally disconnected from graph

            edge("1", "2")
            edge("1", "3")
            edge("2", "4")
            edge("2", "5")
            edge("3", "6")
            edge("3", "7")
            edge("5", "8")
        }
    private val traversal = DepthFirstTraversal(graph)

    @Test
    fun `Path for connected vertices matches expected order`() {
        val expected = listOf("1", "2", "5", "8")

        val start = graph.find { it.value == "1" }!!
        val destination = graph.find { it.value == "8" }!!
        val actual = traversal.path(start, destination).map { it.value }

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Path with no destination gives all vertices in visited order`() {
        val start = graph.find { it.value == "1" }!!

        val expected = listOf("1", "3", "7", "6", "2", "5", "8", "4")
        val actual = traversal.path(start).map { it.value }

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Path for unconnected vertices is empty`() {
        val expected = emptyList<Vertex<String>>()

        val start = graph.find { it.value == "1" }!!
        val destination = graph.find { it.value == "9" }!!
        val actual = traversal.path(start, destination)

        assertContentEquals(expected, actual)
    }

    @Test
    fun `Path for invalid start is empty`() {
        val expected = emptyList<Vertex<String>>()

        val start = Vertex(100L, "123")
        val destination = graph.find { it.value == "8" }!!
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
