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

import com.lillicoder.adventofcode.kotlin.math.Coordinates
import com.lillicoder.adventofcode.kotlin.math.Direction
import com.lillicoder.adventofcode.kotlin.math.Vertex
import com.lillicoder.adventofcode.kotlin.math.to

/**
 * [Grid] implemented with a two-dimensional [List].
 *
 * This implementation has grid structure tied to the underlying data structures used.
 *
 * @param vertices Two-dimensional list of [Vertex].
 * @param height Grid height.
 * @param width Grid width.
 */
class ListGrid<T>(
    private val vertices: List<List<Vertex<T>>>,
    override val height: Int = vertices.size,
    override val width: Int = vertices[0].size,
) : Grid<T> {
    override fun column(index: Int): List<Vertex<T>>? {
        val column = vertices.mapNotNull { it.getOrNull(index) }
        return when (column.isEmpty()) {
            true -> null
            else -> column
        }
    }

    override fun columns() =
        (0..<width).map { columnIndex ->
            vertices.map {
                it[columnIndex]
            }
        }

    override fun coordinates(vertex: Vertex<T>): Coordinates? {
        vertices.forEachIndexed { rowIndex, row ->
            val columnIndex = row.indexOf(vertex)
            if (columnIndex >= 0) return columnIndex.to(rowIndex)
        }

        return null
    }

    override fun neighbor(
        vertex: Vertex<T>,
        direction: Direction,
    ) = when (direction) {
        Direction.UNKNOWN -> null
        else ->
            coordinates(vertex)?.shift(direction)?.let {
                vertices.getOrNull(
                    it.y.toInt(),
                )?.getOrNull(
                    it.x.toInt(),
                )
            }
    }

    override fun row(index: Int) = vertices.getOrNull(index)

    override fun rows() = vertices

    override fun vertex(coordinates: Coordinates) =
        vertices.getOrNull(
            coordinates.y.toInt(),
        )?.getOrNull(
            coordinates.x.toInt(),
        )

    override fun toString() =
        rows().joinToString(System.lineSeparator()) { row ->
            row.joinToString("") {
                it.value.toString()
            }
        }
}

/**
 * Type-safe builder for creating a [ListGrid].
 *
 * Example usage:
 * ```
 * listGrid {
 *     row {
 *         vertex("a")
 *         vertex("b")
 *     }
 *     row {
 *         vertex("c")
 *         vertex("d")
 *     }
 * }
 * ```
 * @param init Function with receiver.
 * @return List grid.
 */
fun <T> listGrid(init: Grid.Builder<T>.() -> Unit): ListGrid<T> {
    val builder = Grid.Builder<T>()
    builder.init()
    return builder.toListGrid()
}
