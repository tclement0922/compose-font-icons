/*
 * Copyright 2024 T. Clément (@tclement0922)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.tclement.dokka.vitepress.renderer

import org.jetbrains.dokka.pages.*

internal fun <T : ContentNode> T.filterRecursively(predicate: (ContentNode) -> Boolean): T {
    @Suppress("UNCHECKED_CAST")
    return when (this) {
        is ContentGroup -> copy(children = children.filter(predicate).map { it.filterRecursively(predicate) })
        is ContentHeader -> copy(children = children.filter(predicate).map { it.filterRecursively(predicate) })
        is ContentCodeBlock -> copy(children = children.filter(predicate).map { it.filterRecursively(predicate) })
        is ContentCodeInline -> copy(children = children.filter(predicate).map { it.filterRecursively(predicate) })
        is ContentTable -> copy(
            header = header.filter(predicate).map { it.filterRecursively(predicate) },
            children = children.filter(predicate).map { it.filterRecursively(predicate) })

        is ContentList -> copy(children = children.filter(predicate).map { it.filterRecursively(predicate) })
        is ContentDivergentGroup -> copy(children = children.filter(predicate).map { it.filterRecursively(predicate) })
        is ContentDivergentInstance -> copy(
            before = before?.filterRecursively(predicate),
            divergent = divergent.filterRecursively(predicate),
            after = after?.filterRecursively(predicate)
        )

        is PlatformHintedContent -> copy(inner = inner.filterRecursively(predicate))
        else -> this
    } as T
}