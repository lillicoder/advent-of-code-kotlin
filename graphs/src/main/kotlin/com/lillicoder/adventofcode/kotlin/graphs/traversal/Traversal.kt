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
