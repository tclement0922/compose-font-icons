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

package dev.tclement.fonticons.symbols.generator

import com.google.common.base.CaseFormat
import com.squareup.kotlinpoet.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.TaskAction

@CacheableTask
open class FontIconsSymbolsTask : DefaultTask() {
    companion object {
        private const val TARGET_PACKAGE = "dev.tclement.fonticons.symbols"
        private const val TARGET_FILE = "MaterialSymbolsChars"
        private const val RECEIVER_PACKAGE = TARGET_PACKAGE
        private const val RECEIVER_CLASS = "MaterialSymbols"
    }

    @TaskAction
    fun run() {
        val codepointsFiles = project.layout.buildDirectory.let {
            listOf(
                it.file("MaterialSymbolsOutlined%5BFILL%2CGRAD%2Copsz%2Cwght%5D.codepoints"),
                it.file("MaterialSymbolsRounded%5BFILL%2CGRAD%2Copsz%2Cwght%5D.codepoints"),
                it.file("MaterialSymbolsSharp%5BFILL%2CGRAD%2Copsz%2Cwght%5D.codepoints"),
            )
        }

        if (!codepointsFiles.all { it.isPresent }
            || !codepointsFiles[0].get().asFile.readBytes()
                .contentEquals(codepointsFiles[1].get().asFile.readBytes())
            || !codepointsFiles[1].get().asFile.readBytes()
                .contentEquals(codepointsFiles[2].get().asFile.readBytes())) {
            error("Some codepoints files are not available or some of their content are not the same")
        }

        val codepoints = codepointsFiles[0].get().asFile.readLines().map {
            val split = it.split(' ')
            split[0] to split[1]
        }

        project.layout.buildDirectory.file("symbols.txt")
            .get().asFile.writeText(codepoints.joinToString("\n") { it.first })

        val fileSpecBuilder = FileSpec.builder(TARGET_PACKAGE, TARGET_FILE)
        fileSpecBuilder.addAnnotation(
            AnnotationSpec.Companion.builder(Suppress::class)
                .addMember("\"RedundantVisibilityModifier\", \"UnusedReceiverParameter\", \"Unused\", \"ObjectPropertyName\", \"SpellCheckingInspection\"")
                .build()
        )
        for (codepoint in codepoints) {
            val iconName =
                CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, codepoint.first).let {
                    if (it.first().isDigit()) "_$it" else it
                }
            val property = PropertySpec.builder(iconName, Char::class)
                .receiver(ClassName(RECEIVER_PACKAGE, RECEIVER_CLASS))
                .getter(
                    FunSpec.getterBuilder()
                        .addCode(
                            buildCodeBlock {
                                addStatement("return '\\u${codepoint.second}'")
                            }
                        )
                        .build()
                )
                .addKdoc("@suppress")
                .build()
            fileSpecBuilder.addProperty(property)
        }
        fileSpecBuilder.build()
            .writeTo(project.layout.projectDirectory.file("src/commonMain/kotlin/").asFile)
    }
}
