/*
 * Copyright 2024-2025 T. Clément (@tclement0922)
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

import org.jetbrains.dokka.model.DisplaySourceSet
import org.jetbrains.dokka.model.properties.PropertyContainer
import org.jetbrains.dokka.pages.ContentKind
import org.jetbrains.dokka.pages.ContentNode
import org.jetbrains.dokka.pages.DCI
import org.jetbrains.dokka.pages.Style

data class EmptyNode(
    override val dci: DCI = DCI(emptySet(), kind = ContentKind.Empty),
    override val extra: PropertyContainer<ContentNode> = PropertyContainer.empty(),
    override val sourceSets: Set<DisplaySourceSet> = emptySet(),
    override val style: Set<Style> = emptySet()
) : ContentNode {
    override fun hasAnyContent(): Boolean = false

    override fun withNewExtras(newExtras: PropertyContainer<ContentNode>): ContentNode = copy(extra = newExtras)

    override fun withSourceSets(sourceSets: Set<DisplaySourceSet>): ContentNode = copy(sourceSets = sourceSets)
}
