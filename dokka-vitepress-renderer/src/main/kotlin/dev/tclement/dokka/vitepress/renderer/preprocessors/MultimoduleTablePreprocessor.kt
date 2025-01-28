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

import org.jetbrains.dokka.pages.*
import org.jetbrains.dokka.transformers.pages.PageTransformer

class MultimoduleTablePreprocessor : PageTransformer {
    override fun invoke(input: RootPageNode): RootPageNode {
        return input.transformContentPagesTree { contentPage ->
            contentPage.modified(content = contentPage.content.recursiveMapTransform<ContentTable, ContentNode> { node ->
                if (MultimoduleTable in node.style) {
                    val hasAnyDescription = node.children.any { row ->
                        row.children.size > 1 && row.children[1].children.isNotEmpty()
                    }
                    val newHeader = node.header.map { header ->
                        val firstChild = header.children.firstOrNull()
                        if (hasAnyDescription && header.children.size == 1 && firstChild is ContentText) {
                            header.copy(children = listOf(firstChild, firstChild.copy(text = "Description")))
                        } else {
                            header
                        }
                    }
                    val newChildren = node.children.map { row ->
                        if (!hasAnyDescription && row.children.size == 2) {
                            row.copy(children = row.children.dropLast(1))
                        } else {
                            row
                        }
                    }
                    node.copy(
                        header = newHeader,
                        children = newChildren
                    )
                } else {
                    node
                }
            })
        }
    }
}