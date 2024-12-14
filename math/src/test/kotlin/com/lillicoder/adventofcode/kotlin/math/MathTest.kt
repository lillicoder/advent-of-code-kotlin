package com.lillicoder.adventofcode.kotlin.math

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for {Math].
 */
class MathTest {
    @Test
    fun `Polygon area matches expected`() {
        val interior = 100L
        val boundary = 25L
        val expected = 89L
        val actual = Math.area(interior, boundary)
        assertEquals(expected, actual)
    }

    @Test
    fun `Least common multiple of a list matches expected`() {
        val values = listOf(1L, 2L, 3L)
        val expected = 6L
        val actual = Math.leastCommonMultiple(values)
        assertEquals(expected, actual)
    }

    @Test
    fun `Least common multiple matches expected`() {
        val first = 2L
        val second = 3L
        val expected = 6L
        val actual = Math.leastCommonMultiple(first, second)
        assertEquals(expected, actual)
    }
}
