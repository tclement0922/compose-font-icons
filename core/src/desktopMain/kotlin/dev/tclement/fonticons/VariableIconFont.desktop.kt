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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import org.jetbrains.skia.Data
import org.jetbrains.skia.makeFromFile
import java.io.File
import org.jetbrains.skia.Typeface as SkTypeface

private fun SkTypeface.Companion.makeFromResource(resourceName: String): SkTypeface {
    val contextClassLoader = Thread.currentThread().contextClassLoader!!
    val resource = contextClassLoader.getResourceAsStream(resourceName)
        ?: (::makeFromResource.javaClass).getResourceAsStream(resourceName)
        ?: error("Can't load font from $resourceName")

    val bytes = resource.use { it.readBytes() }
    return makeFromData(Data.makeFromBytes(bytes))
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
