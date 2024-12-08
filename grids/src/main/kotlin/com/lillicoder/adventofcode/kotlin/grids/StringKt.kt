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

import com.lillicoder.adventofcode.kotlin.math.to

/**
 * Converts this string to a [Grid]. Each character in each line
 * is considered a vertex.
 */
fun <T> String.toGrid(transform: (Char) -> T): Grid<T> {
    val builder = Grid.Builder<T>()

    lines().forEachIndexed { y, row ->
        row.forEachIndexed { x, vertex ->
            builder.vertex(x.to(y), transform(vertex))
        }
    }

    return builder.build()
}
