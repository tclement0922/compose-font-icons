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

package dev.tclement.fonticons.publish

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.credentials.PasswordCredentials
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.credentials

private fun envExists(name: String) =
    try {
        System.getenv(name) != null
    } catch (_: Throwable) {
        false
    }

class PublishPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.apply("com.vanniktech.maven.publish")

            configure<MavenPublishBaseExtension> {
                coordinates(
                    artifactId = target.name
                )

                pom {
                    val uppercaseProjectName = target.name.replace('-', '_').uppercase()
                    name.set(target.properties["POM_${uppercaseProjectName}_NAME"].toString())
                    description.set(target.properties["POM_${uppercaseProjectName}_DESCRIPTION"].toString())
                }

                if (envExists("ORG_GRADLE_PROJECT_mavenCentralUsername") && envExists("ORG_GRADLE_PROJECT_mavenCentralPassword")) {
                    publishToMavenCentral(
                        host = SonatypeHost.CENTRAL_PORTAL,
                        automaticRelease = true
                    )
                }

                signAllPublications()
            }

            configure<PublishingExtension> {
                publications {
                    repositories {
                        if (envExists("ORG_GRADLE_PROJECT_githubPackagesUsername") && envExists("ORG_GRADLE_PROJECT_githubPackagesPassword")) {
                            maven {
                                name = "githubPackages"
                                url = uri(properties["GH_PACKAGES_URL"].toString())
                                credentials(PasswordCredentials::class)
                            }
                        }
                    }
                }
            }
        }
    }
}