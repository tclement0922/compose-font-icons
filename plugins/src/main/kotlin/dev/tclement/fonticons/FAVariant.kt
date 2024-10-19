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

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

private const val TARGET_PACKAGE = "dev.tclement.fonticons.fa"

private abstract class CreateFAVariantTask : DefaultTask() {
    @get:Input
    abstract val variant: Property<String>

    @get:OutputDirectory
    abstract val output: DirectoryProperty

    @TaskAction
    fun runAction() {
        val variant = variant.get()

        val baseFileName = "${variant.uppercaseFirstChar()}FontAwesomeFont"
        val packageName = "$TARGET_PACKAGE.$variant"

        val fileSpecBuilder = FileSpec.builder(packageName, baseFileName)
        val functionSpec = FunSpec.builder("remember${variant.uppercaseFirstChar()}FontAwesomeFont")
            .addAnnotation(ClassName("androidx.compose.runtime", "Composable"))
            .returns(ClassName("dev.tclement.fonticons", "StaticIconFont"))
            .addStatement(
                """
            return rememberStaticIconFont(
                fontResource = Res.font.fontawesome_$variant,
                fontFeatureSettings = "liga"
            )
        """.trimIndent()
            )
            .addKdoc("""
            The FontAwesome Free ${variant.uppercaseFirstChar()} font.
            """.trimIndent())
            .build()
        fileSpecBuilder.addFunction(functionSpec)
        fileSpecBuilder.addImport("dev.tclement.fonticons", "rememberStaticIconFont")
        fileSpecBuilder.addImport("$packageName.resources", "Res", "fontawesome_$variant")
        fileSpecBuilder.build().writeTo(output.get().asFile)
    }
}

fun Project.setupSourcesForFAVariant(variant: String) {
    val createFAVariantFiles = tasks.create(
        name = "createFA${variant.uppercaseFirstChar()}Files",
        type = CreateFAVariantTask::class
    ) {
        this.variant.set(variant)
        output.set(layout.buildDirectory.dir("generated/fa/common"))
    }

    kotlinExtension.sourceSets.named("commonMain") {
        kotlin.srcDir(createFAVariantFiles.output)
    }

    configure<ComposeExtension> {
        configure<ResourcesExtension> {
            customDirectory("commonMain", project.layout.buildDirectory.dir("composeResources"))
        }
    }
}