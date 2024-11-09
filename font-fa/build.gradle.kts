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
    unversioned(libs.plugins.android.library)
    unversioned(libs.plugins.jetbrains.dokka)
    id("fonticons.multiplatform-structure")
    id("fonticons.publish")
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
    val urlPrefix = "https://github.com/FortAwesome/Font-Awesome/raw/${properties["FONT_FA_REVISION"]}"
    val fonts = mapOf(
         "Font%20Awesome%206%20Brands-Regular-400.otf" to "brands",
         "Font%20Awesome%206%20Free-Regular-400.otf" to "regular",
         "Font%20Awesome%206%20Free-Solid-900.otf" to "solid"
    )

    src(fonts.map { (file, _) -> "$urlPrefix/otfs/$file" } + "$urlPrefix/metadata/icons.json")
    dest(layout.projectDirectory)
    eachFile {
        path = if (name.endsWith(".json")) {
            "build/icons.json"
        } else {
            "${fonts[name]}/build/composeResources/font/fontawesome_${fonts[name]}.otf"
        }
    }
    overwrite(false)
}

private val targetPackage = "dev.tclement.fonticons.fa"
private val targetFile = "FontAwesomeChars"
private val receiverPackage = targetPackage
private val receiverClass = "FontAwesome"

val createFAFile by tasks.creating(Task::class) {
    dependsOn(downloadFiles)
    kotlin.sourceSets.commonMain {
        kotlin.srcDir(this@creating)
    }

    val metadataFile = project.layout.buildDirectory.file("icons.json")
    inputs.file(metadataFile)
    outputs.dir(project.layout.buildDirectory.dir("generated/font-fa/kotlin/"))

    doFirst {
        val variants = setOf("brands", "regular", "solid")
        val metadata = readFAMetadata(metadataFile.get().asFile)

        val codepoints =
            variants.associateWith { v -> project.layout.buildDirectory.file("fa-$v.txt").get().asFile }

        val fileSpecBuilder = FileSpec.builder(targetPackage, targetFile)
        fileSpecBuilder.addAnnotation(
            AnnotationSpec.Companion.builder(Suppress::class)
                .addMember("\"RedundantVisibilityModifier\", \"UnusedReceiverParameter\", \"Unused\", \"ObjectPropertyName\", \"SpellCheckingInspection\"")
                .build()
        )

        for ((icon, details) in metadata) {
            for (variant in details.free.filter { it in variants }) {
                for (iconName in details.aliases?.names.orEmpty() + icon) {
                    codepoints[variant]?.appendText("$iconName\n")
                    val formattedIconName = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, iconName).let {
                        if (it.first().isDigit()) "_$it" else it
                    }
                    val property = PropertySpec.builder(formattedIconName, Char::class)
                        .receiver(ClassName(receiverPackage, "$receiverClass.${variant.uppercaseFirstChar()}"))
                        .getter(
                            FunSpec.getterBuilder()
                                .addCode(
                                    buildCodeBlock {
                                        addStatement("return '\\u${details.unicode.padStart(4, '0')}'")
                                    }
                                )
                                .build()
                        )
                        .addKdoc("@suppress")
                        .build()
                    fileSpecBuilder.addProperty(property)
                }
            }
        }

        fileSpecBuilder.build()
            .writeTo(project.layout.buildDirectory.file("generated/font-fa/kotlin/").get().asFile)
    }
}

childProjects.forEach { (_, child) ->
    child.afterEvaluate {
        child.tasks.named("copyNonXmlValueResourcesForCommonMain") { dependsOn(downloadFiles) }
    }
}

afterEvaluate {
    val dokkaHtmlMultiModule by tasks.getting {
        dependsOn(":dokkaHtmlMultiModule")
    }
}
