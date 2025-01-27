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

package dev.tclement.dokka.vitepress.renderer.preprocessors

import dev.tclement.dokka.vitepress.renderer.extras.CodeMetaExtra
import dev.tclement.dokka.vitepress.renderer.filterRecursively
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.resolvers.local.LocationProvider
import org.jetbrains.dokka.pages.*
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.plugability.plugin
import org.jetbrains.dokka.plugability.querySingle
import org.jetbrains.dokka.transformers.pages.PageTransformer

class SymbolPreprocessor(private val context: DokkaContext) : PageTransformer {
    private lateinit var locationProvider: LocationProvider

    override fun invoke(input: RootPageNode): RootPageNode {
        locationProvider =
            context.plugin<DokkaBase>().querySingle { locationProviderFactory }.getLocationProvider(input)
        return input.transformContentPagesTree { contentPage ->
            contentPage.modified(content = contentPage.content.recursiveMapTransform<ContentGroup, ContentNode> { node ->
                if (node.dci.kind == ContentKind.Symbol) {
                    val links = mutableMapOf<Pair<Int, Int>, String>()
                    var text = ""
                    var offset = 0

                    node
                        .filterRecursively { child ->
                            // filter out duplicated annotations for expect/actual declarations
                            child.sourceSets.isEmpty() || node.sourceSets.isEmpty() || node.sourceSets.all { it in child.sourceSets }
                        }
                        .children
                        .forEach {
                            val result = it.toRawText(contentPage, links, offset)
                            offset = result.first
                            text += result.second
                        }

                    val codeNode = ContentCodeBlock(
                        children = listOf(ContentText(text, DCI(emptySet(), kind = ContentKind.Main), emptySet())),
                        "kotlin",
                        node.dci,
                        node.sourceSets,
                        node.style,
                        node.extra + CodeMetaExtra("symbol " + links.entries.joinToString(" ") {
                            "\"${it.key.first},${it.key.second}\"=\"${it.value.replace("\"", "\\\"")}\""
                        })
                    )
                    node.copy(children = listOf(codeNode))
                } else {
                    node
                }
            })
        }
    }

    private fun ContentNode.toRawText(
        pageContext: ContentPage,
        links: MutableMap<Pair<Int, Int>, String>,
        offset: Int = 0
    ): Pair<Int, String> {
        val isBlock = this is ContentGroup
                && hasStyle(TextStyle.Block)
                && !(children.lastOrNull() is ContentGroup && children.last().hasStyle(TextStyle.Block))
        var mOffset = offset
        var mText = ""

        when (this) {
            is ContentText -> {
                mOffset += text.length
                mText += text
            }

            else -> {
                for (child in children) {
                    val childResult = child.toRawText(pageContext, links, mOffset)
                    mOffset = childResult.first
                    mText += childResult.second
                }
                if (this is ContentDRILink) {
                    val location = locationProvider.resolve(address, sourceSets, pageContext)
                    if (location != null)
                        links[offset to mText.length] =
                            if (location.endsWith("/index")) location.dropLast(6) else location
                } else if (this is ContentResolvedLink) {
                    links[offset to mText.length] = address
                }
            }
        }
        if (isBlock) {
            mOffset++
            mText += "\n"
        }
        return mOffset to mText
    }
}