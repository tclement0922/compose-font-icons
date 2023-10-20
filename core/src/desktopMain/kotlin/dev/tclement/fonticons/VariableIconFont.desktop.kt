/*
 * Copyright 2023 T. Clément (@tclement0922)
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
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Typeface
import androidx.compose.ui.unit.Density
import org.jetbrains.skia.Data
import org.jetbrains.skia.makeFromFile
import java.io.File
import org.jetbrains.skia.FontVariation as SkFontVariation
import org.jetbrains.skia.Typeface as SkTypeface

internal class VariableIconFontDesktopImpl(
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

private fun SkTypeface.Companion.makeFromResource(resourceName: String): SkTypeface {
    val contextClassLoader = Thread.currentThread().contextClassLoader!!
    val resource = contextClassLoader.getResourceAsStream(resourceName)
        ?: (::makeFromResource.javaClass).getResourceAsStream(resourceName)
        ?: error("Can't load font from $resourceName")

    val bytes = resource.use { it.readBytes() }
    return makeFromData(Data.makeFromBytes(bytes))
}

/**
 * Creates a variable [IconFont]
 * @param alias internal name to differentiate the typeface
 * @param baseTypeface base Skia Typeface (aliased as [SkTypeface]) that will be cloned for each
 * required variation settings
 * @param weights supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@Composable
public fun rememberVariableIconFont(
    alias: String,
    baseTypeface: SkTypeface,
    weights: Array<FontWeight>,
    fontVariationSettings: Array<FontVariation.Setting> = emptyArray(),
    fontFeatureSettings: String? = null,
    density: Density = LocalDensity.current
): IconFont =
    remember(alias, baseTypeface, weights, fontVariationSettings, fontFeatureSettings, density) {
        VariableIconFontDesktopImpl(
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
 * Creates a variable [IconFont] using a resource name
 * @param resource The resource name in classpath
 * @param weights supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@Composable
public fun rememberVariableIconFont(
    resource: String,
    weights: Array<FontWeight>,
    fontVariationSettings: Array<FontVariation.Setting> = emptyArray(),
    fontFeatureSettings: String? = null,
    density: Density = LocalDensity.current
): IconFont = rememberVariableIconFont(
    alias = resource,
    baseTypeface = SkTypeface.makeFromResource(resource),
    weights = weights,
    fontVariationSettings = fontVariationSettings,
    fontFeatureSettings = fontFeatureSettings,
    density = density
)

/**
 * Creates a variable [IconFont] using a resource name
 * @param file The resource name in classpath
 * @param weights supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@Composable
public fun rememberVariableIconFont(
    file: File,
    weights: Array<FontWeight>,
    fontVariationSettings: Array<FontVariation.Setting> = emptyArray(),
    fontFeatureSettings: String? = null,
    density: Density = LocalDensity.current
): IconFont = rememberVariableIconFont(
    alias = file.name,
    baseTypeface = SkTypeface.makeFromFile(file.absolutePath),
    weights = weights,
    fontVariationSettings = fontVariationSettings,
    fontFeatureSettings = fontFeatureSettings,
    density = density
)

/**
 * Creates a variable [IconFont] using a byte array with loaded font data.
 * @param alias internal name to differentiate the typeface
 * @param data byte array with loaded font data
 * @param weights supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@Composable
public fun rememberVariableIconFont(
    alias: String,
    data: ByteArray,
    weights: Array<FontWeight>,
    fontVariationSettings: Array<FontVariation.Setting> = emptyArray(),
    fontFeatureSettings: String? = null,
    density: Density = LocalDensity.current
): IconFont = rememberVariableIconFont(
    alias = alias,
    baseTypeface = SkTypeface.makeFromData(Data.makeFromBytes(data)),
    weights = weights,
    fontVariationSettings = fontVariationSettings,
    fontFeatureSettings = fontFeatureSettings,
    density = density
)
