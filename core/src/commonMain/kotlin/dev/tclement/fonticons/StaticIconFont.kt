/*
 * Copyright 2024-2025 T. Clément (@tclement0922)
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
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.getSystemResourceEnvironment

/**
 * Fixed icon font, for non-variable fonts. Multiple fonts might be provided to support multiple weights.
 */
public class StaticIconFont internal constructor(
    internal val fontFamily: FontFamily,
    override val featureSettings: String? = null
) : IconFont() {
    internal constructor(
        fonts: Array<out Font>,
        featureSettings: String? = null
    ) : this(
        FontFamily(*fonts),
        featureSettings
    )
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
public expect fun rememberStaticIconFont(
    fontResource: FontResource,
    fontFeatureSettings: String? = null
): StaticIconFont

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

/**
 * Creates a static [IconFont] using a Compose Multiplatform [FontResource].
 *
 * This function is marked as experimental because its Android implementation is using experimental workarounds:
 * - On Android 11 and higher this will shortly create an in-memory file descriptor.
 * - On Android 8 and higher this will create a temporary file (this file will be deleted after the font is loaded).
 * - On Android 7 and lower this will also create a temporary file, but the variation settings will be ignored.
 *
 * This function is not composable, use [rememberStaticIconFont] when in a composition.
 * @param fontResource the font resource used to draw the icons
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 * @param resourceEnvironment the resource environment to use to load the font
 */
@OptIn(ExperimentalResourceApi::class)
@ExperimentalFontIconsApi
public expect suspend fun createStaticIconFont(
    fontResource: FontResource,
    fontFeatureSettings: String? = null,
    resourceEnvironment: ResourceEnvironment = getSystemResourceEnvironment()
): StaticIconFont
