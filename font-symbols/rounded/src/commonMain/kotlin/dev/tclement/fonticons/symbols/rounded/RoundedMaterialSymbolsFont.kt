package dev.tclement.fonticons.symbols.rounded

import androidx.compose.runtime.Composable
import dev.tclement.fonticons.IconFont

/**
 * Material Symbols variable font, Rounded variant
 *
 * @param grade grade of the font, between -50 and 200, 0 by default
 * @param fill whether to use the filled variation of the icons or not
 */
@Composable
public expect fun rememberRoundedMaterialSymbolsFont(grade: Int = 0, fill: Boolean = false): IconFont
