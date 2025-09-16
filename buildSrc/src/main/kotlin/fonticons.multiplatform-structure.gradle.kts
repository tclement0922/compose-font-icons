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

@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.android.build.gradle.LibraryExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinSourceSetConvention
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    org.jetbrains.kotlin.multiplatform
    org.jetbrains.compose
    org.jetbrains.kotlin.plugin.compose
    id("fonticons.java-target")
}

val NamedDomainObjectContainer<KotlinSourceSet>.skikoMain: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention
val NamedDomainObjectContainer<KotlinSourceSet>.skikoTest: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention
val NamedDomainObjectContainer<KotlinSourceSet>.desktopMain: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention
val NamedDomainObjectContainer<KotlinSourceSet>.desktopTest: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention

fun KotlinSourceSet.dependsOn(other: NamedDomainObjectProvider<KotlinSourceSet>) = dependsOn(other.get())

val isLibrary = plugins.hasPlugin("com.android.library")

var packageName = "dev.tclement.fonticons"

if (name != "core") {
    packageName += ".$name"
}

kotlin {
    if (isLibrary)
        explicitApi()

    applyDefaultHierarchyTemplate()

    jvm("desktop")

    androidTarget {
        if (isLibrary) {
            publishLibraryVariants("release")
        }
    }

    js {
        browser()
        if (!isLibrary) {
            binaries.executable()
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        if (!isLibrary) {
            binaries.executable()
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosArm64()
    macosX64()
    // CMP does not support these targets yet
    // tvosX64()
    // tvosArm64()
    // tvosSimulatorArm64()
    // watchosX64()
    // watchosArm64()
    // watchosDeviceArm64()
    // watchosSimulatorArm64()

    sourceSets.apply {
        create("skikoMain") {
            dependsOn(commonMain)
        }
        create("skikoTest") {
            dependsOn(commonTest)
        }
        webMain {
            dependsOn(skikoMain)
        }
        webTest {
            dependsOn(skikoTest)
        }
        desktopMain {
            dependsOn(skikoMain)
        }
        desktopTest {
            dependsOn(skikoTest)
        }
        nativeMain {
            dependsOn(skikoMain)
        }
    }

    targets.forEach { target ->
        target.compilations.all {
            compileTaskProvider {
                compilerOptions {
                    freeCompilerArgs.add("-P")
                    freeCompilerArgs.add(layout.buildDirectory.dir("compose/reports/${target.name}").map {
                        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${it.asFile.absolutePath}"
                    })
                    freeCompilerArgs.add("-P")
                    freeCompilerArgs.add(layout.buildDirectory.dir("compose/metrics/${target.name}").map {
                        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${it.asFile.absolutePath}"
                    })
                }
            }
        }
    }
}

if (isLibrary) {
    compose.resources {
        packageOfResClass = "$packageName.resources"
        publicResClass = false
        generateResClass = auto
    }

    extensions.configure<LibraryExtension> {
        namespace = packageName
        compileSdk = 35

        defaultConfig {
            minSdk = 21

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            consumerProguardFiles("consumer-rules.pro")
        }
    }
}
