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

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat

private fun FontFamily.getTypefaceForWeight(weight: FontWeight, context: Context): Typeface? {
    this as? FontListFontFamily ?: error("Unknown font family type")
    return when (val font = fonts.nearestOf(weight)) {
        is AndroidFont -> {
            font.typefaceLoader.loadBlocking(context, font)
        }

        is ResourceFont -> {
            try {
                ResourcesCompat.getFont(context, font.resId)
            } catch (e: Resources.NotFoundException) {
                null
            }
        }

        else -> error("Unknown font type ${font::class.qualifiedName}")
    }
}

/**
 * An extension function that draws the icon [iconName] using [iconFont] into the receiver [Canvas].
 * The icon will be [size] × [size] dp, and will be tinted with [tint]. If [iconFont] is a variable font,
 * [weight] will applied as a variation setting, or else the font with the nearest weight will be picked.
 *
 * @param iconName the icon name (can be a single character or a string)
 * @param context the [Context] used to get specific values like density or layout direction
 * @param tint the tint to be applied to this icon
 * @param iconFont the icon font used to draw this icon
 * @param size the size of the icon, by default 24 dp
 * @param weight the font weight of the icon, by default [FontWeight.Normal]
 * @param density the [Density] used to compute icon dimensions, by default created from [context]
 */
public fun Canvas.drawIcon(
    iconName: String,
    context: Context,
    tint: Color,
    iconFont: IconFont,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT),
    density: Density = Density(context)
) {
    val typeface = iconFont.getFontFamily(size.value, weight).getTypefaceForWeight(weight, context)

    val sizePx = with(density) {
        size.toPx()
    }

    val paint = Paint()

    paint.setTypeface(typeface)
    paint.fontFeatureSettings = iconFont.featureSettings
    if (iconFont is VariableIconFontAndroidImpl && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val variationSettings = iconFont.variationSettings.toMutableList()
        variationSettings += FontVariation.weight(weight.weight)
        if (!iconFont.opticalSizePreset) {
            variationSettings += FontVariation.Setting("opsz", size.value)
        }
        paint.fontVariationSettings = variationSettings
            .joinToString(separator = ", ") { setting ->
                "'${setting.axisName}' ${setting.toVariationValue(density)}"
            }
    }
    paint.color = tint.toArgb()
    paint.letterSpacing = 0f
    paint.textSize = sizePx
    paint.textAlign = Paint.Align.LEFT

    val bounds = Rect()
    paint.getTextBounds(iconName, 0, iconName.length, bounds)
    val textWidth = bounds.left + bounds.right

    var scale = 1f
    if (textWidth > sizePx) {
        scale = sizePx / textWidth
    }
    paint.textSize *= scale

    drawText(
        iconName,
        sizePx / 2f - bounds.width() * scale / 2f - bounds.left * scale,
        sizePx / 2f + bounds.height() * scale / 2f - bounds.bottom * scale,
        paint
    )
}

/**
 * An extension function that draws the icon [icon] using [iconFont] into the receiver [Canvas].
 * The icon will be [size] × [size] dp, and will be tinted with [tint]. If [iconFont] is a variable font,
 * [weight] will applied as a variation setting, or else the font with the nearest weight will be picked.
 *
 * @param icon the icon unicode character
 * @param context the [Context] used to get specific values like density or layout direction
 * @param tint the tint to be applied to this icon
 * @param iconFont the icon font used to draw this icon
 * @param size the size of the icon, by default 24 dp
 * @param weight the font weight of the icon, by default [FontWeight.Normal]
 * @param density the [Density] used to compute icon dimensions, by default created from [context]
 */
public fun Canvas.drawIcon(
    icon: Char,
    context: Context,
    tint: Color,
    iconFont: IconFont,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT),
    density: Density = Density(context)
) {
    drawIcon(icon.toString(), context, tint, iconFont, size, weight, density)
}
