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

@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT) // Changed temporally because of a Kotlin/JS dependencies issue (should be fixed in Kotlin 2.0)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ComposeFontIcons"

include(
    ":core",
    ":glance",
    ":testapp",
    ":website",
    ":website:dokka-vitepress-renderer",
    ":website:dokka-vitepress-renderer:multimodule",
)
