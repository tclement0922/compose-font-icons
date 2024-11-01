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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import org.jetbrains.compose.resources.*
import androidx.glance.LocalContext as LocalGlanceContext

@Composable
public actual fun rememberStaticIconFont(
    fontResource: FontResource,
    fontFeatureSettings: String?
): StaticIconFont {
    lateinit var font: StaticIconFont
    if (isGlanceContext()) {
        CompositionLocalProvider(
            LocalConfiguration provides LocalGlanceContext.current.resources.configuration,
            LocalDensity provides Density(LocalGlanceContext.current),
            LocalContext provides LocalGlanceContext.current
        ) {
            font = rememberStaticIconFont(
                font = Font(fontResource),
                fontFeatureSettings
            )
        }
    } else {
        font = rememberStaticIconFont(
            font = Font(fontResource),
            fontFeatureSettings
        )
    }
    return font
}

@OptIn(ExperimentalResourceApi::class)
@ExperimentalFontIconsApi
public actual suspend fun createStaticIconFont(
    fontResource: FontResource,
    fontFeatureSettings: String?,
    resourceEnvironment: ResourceEnvironment
): StaticIconFont {
    val bytes = getFontResourceBytes(resourceEnvironment, fontResource)
    return createStaticIconFont(
        fonts = arrayOf(
            ByteArrayAndroidFont(
                bytes,
                FontWeight.Normal,
                FontStyle.Normal,
                FontVariation.Settings()
            )
        ),
        fontFeatureSettings
    )
}
