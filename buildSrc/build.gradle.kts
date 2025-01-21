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
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

fun DependencyHandler.pluginImplementation(plugin: Provider<PluginDependency>) {
    implementation("${plugin.get().pluginId}:${plugin.get().pluginId}.gradle.plugin:${plugin.get().version.displayName}")
}

dependencies {
    implementation(gradleApi())

    pluginImplementation(libs.plugins.kotlin.multiplatform)
    pluginImplementation(libs.plugins.kotlin.compose.compiler)
    pluginImplementation(libs.plugins.jetbrains.compose)
    pluginImplementation(libs.plugins.vanniktech.publish)
    pluginImplementation(libs.plugins.android.library)
    pluginImplementation(libs.plugins.jetbrains.dokka)
}
