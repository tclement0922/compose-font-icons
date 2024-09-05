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

import com.google.common.base.CaseFormat
import com.squareup.kotlinpoet.*
import de.undercouch.gradle.tasks.download.Download
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.dokka)
    id("multiplatform-structure")
    id("publish")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core"))
                implementation(compose.components.resources)
            }
        }
    }
}

val downloadFiles by tasks.creating(Download::class) {
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
                into(layout.projectDirectory.file("$font/src/commonMain/composeResources/font"))
                rename { "material_symbols_$font.ttf" }
            }
        }
    }
}

private val targetPackage = "dev.tclement.fonticons.symbols"
private val targetFile = "MaterialSymbolsChars"
private val receiverPackage = targetPackage
private val receiverClass = "MaterialSymbols"

val createSymbolsFile by tasks.creating(Task::class) {
    dependsOn(downloadFiles)
    kotlin.sourceSets.commonMain {
        kotlin.srcDir(this@creating)
    }

    val codepointsFiles = project.layout.buildDirectory.let {
        listOf(
            it.file("MaterialSymbolsOutlined%5BFILL%2CGRAD%2Copsz%2Cwght%5D.codepoints"),
            it.file("MaterialSymbolsRounded%5BFILL%2CGRAD%2Copsz%2Cwght%5D.codepoints"),
            it.file("MaterialSymbolsSharp%5BFILL%2CGRAD%2Copsz%2Cwght%5D.codepoints"),
        )
    }
    inputs.files(codepointsFiles)
    outputs.dir(project.layout.buildDirectory.dir("generated/font-symbols/kotlin/"))

    doFirst {
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

        val fileSpecBuilder = FileSpec.builder(targetPackage, targetFile)
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
                .receiver(ClassName(receiverPackage, receiverClass))
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
            .writeTo(project.layout.buildDirectory.file("generated/font-symbols/kotlin/").get().asFile)
    }
}

afterEvaluate {
    val dokkaHtmlMultiModule by tasks.getting {
        dependsOn(":dokkaHtmlMultiModule")
    }
}
