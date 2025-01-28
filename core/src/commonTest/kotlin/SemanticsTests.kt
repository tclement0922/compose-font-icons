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

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.*
import dev.tclement.fonticons.ExperimentalFontIconsApi
import dev.tclement.fonticons.FontIcon
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class, ExperimentalFontIconsApi::class)
class SemanticsTests {
    @Test
    fun `Icon with no content description`() = runComposeUiTest {
        setContent {
            FontIcon(
                iconName = "account_circle",
                contentDescription = null,
                iconFont = materialSymbolsOutlined(),
                tint = Color.Black,
                modifier = Modifier.testTag("fontIcon")
            )
        }
        onNodeWithTag("fontIcon")
            .assertContentDescriptionEquals()
            .assertRoleIs(null)
    }

    @Test
    fun `Icon with a content description`() = runComposeUiTest {
        setContent {
            FontIcon(
                iconName = "account_circle",
                contentDescription = "User profile",
                iconFont = materialSymbolsOutlined(),
                tint = Color.Black,
                modifier = Modifier.testTag("fontIcon")
            )
        }
        onNodeWithTag("fontIcon")
            .assertContentDescriptionEquals("User profile")
            .assertRoleIs(Role.Image)
    }
}

private fun SemanticsNodeInteraction.assertRoleIs(role: Role?) = assert(
    SemanticsMatcher("${SemanticsProperties.Role.name} = $role") { node ->
        node.config.getOrNull(SemanticsProperties.Role) == role
    }
)