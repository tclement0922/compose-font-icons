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

@file:OptIn(InternalDokkaApi::class)

package dev.tclement.dokka.vitepress.renderer

import dev.tclement.dokka.vitepress.DokkaVitePressRendererPlugin
import dev.tclement.dokka.vitepress.GfmCommand.Companion.templateCommand
import dev.tclement.dokka.vitepress.ResolveLinkGfmCommand
import dev.tclement.dokka.vitepress.renderer.extras.CodeMetaExtra
import dev.tclement.dokka.vitepress.renderer.extras.NoEscapeExtra
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.dokka.DokkaException
import org.jetbrains.dokka.InternalDokkaApi
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.renderers.OutputWriter
import org.jetbrains.dokka.base.renderers.isImage
import org.jetbrains.dokka.base.resolvers.local.LocationProvider
import org.jetbrains.dokka.model.*
import org.jetbrains.dokka.pages.*
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.plugability.plugin
import org.jetbrains.dokka.plugability.query
import org.jetbrains.dokka.plugability.querySingle
import org.jetbrains.dokka.renderers.Renderer
import org.jetbrains.dokka.transformers.pages.PageTransformer
import org.jetbrains.dokka.utilities.htmlEscape

open class VitePressRenderer(
    private val context: DokkaContext
) : Renderer {
    private val isPartial = context.configuration.delayTemplateSubstitution

    private val outputWriter: OutputWriter = context.plugin<DokkaBase>().querySingle { outputWriter }
    private val preprocessors: Iterable<PageTransformer> =
        context.plugin<DokkaVitePressRendererPlugin>().query { vitePressPreprocessors }

    protected lateinit var locationProvider: LocationProvider


    private fun StringBuilder.buildNewLine() {
        append("\n")
    }

    private fun StringBuilder.buildLineBreak() {
        append("\\")
        buildNewLine()
    }

    private fun StringBuilder.buildParagraph() {
        buildNewLine()
        buildNewLine()
    }

    private fun StringBuilder.buildHeader(level: Int, node: ContentHeader, content: StringBuilder.() -> Unit) {
        buildParagraph()
        append("#".repeat(kotlin.math.max(0, kotlin.math.min(6, level))))
        append(' ')
        content()
        buildParagraph()
    }

    private fun StringBuilder.buildLink(address: String, content: StringBuilder.() -> Unit) {
        append("[")
        content()
        append("](")
        append(address)
        append(")")
    }

    private fun StringBuilder.buildList(
        node: ContentList,
        pageContext: ContentPage,
        sourceSetRestriction: Set<DisplaySourceSet>? = null
    ) {
        node.children.forEach {
            append("- ")
            append(buildString { buildContentNode(it, pageContext, sourceSetRestriction) }.trimStart())
        }
    }

    private fun StringBuilder.buildResource(node: ContentEmbeddedResource, pageContext: ContentPage) {
        if (node.isImage()) {
            append('!')
        }
        append('[')
        append(node.altText)
        append("](")
        append(node.address)
        append(')')
    }

    private fun StringBuilder.buildTable(
        node: ContentTable,
        pageContext: ContentPage,
        sourceSetRestriction: Set<DisplaySourceSet>? = null
    ) {
        val size = node.header.firstOrNull()?.children?.size ?: node.children.firstOrNull()?.children?.size ?: 0
        if (size < 1) return
        append("<table>")
        buildNewLine()
        if (node.header.isNotEmpty()) {
            buildNewLine()
            append("<thead>")
            node.header.forEach { row ->
                append("<tr>")
                row.children.forEach { cell ->
                    append("<th>")
                    buildParagraph()
                    buildContentNode(cell, pageContext, sourceSetRestriction)
                    buildParagraph()
                    append("</th>")
                }
                append("</tr>")
            }
            append("</thead>")
            buildNewLine()
        }
        append("<tbody>")
        node.children.forEach { row ->
            buildNewLine()
            append("<tr>")
            row.children.forEach { cell ->
                append("<td>")
                buildParagraph()
                buildContentNode(cell, pageContext, sourceSetRestriction)
                buildParagraph()
                append("</td>")
            }
            append("</tr>")
            buildNewLine()
        }
        append("</tbody>")
        append("</table>")
        buildNewLine()
    }

    private val ContentText.decorators
        get() = buildString {
            style.forEach {
                when (it) {
                    TextStyle.Bold -> append("**")
                    TextStyle.Italic -> append("*")
                    TextStyle.Strong -> append("**")
                    TextStyle.Strikethrough -> append("~~")
                    else -> Unit
                }
            }
        }

    private fun StringBuilder.buildText(textNode: ContentText) {
        if (textNode.extra[HtmlContent] != null) {
            append(textNode.text)
        } else {
            val decorators = textNode.decorators
            append(textNode.text.takeWhile { it == ' ' })
            append(decorators)
            append(textNode.text.trim().let {
                if (textNode.extra[NoEscapeExtra.Key]?.value == true) it else it.htmlEscape()
            })
            append(decorators.reversed())
            append(textNode.text.takeLastWhile { it == ' ' })
        }
    }

    private fun StringBuilder.buildFrontmatterHeader(page: PageNode) {
        append("---")
        buildNewLine()
        append("title: ")
        append(page.name)
        buildNewLine()
        append("---")
        buildParagraph()
    }

    private fun StringBuilder.buildSourceSetsSelector(page: ContentPage) {
        if (!page.filterable) return
        buildNewLine()
        append("<script setup>")
        buildNewLine()
        append("import { useSessionStorage } from \"@vueuse/core\";")
        buildNewLine()
        append("const hiddenSourceSets = useSessionStorage(\"hiddenSourceSets\", []);")
        buildNewLine()
        append("</script>")
        buildNewLine()
        append("::: info SourceSets")
        buildParagraph()
        append("<ul class=\"source-sets-selector\">")
        buildNewLine()
        page.content.sourceSets.sortedWith(DisplaySourceSetComparator).forEach {
            append("  <li>")
            buildNewLine()
            append("    <label :class=\"{ 'unchecked': hiddenSourceSets.includes('${it.sourceSetIDs.merged.sourceSetName}') }\">")
            buildNewLine()
            append("      ${it.name}")
            buildNewLine()
            append("      <input type=\"checkbox\" id=\"${it.sourceSetIDs.merged.sourceSetName}\" value=\"${it.sourceSetIDs.merged.sourceSetName}\" v-model=\"hiddenSourceSets\" />")
            buildNewLine()
            append("    </label>")
            buildNewLine()
            append("  </li>")
            buildNewLine()
        }
        append("</ul>")
        buildParagraph()
        append(":::")
        buildParagraph()
    }

    private fun buildPage(page: ContentPage, content: (StringBuilder, ContentPage) -> Unit): String = buildString {
        content(this, page)
    }.trim().replace("\n\n+".toRegex(), "\n\n")

    private fun StringBuilder.buildError(node: ContentNode) {
        append("<!-- ")
        append("Unsupported content node: ")
        append(node::class.simpleName)
        append(" -->")
    }

    private fun StringBuilder.buildPlatformDependent(
        content: PlatformHintedContent,
        pageContext: ContentPage,
        sourceSetRestriction: Set<DisplaySourceSet>?
    ) {
        buildPlatformDependent(
            content.sourceSets.filter {
                sourceSetRestriction == null || it in sourceSetRestriction
            }.associateWith { setOf(content.inner) },
            pageContext
        )
    }

    private fun StringBuilder.buildPlatformDependent(
        content: Map<DisplaySourceSet, Collection<ContentNode>>,
        pageContext: ContentPage
    ) {
        content.toSortedMap(DisplaySourceSetComparator).toList().forEachIndexed { index, (sourceSet, inner) ->
            if (index > 0) {
                buildParagraph()
                append("---")
            }
            buildParagraph()
            append("<span class=\"source-set-badge\"")
            if (pageContext.filterable)
                append(" :class=\"{ 'disabled': hiddenSourceSets.includes('${sourceSet.sourceSetIDs.merged.sourceSetName}') }\"")
            append(">")
            append(sourceSet.name)
            append("</span>")
            if (pageContext.filterable) {
                buildNewLine()
                append("<div v-show=\"!hiddenSourceSets.includes('${sourceSet.sourceSetIDs.merged.sourceSetName}')\">")
                buildNewLine()
            }
            inner.forEach {
                it.filterRecursively { node -> node.sourceSets.isEmpty() || sourceSet in node.sourceSets }
                    .build(this, pageContext)
            }
            if (pageContext.filterable) {
                buildNewLine()
                append("</div>")
                buildNewLine()
            }
        }
    }

    private fun StringBuilder.buildGroup(
        node: ContentGroup,
        pageContext: ContentPage,
        sourceSetRestriction: Set<DisplaySourceSet>? = null
    ) {
        wrapGroup(node, pageContext) { node.children.forEach { it.build(this, pageContext, sourceSetRestriction) } }
    }

    private fun StringBuilder.buildDivergent(node: ContentDivergentGroup, pageContext: ContentPage) {
        if (node.implicitlySourceSetHinted) {
            buildPlatformDependent(
                node.children.flatMap { instance -> instance.sourceSets.map { set -> set to instance } }
                    .groupBy(
                        Pair<DisplaySourceSet, ContentDivergentInstance>::first,
                        Pair<DisplaySourceSet, ContentDivergentInstance>::second
                    )
                    .mapValues { (sourceSet, instances) ->
                        instances.flatMap { instance ->
                            listOfNotNull(
                                instance.before,
                                instance.divergent.filterRecursively { node -> node.sourceSets.isEmpty() || sourceSet in node.sourceSets },
                                instance.after
                            )
                        }
                    },
                pageContext
            )
        } else {
            node.children.forEach { it.build(this, pageContext) }
        }
    }

    private fun StringBuilder.wrapGroup(
        node: ContentGroup,
        pageContext: ContentPage,
        childrenCallback: StringBuilder.() -> Unit
    ) {
        if (node.dci.kind === ContentKind.Deprecation) {
            buildNewLine()
            append("::: warning Deprecated")
            buildNewLine()
            childrenCallback()
            buildNewLine()
            append(":::")
            buildNewLine()
        } else if (node.hasStyle(TextStyle.Block) || node.hasStyle(TextStyle.Paragraph)) {
            buildParagraph()
            childrenCallback()
            buildParagraph()
        } else {
            childrenCallback()
        }
    }

    private fun StringBuilder.buildText(
        nodes: List<ContentNode>,
        pageContext: ContentPage,
        sourceSetRestriction: Set<DisplaySourceSet>? = null
    ) {
        nodes.forEach { it.build(this, pageContext, sourceSetRestriction) }
    }

    private fun StringBuilder.buildCodeBlock(code: ContentCodeBlock, pageContext: ContentPage) {
        buildParagraph()
        append("```")
        append(code.language.ifBlank { "kotlin" })
        val meta = code.extra[CodeMetaExtra.Key]
        if (meta != null) {
            append(' ')
            append(meta.value)
        }
        buildNewLine()
        code.children.forEach {
            if (it is ContentText) append(it.text) else if (it is ContentBreakLine) buildNewLine()
        }
        buildNewLine()
        append("```")
        buildParagraph()
    }

    private fun StringBuilder.buildCodeInline(code: ContentCodeInline, pageContext: ContentPage) {
        append('`')
        code.children.forEach { if (it is ContentText) append(it.text) else buildContentNode(it, pageContext) }
        append('`')
    }

    private fun StringBuilder.buildHeader(
        node: ContentHeader,
        pageContext: ContentPage,
        sourceSetRestriction: Set<DisplaySourceSet>? = null
    ) {
        buildHeader(node.level, node) { node.children.forEach { it.build(this, pageContext, sourceSetRestriction) } }
    }

    private fun ContentNode.build(
        builder: StringBuilder,
        pageContext: ContentPage,
        sourceSetRestriction: Set<DisplaySourceSet>? = null
    ) {
        builder.buildContentNode(this, pageContext, sourceSetRestriction)
    }

    private fun StringBuilder.buildContentNode(
        node: ContentNode,
        pageContext: ContentPage,
        sourceSetRestriction: Set<DisplaySourceSet>? = null
    ) {
        if (sourceSetRestriction.isNullOrEmpty() || node.sourceSets.any { it in sourceSetRestriction }) {
            when (node) {
                is ContentText -> buildText(node)
                is ContentHeader -> buildHeader(node, pageContext, sourceSetRestriction)
                is ContentCodeBlock -> buildCodeBlock(node, pageContext)
                is ContentCodeInline -> buildCodeInline(node, pageContext)
                is ContentDRILink -> buildDRILink(node, pageContext, sourceSetRestriction)
                is ContentResolvedLink -> buildResolvedLink(node, pageContext, sourceSetRestriction)
                is ContentEmbeddedResource -> buildResource(node, pageContext)
                is ContentList -> buildList(node, pageContext, sourceSetRestriction)
                is ContentTable -> buildTable(node, pageContext, sourceSetRestriction)
                is ContentGroup -> buildGroup(node, pageContext, sourceSetRestriction)
                is ContentBreakLine -> buildLineBreak()
                is PlatformHintedContent -> buildPlatformDependent(node, pageContext, sourceSetRestriction)
                is ContentDivergentGroup -> buildDivergent(node, pageContext)
                is ContentDivergentInstance -> buildDivergentInstance(node, pageContext)
                is EmptyNode -> Unit
                else -> buildError(node)
            }
        }
    }

    private fun StringBuilder.buildDRILink(
        node: ContentDRILink,
        pageContext: ContentPage,
        sourceSetRestriction: Set<DisplaySourceSet>?
    ) {
        val location = locationProvider.resolve(node.address, node.sourceSets, pageContext)
        if (location == null) {
            if (isPartial) {
                templateCommand(ResolveLinkGfmCommand(node.address)) {
                    buildText(node.children, pageContext, sourceSetRestriction)
                }
            } else {
                buildText(node.children, pageContext, sourceSetRestriction)
            }
        } else {
            buildLink(location) {
                buildText(node.children, pageContext, sourceSetRestriction)
            }
        }
    }

    private fun StringBuilder.buildResolvedLink(
        node: ContentResolvedLink,
        pageContext: ContentPage,
        sourceSetRestriction: Set<DisplaySourceSet>?
    ) {
        buildLink(node.address) {
            buildText(node.children, pageContext, sourceSetRestriction)
        }
    }

    private fun StringBuilder.buildDivergentInstance(node: ContentDivergentInstance, pageContext: ContentPage) {
        node.before?.build(this, pageContext)
        node.divergent.build(this, pageContext)
        node.after?.build(this, pageContext)
    }

    private fun buildPageContent(context: StringBuilder, page: ContentPage) {
        context.buildFrontmatterHeader(page)
        context.buildSourceSetsSelector(page)
        page.content.build(context, page)
    }

    private suspend fun renderPage(page: PageNode) {
        val path by lazy {
            locationProvider.resolve(page, skipExtension = true)
                ?: throw DokkaException("Cannot resolve path for ${page.name}")
        }
        when (page) {
            is ContentPage -> outputWriter.write(path, buildPage(page) { c, p -> buildPageContent(c, p) }, ".md")
            is RendererSpecificPage -> when (val strategy = page.strategy) {
                is RenderingStrategy.Copy -> outputWriter.writeResources(strategy.from, path)
                is RenderingStrategy.Write -> outputWriter.write(path, strategy.text, "")
                is RenderingStrategy.Callback -> outputWriter.write(path, strategy.instructions(this, page), ".md")
                is RenderingStrategy.DriLocationResolvableWrite -> outputWriter.write(
                    path,
                    strategy.contentToResolve { dri, sourcesets ->
                        locationProvider.resolve(dri, sourcesets)
                    },
                    ""
                )

                is RenderingStrategy.PageLocationResolvableWrite -> outputWriter.write(
                    path,
                    strategy.contentToResolve { pageToLocate, context ->
                        locationProvider.resolve(pageToLocate, context)
                    },
                    ""
                )

                RenderingStrategy.DoNothing -> Unit
            }

            else -> throw AssertionError(
                "Page ${page.name} cannot be rendered by renderer as it is not renderer specific nor contains content"
            )
        }
    }

    private suspend fun renderPages(root: PageNode) {
        coroutineScope {
            renderPage(root)

            root.children.forEach {
                launch { renderPages(it) }
            }
        }
    }

    override fun render(root: RootPageNode) {
        val newRoot = preprocessors.fold(root) { acc, t -> t(acc) }

        locationProvider =
            context.plugin<DokkaBase>().querySingle { locationProviderFactory }.getLocationProvider(newRoot)

        val sidebarConfig = buildString {
            buildSidebarConfig(newRoot)
        }

        runBlocking(Dispatchers.Default) {
            renderPages(newRoot)
            outputWriter.write("sidebar", sidebarConfig, ".ts")
        }
    }

    protected open fun StringBuilder.buildSidebarConfig(root: RootPageNode) {
        append("export default (prefix: string) => [\n")
        root.children.forEachIndexed { i, child ->
            buildSidebarConfigItem(child)
        }
        append(']')
    }

    private fun StringBuilder.buildSidebarConfigItem(page: PageNode, indent: Int = 1) {
        if (page is RendererSpecificResourcePage && page.name == "package-list") return

        fun indent() = append("  ".repeat(indent))

        val location = try {
            locationProvider.resolve(page, skipExtension = true)
        } catch (_: AssertionError) {
            null
        }

        indent()
        append("{\n")
        indent()
        append("  text: \"<span class=\\\"nav-api-item ")
        when (page) {
            is ModulePageNode -> append("module")
            is PackagePageNode -> append("package")
            is ClasslikePageNode -> {
                append("class-like")

                when {
                    page.documentables.allAre<DClass>() -> append(" class")
                    page.documentables.allAre<DEnum>() -> append(" enum")
                    page.documentables.allAre<DInterface>() -> append(" interface")
                    page.documentables.allAre<DObject>() -> append(" object")
                    page.documentables.allAre<DAnnotation>() -> append(" annotation")
                    page.documentables.allAre<DTypeAlias>() -> append(" type-alias")
                }
            }

            is MemberPageNode -> {
                append("member")
                when {
                    page.documentables.all { it is DFunction } -> {
                        append(" function")
                        if (page.documentables.all { it.dri.classNames != null }) append(" method") else append(" top-level")
                        if (page.documentables.allAs<DFunction>().all { it.isConstructor }) append(" constructor")
                        if (page.documentables.all { it.isExtension() }) append(" extension")
                    }

                    page.documentables.all { it is DProperty } -> {
                        append(" property")
                        if (page.documentables.all { it.dri.classNames == null }) append(" top-level")
                        if (page.documentables.all { it.isExtension() }) append(" extension")
                    }

                    page.documentables.allAre<DEnumEntry>() -> append(" enum-entry")

                    page.documentables.allAre<DParameter>() -> append(" parameter")

                    page.documentables.allAre<DTypeParameter>() -> append(" type-parameter")
                }
            }

            is RendererSpecificResourcePage -> append("resource")
            else -> append("unknown")
        }

        if (page is WithDocumentables && page.documentables.allAre<WithAbstraction>()) {
            var modifier: String? = null
            for (withAbstraction in page.documentables.allAs<WithAbstraction>())
                for (sourceSetModifier in withAbstraction.modifier.values) {
                    if (modifier == null) {
                        modifier = sourceSetModifier.name
                    } else if (modifier != sourceSetModifier.name) {
                        modifier = "mixed"
                        break
                    }
                }
            if (!modifier.isNullOrEmpty()) {
                append(' ')
                append(modifier)
            }
        }

        append("\\\">")
        if (
            page is WithDocumentables
            && page.documentables.allAre<DFunction>()
            && page.documentables.allAs<DFunction>().all { it.isConstructor }
        ) {
            if (page.documentables.size == 1) {
                append("constructor")
            } else {
                append("constructors")
            }
        } else {
            append(page.name)
        }
        append("</span>\",\n")
        indent()
        if (page.children.isNotEmpty()) {
            append("  collapsed: ")
            append(if (page is ModulePageNode) "true" else "false")
            append(",\n")
            indent()
            append("  items: [\n")
            page.children.forEach {
                buildSidebarConfigItem(it, indent + 2)
            }
            indent()
            append("  ],\n")
            indent()
        }

        if (location != null) {
            append("  link: prefix")
            if (location != "index") {
                append(" + '")
                if (location.endsWith("/index")) {
                    append(location.dropLast(5))
                } else {
                    append(location)
                }
                append('\'')
            }
            append('\n')
        }

        indent()
        append("},\n")
    }
}

private inline fun <reified T> List<Documentable>.allAre(): Boolean {
    return all { it is T }
}

private inline fun <reified T> List<Documentable>.allAs(): List<T> {
    @Suppress("UNCHECKED_CAST")
    return if (allAre<T>()) this as List<T> else throw ClassCastException("Every element is not of type ${T::class.simpleName}")
}

private object DisplaySourceSetComparator : Comparator<DisplaySourceSet> {
    private val DisplaySourceSet.comparableKey
        get() = sourceSetIDs.merged.let { it.scopeId + it.sourceSetName }

    override fun compare(o1: DisplaySourceSet, o2: DisplaySourceSet): Int {
        return o1.comparableKey.compareTo(o2.comparableKey)
    }
}

private val PageNode.filterable get() = this !is RootPageNode && this !is PackagePageNode
