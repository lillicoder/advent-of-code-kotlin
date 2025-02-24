/*
 * Copyright 2025 Scott Weeden-Moody
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

package com.lillicoder.adventofcode.kotlin.collections

/**
 * Splits this list into sub-lists of contiguous elements that
 * satisfy the given predicate.
 * @param predicate Predicate to split by.
 * @return Sub-lists.
 */
fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> {
    return foldIndexed(mutableListOf<MutableList<T>>()) { index, list, element ->
        when {
            predicate(element) -> if (index < size - 1 && !predicate(get(index + 1))) list.add(mutableListOf())
            list.isNotEmpty() -> list.last().add(element)
            else -> list.add(mutableListOf(element))
        }
        list
    }
}
