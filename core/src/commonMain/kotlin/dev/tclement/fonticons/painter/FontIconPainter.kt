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

package dev.tclement.fonticons.painter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import dev.tclement.fonticons.*

private class FontIconPainter(
    private val iconName: String,
    private val iconFont: IconFont,
    private val fontWeight: FontWeight,
    private val fontFamilyResolver: FontFamily.Resolver,
    private val tint: Color,
    override val intrinsicSize: Size
) : Painter() {
    private val drawCache: DrawCache = DrawCache()
    private var lastSize: Size = Size.Unspecified

    override fun DrawScope.onDraw() = with(drawCache) {
        if (!isInitialized || size != lastSize) {
            recalculate(
                iconName,
                iconFont,
                fontWeight,
                fontFamilyResolver,
            )
            lastSize = size
        }
        draw(tint)
    }
}

/**
 * Returns a [Painter] that draws the icon [iconName] using [iconFont] (with a default value of [LocalIconFont]).
 * The painter's intrinsic size will be [size] × [size] dp, and will be tinted with [tint]. If [iconFont] is a variable
 * font, [weight] will be applied as a variation setting, or else the font with the nearest weight will be picked.
 *
 * @param iconName the icon name (can be a single character or a string)
 * @param tint the tint to be applied to this icon, by default the value of [LocalIconTintProvider], or [LocalIconTint] if null
 * @param weight the font weight of the icon, by default [LocalIconWeight]
 * @param iconFont the icon font used to draw this icon, by default [LocalIconFont]
 * @param size the size of the icon, by default [LocalIconSize]
 */
@Composable
public fun rememberFontIconPainter(
    iconName: String,
    tint: Color = LocalIconTintProvider.current?.current ?: LocalIconTint.current,
    weight: FontWeight = LocalIconWeight.current,
    iconFont: IconFont = LocalIconFont.current,
    size: Dp = LocalIconSize.current,
): Painter {
    val fontFamilyResolver = LocalFontFamilyResolver.current
    val density = LocalDensity.current

    return remember(iconName, iconFont, weight, fontFamilyResolver, tint, density, size) {
        val intrinsicSize = if (size == Dp.Unspecified) Size.Unspecified else with(density) {
            Size(size.toPx(), size.toPx())
        }
        FontIconPainter(iconName, iconFont, weight, fontFamilyResolver, tint, intrinsicSize)
    }
}

/**
 * Returns a [Painter] that draws the icon [icon] using [iconFont] (with a default value of [LocalIconFont]).
 * The painter's intrinsic size will be [size] × [size] dp, and will be tinted with [tint]. If [iconFont] is a variable
 * font, [weight] will be applied as a variation setting, or else the font with the nearest weight will be picked.
 *
 * @param icon the icon Unicode character
 * @param tint the tint to be applied to this icon, by default the value of [LocalIconTintProvider], or [LocalIconTint] if null
 * @param weight the font weight of the icon, by default [LocalIconWeight]
 * @param iconFont the icon font used to draw this icon, by default [LocalIconFont]
 * @param size the size of the icon, by default [LocalIconSize]
 */
@Composable
public fun rememberFontIconPainter(
    icon: Char,
    tint: Color = LocalIconTintProvider.current?.current ?: LocalIconTint.current,
    weight: FontWeight = LocalIconWeight.current,
    iconFont: IconFont = LocalIconFont.current,
    size: Dp = LocalIconSize.current,
): Painter = rememberFontIconPainter(
    iconName = icon.toString(),
    tint = tint,
    weight = weight,
    iconFont = iconFont,
    size = size
)
