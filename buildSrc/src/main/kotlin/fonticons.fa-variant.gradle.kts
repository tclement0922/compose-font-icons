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

import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

val mVariant = name.substringAfter("font-fa-")

val createFAVariantFiles = tasks.create(
    name = "createFA${mVariant.uppercaseFirstChar()}Files",
    type = CreateFAVariantTask::class
) {
    this.variant.set(mVariant)
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
