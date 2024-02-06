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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A function that draws the icon [iconName] using [iconFont] into new [Bitmap].
 * The icon will be [size] × [size] dp, and will be tinted with [tint]. If [iconFont] is a variable font,
 * [weight] will applied as a variation setting, or else the font with the nearest weight will be picked.
 *
 * @param iconName the icon name (can be a single character or a string)
 * @param context the [Context] used to get specific values like density or layout direction
 * @param tint the tint to be applied to this icon
 * @param iconFont the icon font used to draw this icon
 * @param size the size of the icon, by default 24 dp
 * @param weight the font weight of the icon, by default [FontWeight.Normal]
 */
public fun FontIconBitmap(
    iconName: String,
    context: Context,
    tint: Color,
    iconFont: IconFont,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
): Bitmap {
    val density = Density(context)
    val bitmap = with(density) {
        Bitmap.createBitmap(size.roundToPx(), size.roundToPx(), Bitmap.Config.ARGB_8888)
    }
    val canvas = Canvas(bitmap)
    canvas.drawIcon(iconName, context, tint, iconFont, size, weight, density)
    return bitmap
}

/**
 * A function that draws the icon [icon] using [iconFont] into new [Bitmap].
 * The icon will be [size] × [size] dp, and will be tinted with [tint]. If [iconFont] is a variable font,
 * [weight] will applied as a variation setting, or else the font with the nearest weight will be picked.
 *
 * @param icon the icon unicode character
 * @param context the [Context] used to get specific values like density or layout direction
 * @param tint the tint to be applied to this icon
 * @param iconFont the icon font used to draw this icon
 * @param size the size of the icon, by default 24 dp
 * @param weight the font weight of the icon, by default [FontWeight.Normal]
 */
public fun FontIconBitmap(
    icon: Char,
    context: Context,
    tint: Color,
    iconFont: IconFont,
    size: Dp = DEFAULT_ICON_SIZE_DP.dp,
    weight: FontWeight = FontWeight(DEFAULT_ICON_WEIGHT)
): Bitmap = FontIconBitmap(icon.toString(), context, tint, iconFont, size, weight)
