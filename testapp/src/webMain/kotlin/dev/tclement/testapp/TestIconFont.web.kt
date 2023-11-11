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

package dev.tclement.testapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import dev.tclement.fonticons.IconFont
import dev.tclement.fonticons.rememberVariableIconFont

@Composable
actual fun testIconFont(
    weights: Array<FontWeight>,
    grade: Int,
    fill: Float,
    manualOpsz: Boolean,
    opsz: Float
): IconFont = rememberVariableIconFont(
    // from https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200
    resource = "https://fonts.gstatic.com/s/materialsymbolsoutlined/v148/kJEhBvYX7BgnkSrUwT8OhrdQw4oELdPIeeII9v6oFsI.woff2",
    weights = arrayOf(
        FontWeight.W100,
        FontWeight.W200,
        FontWeight.W300,
        FontWeight.W400,
        FontWeight.W500,
        FontWeight.W600,
        FontWeight.W700,
    ),
    fontVariationSettings = buildList {
        add(FontVariation.grade(grade))
        add(FontVariation.Setting("FILL", fill))
        if (manualOpsz) {
            add(FontVariation.Setting("opsz", opsz))
        }
    }.toTypedArray()
)