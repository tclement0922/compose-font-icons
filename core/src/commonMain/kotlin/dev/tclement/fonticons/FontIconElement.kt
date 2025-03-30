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

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection

internal class FontIconElement(
    private val iconName: String,
    private val tint: Color,
    private val fontWeight: FontWeight,
    private val iconFont: IconFont,
    private val fontFamilyResolver: FontFamily.Resolver,
    private val layoutDirection: LayoutDirection,
    private val contentDescription: String?,
) : ModifierNodeElement<FontIconNode>() {
    override fun create(): FontIconNode = FontIconNode(
        iconName,
        tint,
        fontWeight,
        iconFont,
        fontFamilyResolver,
        layoutDirection,
        contentDescription,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FontIconElement) return false

        if (tint != other.tint) return false
        if (iconName != other.iconName) return false

        if (fontWeight != other.fontWeight) return false
        if (iconFont !== other.iconFont) return false
        if (fontFamilyResolver != other.fontFamilyResolver) return false
        if (layoutDirection != other.layoutDirection) return false
        if (contentDescription != other.contentDescription) return false

        return true
    }

    override fun hashCode(): Int {
        var result = iconName.hashCode()
        result = 31 * result + tint.hashCode()
        result = 31 * result + fontWeight.hashCode()
        result = 31 * result + iconFont.hashCode()
        result = 31 * result + fontFamilyResolver.hashCode()
        result = 31 * result + layoutDirection.hashCode()
        result = 31 * result + (contentDescription?.hashCode() ?: 0)
        return result
    }

    override fun update(node: FontIconNode) = node.update(
        iconName,
        tint,
        fontWeight,
        iconFont,
        fontFamilyResolver,
        layoutDirection,
        contentDescription,
    )
}