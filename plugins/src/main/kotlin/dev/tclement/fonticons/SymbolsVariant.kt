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

package dev.tclement.fonticons

import com.squareup.kotlinpoet.*
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

private const val TARGET_PACKAGE = "dev.tclement.fonticons.symbols"

private abstract class CreateSymbolsVariantTask : DefaultTask() {
    @get:Input
    abstract val variant: Property<String>

    @get:OutputDirectory
    abstract val commonOutput: DirectoryProperty

    @get:OutputDirectory
    abstract val skikoOutput: DirectoryProperty

    @get:OutputDirectory
    abstract val androidOutput: DirectoryProperty

    @TaskAction
    fun runAction() {
        val variant = variant.get()

        fun commonFunSpec() = FunSpec.builder("remember${variant.uppercaseFirstChar()}MaterialSymbolsFont")
            .addAnnotation(ClassName("androidx.compose.runtime", "Composable"))
            .returns(ClassName("dev.tclement.fonticons", "IconFont"))

        val baseFileName = "${variant.uppercaseFirstChar()}MaterialSymbolsFont"
        val packageName = "$TARGET_PACKAGE.$variant"
        val commonFileSpecBuilder = FileSpec.builder(packageName, baseFileName)
        val expectFunctionSpec = commonFunSpec()
            .addParameter(ParameterSpec.builder("grade", Int::class).defaultValue("0").build())
            .addParameter(ParameterSpec.builder("fill", Boolean::class).defaultValue("false").build())
            .addModifiers(KModifier.EXPECT)
            .build()
        commonFileSpecBuilder.addFunction(expectFunctionSpec)
        commonFileSpecBuilder.build().writeTo(commonOutput.get().asFile)

        val skikoFileSpecBuilder = FileSpec.builder(packageName, "$baseFileName.skiko")
        val skikoFunctionSpec = commonFunSpec()
            .addParameter("grade", Int::class)
            .addParameter("fill", Boolean::class)
            .addModifiers(KModifier.ACTUAL)
            .addStatement("""
            return rememberVariableIconFont(
                fontResource = Res.font.material_symbols_$variant,
                weights = arrayOf(
                    FontWeight.W100,
                    FontWeight.W200,
                    FontWeight.W300,
                    FontWeight.W400,
                    FontWeight.W500,
                    FontWeight.W600,
                    FontWeight.W700,
                ),
                fontVariationSettings = arrayOf(
                    FontVariation.grade(grade),
                    FontVariation.Setting("FILL", if (fill) 1f else 0f)
                ),
                fontFeatureSettings = "liga"
            )
        """.trimIndent())
            .build()
        skikoFileSpecBuilder.addFunction(skikoFunctionSpec)
        skikoFileSpecBuilder.addImport("dev.tclement.fonticons", "rememberVariableIconFont")
        skikoFileSpecBuilder.addImport("$packageName.resources", "Res", "material_symbols_$variant")
        skikoFileSpecBuilder.addImport("androidx.compose.ui.text.font", "FontVariation", "FontWeight")
        skikoFileSpecBuilder.build().writeTo(skikoOutput.get().asFile)

        val androidFileSpecBuilder = FileSpec.builder(packageName, "$baseFileName.android")
        val androidFunctionSpec = commonFunSpec()
            .addParameter("grade", Int::class)
            .addParameter("fill", Boolean::class)
            .addModifiers(KModifier.ACTUAL)
            .addStatement("""
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                rememberStaticIconFont(
                    fontResource = Res.font.material_symbols_$variant
                )
            } else {
                rememberVariableIconFont(
                    fontResource = Res.font.material_symbols_$variant,
                    weights = arrayOf(
                        FontWeight.W100,
                        FontWeight.W200,
                        FontWeight.W300,
                        FontWeight.W400,
                        FontWeight.W500,
                        FontWeight.W600,
                        FontWeight.W700,
                    ),
                    fontVariationSettings = arrayOf(
                        FontVariation.grade(grade),
                        FontVariation.Setting("FILL", if (fill) 1f else 0f)
                    ),
                    fontFeatureSettings = "liga"
                )
            }
        """.trimIndent())
            .build()
        androidFileSpecBuilder.addFunction(androidFunctionSpec)
        androidFileSpecBuilder.addImport("dev.tclement.fonticons", "rememberVariableIconFont", "rememberStaticIconFont")
        androidFileSpecBuilder.addImport("$packageName.resources", "Res", "material_symbols_$variant")
        androidFileSpecBuilder.addImport("androidx.compose.ui.text.font", "FontVariation", "FontWeight")
        androidFileSpecBuilder.addImport("android.os", "Build")
        androidFileSpecBuilder.build().writeTo(androidOutput.get().asFile)
    }
}

fun Project.setupSourcesForSymbolsVariant(variant: String) {
    val createSymbolsVariantFiles = tasks.create(
        name = "createSymbols${variant.uppercaseFirstChar()}Files",
        type = CreateSymbolsVariantTask::class
    ) {
        this.variant.set(variant)
        commonOutput.set(layout.buildDirectory.dir("generated/symbols/common"))
        skikoOutput.set(layout.buildDirectory.dir("generated/symbols/skiko"))
        androidOutput.set(layout.buildDirectory.dir("generated/symbols/android"))
    }

    kotlinExtension.sourceSets.named("commonMain") {
        kotlin.srcDir(createSymbolsVariantFiles.commonOutput)
    }

    kotlinExtension.sourceSets.named("skikoMain") {
        kotlin.srcDir(createSymbolsVariantFiles.skikoOutput)
    }

    kotlinExtension.sourceSets.named("androidMain") {
        kotlin.srcDir(createSymbolsVariantFiles.androidOutput)
    }
}