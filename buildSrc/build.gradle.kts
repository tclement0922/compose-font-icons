import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

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
    alias(libs.plugins.kotlin.serialization) version embeddedKotlinVersion
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

fun DependencyHandler.pluginImplementation(plugin: Provider<PluginDependency>) {
    implementation("${plugin.get().pluginId}:${plugin.get().pluginId}.gradle.plugin:${plugin.get().version.displayName}")
}

val injectedCatalogs = mutableSetOf<AbstractExternalDependencyFactory>()

fun <C : AbstractExternalDependencyFactory> DependencyHandler.injectCatalog(catalog: C) {
    injectedCatalogs.add(catalog)
    // workaround from https://github.com/gradle/gradle/issues/15383#issuecomment-779893192 to make the version catalog
    // available in convention plugins
    implementation(files(catalog.javaClass.protectionDomain.codeSource.location))
}

dependencies {
    injectCatalog(libs)

    implementation(gradleApi())
    implementation(libs.squareup.kotlinpoet)
    implementation(libs.google.guava)
    implementation(libs.kotlinx.serialization.json) {
        version {
            require("1.6.3") // Can be removed when Gradle's embedded Kotlin version is set to at least 2.0
        }
    }

    pluginImplementation(libs.plugins.kotlin.multiplatform)
    pluginImplementation(libs.plugins.kotlin.compose.compiler)
    pluginImplementation(libs.plugins.jetbrains.compose)
    pluginImplementation(libs.plugins.undercouch.download)
    pluginImplementation(libs.plugins.vanniktech.publish)
    pluginImplementation(libs.plugins.android.library)
    pluginImplementation(libs.plugins.jetbrains.dokka)
}

// the workaround doesn't work in the context of the `plugins` block since they are extracted from the script at compile
// time, so we patch the generated script files to replace the `alias` calls with the actual plugin id
tasks.extractPrecompiledScriptPluginPlugins {
    this as Task
    doLast {
        for (outDir in outputs.files) for (file in outDir.listFiles() ?: emptyArray()) {
            val lines = file.readText()
            file.writeText(lines.replace("alias\\((.*)\\)".toRegex()) { result ->
                try {
                    val notation = result.groupValues[1].split('.')
                    val catalog =
                        injectedCatalogs.find { it::class.simpleName == "LibrariesFor" + notation[0].uppercaseFirstChar() + "_Decorated" } ?:
                        injectedCatalogs.find { it::class.simpleName == "LibrariesFor" + notation[0].uppercaseFirstChar() }!!
                    var lastMember: Any = catalog
                    for (i in 1 until notation.size) {
                        lastMember =
                            lastMember::class.members.find { it.name == "get" + notation[i].uppercaseFirstChar() }!!
                                .call(lastMember)!!
                    }
                    @Suppress("UNCHECKED_CAST")
                    lastMember as Provider<PluginDependency>
                    "id(\"${lastMember.get().pluginId}\")"
                } catch (e: Throwable) {
                    result.value
                }
            })
        }
    }
}
