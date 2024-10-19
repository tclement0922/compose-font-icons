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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Typeface
import androidx.compose.ui.unit.Density
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.getFontResourceBytes
import org.jetbrains.skia.Data
import org.jetbrains.skia.FontMgr
import org.jetbrains.skia.FontVariation as SkFontVariation
import org.jetbrains.skia.Typeface as SkTypeface

/**
 * Skiko (Kotlin's Skia wrapper) implementation of [VariableIconFont].
 */
internal class VariableIconFontSkikoImpl(
    private val alias: String,
    private val typefaceConstructor: (variationSettings: FontVariation.Settings) -> SkTypeface,
    private val weights: Array<out FontWeight>,
    override val variationSettings: Array<out FontVariation.Setting>,
    override val featureSettings: String?
) : VariableIconFont() {
    private val fontFamilies: MutableMap<Pair<Float, FontWeight>, FontFamily> = mutableMapOf()
    override val opticalSizePreset: Boolean = variationSettings.any { it.axisName == "opsz" }

    override fun textStyleWeightFor(weight: FontWeight): FontWeight {
        return FontWeight.W400
    }

    override fun getFontFamily(
        size: Float,
        weight: FontWeight
    ): FontFamily {
        return fontFamilies.getOrPut((if (opticalSizePreset) -1f else size) to weight) {
            val variationSettings = if (opticalSizePreset) {
                FontVariation.Settings(
                    weight = weights.nearestOf(weight),
                    style = FontStyle.Normal,
                    *variationSettings
                )
            } else {
                FontVariation.Settings(
                    weight = weights.nearestOf(weight),
                    style = FontStyle.Normal,
                    FontVariation.Setting("opsz", size),
                    *variationSettings
                )
            }
            FontFamily(
                Typeface(
                    typeface = typefaceConstructor(variationSettings).apply {
                        this.isBold
                    },
                    // Generate a different alias for each requested variation,
                    // or else it would always return the first requested one
                    alias = "${variationSettings.hashCode()}-$alias"
                )
            )
        }
    }
}

/**
 * Creates a variable [IconFont] using a Skia Typeface.
 * @param alias the internal name to differentiate the typeface
 * @param baseTypeface the base Skia [SkTypeface] that will be cloned for each required variation settings
 * @param weights the supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 * @param density the density of the screen, used to convert the icon size to pixels
 */
@Composable
public fun rememberVariableIconFont(
    alias: String,
    baseTypeface: SkTypeface,
    weights: Array<FontWeight>,
    fontVariationSettings: Array<FontVariation.Setting> = emptyArray(),
    fontFeatureSettings: String? = null,
    density: Density = LocalDensity.current
): VariableIconFont =
    remember(alias, baseTypeface, weights, fontVariationSettings, fontFeatureSettings, density) {
        VariableIconFontSkikoImpl(
            alias = alias,
            typefaceConstructor = { settings ->
                baseTypeface.makeClone(
                    settings.settings.map { setting ->
                        SkFontVariation(setting.axisName, setting.toVariationValue(density))
                    }.toTypedArray()
                )
            },
            weights = weights,
            featureSettings = fontFeatureSettings,
            variationSettings = fontVariationSettings
        )
    }

/**
 * Creates a variable [IconFont] using a byte array with loaded font data.
 * @param alias the internal name to differentiate the typeface
 * @param data a byte array with loaded font data
 * @param weights the supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 * @param density the density of the screen, used to convert the icon size to pixels
 */
@Composable
public fun rememberVariableIconFont(
    alias: String,
    data: ByteArray,
    weights: Array<FontWeight>,
    fontVariationSettings: Array<FontVariation.Setting> = emptyArray(),
    fontFeatureSettings: String? = null,
    density: Density = LocalDensity.current
): VariableIconFont = rememberVariableIconFont(
    alias = alias,
    baseTypeface = FontMgr.default.makeFromData(Data.makeFromBytes(data))!!,
    weights = weights,
    fontVariationSettings = fontVariationSettings,
    fontFeatureSettings = fontFeatureSettings,
    density = density
)

@OptIn(ExperimentalResourceApi::class)
@Composable
public actual fun rememberVariableIconFont(
    fontResource: FontResource,
    weights: Array<FontWeight>,
    fontVariationSettings: Array<FontVariation.Setting>,
    fontFeatureSettings: String?
): VariableIconFont {
    val environment = LocalIconResourceEnvironment.current
    val typeface by produceState(SkTypeface.makeEmpty(), environment, fontResource) {
        val bytes = getFontResourceBytes(environment, fontResource)
        value = FontMgr.default.makeFromData(Data.makeFromBytes(bytes))!!
    }
    return rememberVariableIconFont(
        alias = typeface.uniqueId.toString(),
        baseTypeface = typeface,
        weights = weights,
        fontVariationSettings = fontVariationSettings,
        fontFeatureSettings = fontFeatureSettings,
        density = LocalDensity.current
    )
}

/**
 * Creates a variable [IconFont] using a Skia Typeface.
 *
 * This function is not composable, use [rememberVariableIconFont] when in a composition
 * @param alias the internal name to differentiate the typeface
 * @param baseTypeface the base Skia [SkTypeface] that will be cloned for each required variation settings
 * @param weights the supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 * @param density the density of the screen, used to convert the icon size to pixels
 */
public fun createVariableIconFont(
    alias: String,
    baseTypeface: SkTypeface,
    weights: Array<FontWeight>,
    fontVariationSettings: Array<FontVariation.Setting> = emptyArray(),
    fontFeatureSettings: String? = null,
    density: Density
): VariableIconFont =
    VariableIconFontSkikoImpl(
        alias = alias,
        typefaceConstructor = { settings ->
            baseTypeface.makeClone(
                settings.settings.map { setting ->
                    SkFontVariation(setting.axisName, setting.toVariationValue(density))
                }.toTypedArray()
            )
        },
        weights = weights,
        featureSettings = fontFeatureSettings,
        variationSettings = fontVariationSettings
    )