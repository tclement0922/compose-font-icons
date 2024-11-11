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

import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

plugins {
    id("fonticons.dokka")
}

val mVariant = name.substringAfter("font-symbols-")

val createSymbolsVariantFiles = tasks.create(
    name = "createSymbols${mVariant.uppercaseFirstChar()}Files",
    type = CreateSymbolsVariantTask::class
) {
    this.variant.set(mVariant)
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

configure<ComposeExtension> {
    configure<ResourcesExtension> {
        customDirectory("commonMain", project.layout.buildDirectory.dir("composeResources"))
    }
}