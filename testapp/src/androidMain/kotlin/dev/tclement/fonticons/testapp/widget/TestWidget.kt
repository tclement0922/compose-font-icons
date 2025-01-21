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

package dev.tclement.fonticons.testapp.widget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.background
import androidx.glance.appwidget.provideContent
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import dev.tclement.fonticons.ExperimentalFontIconsApi
import dev.tclement.fonticons.glance.FontIcon
import dev.tclement.fonticons.glance.ProvideGlanceIconParameters
import dev.tclement.fonticons.rememberVariableIconFont
import dev.tclement.fonticons.testapp.res.Res
import dev.tclement.fonticons.testapp.res.material_symbols_rounded

class TestWidget : GlanceAppWidget() {
    @OptIn(ExperimentalFontIconsApi::class)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            ProvideGlanceIconParameters(
                iconFont = rememberVariableIconFont(
                    fontResource = Res.font.material_symbols_rounded,
                    weights = arrayOf(
                        FontWeight.W300,
                        FontWeight.W400,
                        FontWeight.W500,
                        FontWeight.W600,
                        FontWeight.W700,
                    )
                ),
                tint = ColorProvider(Color.Black, Color.White)
            ) {
                Box(
                    modifier = GlanceModifier.background(Color.White, Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    FontIcon(
                        iconName = "account_circle",
                        contentDescription = null
                    )
                }
            }
        }
    }

    class Receiver : GlanceAppWidgetReceiver() {
        override val glanceAppWidget: GlanceAppWidget = TestWidget()
    }
}