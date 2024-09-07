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

package dev.tclement.fonticons.testapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.tclement.fonticons.FontIcon
import dev.tclement.fonticons.LocalIconTintProvider
import dev.tclement.fonticons.fa.FontAwesome
import dev.tclement.fonticons.symbols.MaterialSymbols
import dev.tclement.fonticons.symbols.Star
import dev.tclement.fonticons.testapp.screen.FontAwesomeScreen
import dev.tclement.fonticons.testapp.screen.MaterialSymbolsScreen
import dev.tclement.fonticons.testapp.ui.theme.MaterialSymbolsExperimentsTheme

private const val NAV_MATERIAL_SYMBOLS = "symbols"
private const val NAV_FONTAWESOME = "fontawesome"

@Composable
fun MainContent() {
    MaterialSymbolsExperimentsTheme {
        CompositionLocalProvider(
            LocalIconTintProvider provides LocalContentColor
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val currentEntry by navController.currentBackStackEntryAsState()
                            NavigationBarItem(
                                selected = currentEntry?.destination?.route === NAV_MATERIAL_SYMBOLS,
                                onClick = {
                                    navController.navigate(NAV_MATERIAL_SYMBOLS)
                                },
                                icon = {
                                    FontIcon(
                                        icon = MaterialSymbols.Star,
                                        contentDescription = "Material Symbols",
                                        iconFont = rememberMaterialSymbolsFont()
                                    )
                                },
                                label = {
                                    Text(text = "Material Symbols")
                                }
                            )
                            NavigationBarItem(
                                selected = currentEntry?.destination?.route === NAV_FONTAWESOME,
                                onClick = {
                                    navController.navigate(NAV_FONTAWESOME)
                                },
                                icon = {
                                    FontIcon(
                                        icon = FontAwesome.Regular.FontAwesome,
                                        contentDescription = "FontAwesome",
                                        iconFont = rememberFontAwesomeFont()
                                    )
                                },
                                label = {
                                    Text(text = "FontAwesome")
                                }
                            )
                        }
                    }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = NAV_MATERIAL_SYMBOLS
                    ) {
                        composable(NAV_MATERIAL_SYMBOLS) {
                            MaterialSymbolsScreen()
                        }
                        composable(NAV_FONTAWESOME) {
                            FontAwesomeScreen()
                        }
                    }
                }
            }
        }
    }
}
