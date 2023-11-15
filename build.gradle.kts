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
import org.jetbrains.dokka.gradle.AbstractDokkaLeafTask
import org.jetbrains.dokka.gradle.AbstractDokkaParentTask
import org.jetbrains.dokka.gradle.AbstractDokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.konan.properties.loadProperties
import java.net.URL

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

fun Project.configureDokka() {
    tasks.withType(AbstractDokkaTask::class) {
        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "Copyright (c) 2023 T. Clément (@tclement0922)"
            mergeImplicitExpectActualDeclarations = false
            customStyleSheets =
                listOf(rootDir.resolve("docs-assets/sourcesets.css"))
        }
    }
    tasks.withType(AbstractDokkaParentTask::class) {
        outputDirectory.set(rootDir.resolve("docs"))
        moduleVersion.set(libProperties["version"] as? String)
    }
    tasks.withType(AbstractDokkaLeafTask::class) {
        dokkaSourceSets {
            configureEach {
                val newName = when (displayName.orNull) {
                    "common" -> "Common"
                    "androidJvm" -> "Android"
                    else -> when {
                        name.startsWith("android") -> "Android"
                        name.startsWith("skiko") -> "Skiko (Desktop & Web)"
                        name.startsWith("desktop") -> "Desktop (JVM)"
                        name.startsWith("js") -> "Web (JS)"
                        name.startsWith("web") -> "Web"
                        else -> name
                    }
                }
                displayName.set(newName)
                reportUndocumented = true

                sourceLink {
                    localDirectory.set(projectDir.resolve("src"))
                    remoteUrl.set(URL("https://github.com/tclement0922/compose-font-icons/tree/main/${projectDir.toRelativeString(rootDir)}/src"))
                    remoteLineSuffix.set("#L")
                }
            }
        }
    }
    if (subprojects.isNotEmpty()) subprojects { configureDokka() }
}

configureDokka()
