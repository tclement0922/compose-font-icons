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

package dev.tclement.fonticons.testapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import dev.tclement.fonticons.IconFont
import dev.tclement.fonticons.fa.regular.resources.fontawesome_regular
import dev.tclement.fonticons.fa.solid.resources.fontawesome_solid
import dev.tclement.fonticons.rememberStaticIconFont
import dev.tclement.fonticons.rememberVariableIconFont
import dev.tclement.fonticons.symbols.rounded.resources.material_symbols_rounded
import dev.tclement.fonticons.fa.regular.resources.Res as FARegularRes
import dev.tclement.fonticons.fa.solid.resources.Res as FASolidRes
import dev.tclement.fonticons.symbols.rounded.resources.Res as RoundedSymbolsRes

@Composable
fun rememberMaterialSymbolsFont(
    grade: Int = 24,
    fill: Float = 0f,
    manualOpsz: Boolean = false,
    opsz: Float = 24f
): IconFont = rememberVariableIconFont(
    fontResource = RoundedSymbolsRes.font.material_symbols_rounded,
    weights = arrayOf(
        FontWeight.W100,
        FontWeight.W200,
        FontWeight.W300,
        FontWeight.W400,
        FontWeight.W500,
        FontWeight.W600,
        FontWeight.W700,
    ),
    fontVariationSettings = buildList {
        add(FontVariation.grade(grade))
        add(FontVariation.Setting("FILL", fill))
        if (manualOpsz) {
            add(FontVariation.Setting("opsz", opsz))
        }
    }.toTypedArray()
)

@Composable
fun rememberFontAwesomeFont(solid: Boolean = false) = rememberStaticIconFont(
    fontResource = if (solid) FASolidRes.font.fontawesome_solid else FARegularRes.font.fontawesome_regular,
    fontFeatureSettings = "liga"
)
