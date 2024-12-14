package com.lillicoder.adventofcode.kotlin.math

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/**
 * Unit tests for [Vertex].
 */
internal class VertexTest {
    @Test
    fun `Vertices with equal values but different IDs are not equal`() {
        val first = Vertex(1, "Test")
        val second = Vertex(2, "Test")
        assertNotEquals(first, second)
    }

    @Test
    fun `Vertices with different values but equal IDs are equal`() {
        val first = Vertex(1, "Test 1")
        val second = Vertex(1, "Test 2")
        assertEquals(first, second)
    }
}
