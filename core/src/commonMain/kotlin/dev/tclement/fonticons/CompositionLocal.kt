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

package dev.tclement.fonticons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Default [IconFont] used when none is provided as a parameter of one of the FontIcon composables.
 *
 * No default value (will throw an error if not provided).
 * @throws IllegalStateException if no value is provided.
 */
public val LocalIconFont: ProvidableCompositionLocal<IconFont> = compositionLocalOf {
    error("LocalIconFont is not provided in this composable, please provide one with ProvideIconParameters")
}

/**
 * Default size (in [Dp]) used when none is provided in the composable Modifier.
 *
 * 24 dp by default.
 */
public val LocalIconSize: ProvidableCompositionLocal<Dp> = compositionLocalOf { DEFAULT_ICON_SIZE_DP.dp }

/**
 * Default icon tint (a [Color]) used when none is provided as a parameter of one of the FontIcon composables.
 *
 * No default value (will throw an error if not provided).
 * @throws IllegalStateException if no value is provided.
 */
public val LocalIconTint: ProvidableCompositionLocal<Color> = compositionLocalOf {
    error("LocalIconTint is not provided in this composable, please provide one with ProvideIconParameters")
}

/**
 * Default icon tint provider (a [CompositionLocal] for a [Color]) used when none is provided as a parameter of one of the FontIcon composables.
 * If null, the value of [LocalIconTint] will be used instead.
 * When using Material3, should be most likely set to LocalContentColor.
 *
 * null by default.
 */
public val LocalIconTintProvider: ProvidableCompositionLocal<CompositionLocal<Color>?> = compositionLocalOf {
    null
}

/**
 * Default icon weight (a [FontWeight]) used when none is provided as a parameter of one of the FontIcon composables.
 *
 * [FontWeight.W400] by default.
 */
public val LocalIconWeight: ProvidableCompositionLocal<FontWeight> = compositionLocalOf { FontWeight(DEFAULT_ICON_WEIGHT) }

/**
 * A shortcut method to set default values for FontIcon composables. Might be better to just use
 * ProvideIconParameters if it's for setting only one of the default values.
 */
@Composable
public fun ProvideIconParameters(
    iconFont: IconFont = LocalIconFont.current,
    size: Dp = LocalIconSize.current,
    tint: Color = LocalIconTint.current,
    weight: FontWeight = LocalIconWeight.current,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalIconFont provides iconFont,
        LocalIconSize provides size,
        LocalIconTint provides tint,
        LocalIconWeight provides weight,
        content = content
    )
}

/**
 * A shortcut method to set default values for FontIcon composables. Might be better to just use
 * ProvideIconParameters if it's for setting only one of the default values.
 */
@Composable
public fun ProvideIconParameters(
    iconFont: IconFont = LocalIconFont.current,
    size: Dp = LocalIconSize.current,
    tintProvider: CompositionLocal<Color>? = LocalIconTintProvider.current,
    weight: FontWeight = LocalIconWeight.current,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalIconFont provides iconFont,
        LocalIconSize provides size,
        LocalIconTintProvider provides tintProvider,
        LocalIconWeight provides weight,
        content = content
    )
}
