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

package com.lillicoder.adventofcode.kotlin.graphs

import com.lillicoder.adventofcode.kotlin.grids.Grid
import com.lillicoder.adventofcode.kotlin.math.Coordinates
import com.lillicoder.adventofcode.kotlin.math.Direction
import com.lillicoder.adventofcode.kotlin.math.Vertex

/**
 * [Graph] in which all vertices form a square lattice.
 * @param graph Internal [Graph] containing each [Vertex] and [Edge].
 * @param grid Internal [Grid] containing each vertex and [Coordinates].
 */
class SquareLatticeGraph<T>(
    private val graph: Graph<T>,
    private val grid: Grid<T>,
    val height: Int = grid.height,
    val width: Int = grid.width,
) : Graph<T> by graph {
    /**
     * Gets the column of [Vertex] for the given column index.
     * @param index Column index.
     * @return Column or null if there is no column for the given index.
     */
    fun column(index: Int) = grid.column(index)

    /**
     * Gets each column of [Vertex] in this graph in index order.
     * Vertices in each column are in row index order.
     * @return Rows.
     */
    fun columns() = grid.columns()

    /**
     * Gets the [Coordinates] for the given [Vertex].
     * @param vertex Vertex.
     * @Return Coordinates or null if there are no coordinates for the given vertex.
     */
    fun coordinates(vertex: Vertex<T>) = grid.coordinates(vertex)

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
    ) = grid.direction(source, destination)

    /**
     * Gets the [Vertex] neighboring the given vertex in the given direction.
     * @param vertex Vertex.
     * @param direction Direction.
     * @return Neighboring vertex or null if there is no neighbor in the given direction.
     */
    fun neighbor(
        vertex: Vertex<T>,
        direction: Direction,
    ) = grid.neighbor(vertex, direction)

    /**
     * Gets the row of [Vertex] for the given row index.
     * @param index Row index.
     * @return Row or null if there is no row for the given index.
     */
    fun row(index: Int) = grid.row(index)

    /**
     * Gets each row of [Vertex] in this graph in index order.
     * Vertices in each row are in column index order.
     * @return Rows.
     */
    fun rows() = grid.rows()

    override fun toString() = grid.toString()

    /**
     * [Graph.Builder] for [SquareLatticeGraph] instances.
     * @param builder [Grid.Builder] for building structure.
     */
    class Builder<T>(private val builder: Grid.Builder<T> = Grid.Builder()) : Graph.Builder<T>() {
        override fun build() = build(allowDiagonals = false)

        /**
         * Creates a new [SquareLatticeGraph] from this builder.
         * @param allowDiagonals True to connect diagonally adjacent vertices with edges.
         * @param weight Operation to determine edge weight, if any, when connecting two vertices.
         * @return Graph.
         */
        fun build(
            grid: Grid<T> = builder.build(),
            allowDiagonals: Boolean = false,
            weight: (Vertex<T>, Vertex<T>) -> Long = { _, _ -> 1L },
        ) = vertices.keys.forEach { vertex ->
            connectToNeighbors(
                vertex,
                grid,
                allowDiagonals,
                weight,
            )
        }.let {
            SquareLatticeGraph(
                AdjacencyListGraph(this),
                grid,
            )
        }

        /**
         * Adds a new [Vertex] to this graph with the given coordinates.
         * @param coordinates Coordinates.
         * @param element Value.
         * @return Builder.
         */
        fun vertex(
            coordinates: Coordinates,
            element: T,
        ) = apply {
            vertex(element) {
                builder.vertex(coordinates, it)
            }
        }

        /**
         * Connects the given [Vertex] to all of its neighboring vertices
         * with an [Edge].
         * @param vertex Vertex to connect.
         * @param allowDiagonals True to also connect diagonally adjacent vertices with edges.
         * @param weight Operation to determine edge weight, if any, when connecting two vertices.
         */
        private fun connectToNeighbors(
            vertex: Vertex<T>,
            grid: Grid<T>,
            allowDiagonals: Boolean,
            weight: (Vertex<T>, Vertex<T>) -> Long,
        ) {
            grid.coordinates(vertex)?.apply {
                listOfNotNull(
                    grid.neighbor(vertex, Direction.LEFT),
                    grid.neighbor(vertex, Direction.RIGHT),
                    grid.neighbor(vertex, Direction.UP),
                    grid.neighbor(vertex, Direction.DOWN),
                    if (allowDiagonals) grid.neighbor(vertex, Direction.LEFT_UP) else null,
                    if (allowDiagonals) grid.neighbor(vertex, Direction.RIGHT_UP) else null,
                    if (allowDiagonals) grid.neighbor(vertex, Direction.RIGHT_DOWN) else null,
                    if (allowDiagonals) grid.neighbor(vertex, Direction.LEFT_DOWN) else null,
                ).forEach {
                    edge(
                        source = vertex,
                        destination = it,
                        weight = weight(vertex, it),
                    )
                }
            }
        }
    }
}
