package com.lillicoder.adventofcode.kotlin.math

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [UnorderedPair].
 */
internal class UnorderedPairTest {
    @Test
    fun `Pairs are equal if elements are in same order`() {
        val first = UnorderedPair("Test 1", "Test 2")
        val second = UnorderedPair("Test 1", "Test 2")
        assertEquals(first, second)
    }

    @Test
    fun `Pairs are equal if elements are not in same order`() {
        val first = UnorderedPair("Test 1", "Test 2")
        val second = UnorderedPair("Test 2", "Test 1")
        assertEquals(first, second)
    }

    @Test
    fun `toString matches expected format`() {
        val expected = "(Test 1, Test 2)"
        val actual = UnorderedPair("Test 1", "Test 2").toString()
        assertEquals(expected, actual)
    }
}
