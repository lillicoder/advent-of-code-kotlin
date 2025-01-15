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

/**
 * [Grid] implemented with two-way mappings of [Vertex] and [Coordinates].
 *
 * This implementation separates the grid structure from the underlying data structures used.
 *
 * @param coordinatesByVertex Each vertex mapped to its [Coordinates].
 * @param vertexByCoordinates Each coordinate mapped to its vertex.
 * @param columns Each column of vertices mapped to its column index.
 * @param rows Each row of vertices mapped to its row index.
 * @param height Grid height.
 * @param width Grid width.
 */
class MapGrid<T>(
    private val coordinatesByVertex: Map<Vertex<T>, Coordinates>,
    private val vertexByCoordinates: Map<Coordinates, Vertex<T>>,
    private val columns: Map<Long, List<Vertex<T>>> =
        vertexByCoordinates.keys.groupBy(Coordinates::x).mapValues { entry ->
            entry.value.map { vertexByCoordinates[it]!! }
        },
    private val rows: Map<Long, List<Vertex<T>>> =
        vertexByCoordinates.keys.groupBy(Coordinates::y).mapValues { entry ->
            entry.value.map { vertexByCoordinates[it]!! }
        },
    override val height: Int = rows.size,
    override val width: Int = columns.size,
) : Grid<T> {
    override fun column(index: Int) = columns[index.toLong()]

    override fun columns() = columns.map { it.value }

    override fun coordinates(vertex: Vertex<T>) = coordinatesByVertex[vertex]

    override fun neighbor(
        vertex: Vertex<T>,
        direction: Direction,
    ) = when (direction) {
        Direction.UNKNOWN -> null
        else -> vertexByCoordinates[coordinatesByVertex[vertex]?.shift(direction)]
    }

    override fun row(index: Int) = rows[index.toLong()]

    override fun rows() = rows.map { it.value }

    override fun vertex(coordinates: Coordinates) = vertexByCoordinates[coordinates]

    override fun toString() =
        rows.values.joinToString(System.lineSeparator()) { row ->
            row.joinToString("") {
                it.value.toString()
            }
        }
}

/**
 * Type-safe builder for creating a [MapGrid].
 *
 * Example usage:
 * ```
 * mapGrid {
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
fun <T> mapGrid(init: Grid.Builder<T>.() -> Unit): MapGrid<T> {
    val builder = Grid.Builder<T>()
    builder.init()
    return builder.toMapGrid()
}
