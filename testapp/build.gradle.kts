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
import dev.tclement.fonticons.multiplatform.desktopMain
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.android.application)
    alias(libs.plugins.undercouch.download)
    id("multiplatform-structure")
}

kotlin {
    androidTarget { }

    sequence {
        yield(iosX64())
        yield(iosArm64())
        yield(iosSimulatorArm64())
        yield(macosArm64())
        yield(macosX64())
        // yield(tvosX64())
        // yield(tvosArm64())
        // yield(tvosSimulatorArm64())
        // yield(watchosX64())
        // yield(watchosArm64())
        // yield(watchosDeviceArm64())
        // yield(watchosSimulatorArm64())
    }.forEach { target ->
        target.binaries.framework {
            binaryOption("bundleId", "shared")
            binaryOption("bundleVersion", "1")
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core"))
                implementation(project(":font-symbols"))
                implementation(project(":font-symbols:font-symbols-rounded"))
                implementation(project(":font-fa:font-fa-regular"))
                implementation(project(":font-fa:font-fa-solid"))
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.components.resources)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.uiUtil)
                implementation(libs.jetbrains.navigation.compose)
            }
        }

        androidMain {
            dependencies {
                implementation(project(":core-glance"))
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.lifecycle.runtime.ktx)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.glance.appwidget)
            }
        }

        desktopMain {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }


}

compose.resources {
    packageOfResClass = "dev.tclement.fonticons.testapp.res"
}

android {
    namespace = "dev.tclement.fonticons.testapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.tclement.fonticons.testappp"
        minSdk = 24
        targetSdk = 34
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
