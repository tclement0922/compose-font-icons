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

import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.jetbrains.dokka.gradle.formats.DokkaFormatPlugin
import org.jetbrains.dokka.gradle.internal.InternalDokkaGradlePluginApi

@OptIn(InternalDokkaGradlePluginApi::class)
abstract class DokkaVitePressFormatPlugin : DokkaFormatPlugin("vitepress") {
    override fun DokkaFormatPluginContext.configure() {
        project.dependencies {
            add("dokkaPlugin", project(":website:dokka-vitepress-renderer"))
        }

        formatDependencies.dokkaPublicationPluginClasspathApiOnly
            .dependencies
            .let {
                it.addAllLater(dokkaExtension.dokkaEngineVersion.map { v ->
                    listOf(
                        project.dependencies.create("org.jetbrains.dokka:all-modules-page-plugin:$v"),
                        project.dependencies.create("org.jetbrains.dokka:templating-plugin:$v"),
                        project.dependencies.project(":website:dokka-vitepress-renderer:multimodule")
                    )
                })
            }
    }
}