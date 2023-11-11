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

package dev.tclement.fonticons.symbols

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontVariation
import dev.tclement.fonticons.IconFont
import dev.tclement.fonticons.rememberVariableIconFont

/**
 * Material Symbols variable font, compatible with all variants (Outlined, Rounded and Sharp)
 *
 * @param resource fetch path of the font file as seen [here](https://developer.mozilla.org/en-US/docs/Web/API/fetch#resource)
 * @param grade grade of the font, between -50 and 200, 0 by default
 * @param fill whether to use the filled variation of the icons or not
 */
@Composable
public fun rememberMaterialSymbolsFont(
    resource: Any?,
    grade: Int = 0,
    fill: Boolean = false
): IconFont = rememberVariableIconFont(
    resource = resource,
    weights = MaterialSymbols.supportedWeights,
    fontVariationSettings = arrayOf(
        FontVariation.grade(grade),
        FontVariation.Setting("FILL", if (fill) 1f else 0f)
    )
)
