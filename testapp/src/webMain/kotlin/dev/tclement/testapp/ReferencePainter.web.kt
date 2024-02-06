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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

@Composable
private fun rememberAccountCircle(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "account_circle",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(9.917f, 29.208f)
                quadToRelative(2.375f, -1.625f, 4.812f, -2.541f)
                quadToRelative(2.438f, -0.917f, 5.271f, -0.917f)
                quadToRelative(2.833f, 0f, 5.271f, 0.917f)
                quadToRelative(2.437f, 0.916f, 4.854f, 2.541f)
                quadToRelative(1.75f, -1.958f, 2.625f, -4.27f)
                quadToRelative(0.875f, -2.313f, 0.875f, -4.938f)
                quadToRelative(0f, -5.75f, -3.937f, -9.688f)
                quadTo(25.75f, 6.375f, 20f, 6.375f)
                reflectiveQuadToRelative(-9.688f, 3.937f)
                quadTo(6.375f, 14.25f, 6.375f, 20f)
                quadToRelative(0f, 2.625f, 0.875f, 4.938f)
                quadToRelative(0.875f, 2.312f, 2.667f, 4.27f)
                close()
                moveTo(20f, 21f)
                quadToRelative(-2.25f, 0f, -3.792f, -1.521f)
                quadToRelative(-1.541f, -1.521f, -1.541f, -3.771f)
                reflectiveQuadToRelative(1.541f, -3.791f)
                quadTo(17.75f, 10.375f, 20f, 10.375f)
                reflectiveQuadToRelative(3.792f, 1.542f)
                quadToRelative(1.541f, 1.541f, 1.541f, 3.791f)
                reflectiveQuadToRelative(-1.541f, 3.771f)
                quadTo(22.25f, 21f, 20f, 21f)
                close()
                moveToRelative(0f, 14.625f)
                quadToRelative(-3.25f, 0f, -6.104f, -1.229f)
                reflectiveQuadToRelative(-4.958f, -3.354f)
                quadToRelative(-2.105f, -2.125f, -3.334f, -4.959f)
                quadTo(4.375f, 23.25f, 4.375f, 20f)
                reflectiveQuadToRelative(1.229f, -6.104f)
                quadToRelative(1.229f, -2.854f, 3.354f, -4.958f)
                quadToRelative(2.125f, -2.105f, 4.959f, -3.334f)
                quadTo(16.75f, 4.375f, 20f, 4.375f)
                reflectiveQuadToRelative(6.104f, 1.229f)
                quadToRelative(2.854f, 1.229f, 4.958f, 3.354f)
                quadToRelative(2.105f, 2.125f, 3.334f, 4.959f)
                quadTo(35.625f, 16.75f, 35.625f, 20f)
                reflectiveQuadToRelative(-1.229f, 6.104f)
                quadToRelative(-1.229f, 2.854f, -3.354f, 4.958f)
                quadToRelative(-2.125f, 2.105f, -4.959f, 3.334f)
                quadTo(23.25f, 35.625f, 20f, 35.625f)
                close()
                moveToRelative(0f, -2f)
                quadToRelative(2.292f, 0f, 4.458f, -0.708f)
                quadToRelative(2.167f, -0.709f, 4.084f, -2.25f)
                quadToRelative(-1.917f, -1.417f, -4.063f, -2.167f)
                quadToRelative(-2.146f, -0.75f, -4.479f, -0.75f)
                reflectiveQuadToRelative(-4.5f, 0.729f)
                quadToRelative(-2.167f, 0.729f, -4.042f, 2.188f)
                quadToRelative(1.917f, 1.541f, 4.084f, 2.25f)
                quadToRelative(2.166f, 0.708f, 4.458f, 0.708f)
                close()
                moveTo(20f, 19f)
                quadToRelative(1.417f, 0f, 2.375f, -0.938f)
                quadToRelative(0.958f, -0.937f, 0.958f, -2.354f)
                quadToRelative(0f, -1.458f, -0.958f, -2.396f)
                quadToRelative(-0.958f, -0.937f, -2.375f, -0.937f)
                reflectiveQuadToRelative(-2.375f, 0.937f)
                quadToRelative(-0.958f, 0.938f, -0.958f, 2.396f)
                quadToRelative(0f, 1.417f, 0.958f, 2.354f)
                quadTo(18.583f, 19f, 20f, 19f)
                close()
                moveToRelative(0f, -3.292f)
                close()
                moveToRelative(0f, 14.959f)
                close()
            }
        }.build()
    }
}

@Composable
actual fun referencePainter(): Painter = rememberVectorPainter(rememberAccountCircle())