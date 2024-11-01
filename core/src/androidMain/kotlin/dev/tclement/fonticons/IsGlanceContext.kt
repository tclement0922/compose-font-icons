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

package dev.tclement.fonticons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.remember
import androidx.glance.LocalContext

// Dirty hack to avoid the fact that we can't use composables in a try/catch, making it impossible to check if
// LocalContext or LocalGlanceContext has a value and that LocalGlanceContext exists (since Glance is a compileOnly
// dependency)
@Composable
internal fun isGlanceContext(): Boolean {
    val compositionLocalMap = currentComposer.currentCompositionLocalMap
    return remember {
        try {
            compositionLocalMap[LocalContext]
            true
        } catch (_: Throwable) {
            false
        }
    }
}