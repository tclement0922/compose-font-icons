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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.tclement.fonticons.ExperimentalFontIconsApi
import dev.tclement.fonticons.FontIcon
import dev.tclement.fonticons.IconFont
import dev.tclement.fonticons.painter.rememberFontIconPainter
import dev.tclement.fonticons.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

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

@OptIn(ExperimentalTestApi::class, ExperimentalFontIconsApi::class)
class RenderingTests {
    private fun iconRenderingTest(
        font: @Composable () -> IconFont,
        iconName: String,
        iconRes: DrawableResource,
        size: Dp
    ) = runComposeUiTest {
        lateinit var composableIconBitmap: ImageBitmap
        lateinit var painterBitmap: ImageBitmap
        lateinit var xmlIconBitmap: ImageBitmap

        setContent {
            val font = font()
            FontIcon(
                iconName = iconName,
                contentDescription = null,
                iconFont = font,
                tint = Color.Black,
                modifier = Modifier.size(size).saveToImageBitmap { composableIconBitmap = it }
            )
            Image(
                painter = rememberFontIconPainter(
                    iconName = iconName,
                    iconFont = font,
                    tint = Color.Black
                ),
                contentDescription = null,
                modifier = Modifier.size(size).saveToImageBitmap { painterBitmap = it }
            )
            Image(
                imageVector = vectorResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(size).saveToImageBitmap { xmlIconBitmap = it },
                colorFilter = ColorFilter.tint(Color.Black)
            )
        }

        assertEquals(
            xmlIconBitmap.width,
            composableIconBitmap.width,
            "Widths differ between composable and XML icon"
        )
        assertEquals(
            xmlIconBitmap.height,
            composableIconBitmap.height,
            "Heights differ between composable and XML icon"
        )
        assertEquals(
            xmlIconBitmap.width,
            painterBitmap.width,
            "Widths differ between painter and XML icon"
        )
        assertEquals(
            xmlIconBitmap.height,
            painterBitmap.height,
            "Heights differ between painter and XML icon"
        )
        val composableIconPixels = composableIconBitmap.readPixels()
        val painterIconPixels = painterBitmap.readPixels()
        val xmlIconPixels = xmlIconBitmap.readPixels()
        // We tolerate pixels to differ in alpha channel unless one is fully transparent and the other is opaque.
        // Since the rendering mechanisms are different between a vector image and a font icon (essentially a text
        // character), we can't expect the pixels on the borders of the icons to be exactly the same.
        assertEquals(
            composableIconPixels.size,
            composableIconPixels.zip(xmlIconPixels)
                .count { (a, b) -> a == b || abs((a shr 24) - (b shr 24)) < 0xFF },
            "The pixels differ between composable and XML icon"
        )
        assertEquals(
            composableIconPixels.size,
            composableIconPixels.zip(painterIconPixels)
                .count { (a, b) -> a == b || abs((a shr 24) - (b shr 24)) < 0xFF },
            "The pixels differ between painter and XML icon"
        )
    }

    // The minimum optical size is 20, so going below will render the same as 20 but with a smaller size smaller.
    @Test
    fun `Material Symbols Outlined account_circle 12dp`() = iconRenderingTest(
        font = materialSymbolsOutlined,
        iconName = "account_circle",
        iconRes = Res.drawable.ms_account_circle_20px,
        size = 12.dp
    )

    @Test
    fun `Material Symbols Outlined account_circle 20dp`() = iconRenderingTest(
        font = materialSymbolsOutlined,
        iconName = "account_circle",
        iconRes = Res.drawable.ms_account_circle_20px,
        size = 20.dp
    )

    @Test
    fun `Material Symbols Outlined account_circle 24dp`() = iconRenderingTest(
        font = materialSymbolsOutlined,
        iconName = "account_circle",
        iconRes = Res.drawable.ms_account_circle_24px,
        size = 24.dp
    )

    @Test
    fun `Material Symbols Outlined account_circle 48dp`() = iconRenderingTest(
        font = materialSymbolsOutlined,
        iconName = "account_circle",
        iconRes = Res.drawable.ms_account_circle_48px,
        size = 48.dp
    )

    // The variable font should cap the optical size changes at 48, so the result should be the same as the 48dp icon.
    @Test
    fun `Material Symbols Outlined account_circle 256dp`() = iconRenderingTest(
        font = materialSymbolsOutlined,
        iconName = "account_circle",
        iconRes = Res.drawable.ms_account_circle_48px,
        size = 256.dp
    )

    @Test
    fun `Material Symbols Outlined home 20dp`() = iconRenderingTest(
        font = materialSymbolsOutlined,
        iconName = "home",
        iconRes = Res.drawable.ms_home_20px,
        size = 20.dp
    )

    @Test
    fun `Material Symbols Outlined home 24dp`() = iconRenderingTest(
        font = materialSymbolsOutlined,
        iconName = "home",
        iconRes = Res.drawable.ms_home_24px,
        size = 24.dp
    )

    @Test
    fun `Material Symbols Outlined home 48dp`() = iconRenderingTest(
        font = materialSymbolsOutlined,
        iconName = "home",
        iconRes = Res.drawable.ms_home_48px,
        size = 48.dp
    )

    @Test
    fun `Material Symbols Filled home 24dp`() = iconRenderingTest(
        font = materialSymbolsFilled,
        iconName = "home",
        iconRes = Res.drawable.ms_home_24px_filled,
        size = 24.dp
    )

    @Test
    fun `Font Awesome Regular circle-user 20dp`() = iconRenderingTest(
        font = fontAwesomeRegular,
        iconName = "circle-user",
        iconRes = Res.drawable.fa_circle_user,
        size = 20.dp
    )

    @Test
    fun `Font Awesome Regular circle-user 24dp`() = iconRenderingTest(
        font = fontAwesomeRegular,
        iconName = "circle-user",
        iconRes = Res.drawable.fa_circle_user,
        size = 24.dp
    )

    @Test
    fun `Font Awesome Regular circle-user 48dp`() = iconRenderingTest(
        font = fontAwesomeRegular,
        iconName = "circle-user",
        iconRes = Res.drawable.fa_circle_user,
        size = 48.dp
    )

    @Test
    fun `Font Awesome Solid circle-user 24dp`() = iconRenderingTest(
        font = fontAwesomeSolid,
        iconName = "circle-user",
        iconRes = Res.drawable.fa_circle_user_solid,
        size = 24.dp
    )

    @Test
    fun `Font Awesome Solid lungs 48dp`() = iconRenderingTest(
        font = fontAwesomeSolid,
        iconName = "lungs",
        iconRes = Res.drawable.fa_lungs,
        size = 48.dp
    )
}

private fun Modifier.saveToImageBitmap(callback: (ImageBitmap) -> Unit): Modifier = this then drawWithCache {
    val width = size.width.toInt()
    val height = size.height.toInt()

    onDrawWithContent {
        val bitmap = ImageBitmap(width, height)
        val bitmapCanvas = Canvas(bitmap)
        for (canvas in arrayOf(bitmapCanvas, drawContext.canvas)) {
            draw(this, layoutDirection, canvas, size) {
                this@onDrawWithContent.drawContent()
            }
        }
        callback(bitmap)
    }
}

private fun ImageBitmap.readPixels() = IntArray(width * height).also { readPixels(it) }
