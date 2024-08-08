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
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core"))
                implementation(project(":font-symbols"))
                implementation(project(":font-symbols:font-symbols-rounded"))
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.components.resources)
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

    androidTarget { }
}

compose {
    resources {
        packageOfResClass = "dev.tclement.fonticons.testapp.res"
    }
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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

val downloadFontAwesomeRegular by tasks.creating(Download::class) {
    src("https://github.com/FortAwesome/Font-Awesome/raw/6.x/otfs/Font%20Awesome%206%20Free-Regular-400.otf")
    dest(layout.projectDirectory.file("src/commonMain/composeResources/font/fontawesome_regular.otf"))
}

val downloadFontAwesomeSolid by tasks.creating(Download::class) {
    src("https://github.com/FortAwesome/Font-Awesome/raw/6.x/otfs/Font%20Awesome%206%20Free-Solid-900.otf")
    dest(layout.projectDirectory.file("src/commonMain/composeResources/font/fontawesome_solid.otf"))
}

for (task in arrayOf(
    tasks.copyNonXmlValueResourcesForCommonMain,
    tasks.generateComposeResClass
)) task {
    dependsOn(downloadFontAwesomeRegular)
    dependsOn(downloadFontAwesomeSolid)
}
