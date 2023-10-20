package dev.tclement.fonticons.symbols.sharp

import androidx.compose.runtime.Composable
import dev.tclement.fonticons.IconFont
import dev.tclement.fonticons.symbols.InternalSymbolsApi
import dev.tclement.fonticons.symbols.materialSymbolsVariableIconFont

@OptIn(InternalSymbolsApi::class)
@Composable
public actual fun rememberSharpMaterialSymbolsFont(
    grade: Int,
    fill: Boolean
): IconFont = materialSymbolsVariableIconFont(
    resource = "font/material_symbols_rounded.ttf",
    grade, fill
)
