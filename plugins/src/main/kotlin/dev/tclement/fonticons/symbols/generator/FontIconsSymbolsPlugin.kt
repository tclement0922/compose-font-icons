/*
 * Copyright 2023 T. Clément (@tclement0922)
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

package dev.tclement.fonticons.symbols.generator

import de.undercouch.gradle.tasks.download.Download
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.filter
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import java.io.FilterReader

class FontIconsSymbolsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.create("downloadFiles", Download::class.java) {
                val fontNamePrefix = "MaterialSymbols"
                val urlPrefix =
                    "https://github.com/google/material-design-icons/raw/master/variablefont/$fontNamePrefix"
                val fontNameSuffix = "%5BFILL%2CGRAD%2Copsz%2Cwght%5D"
                val fonts = arrayOf(
                    "outlined", "rounded", "sharp"
                )

                src(fonts.flatMap { font ->
                    listOf(
                        "$urlPrefix${font.uppercaseFirstChar()}$fontNameSuffix.ttf",
                        "$urlPrefix${font.uppercaseFirstChar()}$fontNameSuffix.codepoints"
                    )
                })
                dest(layout.buildDirectory)
                overwrite(false)

                doLast {
                    for (font in fonts) {
                        copy {
                            from(layout.buildDirectory.file("$fontNamePrefix${font.uppercaseFirstChar()}$fontNameSuffix.ttf"))
                            into(layout.projectDirectory.file("$font/src/desktopMain/resources/font"))
                            rename { "material_symbols_$font.ttf" }
                        }
                        copy {
                            from(layout.buildDirectory.file("$fontNamePrefix${font.uppercaseFirstChar()}$fontNameSuffix.ttf"))
                            into(layout.projectDirectory.file("$font/src/androidMain/res/font"))
                            rename { "material_symbols_$font.ttf" }
                        }
                    }
                }
            }

            tasks.create("createSymbolsFile", FontIconsSymbolsTask::class.java) {
                dependsOn(tasks.getByName("downloadFiles"))
            }
        }
    }
}

interface FontIconsSymbolsPluginConfigScope {
    var prebuildTask: TaskProvider<DefaultTask>?
}

private class FontIconsSymbolsPluginConfigScopeImpl : FontIconsSymbolsPluginConfigScope {
    override var prebuildTask: TaskProvider<DefaultTask>? = null
        set(value) {
            field = value
            value?.invoke {
                dependsOn(project.tasks.getByName("createSymbolsFile"))
            }
        }
}

fun fontIconsSymbolsPluginConfig(config: FontIconsSymbolsPluginConfigScope.() -> Unit) {
    config(FontIconsSymbolsPluginConfigScopeImpl())
}
