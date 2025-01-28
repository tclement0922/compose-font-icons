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

package dev.tclement.dokka.vitepress.multimodule.renderer

import dev.tclement.dokka.vitepress.renderer.VitePressRenderer
import org.jetbrains.dokka.allModulesPage.AllModulesPageGeneration
import org.jetbrains.dokka.pages.RootPageNode
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.plugability.plugin
import org.jetbrains.dokka.plugability.querySingle
import org.jetbrains.dokka.templates.TemplatingPlugin

class VitePressMultimoduleRenderer(private val context: DokkaContext) : VitePressRenderer(context) {
    private val allModulesContext by lazy {
        context.plugin<TemplatingPlugin>()
            .querySingle { submoduleTemplateProcessor }
            .process(context.configuration.modules)
            .let { AllModulesPageGeneration.DefaultAllModulesContext(it) }
    }

    private val modules by lazy {
        context.configuration.modules.filter { it.name in allModulesContext.nonEmptyModules }.sortedBy { it.name }
    }

    override fun StringBuilder.buildSidebarConfig(root: RootPageNode) {
        modules.forEach { module ->
            append("import ")
            append(module.name.camelCase)
            append(" from './")
            append(module.relativePathToOutputDirectory)
            append("/sidebar'\n")
        }

        append('\n')

        append("export default (prefix: string) => [\n")
        append("  {\n")
        append("    text: 'All modules',\n")
        append("    link: prefix\n")
        append("  },\n")
        modules.forEachIndexed { i, module ->
            append("  ...")
            append(module.name.camelCase)
            append("(prefix + '")
            append(module.relativePathToOutputDirectory)
            append("/')")
            if (i < modules.size - 1) {
                append(",\n")
            }
        }
        append("\n]\n")
    }
}

private val String.camelCase: String
    get() = replace("-([a-zA-Z])".toRegex()) { result ->
        result.groups[1]?.value?.uppercase() ?: result.value.replace("-", "")
    }
