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

import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.undercouch.download) apply false
    alias(libs.plugins.jetbrains.dokka)
}

buildscript {
    dependencies {
        classpath(libs.jetbrains.dokka.plugin.android)
    }
}

val libProperties = loadProperties(rootDir.absolutePath + "/library.properties")

tasks.dokkaHtmlMultiModule {
    outputDirectory.set(rootDir.resolve("docs"))
    moduleVersion.set(libProperties["version"] as? String)

    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        footerMessage = "(c) 2023 T. Clément (@tclement0922)"
        mergeImplicitExpectActualDeclarations = false
    }
}

fun Project.configureDokka() {
    tasks.withType(DokkaTaskPartial::class) {
        dokkaSourceSets {
            configureEach {
                reportUndocumented = true
            }
        }
    }
    if (subprojects.isNotEmpty()) subprojects { configureDokka() }
}

configureDokka()
