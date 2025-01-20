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

import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.api.credentials.PasswordCredentials

private fun envExists(name: String) =
    try {
        System.getenv(name) != null
    } catch (_: Throwable) {
        false
    }

plugins {
    com.vanniktech.maven.publish
}

val isMavenCentralPublishDefined = properties.containsKey("mavenCentralUsername") && properties.containsKey("mavenCentralPassword")
val isGithubPackagesPublishDefined = properties.containsKey("githubPackagesUsername") && properties.containsKey("githubPackagesPassword")

mavenPublishing {
    coordinates(
        artifactId = project.name
    )

    pom {
        val uppercaseProjectName = project.name.replace('-', '_').uppercase()
        name.set(project.properties["POM_${uppercaseProjectName}_NAME"] as? String)
        description.set(project.properties["POM_${uppercaseProjectName}_DESCRIPTION"] as? String)
    }

    if (isMavenCentralPublishDefined) {
        publishToMavenCentral(
            host = SonatypeHost.CENTRAL_PORTAL,
            automaticRelease = true
        )
    }

    if (isGithubPackagesPublishDefined || isMavenCentralPublishDefined)
        signAllPublications()
}

publishing {
    publications {
        repositories {
            if (isGithubPackagesPublishDefined) {
                maven {
                    name = "githubPackages"
                    url = uri(properties["GH_PACKAGES_URL"] as String)
                    credentials(PasswordCredentials::class)
                }
            }
        }
    }
}
