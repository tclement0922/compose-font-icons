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

import androidx.compose.ui.text.font.FontWeight

/**
 * [Material Symbols icon](https://m3.material.io/styles/icons/overview) as seen on [Google Fonts](https://fonts.google.com/icons).
 *
 * Material symbol icons can be called like this: `MaterialSymbols.IconName`
 *
 * Those icons are in fact unicode [Char]s, so they're incompatible with the official Icon composable.
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
