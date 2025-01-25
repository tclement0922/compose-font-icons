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

import de.undercouch.gradle.tasks.download.Download
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    unversioned(libs.plugins.android.application)
    alias(libs.plugins.undercouch.download)
    id("fonticons.multiplatform-structure")
}

kotlin {
    androidTarget { }

    setOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        macosArm64(),
        macosX64(),
        // tvosX64(),
        // tvosArm64(),
        // tvosSimulatorArm64(),
        // watchosX64(),
        // watchosArm64(),
        // watchosDeviceArm64(),
        // watchosSimulatorArm64(),
    ).forEach { target ->
        target.binaries.framework {
            binaryOption("bundleId", "shared")
            binaryOption("bundleVersion", "1")
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.uiUtil)
            implementation(libs.jetbrains.navigation.compose)
        }


        androidMain.dependencies {
            implementation(project(":glance"))
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.lifecycle.runtime.ktx)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.glance.appwidget)
        }


        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.resources {
    packageOfResClass = "dev.tclement.fonticons.testapp.res"
}

android {
    namespace = "dev.tclement.fonticons.testapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.tclement.fonticons.testappp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

compose.desktop {
    application {
        mainClass = "dev.tclement.fonticons.testapp.MainWindowKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "dev.tclement.fonticons.testapp"
            packageVersion = "1.0.0"
        }
    }
}

// Should be run at least once before running the app
val downloadFonts by tasks.registering(Download::class) {
    fun ms(name: String) =
        "https://github.com/google/material-design-icons/raw/${
            properties["FONT_SYMBOLS_REVISION"]
        }/variablefont/MaterialSymbols${
            name.uppercaseFirstChar()
        }%5BFILL%2CGRAD%2Copsz%2Cwght%5D.ttf" to "material_symbols_${name}"

    fun fa(name: String) =
        "https://github.com/FortAwesome/Font-Awesome/raw/${
            properties["FONT_FA_REVISION"]
        }/otfs/Font%20Awesome%206%20${name}.otf" to "font_awesome_${
            name.replace('-', '_').lowercase()
        }"

    val fonts = mapOf(
        ms("outlined"),
        ms("rounded"),
        ms("sharp"),
        fa("Brands-Regular-400"),
        fa("Free-Regular-400"),
        fa("Free-Solid-900")
    )

    src(fonts.keys)
    dest(layout.projectDirectory)
    eachFile {
        val url = sourceURL.toExternalForm()
        path = "build/composeResources/font/${fonts[url]}.${url.substringAfterLast(".")}"
    }
    overwrite(false)
}

tasks.named("copyNonXmlValueResourcesForCommonMain") {
    dependsOn(downloadFonts)
}

compose.resources {
    customDirectory("commonMain", project.layout.buildDirectory.dir("composeResources"))
}
