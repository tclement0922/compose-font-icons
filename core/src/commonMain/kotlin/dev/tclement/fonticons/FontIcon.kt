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

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toolingGraphicsLayer
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints

private object EmptyMeasurePolicy : MeasurePolicy {
    private val placementBlock: Placeable.PlacementScope.() -> Unit = {}
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {
        return layout(constraints.maxWidth, constraints.maxHeight, placementBlock = placementBlock)
    }
}

/**
 * A component that draws the icon [iconName] using [iconFont] (with a default value of [LocalIconFont].
 * The icon will be [size] × [size] dp, and will be tinted with [tint]. If [iconFont] is a variable font,
 * [weight] will applied as a variation setting, or else the font with the nearest weight will be picked.
 *
 * @param iconName the icon name (can be a single character or a string)
 * @param contentDescription the text used by accessibility services to describe what this icon represents.
 * This should always be provided unless this icon is used for decorative purposes, and does not
 * represent a meaningful action that a user can take. This text should be localized, such as by using
 * [org.jetbrains.compose.resources.stringResource] or similar
 * @param modifier the [Modifier] to be applied to this icon
 * @param tint the tint to be applied to this icon, by default the value of [LocalIconTintProvider], or [LocalIconTint] if null
 * @param weight the font weight of the icon, by default [LocalIconWeight]
 * @param iconFont the icon font used to draw this icon, by default [LocalIconFont]
 */
@Composable
public fun FontIcon(
    iconName: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalIconTintProvider.current?.current ?: LocalIconTint.current,
    weight: FontWeight = LocalIconWeight.current,
    iconFont: IconFont = LocalIconFont.current
) {
    val fontFamilyResolver = LocalFontFamilyResolver.current
    val layoutDirection = LocalLayoutDirection.current

    Layout(
        modifier = modifier then Modifier.toolingGraphicsLayer()
            .size(LocalIconSize.current)
            .then(
                FontIconElement(
                    iconName,
                    tint,
                    weight,
                    iconFont,
                    fontFamilyResolver,
                    layoutDirection,
                    contentDescription
                )
            ),
        measurePolicy = EmptyMeasurePolicy
    )
}

/**
 * A component that draws the icon [icon] using [iconFont] (with a default value of [LocalIconFont].
 * The icon will be [size] × [size] dp, and will be tinted with [tint]. If [iconFont] is a variable font,
 * [weight] will applied as a variation setting, or else the font with the nearest weight will be picked.
 *
 * @param icon the icon Unicode character
 * @param contentDescription the text used by accessibility services to describe what this icon represents.
 * This should always be provided unless this icon is used for decorative purposes, and does not
 * represent a meaningful action that a user can take. This text should be localized, such as by using
 * [org.jetbrains.compose.resources.stringResource] or similar
 * @param modifier the [Modifier] to be applied to this icon
 * @param tint the tint to be applied to this icon, by default the value of [LocalIconTintProvider], or [LocalIconTint] if null
 * @param weight the font weight of the icon, by default [LocalIconWeight]
 * @param iconFont the icon font used to draw this icon, by default [LocalIconFont]
 */
@Composable
public fun FontIcon(
    icon: Char,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalIconTintProvider.current?.current ?: LocalIconTint.current,
    weight: FontWeight = LocalIconWeight.current,
    iconFont: IconFont = LocalIconFont.current
) {
    FontIcon(
        iconName = icon.toString(),
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint,
        weight = weight,
        iconFont = iconFont
    )
}
