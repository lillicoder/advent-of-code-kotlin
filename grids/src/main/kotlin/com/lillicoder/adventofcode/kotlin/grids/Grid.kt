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

import com.lillicoder.adventofcode.kotlin.io.Resources
import com.lillicoder.adventofcode.kotlin.io.splitMapNotEmpty
import com.lillicoder.adventofcode.kotlin.math.Direction
import kotlin.math.abs

/**
 * An arbitrary two-dimensional grid of [Node].
 */
data class Grid<T>(
    private val nodes: List<List<Node<T>>>,
    val width: Int = nodes[0].size,
    val height: Int = nodes.size,
) {
    companion object {
        /**
         * Creates a new [Grid] from the given list of strings. Each still
         * will be considered as a row and each character in each string
         * considered as a column.
         */
        fun create(raw: List<String>) = create(raw) { it.toString() }

        /**
         * Creates a new [Grid] from the given list of strings. Each string
         * will be considered as a row and each character in each string
         * considered as a column.
         * @param raw Raw values.
         * @return Grid.
         */
        fun <T> create(
            raw: List<String>,
            transform: (Char) -> T,
        ) = Grid(
            raw.mapIndexed { y, row ->
                row.mapIndexed { x, node ->
                    Node(x.toLong(), y.toLong(), transform(node))
                }
            },
        )

        /**
         * Creates a new [Grid] by reading the resource with the given filename.
         */
        fun read(filename: String) = read(filename) { it.toString() }

        /**
         * Creates a new [Grid] by reading the resource with the given filename.
         * @param filename Resource filename.
         * @return Grid.
         */
        fun <T> read(
            filename: String,
            transform: (Char) -> T,
        ) = readAll(
            filename = filename,
            transform = transform,
        ).first()

        /**
         * Creates one or more [Grid] by reading the resource with the given filename.
         * @param filename Resource filename.
         * @param separator Line separator.
         * @return Grids.
         */
        fun <T> readAll(
            filename: String,
            separator: String = System.lineSeparator(),
            transform: (Char) -> T,
        ): List<Grid<T>> {
            val raw = Resources.text(filename) ?: throw IllegalArgumentException("Could not read input from file.")
            return raw.splitMapNotEmpty("$separator$separator") { create(it.lines(), transform) }
        }
    }

    /**
     * Gets the [Node] adjacent to the given node in the given [Direction].
     * @param node Node.
     * @param direction Direction.
     * @return Adjacent node.
     */
    fun adjacent(
        node: Node<T>,
        direction: Direction,
    ) = adjacent(node) { _, dir -> dir == direction }.firstOrNull()

    /**
     * Gets the list of [Node] adjacent to the given node that satisfy the given predicate.
     * A node is considered adjacent if it is directly above, below, to the left of, or to the right of
     * the given node.
     * @param node Node.
     * @param predicate Predicate to check. Defaults to matching all directions.
     * @return Adjacent nodes.
     */
    fun adjacent(
        node: Node<T>,
        predicate: (Node<T>, Direction) -> Boolean = { _, _ -> true },
    ) = listOfNotNull(
        nodes.getOrNull(node.y.toInt())?.getOrNull(node.x.toInt() - 1)?.takeIf { predicate(it, Direction.LEFT) },
        nodes.getOrNull(node.y.toInt() - 1)?.getOrNull(node.x.toInt())?.takeIf { predicate(it, Direction.UP) },
        nodes.getOrNull(node.y.toInt() + 1)?.getOrNull(node.x.toInt())?.takeIf { predicate(it, Direction.DOWN) },
        nodes.getOrNull(node.y.toInt())?.getOrNull(node.x.toInt() + 1)?.takeIf { predicate(it, Direction.RIGHT) },
    )

    /**
     * Gets the column for this grid at the given index.
     * @param index Column index.
     * @return Column nodes.
     * @throws IndexOutOfBoundsException
     */
    fun column(index: Int) =
        nodes.map {
            it[index]
        }

    /**
     * Counts the number of nodes per row matching the given predicate.
     * @param predicate Predicate to check.
     * @return Number of nodes per row satisfying the predicate.
     */
    fun countNodesByRow(predicate: (Node<T>) -> Boolean) = nodes.map { it.count(predicate) }

    /**
     * Gets the Manhattan distance between the given starting and ending nodes.
     * @param start Start node.
     * @param end End node.
     * @return Manhattan distance.
     */
    fun distance(
        start: Node<T>,
        end: Node<T>,
    ) = abs(start.x - end.x) + abs(start.y - end.y)

    /**
     * Finds the first [Node] that satisfies the given predicate.
     * @param predicate Predicate to check.
     * @return Found node or null if no node satisfied the predicate.
     */
    fun find(predicate: (T) -> Boolean): Node<T>? {
        nodes.forEach {
            it.forEach { node ->
                if (predicate.invoke(node.value)) return node
            }
        }

        return null
    }

    /**
     * Gets the first [Node] for this grid. The first node is the one at position (0, 0).
     * @return First node.
     */
    fun first() = nodes.first().first()

    /**
     * Performs the given action on each node in this grid.
     * @param action Action to perform.
     */
    fun forEach(action: (Node<T>) -> Unit) =
        nodes.forEach {
            it.forEach { node ->
                action(node)
            }
        }

    /**
     * Performs the given action on each column in this grid.
     * @param action Action to perform.
     */
    fun forEachColumnIndexed(action: (Int, List<Node<T>>) -> Unit) {
        for (index in 0..<width) {
            action(index, column(index))
        }
    }

    /**
     * Performs the given action on each row in this grid.
     * @param action Action to perform.
     */
    fun forEachRowIndexed(action: (Int, List<Node<T>>) -> Unit) =
        nodes.forEachIndexed { index, node ->
            action(index, node)
        }

    /**
     * Filters all [Node] and returns a list of nodes matching the given predicate.
     * @param predicate Predicate to check.
     * @return Nodes matching predicate.
     */
    fun filter(predicate: (T) -> Boolean) =
        nodes.flatMap { row ->
            row.filter { predicate(it.value) }
        }

    /**
     * Gets the last [Node] for this grid. The last node is the one at
     * position ([width] - 1, [height] -1).
     * @return Last node.
     */
    fun last() = nodes.last().last()

    /**
     * Returns a [Grid] containing the results of applying the given transform function
     * to each node in this grid.
     * @param transform Transform to apply.
     * @return Mapped grid.
     */
    fun <R> map(transform: (Node<T>) -> Node<R>): Grid<R> {
        val mapped =
            nodes.map { row ->
                row.map {
                    transform(it)
                }
            }

        return Grid(mapped)
    }

    fun Iterator<T>.find(predicate: (T) -> Boolean): T? {
        for (element in this) if (predicate(element)) return element
        return null
    }

    /**
     * Returns a list containing the results of applying the given transform function
     * to each column of nodes in this grid.
     * @param transform Transform to apply.
     * @return Mapped columns.
     */
    fun <R> mapColumns(transform: (List<Node<T>>) -> R): List<R> {
        val transformed = mutableListOf<R>()
        forEachColumnIndexed { _, column ->
            transformed.add(transform(column))
        }
        return transformed
    }

    /**
     * Returns a list containing the results of applying the given transform function
     * to each row of nodes in this grid.
     * @param transform Transform to apply.
     * @return Mapped rows.
     */
    fun <R> mapRows(transform: (List<Node<T>>) -> R) = nodes.map(transform)

    /**
     * Gets the list of [Node] neighboring the given node. A node is considered a neighbor
     * if it is adjacent to or diagonally touching the given node.
     * @param node Node.
     * @return Neighboring nodes.
     */
    fun neighbors(node: Node<T>) =
        adjacent(node) +
            listOfNotNull(
                nodes.getOrNull(node.y.toInt() - 1)?.getOrNull(node.x.toInt() - 1),
                nodes.getOrNull(node.y.toInt() + 1)?.getOrNull(node.x.toInt() - 1),
                nodes.getOrNull(node.y.toInt() - 1)?.getOrNull(node.x.toInt() + 1),
                nodes.getOrNull(node.y.toInt() + 1)?.getOrNull(node.x.toInt() + 1),
            )

    /**
     * Gets the list of [Node] that form a path from this grid's first
     * node to the given node.
     * @param destination Destination value.
     * @param traversal [Traversal] to use for pathfinding.
     * @return Path or empty list if there is no path.
     */
    fun path(
        destination: Node<T>,
        traversal: Traversal<T> = BreadthFirstTraversal(this),
    ) = traversal.path(destination)

    /**
     * Gets the row for this grid at the given index.
     * @param index Row index.
     * @return Row nodes.
     * @throws IndexOutOfBoundsException
     */
    fun row(index: Int) = nodes[index]

    override fun toString() =
        nodes.joinToString("\n") {
            it.joinToString("") { node ->
                node.value.toString()
            }
        }
}
