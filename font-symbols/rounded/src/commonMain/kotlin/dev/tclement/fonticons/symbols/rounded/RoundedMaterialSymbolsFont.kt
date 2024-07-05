package dev.tclement.fonticons.symbols.rounded

import androidx.compose.runtime.Composable
import dev.tclement.fonticons.IconFont
import dev.tclement.fonticons.symbols.InternalSymbolsApi
import dev.tclement.fonticons.symbols.materialSymbolsVariableIconFont
import dev.tclement.fonticons.symbols.rounded.resources.Res
import dev.tclement.fonticons.symbols.rounded.resources.material_symbols_rounded

/**
 * The Material Symbols variable font, Rounded variant.
 *
 * @param grade grade of the font, between -50 and 200, 0 by default
 * @param fill whether to use the filled variation of the icons or not
 */
@OptIn(InternalSymbolsApi::class)
@Composable
public fun rememberRoundedMaterialSymbolsFont(grade: Int = 0, fill: Boolean = false): IconFont =
    materialSymbolsVariableIconFont(
        fontResource = Res.font.material_symbols_rounded,
        grade, fill
    )
