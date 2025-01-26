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

@file:Suppress("FunctionName")

package dev.tclement.fonticons

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private inline fun creatingBitmap(size: Dp, density: Density, block: (Canvas) -> Unit): Bitmap {
    val bitmap = with(density) {
        Bitmap.createBitmap(size.roundToPx(), size.roundToPx(), Bitmap.Config.ARGB_8888)
    }
    val canvas = Canvas(bitmap)
    block(canvas)
    return bitmap
}

/**
 * A function that draws the icon [iconName] using the [typeface] into a new [Bitmap].
 * The icon will be [size] × [size] dp, and will be tinted with [tint].
 *
 * @param iconName the icon name (icon aliases/font ligatures are supported)
 * @param typeface the [Paint] used to draw this icon
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 * @param tint the tint to be applied to this icon
 * @param size the size of the icon, by default 24 dp
 */
public fun FontIconBitmap(
    iconName: String,
    typeface: Typeface,
    fontFeatureSettings: String?,
    tint: Color,
    density: Density,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp
): Bitmap = creatingBitmap(size, density) { canvas ->
    canvas.drawFontIcon(iconName, typeface, fontFeatureSettings, tint, density, size)
}

/**
 * A function that draws the [icon] using the [typeface] into a new [Bitmap].
 * The icon will be [size] × [size] dp, and will be tinted with [tint].
 *
 * @param icon the icon Unicode character
 * @param typeface the [Paint] used to draw this icon
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 * @param tint the tint to be applied to this icon
 * @param size the size of the icon, by default 24 dp
 */
public fun FontIconBitmap(
    icon: Char,
    typeface: Typeface,
    fontFeatureSettings: String?,
    tint: Color,
    density: Density,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp
): Bitmap = creatingBitmap(size, density) { canvas ->
    canvas.drawFontIcon(icon, typeface, fontFeatureSettings, tint, density, size)
}

/**
 * A function that draws the icon [iconName] using the [iconFont] into a new [Bitmap].
 * The icon will be [size] × [size] dp, and will be tinted with [tint].
 *
 * @param iconName the icon name (icon aliases/font ligatures are supported)
 * @param iconFont the [StaticIconFont] used to draw this icon
 * @param tint the tint to be applied to this icon
 * @param context the [Context] used to access some typefaces
 * @param size the size of the icon, by default 24 dp
 * @param weight the font weight of the icon, by default [FontWeight.Normal]
 */
public fun FontIconBitmap(
    iconName: String,
    iconFont: StaticIconFont,
    tint: Color,
    context: Context,
    density: Density,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
): Bitmap = creatingBitmap(size, density) { canvas ->
    canvas.drawFontIcon(iconName, iconFont, tint, context, density, size, weight)
}

/**
 * A function that draws the [icon] using the [iconFont] into a new [Bitmap].
 * The icon will be [size] × [size] dp, and will be tinted with [tint].
 *
 * @param icon the icon Unicode character
 * @param iconFont the [StaticIconFont] used to draw this icon
 * @param tint the tint to be applied to this icon
 * @param context the [Context] used to access some typefaces
 * @param size the size of the icon, by default 24 dp
 * @param weight the font weight of the icon, by default [FontWeight.Normal]
 */
public fun FontIconBitmap(
    icon: Char,
    iconFont: StaticIconFont,
    tint: Color,
    context: Context,
    density: Density,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
): Bitmap = creatingBitmap(size, density) { canvas ->
    canvas.drawFontIcon(icon, iconFont, tint, context, density, size, weight)
}

/**
 * A function that draws the icon [iconName] using the variable [typeface] into a new [Bitmap].
 * The icon will be [size] × [size] dp, and will be tinted with [tint].
 *
 * @param iconName the icon name (icon aliases/font ligatures are supported)
 * @param typeface the [Paint] used to draw this icon
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz') or the weight
 * ('wght')
 * @param tint the tint to be applied to this icon
 * @param density the [Density] used to compute icon dimensions and variation values
 * @param size the size of the icon, by default 24 dp
 * @param weight the font weight of the icon, by default [FontWeight.Normal]
 */
public fun FontIconBitmap(
    iconName: String,
    typeface: Typeface,
    fontFeatureSettings: String?,
    fontVariationSettings: FontVariation.Settings,
    tint: Color,
    density: Density,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
): Bitmap = creatingBitmap(size, density) { canvas ->
    canvas.drawFontIcon(iconName, typeface, fontFeatureSettings, fontVariationSettings, tint, density, size, weight)
}

/**
 * A function that draws the [icon] using the variable [typeface] into a new [Bitmap].
 * The icon will be [size] × [size] dp, and will be tinted with [tint].
 *
 * @param icon the icon Unicode character
 * @param typeface the [Paint] used to draw this icon
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz') or the weight
 * ('wght')
 * @param tint the tint to be applied to this icon
 * @param density the [Density] used to compute icon dimensions and variation values
 * @param size the size of the icon, by default 24 dp
 * @param weight the font weight of the icon, by default [FontWeight.Normal]
 */
public fun FontIconBitmap(
    icon: Char,
    typeface: Typeface,
    fontFeatureSettings: String?,
    fontVariationSettings: FontVariation.Settings,
    tint: Color,
    density: Density,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
): Bitmap = creatingBitmap(size, density) { canvas ->
    canvas.drawFontIcon(icon, typeface, fontFeatureSettings, fontVariationSettings, tint, density, size, weight)
}

/**
 * A function that draws the icon [iconName] using the variable [iconFont] into a new [Bitmap].
 * The icon will be [size] × [size] dp, and will be tinted with [tint].
 *
 * @param iconName the icon name (icon aliases/font ligatures are supported)
 * @param iconFont the [VariableIconFont] used to draw this icon
 * @param tint the tint to be applied to this icon
 * @param context the [Context] used to access some typefaces
 * @param density the [Density] used to compute icon dimensions and variation values
 * @param size the size of the icon, by default 24 dp
 * @param weight the font weight of the icon, by default [FontWeight.Normal]
 */
public fun FontIconBitmap(
    iconName: String,
    iconFont: VariableIconFont,
    tint: Color,
    context: Context,
    density: Density,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
): Bitmap = creatingBitmap(size, density) { canvas ->
    canvas.drawFontIcon(iconName, iconFont, tint, context, density, size, weight)
}

/**
 * A function that draws the [icon] using the variable [iconFont] into a new [Bitmap].
 * The icon will be [size] × [size] dp, and will be tinted with [tint].
 *
 * @param icon the icon Unicode character
 * @param iconFont the [VariableIconFont] used to draw this icon
 * @param tint the tint to be applied to this icon
 * @param context the [Context] used to access some typefaces
 * @param density the [Density] used to compute icon dimensions and variation values
 * @param size the size of the icon, by default 24 dp
 * @param weight the font weight of the icon, by default [FontWeight.Normal]
 */
public fun FontIconBitmap(
    icon: Char,
    iconFont: VariableIconFont,
    tint: Color,
    context: Context,
    density: Density,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
): Bitmap = creatingBitmap(size, density) { canvas ->
    canvas.drawFontIcon(icon, iconFont, tint, context, density, size, weight)
}
