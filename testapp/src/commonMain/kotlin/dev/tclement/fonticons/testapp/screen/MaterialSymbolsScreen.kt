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

package dev.tclement.fonticons.testapp.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.tclement.fonticons.FontIcon
import dev.tclement.fonticons.ProvideIconParameters
import dev.tclement.fonticons.testapp.additionalPreviews
import dev.tclement.fonticons.testapp.rememberMaterialSymbolsFont

private val accountCircle: ImageVector = ImageVector.Builder(
    name = "Account_circle",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 960f,
    viewportHeight = 960f
).apply {
    path(
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(234f, 684f)
        quadToRelative(51f, -39f, 114f, -61.5f)
        reflectiveQuadTo(480f, 600f)
        reflectiveQuadToRelative(132f, 22.5f)
        reflectiveQuadTo(726f, 684f)
        quadToRelative(35f, -41f, 54.5f, -93f)
        reflectiveQuadTo(800f, 480f)
        quadToRelative(0f, -133f, -93.5f, -226.5f)
        reflectiveQuadTo(480f, 160f)
        reflectiveQuadToRelative(-226.5f, 93.5f)
        reflectiveQuadTo(160f, 480f)
        quadToRelative(0f, 59f, 19.5f, 111f)
        reflectiveQuadToRelative(54.5f, 93f)
        moveToRelative(246f, -164f)
        quadToRelative(-59f, 0f, -99.5f, -40.5f)
        reflectiveQuadTo(340f, 380f)
        reflectiveQuadToRelative(40.5f, -99.5f)
        reflectiveQuadTo(480f, 240f)
        reflectiveQuadToRelative(99.5f, 40.5f)
        reflectiveQuadTo(620f, 380f)
        reflectiveQuadToRelative(-40.5f, 99.5f)
        reflectiveQuadTo(480f, 520f)
        moveToRelative(0f, 360f)
        quadToRelative(-83f, 0f, -156f, -31.5f)
        reflectiveQuadTo(197f, 763f)
        reflectiveQuadToRelative(-85.5f, -127f)
        reflectiveQuadTo(80f, 480f)
        reflectiveQuadToRelative(31.5f, -156f)
        reflectiveQuadTo(197f, 197f)
        reflectiveQuadToRelative(127f, -85.5f)
        reflectiveQuadTo(480f, 80f)
        reflectiveQuadToRelative(156f, 31.5f)
        reflectiveQuadTo(763f, 197f)
        reflectiveQuadToRelative(85.5f, 127f)
        reflectiveQuadTo(880f, 480f)
        reflectiveQuadToRelative(-31.5f, 156f)
        reflectiveQuadTo(763f, 763f)
        reflectiveQuadToRelative(-127f, 85.5f)
        reflectiveQuadTo(480f, 880f)
        moveToRelative(0f, -80f)
        quadToRelative(53f, 0f, 100f, -15.5f)
        reflectiveQuadToRelative(86f, -44.5f)
        quadToRelative(-39f, -29f, -86f, -44.5f)
        reflectiveQuadTo(480f, 680f)
        reflectiveQuadToRelative(-100f, 15.5f)
        reflectiveQuadToRelative(-86f, 44.5f)
        quadToRelative(39f, 29f, 86f, 44.5f)
        reflectiveQuadTo(480f, 800f)
        moveToRelative(0f, -360f)
        quadToRelative(26f, 0f, 43f, -17f)
        reflectiveQuadToRelative(17f, -43f)
        reflectiveQuadToRelative(-17f, -43f)
        reflectiveQuadToRelative(-43f, -17f)
        reflectiveQuadToRelative(-43f, 17f)
        reflectiveQuadToRelative(-17f, 43f)
        reflectiveQuadToRelative(17f, 43f)
        reflectiveQuadToRelative(43f, 17f)
        moveToRelative(0f, 300f)
    }
}.build()

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MaterialSymbolsScreen() {
    var weight by remember { mutableIntStateOf(400) }
    var grade by remember { mutableIntStateOf(0) }
    var manualOpsz by remember { mutableStateOf(true) }
    var size by remember { mutableIntStateOf(24) }
    var fill by remember { mutableStateOf(false) }
    val fillValue by animateFloatAsState(
        targetValue = if (fill) 1f else 0f,
        animationSpec = tween()
    )
    var overlap by remember {
        mutableStateOf(false)
    }

    val iconFont = rememberMaterialSymbolsFont(
        grade = grade,
        fill = fillValue,
        manualOpsz = manualOpsz,
        opsz = size.toFloat()
    )

    var iconName by remember { mutableStateOf("account_circle") }
    val additionalPreviews = additionalPreviews(iconName)
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(all = 16.dp)
    ) {
        item {
            TextField(
                value = iconName,
                onValueChange = { v ->
                    iconName = v
                },
                label = {
                    Text(text = "Icon name")
                }
            )
        }
        item {
            val iconSize = if (manualOpsz) 128.dp else size.dp
            ProvideIconParameters(
                iconFont = iconFont,
                size = iconSize,
                tint = LocalContentColor.current,
                weight = FontWeight(weight)
            ) {
                if (overlap) {
                    Box {
                        if (iconName == "account_circle")
                            Icon(
                                imageVector = accountCircle,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(iconSize)
                                    .alpha(0.1f)
                            )
                        FontIcon(
                            iconName = iconName,
                            contentDescription = null,
                            tint = Color.Red
                        )
                        additionalPreviews.forEach {
                            it()
                        }
                    }
                } else {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(
                            10.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalArrangement = Arrangement.spacedBy(
                            10.dp,
                            Alignment.CenterVertically
                        )
                    ) {
                        Box(
                            modifier = Modifier.border(1.dp, Color.Red)
                        ) {
                            if (iconName == "account_circle")
                                Icon(
                                    imageVector = accountCircle,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(iconSize)
                                        .alpha(0.1f)
                                )
                            FontIcon(
                                iconName = iconName,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                        Box(
                            modifier = Modifier.border(1.dp, Color.Magenta)
                        ) {
                            Icon(
                                imageVector = accountCircle,
                                contentDescription = null,
                                modifier = Modifier.size(iconSize),
                                tint = Color.Magenta
                            )
                        }
                        additionalPreviews.forEach {
                            Box(
                                modifier = Modifier.border(1.dp, Color.Red)
                            ) {
                                if (iconName == "account_circle")
                                    Icon(
                                        imageVector = accountCircle,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(iconSize)
                                            .alpha(0.1f)
                                    )
                                it()
                            }
                        }
                    }
                }
            }
        }

        item {
            Text(text = "Weight ($weight)")
            Slider(
                value = weight.toFloat(),
                onValueChange = { weight = it.toInt() },
                valueRange = 100f..700f,
                steps = 5
            )
        }

        item {
            Text(text = "Grade ($grade)")
            Slider(
                value = grade.toFloat(),
                onValueChange = { grade = it.toInt() },
                valueRange = -50f..200f,
                steps = 248
            )
        }

        item {
            Text(text = "Manual optical size")
            Switch(checked = manualOpsz, onCheckedChange = { manualOpsz = it })
            if (manualOpsz) {
                Text(text = "Optical size ($size)")
                Slider(
                    value = size.toFloat(),
                    onValueChange = { size = it.toInt() },
                    valueRange = 20f..48f,
                    steps = 26
                )
            } else {
                Text(text = "Size ($size)")
                Slider(
                    value = size.toFloat(),
                    onValueChange = { size = it.toInt() },
                    valueRange = 8f..128f,
                    steps = 118
                )
            }
        }

        item {
            Text(text = "Fill ($fill)")
            Switch(checked = fill, onCheckedChange = { fill = it })
        }

        if (additionalPreviews.isNotEmpty()) {
            item {
                Text(text = "Overlap ($overlap)")
                Switch(checked = overlap, onCheckedChange = { overlap = it })
            }
        }
    }
}
