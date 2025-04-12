/*
 * Copyright 2024-2025 T. Clément (@tclement0922)
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

package dev.tclement.fonticons

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.*
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth

internal class FontIconNode(
    private var iconName: String,
    private var tint: Color,
    private var fontWeight: FontWeight,
    private var iconFont: IconFont,
    private var fontFamilyResolver: FontFamily.Resolver,
    private var contentDescription: String?,
) : Modifier.Node(), LayoutModifierNode, DrawModifierNode, SemanticsModifierNode, CompositionLocalConsumerModifierNode {
    override val shouldAutoInvalidate: Boolean = false

    private val drawCache: DrawCache = DrawCache()
    private var dirty: Boolean = true
    private var lastSize: Size = Size.Unspecified

    fun update(
        iconName: String,
        tint: Color,
        fontWeight: FontWeight,
        iconFont: IconFont,
        fontFamilyResolver: FontFamily.Resolver,
        contentDescription: String?,
    ) {
        var invalidateDraw = false
        var invalidateSemantics = false

        if (this.tint != tint) {
            this.tint = tint
            invalidateDraw = true
        }

        if (this.iconName != iconName ||
            this.fontWeight != fontWeight ||
            this.iconFont != iconFont ||
            this.fontFamilyResolver != fontFamilyResolver
        ) {
            this.iconName = iconName
            this.fontWeight = fontWeight
            this.iconFont = iconFont
            this.fontFamilyResolver = fontFamilyResolver
            dirty = true
            invalidateDraw = true
        }

        if (this.contentDescription != contentDescription) {
            this.contentDescription = contentDescription
            invalidateSemantics = true
        }

        if (invalidateDraw) invalidateDraw()
        if (invalidateSemantics) invalidateSemantics()
    }

    override fun ContentDrawScope.draw() = with(drawCache) {
        if (dirty || !isInitialized || lastSize != size) {
            recalculate(
                iconName,
                iconFont,
                fontWeight,
                fontFamilyResolver,
            )
        }
        draw(tint)
    }

    override fun SemanticsPropertyReceiver.applySemantics() {
        this@FontIconNode.contentDescription?.let {
            contentDescription = it
            role = Role.Image
        }
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val iconSize = currentValueOf(LocalIconSize).roundToPx()
        val width = constraints.constrainWidth(iconSize)
        val height = constraints.constrainHeight(iconSize)
        val placeable = measurable.measure(Constraints(width, width, height, height))
        return layout(width, height) {
            placeable.placeRelative(0, 0)
        }
    }
}