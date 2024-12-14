package com.lillicoder.adventofcode.kotlin.math

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Coordinates].
 */
internal class CoordinatesTest {
    @Test
    fun `Cross product matches expected`() {
        val first = 1.to(2)
        val second = 3.to(4)
        val expected = -2L
        val actual = first.cross(second)
        assertEquals(expected, actual)
    }

    @Test
    fun `Distance matches expected`() {
        val first = 1.to(2)
        val second = 3.to(4)
        val expected = 4L
        val actual = first.distance(second)
        assertEquals(expected, actual)
    }

    @Test
    fun `Right creates coordinates in correct direction`() {
        val expected = 2.to(2)
        val actual = 1.to(2).right()
        assertEquals(expected, actual)
    }

    @Test
    fun `Right up creates coordinates in correct direction`() {
        val expected = 2.to(1)
        val actual = 1.to(2).rightUp()
        assertEquals(expected, actual)
    }

    @Test
    fun `Up creates coordinates in correct direction`() {
        val expected = 1.to(1)
        val actual = 1.to(2).up()
        assertEquals(expected, actual)
    }

    @Test
    fun `Left up creates coordinates in correct direction`() {
        val expected = 0.to(1)
        val actual = 1.to(2).leftUp()
        assertEquals(expected, actual)
    }

    @Test
    fun `Left creates coordinates in correct direction`() {
        val expected = 0.to(2)
        val actual = 1.to(2).left()
        assertEquals(expected, actual)
    }

    @Test
    fun `Left down creates coordinates in correct direction`() {
        val expected = 0.to(3)
        val actual = 1.to(2).leftDown()
        assertEquals(expected, actual)
    }

    @Test
    fun `Down creates coordinates in correct direction`() {
        val expected = 1.to(3)
        val actual = 1.to(2).down()
        assertEquals(expected, actual)
    }

    @Test
    fun `Right down creates coordinates in correct direction`() {
        val expected = 2.to(3)
        val actual = 1.to(2).rightDown()
        assertEquals(expected, actual)
    }

    @Test
    fun `Shift creates coordinates correctly for all directions`() {
        val coordinates = 1.to(2)

        val expectedRight = 2.to(2)
        val actualRight = coordinates.shift(Direction.RIGHT)
        assertEquals(expectedRight, actualRight)

        val expectedRightUp = 2.to(1)
        val actualRightUp = coordinates.shift(Direction.RIGHT_UP)
        assertEquals(expectedRightUp, actualRightUp)

        val expectedUp = 1.to(1)
        val actualUp = coordinates.shift(Direction.UP)
        assertEquals(expectedUp, actualUp)

        val expectedLeftUp = 0.to(1)
        val actualLeftUp = coordinates.shift(Direction.LEFT_UP)
        assertEquals(expectedLeftUp, actualLeftUp)

        val expectedLeft = 0.to(2)
        val actualLeft = coordinates.shift(Direction.LEFT)
        assertEquals(expectedLeft, actualLeft)

        val expectedLeftDown = 0.to(3)
        val actualLeftDown = coordinates.shift(Direction.LEFT_DOWN)
        assertEquals(expectedLeftDown, actualLeftDown)

        val expectedDown = 1.to(3)
        val actualDown = coordinates.shift(Direction.DOWN)
        assertEquals(expectedDown, actualDown)

        val expectedRightDown = 2.to(3)
        val actualRightDown = coordinates.shift(Direction.RIGHT_DOWN)
        assertEquals(expectedRightDown, actualRightDown)

        val actualUnknown = coordinates.shift(Direction.UNKNOWN)
        assertEquals(coordinates, actualUnknown)
    }

    @Test
    fun `toString matches expected format`() {
        val expected = "(1, 2)"
        val actual = 1.to(2).toString()
        assertEquals(expected, actual)
    }

    @Test
    fun `to function correctly creates coordinates from integers`() {
        val expected = Coordinates(1, 2)
        val actual = 1.to(2)
        assertEquals(expected, actual)
    }

    @Test
    fun `to function correctly creates coordinates from longs`() {
        val expected = Coordinates(1L, 2L)
        val actual = 1L.to(2L)
        assertEquals(expected, actual)
    }

    @Test
    fun `Polygon area from coordinates matches expected`() {
        // Make a square of side length 3, should have area 3^2
        val coordinates =
            listOf(
                0.to(0),
                3.to(0),
                3.to(3),
                0.to(3),
            )
        val expected = 9L
        val actual = coordinates.area()
        assertEquals(expected, actual)
    }
}
