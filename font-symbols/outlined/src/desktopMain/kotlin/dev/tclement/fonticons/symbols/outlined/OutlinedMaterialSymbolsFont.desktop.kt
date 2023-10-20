package dev.tclement.fonticons.symbols.outlined

import androidx.compose.runtime.Composable
import dev.tclement.fonticons.IconFont
import dev.tclement.fonticons.symbols.InternalSymbolsApi
import dev.tclement.fonticons.symbols.materialSymbolsVariableIconFont

@OptIn(InternalSymbolsApi::class)
@Composable
public actual fun rememberOutlinedMaterialSymbolsFont(
    grade: Int,
    fill: Boolean
): IconFont = materialSymbolsVariableIconFont(
    resource = "font/material_symbols_outlined.ttf",
    grade, fill
)
