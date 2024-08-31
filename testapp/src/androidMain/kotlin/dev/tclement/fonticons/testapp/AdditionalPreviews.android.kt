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

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.core.graphics.drawable.toBitmap
import dev.tclement.fonticons.*

actual fun additionalPreviews(iconName: String): Array<@Composable () -> Unit> = arrayOf(
    {
        val drawable = when (val iconFont = LocalIconFont.current) {
            is StaticIconFont -> FontIconDrawable(
                iconName, iconFont, Color.Blue, LocalContext.current, LocalDensity.current, LocalIconSize.current, LocalIconWeight.current
            )

            is VariableIconFont -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) FontIconDrawable(
                iconName, iconFont, Color.Blue, LocalContext.current, LocalDensity.current, LocalIconSize.current, LocalIconWeight.current
            ) else null
        }
        if (drawable != null)
            Image(
                bitmap = drawable.toBitmap().asImageBitmap(),
                contentDescription = null
            )
    },
    {
        val bitmap = when (val iconFont = LocalIconFont.current) {
            is StaticIconFont -> FontIconBitmap(
                iconName, iconFont, Color.Green, LocalContext.current, LocalDensity.current, LocalIconSize.current, LocalIconWeight.current
            )

            is VariableIconFont -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) FontIconBitmap(
                iconName, iconFont, Color.Green, LocalContext.current, LocalDensity.current, LocalIconSize.current, LocalIconWeight.current
            ) else null
        }
        if (bitmap != null)
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null
            )
    }
)