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
 * Represents a method of traversal for a [Graph].
 */
interface Traversal<T> : Iterator<Vertex<T>> {
    /**
     * Gets the path from the first [Vertex]
     * to the given destination vertex.
     * @param destination Destination.
     * @return Path taken or an empty list if there is no path to the given vertex.
     */
    fun path(destination: Vertex<T>): List<Vertex<T>> {
        while (hasNext()) {
            val node = next()
            if (node == destination) {
                return pathTaken(destination)
            }
        }

        return emptyList()
    }

    /**
     * Gets the list of [Vertex] traversed to reach the given vetex.
     * @param destination Destination.
     * @return Path taken.
     */
    fun pathTaken(destination: Vertex<T>): List<Vertex<T>>
}

/**
 * Breadth-first [Traversal] of a given [Graph].
 * @param graph [Graph] to traverse.
 * @param root Starting node.
 */
class BreadthFirstTraversal<T>(
    private val graph: Graph<T>,
    private val root: Vertex<T> = graph.root(),
) : Traversal<T> {
    private val queue = ArrayDeque<Vertex<T>>().also { it.add(root) }
    private val visited = linkedMapOf(root to true)

    override fun hasNext() = queue.isNotEmpty()

    override fun next(): Vertex<T> {
        val next = queue.removeFirst()
        graph.neighbors(next).forEach {
            if (!visited.contains(it)) {
                visited[it] = true
                queue.add(it)
            }
        }

        return next
    }

    override fun pathTaken(destination: Vertex<T>) = visited.keys.toList()
}

/**
 * Depth-first [Traversal] of a given [Graph].
 * @param graph [Graph] to traverse.
 * @param root Starting node.
 */
class DepthFirstTraversal<T>(
    private val graph: Graph<T>,
    private val root: Vertex<T> = graph.root(),
) : Traversal<T> {
    private val stack = ArrayDeque(listOf(root))
    private val visited = linkedMapOf<Vertex<T>, Boolean>()

    override fun hasNext() = stack.isNotEmpty()

    override fun next(): Vertex<T> {
        val next = stack.removeFirst()
        if (!visited.contains(next)) {
            visited[next] = true
            graph.neighbors(next).forEach {
                if (!visited.contains(it) && !stack.contains(it)) {
                    stack.addFirst(it)
                }
            }
        }

        return next
    }

    override fun pathTaken(destination: Vertex<T>) = visited.keys.toList()
}

class DijkstraTraversal<T>(
    private val graph: Graph<T>,
    private val root: Vertex<T> = graph.root(),
) : Traversal<T> {
    private val distances = mutableMapOf(root to 0L).withDefault { Long.MAX_VALUE }
    private val predcessors = mutableMapOf<Vertex<T>, Vertex<T>?>(root to null)

    private val processed = mutableSetOf<Vertex<T>>()
    private val frontier = mutableSetOf(root)

    override fun hasNext() = frontier.isNotEmpty()

    override fun next(): Vertex<T> {
        val current =
            frontier.minBy {
                distances.getValue(it)
            }.also {
                frontier.remove(it)
                processed.add(it)
            }

        graph.neighbors(current).filterNot {
            processed.contains(it)
        }.forEach { neighbor ->
            val weight = graph.edge(current, neighbor)?.weight ?: 0L
            val candidate = distances.getValue(current) + weight
            if (candidate < distances.getValue(neighbor)) {
                // Better distance, update distance and path
                distances[neighbor] = candidate
                predcessors[neighbor] = current
            }

            // Mark as unprocessed
            frontier.add(neighbor)
        }

        return current
    }

    override fun pathTaken(destination: Vertex<T>): List<Vertex<T>> {
        val path = mutableListOf<Vertex<T>>()

        var previous = predcessors[destination]
        while (previous != null) {
            path.add(previous)
            previous = predcessors[previous]
        }

        return path
    }
}

/**
 * Insertion order [Traversal] of a given [Graph].
 * @param graph [Graph] to traverse.
 * @param root Starting node.
 */
class InsertOrderTraversal<T>(
    private val graph: Graph<T>,
    private val root: Vertex<T> = graph.root(),
) : Traversal<T> {
    private val queue = ArrayDeque<Vertex<T>>().also { it.add(root) }
    private val visited = linkedMapOf(root to true)

    override fun hasNext() = queue.isNotEmpty()

    override fun next(): Vertex<T> =
        queue.removeFirst().also { next ->
            visited[next] = true
            graph.next(next)?.let { queue.add(it) }
        }

    override fun pathTaken(destination: Vertex<T>) = visited.keys.toList()
}
