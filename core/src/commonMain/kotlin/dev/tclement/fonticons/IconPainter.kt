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

package dev.tclement.fonticons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import kotlin.math.ceil
import kotlin.math.min

/**
 * A custom painter that draws the provided icon to the composable it is applied to.
 */
internal class IconPainter(
    iconName: String,
    private val density: Density,
    tint: Color,
    private val fontWeight: FontWeight,
    private val iconFont: IconFont,
    private val fontFamilyResolver: FontFamily.Resolver,
    private val layoutDirection: LayoutDirection
) : Painter() {
    override var intrinsicSize: Size = Size.Unspecified

    private val annotatedIconName = AnnotatedString(iconName)

    private val constraints = Constraints()

    private var textStyle = TextStyle(
        color = tint,
        fontSize = TextUnit.Unspecified,
        fontWeight = if (iconFont is VariableIconFont) iconFont.textStyleWeightFor(fontWeight) else FontWeight.W400,
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

    private lateinit var multiParagraphIntrinsics: MultiParagraphIntrinsics

    private lateinit var multiParagraph: MultiParagraph

    private lateinit var textLayoutInput: TextLayoutInput

    private lateinit var textLayoutResult: TextLayoutResult

    private var offset: Offset = Offset.Unspecified

    override fun DrawScope.onDraw() {
        updateSize(size)
        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = offset
        )
    }

    private fun updateSize(size: Size) {
        if (intrinsicSize != size) {
            intrinsicSize = size
            val opticalSize = min(size.width, size.height)
            textStyle = textStyle.copy(
                fontSize = with(density) {
                    opticalSize.toSp()
                },
                fontFamily = iconFont.getFontFamily(
                    size = with(density) { opticalSize.toDp() }.value,
                    weight = fontWeight
                )
            )
            multiParagraphIntrinsics = MultiParagraphIntrinsics(
                annotatedString = annotatedIconName,
                style = textStyle,
                placeholders = emptyList(),
                density = density,
                fontFamilyResolver = fontFamilyResolver,
            )
            multiParagraph = MultiParagraph(
                intrinsics = multiParagraphIntrinsics,
                constraints = Constraints(maxWidth = ceil(multiParagraphIntrinsics.maxIntrinsicWidth).toInt()),
                maxLines = 1,
                ellipsis = false
            )
            textLayoutInput = TextLayoutInput(
                text = annotatedIconName,
                style = textStyle,
                placeholders = emptyList(),
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip,
                density = density,
                layoutDirection = layoutDirection,
                fontFamilyResolver = fontFamilyResolver,
                constraints = constraints
            )
            textLayoutResult = TextLayoutResult(
                layoutInput = textLayoutInput,
                multiParagraph = multiParagraph,
                size = IntSize(
                    ceil(multiParagraph.width).toInt(),
                    ceil(multiParagraph.height).toInt()
                )
            )
            offset = Offset(
                x = (intrinsicSize.width - textLayoutResult.size.width) / 2,
                y = (intrinsicSize.height - textLayoutResult.size.height) / 2
            )
        }
    }
}
