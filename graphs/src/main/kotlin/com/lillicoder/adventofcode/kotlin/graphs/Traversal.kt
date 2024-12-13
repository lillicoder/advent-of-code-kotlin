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

import com.lillicoder.adventofcode.kotlin.math.Vertex

/**
 * Represents a method of traversal for a [Graph].
 */
interface Traversal<T> {
    /**
     * Gets the path from the given start [Vertex]
     * to the given destination vertex.
     * @param destination Destination.
     * @return Path taken or an empty list if there is no path between the given vertices.
     */
    fun path(
        start: Vertex<T>,
        destination: Vertex<T>,
    ): List<Vertex<T>>
}

/**
 * Breadth-first [Traversal] of a given [Graph].
 * @param graph Graph to traverse.
 */
class BreadthFirstTraversal<T>(private val graph: Graph<T>) : Traversal<T> {
    private val queue = ArrayDeque<Vertex<T>>()
    private val visited = linkedMapOf<Vertex<T>, Boolean>()

    override fun path(
        start: Vertex<T>,
        destination: Vertex<T>,
    ): List<Vertex<T>> {
        queue.add(start)
        visited[start] = true

        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            if (next == destination) {
                visited[next] = true
                return visited.keys.toList()
            }

            graph.neighbors(next).forEach {
                if (!visited.contains(it)) {
                    visited[it] = true
                    queue.add(it)
                }
            }
        }

        return emptyList()
    }
}

/**
 * Depth-first [Traversal] of a given [Graph].
 * @param graph Graph to traverse.
 */
class DepthFirstTraversal<T>(private val graph: Graph<T>) : Traversal<T> {
    private val stack = ArrayDeque<Vertex<T>>()
    private val visited = linkedMapOf<Vertex<T>, Boolean>()

    override fun path(
        start: Vertex<T>,
        destination: Vertex<T>,
    ): List<Vertex<T>> {
        stack.add(start)

        while (stack.isNotEmpty()) {
            val next = stack.removeFirst()
            if (destination == next) {
                visited[next] = true
                return visited.keys.toList()
            }

            if (!visited.contains(next)) {
                visited[next] = true
                graph.neighbors(next).forEach {
                    if (!visited.contains(it) && !stack.contains(it)) {
                        stack.addFirst(it)
                    }
                }
            }
        }

        return emptyList()
    }
}

/**
 * Dijkstra [Traversal] of a given [Graph].
 * @param graph Graph to traverse.
 */
class DijkstraTraversal<T>(private val graph: Graph<T>) : Traversal<T> {
    private val distances = mutableMapOf<Vertex<T>, Long>().withDefault { Long.MAX_VALUE }
    private val predecessors = mutableMapOf<Vertex<T>, Vertex<T>>()

    private val processed = mutableSetOf<Vertex<T>>()
    private val frontier = mutableSetOf<Vertex<T>>()

    override fun path(
        start: Vertex<T>,
        destination: Vertex<T>,
    ): List<Vertex<T>> {
        distances[start] = 0L
        frontier.add(start)

        while (frontier.isNotEmpty()) {
            val current =
                frontier.minBy {
                    distances.getValue(it)
                }.also {
                    frontier.remove(it)
                    processed.add(it)
                }
            if (current == destination) return unwind(current)

            graph.neighbors(current).filterNot {
                processed.contains(it)
            }.forEach { neighbor ->
                val weight = graph.edge(current, neighbor)?.weight ?: 0L
                val candidate = distances.getValue(current) + weight
                if (candidate < distances.getValue(neighbor)) {
                    // Better distance, update distance and path
                    distances[neighbor] = candidate
                    predecessors[neighbor] = current
                }

                // Mark as unprocessed
                frontier.add(neighbor)
            }
        }

        return emptyList()
    }

    /**
     * Builds the path taken to reach the given destination [Vertex].
     * @param destination Destination.
     * @return Path.
     */
    private fun unwind(destination: Vertex<T>): List<Vertex<T>> {
        val path = mutableListOf<Vertex<T>>()

        var previous = predecessors.getOrElse(destination) { null }
        while (previous != null) {
            path.add(previous)
            previous = predecessors[previous]
        }

        return path.reversed()
    }
}
