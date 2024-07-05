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

package dev.tclement.fonticons.glance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.glance.*
import androidx.glance.layout.ContentScale
import androidx.glance.layout.size
import androidx.glance.unit.ColorProvider
import dev.tclement.fonticons.*

/**
 * A component that draws the icon [iconName] using [iconFont] (with a default value of [LocalIconFont].
 * The icon will be [size] × [size] dp, and will be tinted with [tint]. If [iconFont] is a variable font,
 * [weight] will applied as a variation setting, or else the font with the nearest weight will be picked.
 *
 * @param iconName the icon name in the font (can be a single character or a string)
 * @param contentDescription text used by accessibility services to describe what this icon represents.
 * This should always be provided unless this icon is used for decorative purposes, and does not
 * represent a meaningful action that a user can take. This text should be localized, such as by using
 * androidx.compose.ui.res.stringResource or similar
 * @param modifier the [Modifier] to be applied to this icon
 * @param size the size of the icon, by default [LocalIconSize]
 * @param tint the tint to be applied to this icon, by default [LocalGlanceIconTint]
 * @param weight the font weight of the icon, by default [LocalIconWeight]
 * @param iconFont the icon font used to draw this icon, by default [LocalIconFont]
 */
@Composable
@GlanceComposable
public fun FontIcon(
    iconName: String,
    contentDescription: String?,
    modifier: GlanceModifier = GlanceModifier,
    size: Dp = LocalIconSize.current,
    tint: ColorProvider = LocalGlanceIconTint.current,
    weight: FontWeight = LocalIconWeight.current,
    iconFont: IconFont = LocalIconFont.current
) {
    val context = LocalContext.current

    val bitmap = remember(iconName, context, iconFont, size, weight) {
        FontIconBitmap(iconName, context, Color.Black, iconFont, size, weight)
    }

    val colorFilter = if (tint.getColor(context) == Color.Unspecified) null else ColorFilter.tint(tint)

    Image(
        provider = ImageProvider(bitmap = bitmap),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        colorFilter = colorFilter,
        modifier = modifier.size(size)
    )
}

/**
 * A component that draws the icon [icon] using [iconFont] (with a default value of [LocalIconFont].
 * The icon will be [size] × [size] dp, and will be tinted with [tint]. If [iconFont] is a variable font,
 * [weight] will applied as a variation setting, or else the font with the nearest weight will be picked.
 *
 * @param icon the icon unicode character
 * @param contentDescription text used by accessibility services to describe what this icon represents.
 * This should always be provided unless this icon is used for decorative purposes, and does not
 * represent a meaningful action that a user can take. This text should be localized, such as by using
 * androidx.compose.ui.res.stringResource or similar
 * @param modifier the [Modifier] to be applied to this icon
 * @param size the size of the icon, by default [LocalIconSize]
 * @param tint the tint to be applied to this icon, by default [LocalGlanceIconTint]
 * @param weight the font weight of the icon, by default [LocalIconWeight]
 * @param iconFont the icon font used to draw this icon, by default [LocalIconFont]
 */
@Composable
@GlanceComposable
public fun FontIcon(
    icon: Char,
    contentDescription: String?,
    modifier: GlanceModifier = GlanceModifier,
    size: Dp = LocalIconSize.current,
    tint: ColorProvider = LocalGlanceIconTint.current,
    weight: FontWeight = LocalIconWeight.current,
    iconFont: IconFont = LocalIconFont.current
) {
    FontIcon(
        iconName = icon.toString(),
        contentDescription = contentDescription,
        modifier = modifier,
        size = size,
        tint = tint,
        weight = weight,
        iconFont = iconFont
    )
}
