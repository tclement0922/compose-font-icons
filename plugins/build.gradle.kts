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
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

gradlePlugin {
    plugins.register("font-icons-symbols-task") {
        id = "symbols-task"
        implementationClass = "dev.tclement.fonticons.symbols.generator.FontIconsSymbolsPlugin"
    }
    plugins.register("multiplatform-structure") {
        id = "multiplatform-structure"
        implementationClass = "dev.tclement.fonticons.multiplatform.MultiplatformPlugin"
    }
    plugins.register("publish") {
        id = "publish"
        implementationClass = "dev.tclement.fonticons.publish.PublishPlugin"
    }
}

fun Provider<PluginDependency>.asDependency(): String {
    return "${get().pluginId}:${get().pluginId}.gradle.plugin:${get().version.displayName}"
}

dependencies {
    implementation(gradleApi())
    implementation(libs.squareup.kotlinpoet)
    implementation(libs.google.guava)
    implementation(libs.plugins.kotlin.multiplatform.asDependency())
    implementation(libs.plugins.jetbrains.compose.asDependency())
    implementation(libs.plugins.undercouch.download.asDependency())
}
