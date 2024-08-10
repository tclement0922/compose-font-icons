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

import dev.tclement.fonticons.symbols.generator.fontIconsSymbolsPluginConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.dokka)
    id("symbols-task")
    id("multiplatform-structure")
    id("publish")
}

kotlin {
    explicitApi()

    sourceSets {
        commonMain {
            dependencies {
                api(project(":core"))
                implementation(compose.components.resources)
            }
        }
    }
}

android {
    namespace = "dev.tclement.fonticons.symbols"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

fontIconsSymbolsPluginConfig {
    prebuildTask = tasks.generateComposeResClass
}

afterEvaluate {
    val dokkaHtmlMultiModule by tasks.getting {
        dependsOn(":dokkaHtmlMultiModule")
    }
}
