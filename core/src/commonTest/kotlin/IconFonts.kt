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

@file:OptIn(ExperimentalFontIconsApi::class)

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import dev.tclement.fonticons.ExperimentalFontIconsApi
import dev.tclement.fonticons.rememberStaticIconFont
import dev.tclement.fonticons.rememberVariableIconFont
import dev.tclement.fonticons.resources.Res
import dev.tclement.fonticons.resources.font_awesome_free_regular_400
import dev.tclement.fonticons.resources.font_awesome_free_solid_900
import dev.tclement.fonticons.resources.material_symbols_outlined

/*
 * Copyright 2025 T. Clément (@tclement0922)
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

internal val materialSymbolsOutlined = @Composable {
    rememberVariableIconFont(
        fontResource = Res.font.material_symbols_outlined,
        weights = arrayOf(
            FontWeight.W300,
            FontWeight.W400,
            FontWeight.W500,
            FontWeight.W600,
            FontWeight.W700
        )
    )
}

internal val materialSymbolsFilled = @Composable {
    rememberVariableIconFont(
        fontResource = Res.font.material_symbols_outlined,
        weights = arrayOf(
            FontWeight.W300,
            FontWeight.W400,
            FontWeight.W500,
            FontWeight.W600,
            FontWeight.W700
        ),
        fontVariationSettings = FontVariation.Settings(FontVariation.Setting("FILL", 1f))
    )
}

internal val fontAwesomeRegular = @Composable {
    rememberStaticIconFont(fontResource = Res.font.font_awesome_free_regular_400)
}

internal val fontAwesomeSolid = @Composable {
    rememberStaticIconFont(fontResource = Res.font.font_awesome_free_solid_900)
}