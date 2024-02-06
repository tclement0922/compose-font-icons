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

package dev.tclement.testapp

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import dev.tclement.fonticons.FontIconBitmap
import dev.tclement.fonticons.FontIconDrawable
import dev.tclement.fonticons.LocalIconFont
import dev.tclement.fonticons.LocalIconSize
import dev.tclement.fonticons.LocalIconWeight
import dev.tclement.fonticons.symbols.AccountCircle
import dev.tclement.fonticons.symbols.MaterialSymbols

actual val additionalPreviews: Array<@Composable () -> Unit> = arrayOf(
    {
        Image(
            bitmap = FontIconDrawable(
                icon = MaterialSymbols.AccountCircle,
                context = LocalContext.current,
                size = LocalIconSize.current,
                tint = Color.Blue,
                weight = LocalIconWeight.current,
                iconFont = LocalIconFont.current
            )
                .toBitmap()
                .asImageBitmap(),
            contentDescription = null
        )
    },
    {
        Image(
            bitmap = FontIconBitmap(
                icon = MaterialSymbols.AccountCircle,
                context = LocalContext.current,
                size = LocalIconSize.current,
                tint = Color.Green,
                weight = LocalIconWeight.current,
                iconFont = LocalIconFont.current
            ).asImageBitmap(),
            contentDescription = null
        )
    }
)