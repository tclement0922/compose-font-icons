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

import com.github.gradle.node.npm.task.NpmTask

/*
 * Copyright 2025 T. Clément (@tclement0922)
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
    alias(libs.plugins.node)
}

node {
    npmInstallCommand = if (System.getenv("CI") != null) "ci" else "install"
}

val npmDev by tasks.registering(NpmTask::class) {
    npmCommand = listOf("run", "docs:dev")
}

val npmBuild by tasks.registering(NpmTask::class) {
    npmCommand = listOf("run", "docs:build")

    dependsOn(rootProject.tasks.named("dokkaGeneratePublicationVitepress"))
}

val npmPreview by tasks.registering(NpmTask::class) {
    npmCommand = listOf("run", "docs:preview")
}
