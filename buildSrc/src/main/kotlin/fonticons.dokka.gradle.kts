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

plugins {
    org.jetbrains.dokka
    id("fonticons.dokka-vitepress")
}

dokka {
    dokkaSourceSets.configureEach {
        val newName = when (displayName.orNull) {
            "common" -> "Common"
            "androidJvm" -> "Android"
            else -> when {
                name.startsWith("android") -> "Android"
                name.startsWith("skiko") -> "Skiko (Desktop & Web)"
                name.startsWith("desktop") -> "Desktop (JVM)"
                name.startsWith("js") -> "JS"
                name.startsWith("wasm") -> "WASM"
                name.startsWith("web") -> "Web (JS & WASM)"
                else -> name
            }
        }
        displayName.set(newName)
        reportUndocumented = true
        suppressGeneratedFiles = false

        perPackageOption {
            matchingRegex.set(".*.resources")
            suppress.set(true)
        }

        sourceLink {
            localDirectory.set(project.projectDir.resolve("src"))
            remoteUrl(
                "https://github.com/tclement0922/compose-font-icons/tree/main/${
                    project.projectDir.toRelativeString(rootDir)
                }/src"
            )
            remoteLineSuffix.set("#L")
        }
    }

    pluginsConfiguration.html {
        footerMessage = "Copyright (c) 2024 T. Clément (@tclement0922)"
        customStyleSheets.from(rootDir.resolve("docs-assets/sourcesets.css"))
    }

    // to avoid OOM errors
    dokkaGeneratorIsolation = ClassLoaderIsolation()
}
