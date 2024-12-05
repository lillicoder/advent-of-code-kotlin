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

package com.lillicoder.adventofcode.grids

/**
 * Represents a method of traversal for a [Grid].
 */
interface Traversal<T> : Iterator<Node<T>> {
    /**
     * Gets the path from the first [Node]
     * to the given destination node.
     * @param destination Destination node.
     * @return Path or an empty list if there is no path to the given node.
     */
    fun path(destination: Node<T>): List<Node<T>> {
        while (hasNext()) {
            val node = next()
            if (node == destination) {
                return visited(node)
            }
        }

        return emptyList()
    }

    // TODO This is clunky AF

    /**
     * Gets the list of [Node] visited by this traversal.
     * @param node Node whose traversed path we're interested in.
     * @return Nodes visited.
     */
    fun visited(node: Node<T>): List<Node<T>>
}

/**
 * Breadth-first [Traversal].
 * @param grid [Grid] to traverse.
 * @param start Starting [Node].
 */
class BreadthFirstTraversal<T>(
    private val grid: Grid<T>,
    private val start: Node<T> = grid.first(),
) : Traversal<T> {
    private val queue = ArrayDeque<Node<T>>().also { it.add(start) }
    private val visited = linkedMapOf(start to true)

    override fun hasNext() = !queue.isEmpty()

    override fun next(): Node<T> {
        val next = queue.removeFirst()
        grid.adjacent(next).forEach {
            if (!visited.contains(it)) {
                visited[it] = true
                queue.add(it)
            }
        }

        return next
    }

    override fun visited(node: Node<T>) = visited.keys.toList()
}

/**
 * Depth-first [Traversal].
 * @param grid [Grid] to traverse.
 * @param start Starting [Node].
 */
class DepthFirstTraversal<T>(
    private val grid: Grid<T>,
    private val start: Node<T> = grid.first(),
) : Traversal<T> {
    private val stack = ArrayDeque<Node<T>>().also { it.add(start) }
    private val visited = linkedMapOf<Node<T>, Boolean>()

    override fun hasNext() = !stack.isEmpty()

    override fun next(): Node<T> {
        val next = stack.removeFirst()
        if (!visited.contains(next)) {
            visited[next] = true
            grid.adjacent(next).forEach {
                if (!visited.contains(it) && !stack.contains(it)) {
                    stack.addFirst(it)
                }
            }
        }

        return next
    }

    override fun visited(node: Node<T>) = visited.keys.toList()
}

/**
 * Dijkstra's algorithm [Traversal].
 * @param grid [Grid] to traverse.
 * @param start Starting [Node].
 */
class DijkstraTraversal<T>(
    private val grid: Grid<T>,
    private val start: Node<T> = grid.first(),
    private val weight: (Node<T>, Node<T>) -> Int,
) : Traversal<T> {
    private val distances = mutableMapOf(start to 0).withDefault { Int.MAX_VALUE }
    private val paths = mutableMapOf<Node<T>, List<Node<T>>>().withDefault { listOf() }

    private val processed = mutableSetOf<Node<T>>()
    private val frontier = mutableSetOf(start)

    override fun hasNext() = frontier.isNotEmpty()

    override fun next(): Node<T> {
        // Get next smallest distance node
        val current =
            frontier.minBy {
                distances.getValue(it)
            }.also {
                frontier.remove(it)
                processed.add(it)
            }

        // Update any unprocessed neighbors and add them to the queue
        grid.adjacent(
            current,
        ).filterNot {
            processed.contains(it)
        }.forEach { adjacent ->
            val candidate = distances.getValue(current) + weight(current, adjacent)
            if (candidate < distances.getValue(adjacent)) {
                // Better distance, update distance and path
                distances[adjacent] = candidate
                paths.merge(adjacent, paths.getValue(current) + current, List<Node<T>>::plus)
            }

            // Mark as unprocessed
            frontier.add(adjacent)
        }

        return current
    }

    override fun visited(node: Node<T>) = paths.getValue(node)
}
