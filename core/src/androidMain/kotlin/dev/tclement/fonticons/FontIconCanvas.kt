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
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat

private fun FontFamily.getTypefaceForWeight(weight: FontWeight, context: Context): Typeface {
    this as? FontListFontFamily ?: error("Unknown font family type")
    return when (val font = fonts.nearestOf(weight)) {
        is AndroidFont -> {
            font.typefaceLoader.loadBlocking(context, font) ?: error("An error occurred while loading the font")
        }

        is ResourceFont -> {
            try {
                ResourcesCompat.getFont(context, font.resId) ?: error("Font resource not found")
            } catch (e: Resources.NotFoundException) {
                error("Font resource not found")
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
@Deprecated(
    message = "This function will be removed in a future release.",
    replaceWith = ReplaceWith("drawFontIcon(iconName, iconFont, tint, context, density, size, weight)"),
    level = DeprecationLevel.WARNING
)
public fun Canvas.drawIcon(
    iconName: String,
    context: Context,
    tint: Color,
    iconFont: IconFont,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT),
    density: Density = Density(context)
) {
    val typeface = when (iconFont) {
        is StaticIconFont -> iconFont.fontFamily
        is VariableIconFont -> iconFont.getFontFamily(size.value, weight)
    }.getTypefaceForWeight(weight, context)

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
 * An extension function that draws the [icon] using [iconFont] into the receiver [Canvas].
 * The icon will be [size] × [size] dp, and will be tinted with [tint]. If [iconFont] is a variable font,
 * [weight] will applied as a variation setting, or else the font with the nearest weight will be picked.
 *
 * @param icon the icon Unicode character
 * @param context the [Context] used to get specific values like density or layout direction
 * @param tint the tint to be applied to this icon
 * @param iconFont the icon font used to draw this icon
 * @param size the size of the icon, by default 24 dp
 * @param weight the font weight of the icon, by default [FontWeight.Normal]
 * @param density the [Density] used to compute icon dimensions, by default created from [context]
 */
@Deprecated(
    message = "This function will be removed in a future release.",
    replaceWith = ReplaceWith("drawFontIcon(icon, iconFont, tint, context, density, size, weight)"),
    level = DeprecationLevel.WARNING
)
public fun Canvas.drawIcon(
    icon: Char,
    context: Context,
    tint: Color,
    iconFont: IconFont,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT),
    density: Density = Density(context)
) {
    @Suppress("Deprecation")
    drawIcon(icon.toString(), context, tint, iconFont, size, weight, density)
}

private fun Canvas.drawFontIcon(
    iconName: String,
    typeface: Typeface,
    fontFeatureSettings: String?,
    tint: Color,
    size: Float,
    paint: Paint
) {
    paint.setTypeface(typeface)
    paint.fontFeatureSettings = fontFeatureSettings
    paint.color = tint.toArgb()
    paint.letterSpacing = 0f
    paint.textSize = size
    paint.textAlign = Paint.Align.LEFT

    val bounds = Rect()
    paint.getTextBounds(iconName, 0, iconName.length, bounds)
    val textWidth = bounds.left + bounds.right

    var scale = 1f
    if (textWidth > size) {
        scale = size / textWidth
    }
    paint.textSize *= scale

    drawText(
        iconName,
        size / 2f - bounds.width() * scale / 2f - bounds.left * scale,
        size / 2f + bounds.height() * scale / 2f - bounds.bottom * scale,
        paint
    )
}

/**
 * An extension function that draws the icon [iconName] using the [typeface] into the receiver [Canvas].
 * The icon will be [size] × [size] dp, and will be tinted with [tint].
 *
 * @param iconName the icon name (icon aliases/font ligatures are supported)
 * @param typeface the [Paint] used to draw this icon
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 * @param tint the tint to be applied to this icon
 * @param size the size of the icon, by default 24 dp
 */
public fun Canvas.drawFontIcon(
    iconName: String,
    typeface: Typeface,
    fontFeatureSettings: String?,
    tint: Color,
    density: Density,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp
) {
    drawFontIcon(iconName, typeface, fontFeatureSettings, tint, with(density) { size.toPx() }, Paint())
}

/**
 * An extension function that draws the [icon] using the [typeface] into the receiver [Canvas].
 * The icon will be [size] × [size] dp, and will be tinted with [tint].
 *
 * @param icon the icon Unicode character
 * @param typeface the [Paint] used to draw this icon
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 * @param tint the tint to be applied to this icon
 * @param size the size of the icon, by default 24 dp
 */
public fun Canvas.drawFontIcon(
    icon: Char,
    typeface: Typeface,
    fontFeatureSettings: String?,
    tint: Color,
    density: Density,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp
) {
    drawFontIcon(icon.toString(), typeface, fontFeatureSettings, tint, density, size)
}

/**
 * An extension function that draws the icon [iconName] using the [iconFont] into the receiver [Canvas].
 * The icon will be [size] × [size] dp, and will be tinted with [tint].
 *
 * @param iconName the icon name (icon aliases/font ligatures are supported)
 * @param iconFont the [StaticIconFont] used to draw this icon
 * @param tint the tint to be applied to this icon
 * @param context the [Context] used to access some typefaces
 * @param size the size of the icon, by default 24 dp
 * @param weight the font weight of the icon, by default [FontWeight.Normal]
 */
public fun Canvas.drawFontIcon(
    iconName: String,
    iconFont: StaticIconFont,
    tint: Color,
    context: Context,
    density: Density,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
) {
    drawFontIcon(
        iconName,
        iconFont.fontFamily.getTypefaceForWeight(weight, context),
        iconFont.featureSettings,
        tint,
        with(density) { size.toPx() },
        Paint()
    )
}

/**
 * An extension function that draws the [icon] using the [iconFont] into the receiver [Canvas].
 * The icon will be [size] × [size] dp, and will be tinted with [tint].
 *
 * @param icon the icon Unicode character
 * @param iconFont the [StaticIconFont] used to draw this icon
 * @param tint the tint to be applied to this icon
 * @param context the [Context] used to access some typefaces
 * @param size the size of the icon, by default 24 dp
 * @param weight the font weight of the icon, by default [FontWeight.Normal]
 */
public fun Canvas.drawFontIcon(
    icon: Char,
    iconFont: StaticIconFont,
    tint: Color,
    context: Context,
    density: Density,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
) {
    drawFontIcon(icon.toString(), iconFont, tint, context, density, size, weight)
}

/**
 * An extension function that draws the icon [iconName] using the variable [typeface] into the receiver [Canvas].
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
@RequiresApi(Build.VERSION_CODES.O)
public fun Canvas.drawFontIcon(
    iconName: String,
    typeface: Typeface,
    fontFeatureSettings: String?,
    fontVariationSettings: Array<out FontVariation.Setting>,
    tint: Color,
    density: Density,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
) {
    val paint = Paint()

    val variationSettings = fontVariationSettings.toMutableList()
    variationSettings += FontVariation.weight(weight.weight)
    if (!variationSettings.any { it.axisName == "opsz" }) {
        variationSettings += FontVariation.Setting("opsz", size.value)
    }
    paint.fontVariationSettings = variationSettings
        .joinToString(separator = ", ") { setting ->
            "'${setting.axisName}' ${setting.toVariationValue(density)}"
        }

    drawFontIcon(iconName, typeface, fontFeatureSettings, tint, with(density) { size.toPx() }, paint)
}

/**
 * An extension function that draws the [icon] using the variable [typeface] into the receiver [Canvas].
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
@RequiresApi(Build.VERSION_CODES.O)
public fun Canvas.drawFontIcon(
    icon: Char,
    typeface: Typeface,
    fontFeatureSettings: String?,
    fontVariationSettings: Array<out FontVariation.Setting>,
    tint: Color,
    density: Density,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
) {
    drawFontIcon(icon.toString(), typeface, fontFeatureSettings, fontVariationSettings, tint, density, size, weight)
}

/**
 * An extension function that draws the icon [iconName] using the variable [iconFont] into the receiver [Canvas].
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
@RequiresApi(Build.VERSION_CODES.O)
public fun Canvas.drawFontIcon(
    iconName: String,
    iconFont: VariableIconFont,
    tint: Color,
    context: Context,
    density: Density,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
) {
    drawFontIcon(
        iconName,
        iconFont.getFontFamily(size.value, weight).getTypefaceForWeight(weight, context),
        iconFont.featureSettings,
        iconFont.variationSettings,
        tint,
        density,
        size,
        weight
    )
}

/**
 * An extension function that draws the [icon] using the variable [iconFont] into the receiver [Canvas].
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
@RequiresApi(Build.VERSION_CODES.O)
public fun Canvas.drawFontIcon(
    icon: Char,
    iconFont: VariableIconFont,
    tint: Color,
    context: Context,
    density: Density,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
) {
    drawFontIcon(icon.toString(), iconFont, tint, context, density, size, weight)
}
