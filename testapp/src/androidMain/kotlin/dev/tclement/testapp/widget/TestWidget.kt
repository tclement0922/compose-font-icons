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

package dev.tclement.testapp.widget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.background
import androidx.glance.appwidget.provideContent
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import dev.tclement.fonticons.glance.FontIcon
import dev.tclement.fonticons.glance.ProvideGlanceIconParameters
import dev.tclement.fonticons.symbols.AccountCircle
import dev.tclement.fonticons.symbols.MaterialSymbols
import dev.tclement.fonticons.symbols.rounded.rememberRoundedMaterialSymbolsFont

class TestWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            ProvideGlanceIconParameters(
                iconFont = rememberRoundedMaterialSymbolsFont(),
                tint = ColorProvider(Color.Black, Color.White)
            ) {
                Box(
                    modifier = GlanceModifier.background(Color.White, Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    FontIcon(
                        icon = MaterialSymbols.AccountCircle,
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