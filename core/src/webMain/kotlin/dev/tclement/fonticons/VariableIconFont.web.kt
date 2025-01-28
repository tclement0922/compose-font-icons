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

package dev.tclement.fonticons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import org.jetbrains.skia.Data
import org.jetbrains.skia.FontMgr
import org.jetbrains.skiko.loadBytesFromPath
import org.jetbrains.skia.Typeface as SkTypeface

private val defaultTypeface = SkTypeface.makeEmpty()

/**
 * Creates a variable [IconFont] using the path of a font.
 * @param resource fetch path of the font file as seen [here](https://developer.mozilla.org/en-US/docs/Web/API/fetch#resource)
 * @param weights supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@Composable
public fun rememberVariableIconFont(
    resource: Any?,
    weights: Array<FontWeight>,
    fontVariationSettings: FontVariation.Settings = FontVariation.Settings(),
    fontFeatureSettings: String? = null,
    density: Density = LocalDensity.current
): VariableIconFont {
    val typeface by produceState(defaultTypeface) {
        value = FontMgr.default.makeFromData(
            Data.makeFromBytes(loadBytesFromPath(resource.toString()))
        )!!
    }

    return rememberVariableIconFont(
        alias = if (typeface === defaultTypeface) "default" else resource.toString(),
        baseTypeface = typeface,
        weights = weights,
        fontVariationSettings = fontVariationSettings,
        fontFeatureSettings = fontFeatureSettings,
        density = density
    )
}
