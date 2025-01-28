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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.getSystemResourceEnvironment

/**
 * Variable icon font, for variable fonts. The optical size (opsz) and weight (wght) axes will be automatically set.
 */
public abstract class VariableIconFont internal constructor() : IconFont() {
    internal abstract val variationSettings: FontVariation.Settings
    internal abstract val opticalSizePreset: Boolean
    internal abstract fun textStyleWeightFor(weight: FontWeight): FontWeight
    internal abstract fun getFontFamily(size: Float, weight: FontWeight): FontFamily
}

/**
 * Creates a variable [IconFont] using a Compose Multiplatform [FontResource].
 *
 * This function is marked as experimental because its Android implementation is using experimental workarounds:
 * - On Android 11 and higher this will shortly create an in-memory file descriptor.
 * - On Android 8 and higher this will create a temporary file (this file will be deleted after the font is loaded).
 * - On Android 7 and lower this will also create a temporary file, but the variation settings will be ignored.
 * @param fontResource the Compose Multiplatform resource ID
 * @param weights the supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@ExperimentalFontIconsApi
@Composable
public expect fun rememberVariableIconFont(
    fontResource: FontResource,
    weights: Array<FontWeight>,
    fontVariationSettings: FontVariation.Settings = FontVariation.Settings(),
    fontFeatureSettings: String? = null
): VariableIconFont

/**
 * Creates a variable [IconFont] using a Compose Multiplatform [FontResource].
 *
 * This function is marked as experimental because its Android implementation is using experimental workarounds:
 * - On Android 11 and higher this will shortly create an in-memory file descriptor.
 * - On Android 8 and higher this will create a temporary file (this file will be deleted after the font is loaded).
 * - On Android 7 and lower this will also create a temporary file, but the variation settings will be ignored.
 *
 * This function is not composable, use [rememberVariableIconFont] when in a composition.
 * @param fontResource the Compose Multiplatform resource ID
 * @param weights the supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 * @param resourceEnvironment the resource environment to use to load the font
 * @param density the density of the screen, optionally used to convert density-dependent variation settings to pixels
 */
@OptIn(ExperimentalResourceApi::class)
@ExperimentalFontIconsApi
public expect suspend fun createVariableIconFont(
    fontResource: FontResource,
    weights: Array<FontWeight>,
    fontVariationSettings: FontVariation.Settings = FontVariation.Settings(),
    fontFeatureSettings: String? = null,
    resourceEnvironment: ResourceEnvironment = getSystemResourceEnvironment(),
    density: Density? = null
): VariableIconFont
