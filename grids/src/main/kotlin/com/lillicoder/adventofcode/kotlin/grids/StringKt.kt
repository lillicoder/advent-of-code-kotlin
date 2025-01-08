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

/**
 * Converts this string to a [Grid]. Each character in each line
 * is considered a vertex. Each character will be converted to a string.
 */
fun String.toGrid() = toGrid { it.toString() }

/**
 * Converts this string to a [Grid]. Each character in each line
 * is considered a vertex.
 */
fun <T> String.toGrid(transform: (Char) -> T) =
    grid {
        lines().forEach { line ->
            row {
                line.forEach { element ->
                    vertex(
                        transform(
                            element,
                        ),
                    )
                }
            }
        }
    }
