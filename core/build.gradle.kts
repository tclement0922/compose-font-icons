import de.undercouch.gradle.tasks.download.Download
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

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
    unversioned(libs.plugins.android.library)
    unversioned(libs.plugins.jetbrains.dokka)
    id("fonticons.multiplatform-structure")
    id("fonticons.publish")
    id("fonticons.dokka")
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            api(compose.ui)
            api(compose.components.resources)
        }

        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            compileOnly(libs.androidx.glance)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.common)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))

            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }

        desktopTest.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

val downloadTestFonts by tasks.registering(Download::class) {
    fun ms(name: String) =
        "https://github.com/google/material-design-icons/raw/${
            properties["FONT_SYMBOLS_REVISION"]
        }/variablefont/MaterialSymbols${
            name.uppercaseFirstChar()
        }%5BFILL%2CGRAD%2Copsz%2Cwght%5D.ttf" to "material_symbols_${name}"

    fun fa(name: String) =
        "https://github.com/FortAwesome/Font-Awesome/raw/${
            properties["FONT_FA_REVISION"]
        }/otfs/Font%20Awesome%206%20${name}.otf" to "font_awesome_${
            name.replace('-', '_').lowercase()
        }"

    val fonts = mapOf(
        ms("outlined"),
        ms("rounded"),
        ms("sharp"),
        fa("Brands-Regular-400"),
        fa("Free-Regular-400"),
        fa("Free-Solid-900")
    )

    src(fonts.keys)
    dest(layout.projectDirectory)
    eachFile {
        val url = sourceURL.toExternalForm()
        path = "src/composeTestResources/font/${fonts[url]}.${url.substringAfterLast(".")}"
    }
    overwrite(false)
}

tasks.named("copyNonXmlValueResourcesForCommonTest") {
    dependsOn(downloadTestFonts)
}

compose.resources {
    generateResClass = auto
    customDirectory("commonTest", provider { layout.projectDirectory.dir("src/composeTestResources") })
}
