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

package dev.tclement.dokka.vitepress.renderer.preprocessors

import dev.tclement.dokka.vitepress.renderer.EmptyNode
import org.jetbrains.dokka.pages.*
import org.jetbrains.dokka.transformers.pages.PageTransformer

class DeprecationPreprocessor : PageTransformer {
    override fun invoke(input: RootPageNode): RootPageNode {
        return input.transformContentPagesTree { contentPage ->
            contentPage.modified(content = contentPage.content.recursiveMapTransform<ContentGroup, ContentNode> { node ->
                if (node.dci.kind == ContentKind.Deprecation) {
                    node.transformChildren {
                        if (it is ContentHeader) {
                            if (it.children.any { child -> child is ContentText && child.text == "Deprecated" })
                                EmptyNode()
                            else
                                ContentGroup(
                                    children = it.children.map { child ->
                                        if (child is ContentText) child.copy(style = child.style + setOf(TextStyle.Strong)) else child
                                    },
                                    dci = it.dci.copy(kind = ContentKind.Main),
                                    sourceSets = it.sourceSets,
                                    style = it.style + TextStyle.Block,
                                    extra = it.extra
                                )
                        } else {
                            it
                        }
                    }
                } else {
                    node
                }
            })
        }
    }
}