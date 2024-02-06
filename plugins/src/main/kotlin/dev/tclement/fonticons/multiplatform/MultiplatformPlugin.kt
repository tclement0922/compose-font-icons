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

@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package dev.tclement.fonticons.multiplatform

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSourceSetConvention
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet


val NamedDomainObjectContainer<KotlinSourceSet>.skikoMain: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention
val NamedDomainObjectContainer<KotlinSourceSet>.skikoTest: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention
val NamedDomainObjectContainer<KotlinSourceSet>.webMain: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention
val NamedDomainObjectContainer<KotlinSourceSet>.webTest: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention
//val NamedDomainObjectContainer<KotlinSourceSet>.wasmJsMain: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention
//val NamedDomainObjectContainer<KotlinSourceSet>.wasmJsTest: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention
val NamedDomainObjectContainer<KotlinSourceSet>.desktopMain: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention
val NamedDomainObjectContainer<KotlinSourceSet>.desktopTest: NamedDomainObjectProvider<KotlinSourceSet> by KotlinSourceSetConvention

fun KotlinSourceSet.dependsOn(other: NamedDomainObjectProvider<KotlinSourceSet>) = dependsOn(other.get())

class MultiplatformPlugin : Plugin<Project> {
    //@OptIn(ExperimentalWasmDsl::class)
    override fun apply(target: Project) {
        val hasWebTarget = !target.name.contains("font-symbols-")
        val isLibrary = target.plugins.hasPlugin("com.android.library")

        with(target) {
            extensions.configure(KotlinMultiplatformExtension::class) {
                jvm("desktop") {
                    jvmToolchain(8)
                }

                androidTarget {
                    if (isLibrary) {
                        publishLibraryVariants("release")
                    }
                    compilations.all {
                        kotlinOptions {
                            jvmTarget = "1.8"
                        }
                    }
                }

                if (hasWebTarget) {
                    js {
                        browser()
                        if (!isLibrary) {
                            binaries.executable()
                        }
                    }

                    //wasmJs {
                    //    browser()
                    //    if (!isLibrary) {
                    //        binaries.executable()
                    //    }
                    //}
                }

                sourceSets.apply {
                    create("skikoMain") {
                        dependsOn(commonMain)
                    }
                    create("skikoTest") {
                        dependsOn(commonTest)
                    }

                    if (hasWebTarget) {
                        create("webMain") {
                            dependsOn(skikoMain)
                        }
                        create("webTest") {
                            dependsOn(skikoTest)
                        }

                        jsMain {
                            dependsOn(webMain)
                        }
                        jsTest {
                            dependsOn(webTest)
                        }

                        //wasmJsMain {
                        //    dependsOn(webMain)
                        //}
                        //wasmJsTest {
                        //    dependsOn(webTest)
                        //}
                    }

                    desktopMain {
                        dependsOn(skikoMain)
                    }
                    desktopTest {
                        dependsOn(skikoTest)
                    }
                }
            }
        }
    }
}
