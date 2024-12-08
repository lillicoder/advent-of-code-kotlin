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
 * A two-dimensional grid of [Vertex].
 * @param coordinatesByVertex Each vertex mapped to its [Coordinates].
 * @param vertexByCoordinates Each coordinate mapped to its vertex.
 * @param columns Each column of vertices mapped to its column index.
 * @param rows Each row of vertices mapped to its row index.
 * @param height Grid height.
 * @param width Grid width.
 */
class Grid<T>(
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
    val height: Int = rows.size,
    val width: Int = columns.size,
) {
    private constructor(builder: Builder<T>) : this(
        builder.coordinatesByVertex,
        builder.vertexByCoordinates,
    )

    /**
     * Gets the column of [Vertex] for the given column index.
     * @param index Column index.
     * @return Column or null if there is no column for the given index.
     */
    fun column(index: Int) = columns[index.toLong()]

    /**
     * Gets each column of [Vertex] in this graph in index order.
     * Vertices in each column are in row index order.
     * @return Rows.
     */
    fun columns() = columns.map { it.value }

    /**
     * Gets the [Coordinates] for the given [Vertex].
     * @param vertex Vertex.
     * @Return Coordinates or null if there are no coordinates for the given vertex.
     */
    fun coordinates(vertex: Vertex<T>) = coordinatesByVertex[vertex]

    /**
     * Determines the [Direction] from the given source [Vertex] to the given destination vertex.
     * @param source Source vertex.
     * @param destination Destination vertex.
     * @return Direction from source to destination or [Direction.UNKNOWN] if there is no
     * direction between the two vertices.
     */
    fun direction(
        source: Vertex<T>,
        destination: Vertex<T>,
    ) = coordinatesByVertex[source]?.let {
        when (coordinatesByVertex[destination]) {
            it.left() -> Direction.LEFT
            it.right() -> Direction.RIGHT
            it.down() -> Direction.DOWN
            it.up() -> Direction.UP
            else -> Direction.UNKNOWN
        }
    } ?: Direction.UNKNOWN

    /**
     * Gets the Manhattan distance between the given [Vertex].
     * @param start Starting vertex.
     * @param end Ending vertex.
     * @return Manhattan distance or -1 if coordinates for either vertex could not be found.
     */
    fun distance(
        start: Vertex<T>,
        end: Vertex<T>,
    ) = coordinates(end)?.let {
        coordinates(start)?.distance(it) ?: -1L
    } ?: -1L

    /**
     * Gets the [Vertex] neighboring the given vertex in the given direction.
     * @param vertex Vertex.
     * @param direction Direction.
     * @return Neighboring vertex or null if there is no neighbor in the given direction.
     */
    fun neighbor(
        vertex: Vertex<T>,
        direction: Direction,
    ) = vertexByCoordinates[coordinatesByVertex[vertex]?.shift(direction)]

    /**
     * Gets each [Vertex] neighboring the given vertex in each [Direction].
     * @param vertex Vertex.
     * @return Neighboring vertices.
     */
    fun neighbors(vertex: Vertex<T>) =
        listOfNotNull(
            neighbor(vertex, Direction.LEFT),
            neighbor(vertex, Direction.UP),
            neighbor(vertex, Direction.DOWN),
            neighbor(vertex, Direction.RIGHT),
        )

    /**
     * Gets the row of [Vertex] for the given row index.
     * @param index Row index.
     * @return Row or null if there is no row for the given index.
     */
    fun row(index: Int) = rows[index.toLong()]

    /**
     * Gets each row of [Vertex] in this graph in index order.
     * Vertices in each row are in column index order.
     * @return Rows.
     */
    fun rows() = rows.map { it.value }

    override fun toString() =
        rows.values.joinToString(System.lineSeparator()) { row ->
            row.joinToString("") {
                it.value.toString()
            }
        }

    /**
     * Builder for [Grid] instances.
     * @param coordinatesByVertex Each [Coordinates] keyed by its [Vertex].
     * @param vertexByCoordinates Each vertex keyed by its coordinates.
     */
    class Builder<T>(
        internal val coordinatesByVertex: MutableMap<Vertex<T>, Coordinates> = mutableMapOf(),
        internal val vertexByCoordinates: MutableMap<Coordinates, Vertex<T>> = mutableMapOf(),
    ) {
        fun build() = Grid(this)

        fun vertex(
            coordinates: Coordinates,
            element: T,
        ) = vertex(
            coordinates,
            Vertex(
                coordinatesByVertex.size.toLong(),
                element,
            ),
        )

        fun vertex(
            coordinates: Coordinates,
            vertex: Vertex<T>,
        ) = apply {
            coordinatesByVertex[vertex] = coordinates
            vertexByCoordinates[coordinates] = vertex
        }
    }
}
