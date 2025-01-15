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
 * A two-dimensional grid of [Vertex].
 */
interface Grid<T> : Iterable<Vertex<T>> {
    val height: Int
    val width: Int

    override fun iterator() =
        object : Iterator<Vertex<T>> {
            private var rows = rows().iterator()
            private var columns =
                when (rows.hasNext()) {
                    true -> rows.next().iterator()
                    else -> emptyList<Vertex<T>>().iterator()
                }

            override fun hasNext() = columns.hasNext() || rows.hasNext()

            override fun next() =
                when (columns.hasNext()) {
                    true -> columns.next()
                    else -> {
                        columns = rows.next().iterator()
                        columns.next()
                    }
                }
        }

    /**
     * Gets the column of [Vertex] for the given column index.
     * @param index Column index.
     * @return Column or null if there is no column for the given index.
     */
    fun column(index: Int): List<Vertex<T>>?

    /**
     * Gets each column of [Vertex] in this graph in index order.
     * Vertices in each column are in row index order.
     * @return Rows.
     */
    fun columns(): List<List<Vertex<T>>>

    /**
     * Gets the [Coordinates] for the given [Vertex].
     * @param vertex Vertex.
     * @Return Coordinates or null if there are no coordinates for the given vertex.
     */
    fun coordinates(vertex: Vertex<T>): Coordinates?

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
    ) = coordinates(source)?.let {
        when (coordinates(destination)) {
            it.left() -> Direction.LEFT
            it.right() -> Direction.RIGHT
            it.down() -> Direction.DOWN
            it.up() -> Direction.UP
            it.leftUp() -> Direction.LEFT_UP
            it.rightUp() -> Direction.RIGHT_UP
            it.leftDown() -> Direction.LEFT_DOWN
            it.rightDown() -> Direction.RIGHT_DOWN
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
    ): Vertex<T>?

    /**
     * Gets each [Vertex] neighboring the given vertex in each cardinal [Direction].
     * @param vertex Vertex.
     * @param allowDiagonals True to include diagonally adjacent vertices.
     * @return Neighboring vertices.
     */
    fun neighbors(
        vertex: Vertex<T>,
        allowDiagonals: Boolean = false,
    ) = listOfNotNull(
        neighbor(vertex, Direction.LEFT),
        neighbor(vertex, Direction.UP),
        neighbor(vertex, Direction.DOWN),
        neighbor(vertex, Direction.RIGHT),
        if (allowDiagonals) neighbor(vertex, Direction.LEFT_UP) else null,
        if (allowDiagonals) neighbor(vertex, Direction.RIGHT_UP) else null,
        if (allowDiagonals) neighbor(vertex, Direction.RIGHT_DOWN) else null,
        if (allowDiagonals) neighbor(vertex, Direction.LEFT_DOWN) else null,
    )

    /**
     * Gets the row of [Vertex] for the given row index.
     * @param index Row index.
     * @return Row or null if there is no row for the given index.
     */
    fun row(index: Int): List<Vertex<T>>?

    /**
     * Gets each row of [Vertex] in this graph in index order.
     * Vertices in each row are in column index order.
     * @return Rows.
     */
    fun rows(): List<List<Vertex<T>>>

    /**
     * Gets the [Vertex] in this grid at the given [Coordinates].
     * @param coordinates Coordinates.
     * @return Vertex or null if there is no vertex at the given coordinates.
     */
    fun vertex(coordinates: Coordinates): Vertex<T>?

    /**
     * Builder for [ListGrid] instances.
     * @param rows Rows of the grid.
     */
    class Builder<T>(
        private val rows: MutableList<Row<T>> = mutableListOf(),
    ) {
        /**
         * Represents a single row of elements in a grid.
         * @param vertices Vertices.
         */
        class Row<T>(
            private val vertices: MutableList<T> = mutableListOf(),
        ) : Iterable<T> {
            override fun iterator() = vertices.iterator()

            /**
             * Gets the size of this row.
             * @return Size.
             */
            fun size() = vertices.size

            /**
             * Adds a vertex to the end of this row with the given value.
             * @param element Value.
             */
            fun vertex(element: T) = vertices.add(element)
        }

        /**
         * Instantiates a new [Grid] from this builder.
         * @return Grid.
         */
        fun build() = toMapGrid()

        /**
         * Type-safe builder for creating a [Row] of a grid.
         *
         * Example usage:
         * ```
         * row {
         *     vertex("a")
         *     vertex("b")
         *     vertex("c")
         * }
         * ```
         * @param init Function with receiver.
         * @return Row.
         */
        fun row(init: Row<T>.() -> Unit): Row<T> {
            val row = Row<T>()
            row.init()
            rows.add(row)
            return row
        }

        /**
         * Creates a [ListGrid] from this builder.
         * @return List grid.
         */
        internal fun toListGrid(): ListGrid<T> {
            val width = rows.firstOrNull()?.size() ?: 0
            val vertices =
                rows.mapIndexed { y, row ->
                    row.mapIndexed { x, element ->
                        Vertex(x.toLong() + (width * y).toLong(), element)
                    }
                }
            return ListGrid(vertices)
        }

        /**
         * Creates a [MapGrid] from this builder.
         * @return Map grid.
         */
        internal fun toMapGrid(): MapGrid<T> {
            val width = rows.firstOrNull()?.size() ?: 0
            val verticesWithCoordinates =
                rows.flatMapIndexed { y, row ->
                    row.mapIndexed { x, element ->
                        Pair(
                            Vertex(x.toLong() + (width * y).toLong(), element),
                            x.to(y),
                        )
                    }
                }
            return MapGrid(
                verticesWithCoordinates.associate { it },
                verticesWithCoordinates.associateBy({ it.second }, { it.first }),
            )
        }
    }
}

/**
 * Type-safe builder for creating a [Grid].
 *
 * Example usage:
 * ```
 * grid {
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
 * @return Grid.
 */
fun <T> grid(init: Grid.Builder<T>.() -> Unit): Grid<T> = mapGrid(init)
