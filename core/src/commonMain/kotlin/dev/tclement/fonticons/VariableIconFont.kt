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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.FontResource

public abstract class VariableIconFont internal constructor(): IconFont() {
    internal abstract val variationSettings: Array<out FontVariation.Setting>
    internal abstract val opticalSizePreset: Boolean
    internal abstract fun textStyleWeightFor(weight: FontWeight): FontWeight
    internal abstract fun getFontFamily(size: Float, weight: FontWeight): FontFamily
}

/**
 * Creates a variable [IconFont] using a Compose Multiplatform [FontResource].
 * @param fontResource the Compose Multiplatform resource ID
 * @param weights the supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@Composable
public expect fun rememberVariableIconFont(
    fontResource: FontResource,
    weights: Array<FontWeight>,
    fontVariationSettings: Array<FontVariation.Setting> = emptyArray(),
    fontFeatureSettings: String? = null
): VariableIconFont
