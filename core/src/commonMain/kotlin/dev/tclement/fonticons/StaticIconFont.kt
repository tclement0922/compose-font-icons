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
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.Font as ResourceFont

/**
 * Fixed icon font, for non-variable fonts. Multiple fonts might be provided to support multiple weights.
 */
public class StaticIconFont internal constructor(
    fonts: Array<out Font>,
    override val featureSettings: String? = null
) : IconFont() {
    internal val fontFamily = FontFamily(*fonts)
}

/**
 * Creates a static [IconFont] using a list of [Font] objects. Multiple fonts might be provided to support multiple
 * weights.
 * @param fonts the font(s) used to draw the icons
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@Composable
public fun rememberStaticIconFont(
    vararg fonts: Font,
    fontFeatureSettings: String? = null
): StaticIconFont = remember(fonts, fontFeatureSettings) {
    StaticIconFont(
        fonts = fonts, featureSettings = fontFeatureSettings
    )
}

/**
 * Creates a static [IconFont] using a [Font].
 * @param font the font used to draw the icons
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@Composable
public fun rememberStaticIconFont(
    font: Font,
    fontFeatureSettings: String? = null
): StaticIconFont = rememberStaticIconFont(
    fonts = arrayOf(font),
    fontFeatureSettings
)

/**
 * Creates a static [IconFont] using a Compose Multiplatform [FontResource].
 * @param fontResource the font resource used to draw the icons
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
@Composable
public fun rememberStaticIconFont(
    fontResource: FontResource,
    fontFeatureSettings: String? = null
): StaticIconFont = rememberStaticIconFont(
    font = ResourceFont(fontResource),
    fontFeatureSettings
)

/**
 * Creates a static [IconFont] using a list of [Font] objects. Multiple fonts might be provided to support multiple
 * weights.
 *
 * This function is not composable, use [rememberStaticIconFont] when in a composition.
 * @param fonts the font(s) used to draw the icons
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 */
public fun createStaticIconFont(
    vararg fonts: Font,
    fontFeatureSettings: String? = null
): StaticIconFont =
    StaticIconFont(
        fonts = fonts, featureSettings = fontFeatureSettings
    )

