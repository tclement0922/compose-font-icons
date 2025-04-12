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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlin.math.ceil
import kotlin.math.min

internal class DrawCache {
    private companion object {
        private val baseTextStyle = TextStyle(
            color = Color.Unspecified,
            fontSize = TextUnit.Unspecified,
            fontWeight = null,
            fontStyle = FontStyle.Normal,
            fontSynthesis = FontSynthesis.None, // For some reason this parameter does absolutely nothing on desktop (at least), a fake bold is automatically applied is the weight is >= than 600
            fontFamily = null,
            fontFeatureSettings = null,
            letterSpacing = 0.sp,
            baselineShift = null,
            textGeometricTransform = null,
            localeList = null,
            background = Color.Unspecified,
            textDecoration = null,
            shadow = null,
            drawStyle = null,
            textAlign = TextAlign.Center,
            textDirection = TextDirection.Unspecified,
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
    }

    private lateinit var paragraph: Paragraph
    private var offset: Offset = Offset.Unspecified
    private var scale: Float = 1f

    val isInitialized: Boolean get() = ::paragraph.isInitialized

    fun DrawScope.recalculate(
        iconName: String,
        iconFont: IconFont,
        fontWeight: FontWeight,
        fontFamilyResolver: FontFamily.Resolver,
    ) {
        val opticalSize = min(size.width, size.height)
        val textStyle = with(density) {
            baseTextStyle.copy(
                fontSize = opticalSize.toSp(),
                fontFamily = when (val font = iconFont) {
                    is StaticIconFont -> font.fontFamily
                    is VariableIconFont -> font.getFontFamily(
                        size = opticalSize.toDp().value,
                        weight = fontWeight
                    )
                },
                fontWeight = (iconFont as? VariableIconFont)?.textStyleWeightFor(fontWeight),
                fontFeatureSettings = iconFont.featureSettings,
                textDirection = if (layoutDirection == LayoutDirection.Rtl) TextDirection.Companion.Rtl else TextDirection.Companion.Ltr
            )
        }
        val paragraphIntrinsics = ParagraphIntrinsics(
            text = iconName,
            style = textStyle,
            density = this,
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
    }

    fun DrawScope.draw(tint: Color) {
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
}