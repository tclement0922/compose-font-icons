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

package dev.tclement.fonticons.glance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.glance.GlanceComposable
import androidx.glance.unit.ColorProvider
import dev.tclement.fonticons.IconFont
import dev.tclement.fonticons.LocalIconFont
import dev.tclement.fonticons.LocalIconSize
import dev.tclement.fonticons.LocalIconWeight

/**
 * Default icon tint for Glance (a [ColorProvider]) used when none is provided as a parameter of one of the FontIcon functions.
 *
 * No default value (will throw an error if not provided).
 * @throws IllegalStateException if no value is provided.
 */
public val LocalGlanceIconTint: ProvidableCompositionLocal<ColorProvider> = compositionLocalOf {
    error("LocalGlanceIconTint is not provided in this composable, please provide one with ProvideGlanceIconParameters")
}

@Composable
@GlanceComposable
public fun ProvideGlanceIconParameters(
    iconFont: IconFont = LocalIconFont.current,
    size: Dp = LocalIconSize.current,
    tint: ColorProvider = LocalGlanceIconTint.current,
    weight: FontWeight = LocalIconWeight.current,
    content: @Composable @GlanceComposable () -> Unit
) {
    CompositionLocalProvider(
        LocalIconFont provides iconFont,
        LocalIconSize provides size,
        LocalGlanceIconTint provides tint,
        LocalIconWeight provides weight,
        content = content
    )
}
