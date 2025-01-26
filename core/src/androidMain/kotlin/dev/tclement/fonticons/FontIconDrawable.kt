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
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

public class FontIconDrawable private constructor(
    private val sizePx: Int,
    private val drawFn: (Canvas) -> Unit
) : Drawable() {
    /**
     * A drawable that draws the icon [iconName] using the [typeface].
     * The icon will be [size] × [size] dp, and will be tinted with [tint].
     *
     * @param iconName the icon name (icon aliases/font ligatures are supported)
     * @param typeface the [Paint] used to draw this icon
     * @param fontFeatureSettings the font feature settings, written in a CSS syntax
     * @param tint the tint to be applied to this icon
     * @param size the size of the icon, by default 24 dp
     */
    public constructor(
        iconName: String,
        typeface: Typeface,
        fontFeatureSettings: String?,
        tint: Color,
        density: Density,
        size: Dp = DEFAULT_ICON_SIZE_DP.dp
    ) : this(
        sizePx = with(density) { size.roundToPx() },
        drawFn = { canvas ->
            canvas.drawFontIcon(iconName, typeface, fontFeatureSettings, tint, density, size)
        }
    )

    /**
     * A drawable that draws the [icon] using the [typeface].
     * The icon will be [size] × [size] dp, and will be tinted with [tint].
     *
     * @param icon the icon Unicode character
     * @param typeface the [Paint] used to draw this icon
     * @param fontFeatureSettings the font feature settings, written in a CSS syntax
     * @param tint the tint to be applied to this icon
     * @param size the size of the icon, by default 24 dp
     */
    public constructor(
        icon: Char,
        typeface: Typeface,
        fontFeatureSettings: String?,
        tint: Color,
        density: Density,
        size: Dp = DEFAULT_ICON_SIZE_DP.dp
    ) : this(
        sizePx = with(density) { size.roundToPx() },
        drawFn = { canvas ->
            canvas.drawFontIcon(icon, typeface, fontFeatureSettings, tint, density, size)
        }
    )

    /**
     * A drawable that draws the icon [iconName] using the [iconFont].
     * The icon will be [size] × [size] dp, and will be tinted with [tint].
     *
     * @param iconName the icon name (icon aliases/font ligatures are supported)
     * @param iconFont the [StaticIconFont] used to draw this icon
     * @param tint the tint to be applied to this icon
     * @param context the [Context] used to access some typefaces
     * @param size the size of the icon, by default 24 dp
     * @param weight the font weight of the icon, by default [FontWeight.Normal]
     */
    public constructor(
        iconName: String,
        iconFont: StaticIconFont,
        tint: Color,
        context: Context,
        density: Density,
        size: Dp = DEFAULT_ICON_SIZE_DP.dp,
        weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
    ) : this(
        sizePx = with(density) { size.roundToPx() },
        drawFn = { canvas ->
            canvas.drawFontIcon(iconName, iconFont, tint, context, density, size, weight)
        }
    )

    /**
     * A drawable that draws the [icon] using the [iconFont].
     * The icon will be [size] × [size] dp, and will be tinted with [tint].
     *
     * @param icon the icon Unicode character
     * @param iconFont the [StaticIconFont] used to draw this icon
     * @param tint the tint to be applied to this icon
     * @param context the [Context] used to access some typefaces
     * @param size the size of the icon, by default 24 dp
     * @param weight the font weight of the icon, by default [FontWeight.Normal]
     */
    public constructor(
        icon: Char,
        iconFont: StaticIconFont,
        tint: Color,
        context: Context,
        density: Density,
        size: Dp = DEFAULT_ICON_SIZE_DP.dp,
        weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
    ) : this(
        sizePx = with(density) { size.roundToPx() },
        drawFn = { canvas ->
            canvas.drawFontIcon(icon, iconFont, tint, context, density, size, weight)
        }
    )

    /**
     * A drawable that draws the icon [iconName] using the variable [typeface].
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
    public constructor(
        iconName: String,
        typeface: Typeface,
        fontFeatureSettings: String?,
        fontVariationSettings: Array<out FontVariation.Setting>,
        tint: Color,
        density: Density,
        size: Dp = DEFAULT_ICON_SIZE_DP.dp,
        weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
    ) : this(
        sizePx = with(density) { size.roundToPx() },
        drawFn = { canvas ->
            canvas.drawFontIcon(
                iconName,
                typeface,
                fontFeatureSettings,
                fontVariationSettings,
                tint,
                density,
                size,
                weight
            )
        }
    )

    /**
     * A drawable that draws the [icon] using the variable [typeface].
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
    public constructor(
        icon: Char,
        typeface: Typeface,
        fontFeatureSettings: String?,
        fontVariationSettings: Array<out FontVariation.Setting>,
        tint: Color,
        density: Density,
        size: Dp = DEFAULT_ICON_SIZE_DP.dp,
        weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
    ) : this(
        sizePx = with(density) { size.roundToPx() },
        drawFn = { canvas ->
            canvas.drawFontIcon(icon, typeface, fontFeatureSettings, fontVariationSettings, tint, density, size, weight)
        }
    )

    /**
     * A drawable that draws the icon [iconName] using the variable [iconFont].
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
    public constructor(
        iconName: String,
        iconFont: VariableIconFont,
        tint: Color,
        context: Context,
        density: Density,
        size: Dp = DEFAULT_ICON_SIZE_DP.dp,
        weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
    ) : this(
        sizePx = with(density) { size.roundToPx() },
        drawFn = { canvas ->
            canvas.drawFontIcon(iconName, iconFont, tint, context, density, size, weight)
        }
    )

    /**
     * A drawable that draws the [icon] using the variable [iconFont].
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
    public constructor(
        icon: Char,
        iconFont: VariableIconFont,
        tint: Color,
        context: Context,
        density: Density,
        size: Dp = DEFAULT_ICON_SIZE_DP.dp,
        weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
    ) : this(
        sizePx = with(density) { size.roundToPx() },
        drawFn = { canvas ->
            canvas.drawFontIcon(icon, iconFont, tint, context, density, size, weight)
        }
    )

    /**
     * @suppress
     */
    override fun getIntrinsicWidth(): Int {
        return sizePx
    }

    /**
     * @suppress
     */
    override fun getIntrinsicHeight(): Int {
        return sizePx
    }

    /**
     * @suppress
     */
    override fun draw(canvas: Canvas) {
        drawFn(canvas)
    }

    /**
     * @suppress
     */
    @Deprecated(message = "setAlpha is not compatible with FontIconDrawable, this method has no effect")
    override fun setAlpha(alpha: Int) {
    }

    /**
     * @suppress
     */
    @Deprecated(message = "setColorFilter is not compatible with FontIconDrawable, this method has no effect")
    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    /**
     * @suppress
     */
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}