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

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.tclement.fonticons.FontIcon
import dev.tclement.fonticons.ProvideIconParameters
import dev.tclement.fonticons.testapp.additionalPreviews
import dev.tclement.fonticons.testapp.rememberFontAwesomeFont

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FontAwesomeScreen() {
    var size by remember { mutableIntStateOf(128) }
    var overlap by remember {
        mutableStateOf(false)
    }

    var solid by remember { mutableStateOf(false) }
    val iconFont = rememberFontAwesomeFont(solid)

    var iconName by remember { mutableStateOf("circle-user") }
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
            val iconSize = size.dp
            ProvideIconParameters(
                iconFont = iconFont,
                size = iconSize,
                tint = LocalContentColor.current
            ) {
                if (overlap) {
                    Box {
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
                            FontIcon(
                                iconName = iconName,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                        additionalPreviews.forEach {
                            Box(
                                modifier = Modifier.border(1.dp, Color.Red)
                            ) {
                                it()
                            }
                        }
                    }
                }
            }
        }

        item {
            Text(text = "Size ($size)")
            Slider(
                value = size.toFloat(),
                onValueChange = { size = it.toInt() },
                valueRange = 8f..128f,
                steps = 118
            )
        }

        item {
            Text(text = "Solid")
            Switch(checked = solid, onCheckedChange = { solid = it })
        }

        if (additionalPreviews.isNotEmpty()) {
            item {
                Text(text = "Overlap ($overlap)")
                Switch(checked = overlap, onCheckedChange = { overlap = it })
            }
        }
    }
}