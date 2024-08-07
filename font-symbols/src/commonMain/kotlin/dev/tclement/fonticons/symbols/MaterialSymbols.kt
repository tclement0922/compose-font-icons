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

package dev.tclement.fonticons.symbols

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import dev.tclement.fonticons.IconFont
import dev.tclement.fonticons.rememberVariableIconFont
import org.jetbrains.compose.resources.FontResource

/**
 * [Material Symbols icon](https://m3.material.io/styles/icons/overview) as seen on [Google Fonts](https://fonts.google.com/icons).
 *
 * A Material Symbols icon can be called like this: `MaterialSymbols.IconName`
 *
 * Those icons are unicode [Char]s, so they're incompatible with the official Icon composable.
 */
public object MaterialSymbols {
    internal val supportedWeights = arrayOf(
        FontWeight.W100,
        FontWeight.W200,
        FontWeight.W300,
        FontWeight.W400,
        FontWeight.W500,
        FontWeight.W600,
        FontWeight.W700,
    )
}

/**
 * Should only be used by the Material Symbols variants libraries.
 * @suppress
 */
@InternalSymbolsApi
@Composable
public fun materialSymbolsVariableIconFont(
    fontResource: FontResource,
    grade: Int = 0,
    fill: Boolean = false
): IconFont = rememberVariableIconFont(
    fontResource = fontResource,
    weights = MaterialSymbols.supportedWeights,
    fontVariationSettings = arrayOf(
        FontVariation.grade(grade),
        FontVariation.Setting("FILL", if (fill) 1f else 0f)
    )
)
