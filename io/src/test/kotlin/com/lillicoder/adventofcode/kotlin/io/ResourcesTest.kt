package com.lillicoder.adventofcode.kotlin.io

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Unit tests for [Resources].
 */
internal class ResourcesTest {
    @Test
    fun `Reading resource as lines matches expected`() {
        val expected =
            listOf(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna",
                "aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint",
                "occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            )
        val actual = Resources.lines("test.txt")
        assertContentEquals(expected, actual)
    }

    @Test
    fun `Reading invalid resource as lines is null`() {
        val actual = Resources.lines("fake")
        assertNull(actual)
    }

    @Test
    fun `Mapping resource lines matches expected`() {
        val expected =
            listOf(
                1,
                2,
                3,
                4,
                5,
            )
        val actual = Resources.lines("test2.txt")?.map { it.toInt() }
        assertContentEquals(expected, actual)
    }

    @Test
    fun `Mapping invalid resource lines is null`() {
        val actual = Resources.lines("fake")?.map { it.toInt() }
        assertNull(actual)
    }

    @Test
    fun `Reading resource as text matches expected`() {
        val lineSeparatorPattern = "\\n|\\r\\n".toRegex() // Ensure consistent line separators regardless of platform
        val expected =
            """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna
            aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
            Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint
            occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
            """.trimIndent().replace(
                lineSeparatorPattern,
                System.lineSeparator(),
            )
        val actual =
            Resources.text(
                "test.txt",
            )?.replace(
                lineSeparatorPattern,
                System.lineSeparator(),
            )
        assertEquals(expected, actual)
    }

    @Test
    fun `Reading invalid resource as text is null`() {
        val actual = Resources.text("fake")
        assertNull(actual)
    }

    @Test
    fun `Reading resource as stream matches expected size`() {
        val expected = 448
        val actual = Resources.stream("test.txt")!!.readAllBytes().size
        assertEquals(expected, actual)
    }

    @Test
    fun `Reading invalid resource as stream is null`() {
        val actual = Resources.stream("fake")
        assertNull(actual)
    }
}
