/*
 * Copyright 2024-2026 T. Clément (@tclement0922)
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
    unversioned(libs.plugins.kotlin.multiplatform) apply false
    unversioned(libs.plugins.jetbrains.compose) apply false
    unversioned(libs.plugins.kotlin.compose.compiler) apply false
    unversioned(libs.plugins.android.application) apply false
    unversioned(libs.plugins.android.kotlin.multiplatform.library) apply false
    unversioned(libs.plugins.android.library) apply false
    alias(libs.plugins.undercouch.download) apply false
    unversioned(libs.plugins.vanniktech.publish) apply false
    unversioned(libs.plugins.jetbrains.dokka)
    alias(libs.plugins.kotlinx.binary.compatibility.validator)
}

val nonLibProjectNames = setOf(
    "testapp",
    "androidApp",
    "desktopApp",
    "webApp",
    "website"
)

dependencies {
    for (project in subprojects.filter { it.name !in nonLibProjectNames })
        dokka(project)
}

dokka {
    dokkaPublications.html {
        outputDirectory.set(rootDir.resolve("website/public/api"))
        moduleVersion.set(properties["VERSION_NAME"] as? String)
    }

    pluginsConfiguration.html {
        footerMessage = "Copyright (c) 2024-2026 T. Clément (@tclement0922)"
    }
}

apiValidation {
    nonPublicMarkers += "dev.tclement.fonticons.ExperimentalFontIconsApi"
    ignoredProjects += nonLibProjectNames
}
