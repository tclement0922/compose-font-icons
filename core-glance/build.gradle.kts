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

import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrains.dokka)
    `maven-publish`
}

val libProperties = loadProperties(rootDir.absolutePath + "/library.properties")
val githubProperties = loadProperties(rootDir.absolutePath + "/github.properties")

group = libProperties.getString("group")
version = libProperties.getString("version")

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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
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
        register<MavenPublication>("release") {
            afterEvaluate {
                from(components.getByName("release"))
            }
        }
    }
}

dependencies {
    api(project(":core"))
    implementation(libs.androidx.glance)
}
