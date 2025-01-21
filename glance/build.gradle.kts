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
    unversioned(libs.plugins.jetbrains.compose)
    unversioned(libs.plugins.kotlin.compose.compiler)
    unversioned(libs.plugins.android.library)
    unversioned(libs.plugins.kotlin.android)
    unversioned(libs.plugins.jetbrains.dokka)
    id("fonticons.java-target")
    id("fonticons.publish")
    id("fonticons.dokka")
}

kotlin {
    explicitApi()
}

android {
    namespace = "dev.tclement.fonticons.glance"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    api(project(":core"))
    api(libs.androidx.glance)
}

compose.resources {
    generateResClass = never
}
