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
    unversioned(libs.plugins.kotlin.multiplatform) apply false
    unversioned(libs.plugins.jetbrains.compose) apply false
    unversioned(libs.plugins.kotlin.compose.compiler) apply false
    unversioned(libs.plugins.android.application) apply false
    unversioned(libs.plugins.kotlin.android) apply false
    unversioned(libs.plugins.android.library) apply false
    unversioned(libs.plugins.undercouch.download) apply false
    unversioned(libs.plugins.vanniktech.publish) apply false
    unversioned(libs.plugins.jetbrains.dokka)
    id("fonticons.dokka-vitepress")
}

dependencies {
    for (project in subprojects.filter { it.name !in setOf("testapp", "dokka-vitepress-renderer") })
        dokka(project)
}

dokka.dokkaPublications.html {
    outputDirectory.set(rootDir.resolve("docs"))
    moduleVersion.set(properties["VERSION_NAME"] as? String)
}

dokka.dokkaPublications.vitepress {
    outputDirectory.set(rootDir.resolve("website/api"))
    moduleVersion.set(properties["VERSION_NAME"] as? String)
}
