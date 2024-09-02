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

package dev.tclement.fonticons.glance

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.glance.GlanceComposable
import androidx.glance.LocalContext
import dev.tclement.fonticons.IconFont
import dev.tclement.fonticons.rememberVariableIconFont

/**
 * Creates a variable [IconFont] from a file in the assets directory
 * @param path full path starting from the assets directory
 * @param weights supported weights for the font
 * @param fontVariationSettings the font variation settings, should not include the optical size ('opsz')
 * and must not include the weight ('wght')
 * @param fontFeatureSettings the font feature settings, written in a CSS syntax
 * @see rememberVariableIconFont
 */
@RequiresApi(Build.VERSION_CODES.O)
@Deprecated(
    message = "The base package now supports using the Glance context, this function is now useless and will be removed in the future",
    replaceWith = ReplaceWith("rememberVariableIconFont", "dev.tclement.fonticons.rememberVariableIconFont"),
    level = DeprecationLevel.WARNING
)
@Composable
@GlanceComposable
public fun rememberVariableIconFont(
    path: String,
    weights: Array<FontWeight>,
    fontVariationSettings: Array<FontVariation.Setting> = emptyArray(),
    fontFeatureSettings: String? = null
): IconFont = rememberVariableIconFont(
    path = path,
    weights = weights,
    assetManager = LocalContext.current.resources.assets,
    fontVariationSettings, fontFeatureSettings
)
