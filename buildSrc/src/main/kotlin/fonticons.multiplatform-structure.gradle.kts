@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.android.build.gradle.LibraryExtension
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSourceSetConvention
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

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

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose.compiler)
}

val NamedDomainObjectContainer<KotlinSourceSet>.skikoMain: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention
val NamedDomainObjectContainer<KotlinSourceSet>.skikoTest: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention
val NamedDomainObjectContainer<KotlinSourceSet>.webMain: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention
val NamedDomainObjectContainer<KotlinSourceSet>.webTest: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention
val NamedDomainObjectContainer<KotlinSourceSet>.desktopMain: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention
val NamedDomainObjectContainer<KotlinSourceSet>.desktopTest: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention

fun KotlinSourceSet.dependsOn(other: NamedDomainObjectProvider<KotlinSourceSet>) = dependsOn(other.get())

val isLibrary = plugins.hasPlugin(libs.plugins.android.library.get().pluginId)

val isSecondaryCoreLibrary = name.startsWith("core-")
val isFontLibrary = name.startsWith("font-")
val isFontVariantLibrary = isFontLibrary && name.count { it == '-' } > 1

var packageName = "dev.tclement.fonticons"
when {
    isSecondaryCoreLibrary -> {
        packageName += "." + name.substringAfter("core-").replace("-", ".")
    }

    isFontLibrary && !isFontVariantLibrary -> {
        packageName += "." + name.substringAfter("font-").substringBefore("-")
    }

    isFontVariantLibrary -> {
        packageName += "." + name.substringAfter("font-").replace("-", ".")
    }
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
    //                tvosX64()
    //                tvosArm64()
    //                tvosSimulatorArm64()
    //                watchosX64()
    //                watchosArm64()
    //                watchosDeviceArm64()
    //                watchosSimulatorArm64()

    sourceSets.apply {
        create("skikoMain") {
            dependsOn(commonMain)
        }
        create("skikoTest") {
            dependsOn(commonTest)
        }
        create("webMain") {
            dependsOn(skikoMain)
        }
        create("webTest") {
            dependsOn(skikoTest)
        }
        jsMain {
            dependsOn(webMain)
        }
        jsTest {
            dependsOn(webTest)
        }
        wasmJsMain {
            dependsOn(webMain)
        }
        wasmJsTest {
            dependsOn(webTest)
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
}

if (isLibrary) {
    compose.resources {
        packageOfResClass = "$packageName.resources"
        if (isFontVariantLibrary) {
            publicResClass = true
            generateResClass = always
        } else {
            publicResClass = false
            generateResClass = never
        }
    }

    extensions.configure<LibraryExtension> {
        namespace = packageName
        compileSdk = 34

        defaultConfig {
            minSdk = 21

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            consumerProguardFiles("consumer-rules.pro")
        }
    }
}
