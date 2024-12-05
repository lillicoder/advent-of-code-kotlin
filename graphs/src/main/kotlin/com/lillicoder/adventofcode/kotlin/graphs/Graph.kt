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

/**
 * [Graph](https://en.wikipedia.org/wiki/Graph_(abstract_data_type)) interface.
 */
interface Graph<T> : Iterable<Vertex<T>> {
    override fun iterator() = InsertOrderTraversal(this)

    /**
     * Determines if there is an edge between the two given [Vertex].
     * @param first First vertex.
     * @param second Second vertex.
     * @return True if there is an edge between the vertices, false otherwise.
     */
    fun adjacent(
        first: Vertex<T>,
        second: Vertex<T>,
    ): Boolean

    /**
     * Gets the [Edge] connecting the two given [Vertex].
     * @param from Source vertex.
     * @param to Destination vertex.
     * @return Edge or null if there is no edge connecting the given vertices.
     */
    fun edge(
        from: Vertex<T>,
        to: Vertex<T>,
    ): Edge<T>?

    /**
     * Gets all neighbors of the given [Vertex]. A vertex is considered
     * a neighbor if there is an edge to it from the given vertex.
     * @param vertex Vertex.
     * @return Neighbors.
     */
    fun neighbors(vertex: Vertex<T>): Set<Vertex<T>>

    /**
     * Gets the [Vertex] that was inserted after the given vertex.
     * @param vertex Vertex.
     * @return Next vertex or null if the given vertex was the last one inserted.
     */
    fun next(vertex: Vertex<T>): Vertex<T>?

    /**
     * Gets the path from the given start [Vertex] to the given
     * destination [Vertex] using the given [Traversal].
     * @param start Starting vertex.
     * @param destination Destination vertex.
     * @param traversal Traversal to use.
     * @return Path from start to destination or an empty list if there is no such path.
     */
    fun path(
        start: Vertex<T>,
        destination: Vertex<T>,
        traversal: Traversal<T> = BreadthFirstTraversal(this, start),
    ) = traversal.path(destination)

    /**
     * Gets the first [Vertex] added to this graph.
     * @return First vertex.
     */
    fun root(): Vertex<T>

    /**
     * Gets the number of vertices in this graph.
     */
    fun size(): Int

    /**
     * Gets the [Vertex] from this graph with the given ID.
     * @param id Vertex ID.
     * @return Vertex or null if there is no matching vertex.
     */
    fun vertex(id: Long): Vertex<T>?

    /**
     * Base class for [Graph] builders.
     * @param vertices Map of each [Vertex] to its [Edge]s.
     * @param edges Set of all edges.
     */
    abstract class Builder<T>(
        internal val vertices: MutableMap<Vertex<T>, MutableSet<Edge<T>>> = mutableMapOf(),
        internal val edges: MutableSet<Edge<T>> = mutableSetOf(),
    ) {
        /**
         * Creates a new [Graph] from this builder.
         * @return Graph.
         */
        abstract fun build(): Graph<T>

        /**
         * Adds an edge connecting the two given [Vertex].
         * @param source First vertex.
         * @param destination Second vertex.
         * @param isDirected True if this edge is directed from source to destination.
         * @param weight Edge weight.
         * @return Builder.
         */
        fun edge(
            source: Vertex<T>,
            destination: Vertex<T>,
            isDirected: Boolean = false,
            weight: Long = 1L,
        ) = apply {
            Edge(
                source,
                destination,
                isDirected,
                weight,
            ).also {
                vertices[source]?.add(it)
                vertices[destination]?.add(it)
                edges.add(it)
            }
        }

        /**
         * Adds a new [Vertex] to this graph.
         * @param element Vertex value.
         * @param block Optional block to call after adding the vertex.
         * @return Builder.
         */
        fun vertex(
            element: T,
            block: (Vertex<T>) -> Unit = {},
        ) = apply {
            Vertex(
                vertices.size.toLong(),
                element,
            ).also {
                vertices[it] = mutableSetOf()
                block(it)
            }
        }
    }
}
