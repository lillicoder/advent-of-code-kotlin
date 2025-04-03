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

package com.lillicoder.adventofcode.kotlin.graphs.traversal

import com.lillicoder.adventofcode.kotlin.graphs.Graph
import com.lillicoder.adventofcode.kotlin.math.Vertex

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
        destination: Vertex<T>?,
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

        // Return empty list if an explicit destination was requested,
        // otherwise give back the vertices in visited order
        // TODO Dijkstra doesn't work this way; we have a list of predecessors
        // and best costs to reach a given vertex, not a discrete path (we flood fill)
        return when (destination == null) {
            true -> emptyList()
            else -> emptyList()
        }
    }

    /**
     * Builds the path taken to reach the given destination [Vertex].
     * @param destination Destination.
     * @return Path.
     */
    private fun unwind(destination: Vertex<T>): List<Vertex<T>> {
        val path = mutableListOf(destination)

        var previous = predecessors.getOrElse(destination) { null }
        while (previous != null) {
            path.add(previous)
            previous = predecessors[previous]
        }

        return path.reversed()
    }
}
