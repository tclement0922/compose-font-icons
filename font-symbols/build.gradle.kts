/*
 * Copyright 2023 T. Clément (@tclement0922)
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
import dev.tclement.fonticons.symbols.generator.FontIconsSymbolsTask
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    alias(libs.plugins.koltin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.dokka)
    `maven-publish`
}

val libProperties = loadProperties(rootDir.absolutePath + "/library.properties")
val githubProperties = loadProperties(rootDir.absolutePath + "/github.properties")

group = libProperties.getString("group")
version = libProperties.getString("version")

kotlin {
    jvm("desktop") {
        jvmToolchain(8)
    }

    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

val downloadFiles by tasks.creating(Download::class) {
    val fontNamePrefix = "MaterialSymbols"
    val urlPrefix = "https://github.com/google/material-design-icons/raw/master/variablefont/$fontNamePrefix"
    val fontNameSuffix = "%5BFILL%2CGRAD%2Copsz%2Cwght%5D"
    val fonts = arrayOf(
        "outlined", "rounded", "sharp"
    )

    src(fonts.flatMap { font -> listOf("$urlPrefix${font.uppercaseFirstChar()}$fontNameSuffix.ttf", "$urlPrefix${font.uppercaseFirstChar()}$fontNameSuffix.codepoints") })
    dest(layout.buildDirectory)
    overwrite(false)

    doLast {
        for (font in fonts) {
            copy {
                from(layout.buildDirectory.file("$fontNamePrefix${font.uppercaseFirstChar()}$fontNameSuffix.ttf"))
                into(layout.projectDirectory.file("$font/src/commonMain/resources/font"))
                rename { "material_symbols_$font.ttf" }
            }
        }
    }
}

val createSymbolsFile by tasks.creating(FontIconsSymbolsTask::class) {
    dependsOn(downloadFiles)
}

tasks.preBuild {
    dependsOn(createSymbolsFile)
}

publishing {
    publications {
        repositories {
            /*maven {
                name = "Local"
                url = uri(rootProject.layout.projectDirectory.dir("maven"))
            }*/
            maven {
                name = "GitHubPackages"
                url = uri(libProperties.getString("packages-url"))
                credentials {
                    username = githubProperties.getString("username")
                    password = githubProperties.getString("token")
                }
            }
        }
    }
}

afterEvaluate {
    val dokkaHtmlMultiModule by tasks.getting {
        dependsOn(":dokkaHtmlMultiModule")
    }
}
