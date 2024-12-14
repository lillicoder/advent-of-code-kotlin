package com.lillicoder.adventofcode.kotlin.graphs

import com.lillicoder.adventofcode.kotlin.math.Vertex
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/**
 * Unit tests for [Edge].
 */
internal class EdgeTest {
    @Test
    fun `Directed edges match if vertices are in same order`() {
        val a = Vertex(1, "Test 1")
        val b = Vertex(2, "Test 2")
        val first = Edge(a, b, isDirected = true)
        val second = Edge(a, b, isDirected = true)
        assertEquals(first, second)
    }

    @Test
    fun `Directed edges do not match if vertices are not in same order`() {
        val a = Vertex(1, "Test 1")
        val b = Vertex(2, "Test 2")
        val first = Edge(a, b, isDirected = true)
        val second = Edge(b, a, isDirected = true)
        assertNotEquals(first, second)
    }

    @Test
    fun `Undirected edges match if vertices are in same order`() {
        val a = Vertex(1, "Test 1")
        val b = Vertex(2, "Test 2")
        val first = Edge(a, b)
        val second = Edge(a, b)
        assertEquals(first, second)
    }

    @Test
    fun `Undirected edges match if vertices are not in same order`() {
        val a = Vertex(1, "Test 1")
        val b = Vertex(2, "Test 2")
        val first = Edge(a, b)
        val second = Edge(b, a)
        assertEquals(first, second)
    }
}
