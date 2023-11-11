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
 * @suppress
 */
@InternalSymbolsApi
@Composable
public fun materialSymbolsVariableIconFont(
    resource: String,
    grade: Int = 0,
    fill: Boolean = false
): IconFont {
    return rememberVariableIconFont(
        resource = resource,
        weights = MaterialSymbols.supportedWeights,
        fontVariationSettings = arrayOf(
            FontVariation.grade(grade),
            FontVariation.Setting("FILL", if (fill) 1f else 0f)
        )
    )
}