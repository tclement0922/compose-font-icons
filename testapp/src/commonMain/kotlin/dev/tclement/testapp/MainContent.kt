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

package dev.tclement.testapp

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.tclement.fonticons.FontIcon
import dev.tclement.fonticons.ProvideIconParameters
import dev.tclement.testapp.ui.theme.MaterialSymbolsExperimentsTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainContent() {
    MaterialSymbolsExperimentsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var weight by remember { mutableIntStateOf(300) }
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

            val iconFont = testIconFont(
                weights = arrayOf(
                    FontWeight.W100,
                    FontWeight.W200,
                    FontWeight.W300,
                    FontWeight.W400,
                    FontWeight.W500,
                    FontWeight.W600,
                    FontWeight.W700,
                ),
                grade = grade,
                fill = fillValue,
                manualOpsz = manualOpsz,
                opsz = size.toFloat()
            )

            LazyColumn(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(all = 16.dp)
            ) {
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
                                Icon(
                                    painter = referencePainter(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(iconSize)
                                        .alpha(0.1f)
                                )
                                FontIcon(
                                    iconName = "account_circle",
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
                                Box {
                                    Icon(
                                        painter = referencePainter(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(iconSize)
                                            .alpha(0.1f)
                                    )
                                    FontIcon(
                                        iconName = "account_circle",
                                        contentDescription = null,
                                        tint = Color.Red
                                    )
                                }
                                additionalPreviews.forEach {
                                    Box {
                                        Icon(
                                            painter = referencePainter(),
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
    }
}
