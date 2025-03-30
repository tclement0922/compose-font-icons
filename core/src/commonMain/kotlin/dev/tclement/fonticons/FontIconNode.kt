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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.SemanticsModifierNode
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.node.invalidateSemantics
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import kotlin.math.ceil
import kotlin.math.min

internal class FontIconNode(
    private var iconName: String,
    private var tint: Color,
    private var fontWeight: FontWeight,
    private var iconFont: IconFont,
    private var fontFamilyResolver: FontFamily.Resolver,
    private var layoutDirection: LayoutDirection,
    private var contentDescription: String?,
) : Modifier.Node(), DrawModifierNode, SemanticsModifierNode {
    private var textStyle = TextStyle(
        color = Color.Unspecified,
        fontSize = TextUnit.Unspecified,
        fontWeight = null,
        fontStyle = FontStyle.Normal,
        fontSynthesis = FontSynthesis.None, // For some reason this parameter does absolutely nothing on desktop (at least), a fake bold is automatically applied is the weight is >= than 600
        fontFamily = null,
        fontFeatureSettings = iconFont.featureSettings,
        letterSpacing = 0.sp,
        baselineShift = null,
        textGeometricTransform = null,
        localeList = null,
        background = Color.Unspecified,
        textDecoration = null,
        shadow = null,
        drawStyle = null,
        textAlign = TextAlign.Center,
        textDirection = if (layoutDirection == LayoutDirection.Rtl) TextDirection.Rtl else TextDirection.Ltr,
        lineHeight = TextUnit.Unspecified,
        textIndent = TextIndent.None,
        platformStyle = null,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        lineBreak = LineBreak.Unspecified,
        hyphens = Hyphens.None,
        textMotion = TextMotion.Static
    )
    private var dirty: Boolean = true
    private var lastSize: Size = Size.Unspecified
    private lateinit var paragraphIntrinsics: ParagraphIntrinsics
    private lateinit var paragraph: Paragraph
    private var scale: Float = 1f
    private var offset: Offset = Offset.Unspecified

    fun update(
        iconName: String,
        tint: Color,
        fontWeight: FontWeight,
        iconFont: IconFont,
        fontFamilyResolver: FontFamily.Resolver,
        layoutDirection: LayoutDirection,
        contentDescription: String?,
    ) {
        var invalidateDraw = false
        if (this.tint != tint) {
            this.tint = tint
            invalidateDraw = true
        }

        if (this.iconName != iconName ||
            this.fontWeight != fontWeight ||
            this.iconFont != iconFont ||
            this.fontFamilyResolver != fontFamilyResolver ||
            this.layoutDirection != layoutDirection
        ) {
            this.iconName = iconName
            this.fontWeight = fontWeight
            this.iconFont = iconFont
            this.fontFamilyResolver = fontFamilyResolver
            this.layoutDirection = layoutDirection
            dirty = true
            invalidateDraw = true
        }

        if (invalidateDraw) {
            invalidateDraw()
        }

        if (this.contentDescription != contentDescription) {
            this.contentDescription = contentDescription
            invalidateSemantics()
        }
    }

    private fun recalculateLayout(density: Density, size: Size) {
        val opticalSize = min(size.width, size.height)
        textStyle = with(density) {
            textStyle.copy(
                fontSize = opticalSize.toSp(),
                fontFamily = when (val font = iconFont) {
                    is StaticIconFont -> font.fontFamily
                    is VariableIconFont -> font.getFontFamily(
                        size = opticalSize.toDp().value,
                        weight = fontWeight
                    )
                },
                fontWeight = (iconFont as? VariableIconFont)?.textStyleWeightFor(fontWeight)
            )
        }
        paragraphIntrinsics = ParagraphIntrinsics(
            text = iconName,
            style = textStyle,
            density = density,
            fontFamilyResolver = fontFamilyResolver,
        )
        paragraph = Paragraph(
            paragraphIntrinsics = paragraphIntrinsics,
            constraints = Constraints(maxWidth = ceil(paragraphIntrinsics.maxIntrinsicWidth).toInt()),
            maxLines = 1,
            ellipsis = false
        )
        scale = 1f
        if (paragraph.width > size.width) { // Fix for issue #2
            scale = size.width / paragraph.width
        }
        offset = Offset(
            x = (size.width - paragraph.width * scale) / 2,
            y = (size.height - paragraph.height * scale) / 2
        )
        lastSize = size
    }

    override fun ContentDrawScope.draw() {
        if (dirty || lastSize != size) {
            recalculateLayout(this, size)
        }
        drawIntoCanvas { canvas ->
            canvas.save()
            canvas.translate(offset.x, offset.y)
            if (scale != 1f) {
                canvas.scale(scale)
            }
            paragraph.paint(canvas, tint)
            canvas.restore()
        }
    }

    override fun SemanticsPropertyReceiver.applySemantics() {
        this@FontIconNode.contentDescription?.let {
            contentDescription = it
            role = Role.Image
        }
    }
}