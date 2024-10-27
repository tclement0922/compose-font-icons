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

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Typeface
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.getFontResourceBytes
import org.jetbrains.skia.Data
import org.jetbrains.skia.FontMgr

@OptIn(ExperimentalResourceApi::class)
@ExperimentalFontIconsApi
public actual suspend fun createStaticIconFont(
    fontResource: FontResource,
    fontFeatureSettings: String?,
    resourceEnvironment: ResourceEnvironment
): StaticIconFont {
    val bytes = getFontResourceBytes(resourceEnvironment, fontResource)
    val typeface = FontMgr.default.makeFromData(Data.makeFromBytes(bytes))!!
    return StaticIconFont(
        fontFamily = FontFamily(
            typeface = Typeface(
                typeface = typeface,
                alias = typeface.uniqueId.toString()
            )
        ),
        featureSettings = fontFeatureSettings
    )
}
