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
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

/**
 * Fixed icon font family, for non-variable fonts.
 */
internal class StaticIconFont(
    fonts: Array<out Font>,
    override val featureSettings: String? = null
): IconFont() {
    private val fontFamily = FontFamily(*fonts)

    /**
     * Always return [fontFamily]
     */
    override fun getFontFamily(size: Float, weight: FontWeight): FontFamily {
        return fontFamily
    }
}

/**
 * Creates a static [IconFont]
 * @param fonts the font(s) used to draw the icons
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@Composable
public fun rememberStaticIconFont(
    vararg fonts: Font,
    fontFeatureSettings: String? = null
): IconFont = remember(fontFeatureSettings) {
    StaticIconFont(
        fonts = fonts, featureSettings = fontFeatureSettings
    )
}

/**
 * Creates a static [IconFont]
 * @param font the font used to draw the icons
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@Composable
public fun rememberStaticIconFont(
    font: Font,
    fontFeatureSettings: String? = null
): IconFont = rememberStaticIconFont(
    fonts = arrayOf(font),
    fontFeatureSettings
)
