package dev.tclement.fonticons.symbols.sharp

import androidx.compose.runtime.Composable
import dev.tclement.fonticons.IconFont
import dev.tclement.fonticons.symbols.InternalSymbolsApi
import dev.tclement.fonticons.symbols.materialSymbolsVariableIconFont
import dev.tclement.fonticons.symbols.sharp.resources.Res
import dev.tclement.fonticons.symbols.sharp.resources.material_symbols_sharp

/**
 * The Material Symbols variable font, Sharp variant.
 *
 * @param grade grade of the font, between -50 and 200, 0 by default
 * @param fill whether to use the filled variation of the icons or not
 */
@OptIn(InternalSymbolsApi::class)
@Composable
public fun rememberSharpMaterialSymbolsFont(grade: Int = 0, fill: Boolean = false): IconFont =
    materialSymbolsVariableIconFont(
        fontResource = Res.font.material_symbols_sharp,
        grade, fill
    )
