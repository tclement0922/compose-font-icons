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

import com.android.build.gradle.BaseExtension
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.AbstractDokkaLeafTask
import org.jetbrains.dokka.gradle.AbstractDokkaParentTask
import org.jetbrains.dokka.gradle.AbstractDokkaTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import java.net.URI

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.kotlin.compose.compiler) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.undercouch.download) apply false
    alias(libs.plugins.jetbrains.dokka)
    alias(libs.plugins.vanniktech.publish) apply false
}

buildscript {
    dependencies {
        classpath(libs.jetbrains.dokka.plugin.android)
    }
}

fun Project.configureDokkaAndJvmVersion() {
    val version = properties["VERSION_NAME"] as? String
    val javaVersion = properties["JAVA_VERSION"] as? String
    tasks {
        withType(AbstractDokkaTask::class) {
            pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
                footerMessage = "Copyright (c) 2024 T. Clément (@tclement0922)"
                mergeImplicitExpectActualDeclarations = false
                customStyleSheets =
                    listOf(rootDir.resolve("docs-assets/sourcesets.css"))
            }
        }
        withType(AbstractDokkaParentTask::class) {
            outputDirectory.set(rootDir.resolve("docs"))
            moduleVersion.set(version)
        }
        withType(AbstractDokkaLeafTask::class) {
            dokkaSourceSets {
                configureEach {
                    val newName = when (displayName.orNull) {
                        "common" -> "Common"
                        "androidJvm" -> "Android"
                        else -> when {
                            name.startsWith("android") -> "Android"
                            name.startsWith("skiko") -> "Skiko (Desktop & Web)"
                            name.startsWith("desktop") -> "Desktop (JVM)"
                            name.startsWith("js") -> "JS"
                            name.startsWith("wasm") -> "WASM"
                            name.startsWith("web") -> "Web (JS & WASM)"
                            else -> name
                        }
                    }
                    displayName.set(newName)
                    reportUndocumented = true
                    suppressGeneratedFiles = false

                    perPackageOption {
                        matchingRegex.set(".*.resources")
                        suppress.set(true)
                    }

                    sourceLink {
                        localDirectory.set(project.projectDir.resolve("src"))
                        remoteUrl.set(
                            URI.create(
                                "https://github.com/tclement0922/compose-font-icons/tree/main/${
                                    project.projectDir.toRelativeString(rootDir)
                                }/src"
                            ).toURL()
                        )
                        remoteLineSuffix.set("#L")
                    }
                }
            }
        }
        withType<KotlinJvmCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.fromTarget(javaVersion ?: "1.8"))
            }
        }
        withType<JavaCompile>().configureEach {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }
    }

    extensions.findByType<BaseExtension>()?.compileOptions {
        sourceCompatibility = JavaVersion.toVersion(javaVersion ?: "1.8")
        targetCompatibility = JavaVersion.toVersion(javaVersion ?: "1.8")
    }

    if (subprojects.isNotEmpty()) subprojects { configureDokkaAndJvmVersion() }
}

configureDokkaAndJvmVersion()
