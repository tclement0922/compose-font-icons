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

@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

val toolchainVersion = properties["JAVA_TOOLCHAIN_VERSION"] as? String ?: "1.8"
val desktopTarget = properties["JAVA_DESKTOP_TARGET"] as? String ?: "1.8"
val androidTarget = properties["JAVA_ANDROID_TARGET"] as? String ?: "1.8"

val toolchainVersionInt =
    if (toolchainVersion.contains('.')) toolchainVersion.substringAfterLast('.').toInt() else toolchainVersion.toInt()

extensions.configure(KotlinBaseExtension::class) {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(toolchainVersionInt))
    }
}

if (extensions.findByType(BaseExtension::class) != null)
    extensions.configure(BaseExtension::class) {
        compileOptions {
            sourceCompatibility = JavaVersion.toVersion(androidTarget)
            targetCompatibility = JavaVersion.toVersion(androidTarget)
        }
    }

if (extensions.findByType(KotlinMultiplatformExtension::class) != null)
    extensions.configure(KotlinMultiplatformExtension::class) {
        afterEvaluate {
            targets.forEach {
                when (it) {
                    is KotlinAndroidTarget -> it.compilerOptions.jvmTarget.set(JvmTarget.fromTarget(androidTarget))
                    is KotlinJvmTarget -> it.compilerOptions.jvmTarget.set(JvmTarget.fromTarget(desktopTarget))
                }
            }
        }
    }
else if (extensions.findByType(KotlinAndroidProjectExtension::class) != null)
    extensions.configure(KotlinAndroidProjectExtension::class) {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(androidTarget))
        }
    }

if (extensions.findByType(JavaCompile::class) != null)
    extensions.configure(JavaCompile::class) {
        sourceCompatibility = desktopTarget
        targetCompatibility = desktopTarget
    }
