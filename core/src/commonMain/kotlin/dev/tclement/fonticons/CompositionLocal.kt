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

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.getSystemResourceEnvironment

/**
 * The default [IconFont] used when none is provided as a parameter of one of the FontIcon composables.
 *
 * No default value (will throw an error if not provided).
 * @throws IllegalStateException if no value is provided.
 */
public val LocalIconFont: ProvidableCompositionLocal<IconFont> = compositionLocalOf {
    error("LocalIconFont is not provided in this composable, please provide one with ProvideIconParameters")
}

/**
 * The default icon size (in [Dp]) used when none is provided in the composable Modifier.
 *
 * 24 dp by default.
 */
public val LocalIconSize: ProvidableCompositionLocal<Dp> = compositionLocalOf { DEFAULT_ICON_SIZE_DP.dp }

/**
 * The default icon tint (a [Color]) used when none is provided as a parameter of one of the FontIcon composables.
 *
 * No default value (will throw an error if not provided).
 * @throws IllegalStateException if no value is provided.
 */
public val LocalIconTint: ProvidableCompositionLocal<Color> = compositionLocalOf {
    error("LocalIconTint is not provided in this composable, please provide one with ProvideIconParameters")
}

/**
 * The default icon tint provider (a [CompositionLocal] for a [Color]) used when none is provided as a parameter of one
 * of the FontIcon composables.
 * If null, the value of [LocalIconTint] will be used instead.
 * When using Material3, should be most likely set to LocalContentColor.
 *
 * null by default.
 */
public val LocalIconTintProvider: ProvidableCompositionLocal<CompositionLocal<Color>?> = compositionLocalOf {
    null
}

/**
 * The default icon weight (a [FontWeight]) used when none is provided as a parameter of one of the FontIcon
 * composables.
 *
 * [FontWeight.W400] by default.
 */
public val LocalIconWeight: ProvidableCompositionLocal<FontWeight> = compositionLocalOf { FontWeight(DEFAULT_ICON_WEIGHT) }

/**
 * The resource environment used by the FontIcon composables that are using the Compose Resources API. Will stay
 * internal until the underlying API becomes stable.
 *
 * [getSystemResourceEnvironment] by default (internally getting the system/app environment).
 */
@ExperimentalResourceApi
internal val LocalIconResourceEnvironment: ProvidableCompositionLocal<ResourceEnvironment> = compositionLocalOf {
    getSystemResourceEnvironment()
}

/**
 * A shortcut method to set default values for FontIcon composables. Might be better to use
 * CompositionLocalProvider if it's for setting only one of the default values.
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
 * A shortcut method to set default values for FontIcon composables. Might be better to use
 * CompositionLocalProvider if it's for setting only one of the default values.
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
