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

import android.content.res.AssetManager
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.annotation.FontRes
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import java.io.File

internal class VariableIconFontAndroidImpl(
    private val fontConstructor: FontConstructor,
    private val weights: Array<out FontWeight>,
    override val variationSettings: Array<out FontVariation.Setting>,
    override val featureSettings: String?
) : VariableIconFont() {
    private val fontFamilies: MutableMap<Float, FontFamily> = mutableMapOf()
    override val opticalSizePreset: Boolean = variationSettings.any { it.axisName == "opsz" }

    override fun textStyleWeightFor(weight: FontWeight): FontWeight {
        return weights.nearestOf(weight)
    }

    init {
        if (opticalSizePreset) {
            Logger.w(
                "VariableIconFont", "Optical size is automatically handled by VariableIconFont, " +
                        "specifying it in variationSettings disables dynamic optical sizing"
            )
            fontFamilies[-1f] = FontFamily(weights.map {
                fontConstructor(
                    it,
                    FontVariation.Settings(
                        weight = it,
                        style = FontStyle.Normal,
                        *variationSettings
                    )
                )
            })
        }
    }

    override fun getFontFamily(
        size: Float,
        weight: FontWeight
    ): FontFamily {
        return fontFamilies.getOrPut(if (opticalSizePreset) -1f else size) {
            FontFamily(weights.map {
                fontConstructor(
                    it,
                    FontVariation.Settings(
                        weight = it,
                        style = FontStyle.Normal,
                        FontVariation.Setting("opsz", size),
                        *variationSettings
                    )
                )
            })
        }
    }
}

public typealias FontConstructor = (weight: FontWeight, variationSettings: FontVariation.Settings) -> Font

/**
 * Creates a variable [IconFont]
 * @param fontConstructor constructor of a [Font] object, taking as parameters the weight ([FontWeight])
 * and the variation settings ([FontVariation.Settings])
 * @param weights supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@Composable
public fun rememberVariableIconFont(
    fontConstructor: FontConstructor,
    weights: Array<FontWeight>,
    fontVariationSettings: Array<FontVariation.Setting> = emptyArray(),
    fontFeatureSettings: String? = null
): IconFont = remember(fontConstructor, weights, fontVariationSettings, fontFeatureSettings) {
    VariableIconFontAndroidImpl(
        fontConstructor,
        weights = weights,
        featureSettings = fontFeatureSettings,
        variationSettings = fontVariationSettings
    )
}

/**
 * Creates a variable [IconFont] using a resource id
 * @param resId Android resource identifier of the font (eg. R.font.my_icon_font)
 * @param weights supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@ExperimentalTextApi
@Composable
public fun rememberVariableIconFont(
    @FontRes resId: Int,
    weights: Array<FontWeight>,
    fontVariationSettings: Array<FontVariation.Setting> = emptyArray(),
    fontFeatureSettings: String? = null
): IconFont {
    val constructor: FontConstructor = remember(resId) {
        { weight, fontVariationSettings ->
            Font(resId = resId, weight = weight, variationSettings = fontVariationSettings)
        }
    }
    return rememberVariableIconFont(
        fontConstructor = constructor,
        weights, fontVariationSettings, fontFeatureSettings
    )
}

/**
 * Creates a variable [IconFont] from a file
 * @param file the font file
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
    fontFeatureSettings: String? = null
): IconFont {
    val constructor: FontConstructor = remember(file) {
        { weight, fontVariationSettings ->
            Font(file = file, weight = weight, variationSettings = fontVariationSettings)
        }
    }
    return rememberVariableIconFont(
        fontConstructor = constructor,
        weights, fontVariationSettings, fontFeatureSettings
    )
}

/**
 * Creates a variable [IconFont] using a [ParcelFileDescriptor]
 * @param fileDescriptor the file descriptor for the font file
 * @param weights supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
public fun rememberVariableIconFont(
    fileDescriptor: ParcelFileDescriptor,
    weights: Array<FontWeight>,
    fontVariationSettings: Array<FontVariation.Setting> = emptyArray(),
    fontFeatureSettings: String? = null
): IconFont {
    val constructor: FontConstructor = remember(fileDescriptor) {
        { weight, fontVariationSettings ->
            Font(
                fileDescriptor = fileDescriptor,
                weight = weight,
                variationSettings = fontVariationSettings
            )
        }
    }
    return rememberVariableIconFont(
        fontConstructor = constructor,
        weights, fontVariationSettings, fontFeatureSettings
    )
}

/**
 * Creates a variable [IconFont] from a file in the assets directory
 * @param path full path starting from the assets directory
 * @param assetManager the Android [AssetManager]
 * @param weights supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@Composable
public fun rememberVariableIconFont(
    path: String,
    assetManager: AssetManager,
    weights: Array<FontWeight>,
    fontVariationSettings: Array<FontVariation.Setting> = emptyArray(),
    fontFeatureSettings: String? = null
): IconFont {
    val constructor: FontConstructor = remember(path, assetManager) {
        { weight, fontVariationSettings ->
            Font(
                path = path,
                assetManager = assetManager,
                weight = weight,
                variationSettings = fontVariationSettings
            )
        }
    }
    return rememberVariableIconFont(
        fontConstructor = constructor,
        weights, fontVariationSettings, fontFeatureSettings
    )
}

/**
 * Creates a variable [IconFont] from a file in the assets directory
 * @param path full path starting from the assets directory
 * @param weights supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@Composable
public fun rememberVariableIconFont(
    path: String,
    weights: Array<FontWeight>,
    fontVariationSettings: Array<FontVariation.Setting> = emptyArray(),
    fontFeatureSettings: String? = null
): IconFont = rememberVariableIconFont(
    path = path,
    assetManager = LocalContext.current.resources.assets,
    weights, fontVariationSettings, fontFeatureSettings
)
