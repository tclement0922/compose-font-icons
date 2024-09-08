# Compose Font Icons for Kotlin Multiplatform

![License](https://img.shields.io/github/license/tclement0922/compose-font-icons?style=for-the-badge)
![GitHub release](https://img.shields.io/github/v/release/tclement0922/compose-font-icons?style=for-the-badge)
![Maven Central Version](https://img.shields.io/maven-central/v/dev.tclement.fonticons/core?style=for-the-badge)
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/tclement0922/compose-font-icons/build.yml?style=for-the-badge)

Makes possible to use icons from a font in JetBrains Compose Multiplatform.
Currently supported targets are Android, Desktop (JVM), Web (JS and WASM), macOS, and iOS.

> [!NOTE]
> iOS and macOS are not tested as much as the other targets. There should not be any issue however, since they use the
> same rendering as every other non-Android target.

This library supports the [Compose Multiplatform Common resources API](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-images-resources.html)
alongside platform-specific resources like resource IDs for Android, classpath resources for JVM...

Variable fonts are supported, the optical size (`opsz`) and weight (`wgth`) axes will be set automatically depending on the
icon size and provided parameters.

> [!IMPORTANT]
> Due to a limitation in the Android API, Android versions older that Oreo (API 26) don't support variable fonts.

# Setup

This library is available on Maven Central and GitHub Packages. 

Add this to your build.gradle(.kts):

```kotlin
dependencies {
    implementation("dev.tclement.fonticons:MODULE:VERSION")
}
```

## Modules

### `core`

The main module, providing the core functionality of the library.

### `core-glance`

An additional module providing support for AndroidX Glance (Android App Widgets / WearOS Tiles). This is the only module
that is only compatible with Android.

### `font-fa`

This module provides support for the Font Awesome Free font. An icon can be referenced by its name, for example 
`FontAwesome.Regular.Star`. The fonts are not provided in this module, they must either be provided by the user or by 
using the packaged font modules.

### `font-fa-brands`, `font-fa-regular` and `font-fa-solid`

Packaged variants of the Font Awesome Free font[^1]. These modules provide their corresponding font as a resource and a 
function to create this font (`rememberBrandsFontAwesomeFont`, `rememberRegularFontAwesomeFont` or
`rememberSolidFontAwesomeFont`).

[^1]: The Font Awesome Free fonts are licensed under the
  [SIL OFL 1.1 license](https://github.com/FortAwesome/Font-Awesome/blob/6.x/LICENSE.txt)

### `font-symbols`

This module provides support for the Material Symbols variable font. An icon can be referenced by its name, for example
`MaterialSymbols.Star`. The fonts are not provided in this module, they must either be provided by the user or by using 
one of the packaged font modules.

> [!CAUTION]
> Android API 25 and lower don't support variable fonts, this means that variations won't be applied. This font will 
> default to its default settings on those versions.

### `font-symbols-outlined`, `font-symbols-rounded` and `font-symbols-sharp`

Packaged variants of the Material Symbols variable font[^2]. These modules provide their corresponding font as a 
resource and a function to create this font (`rememberOutlinedSymbolsFont`, `rememberRoundedSymbolsFont` or
`rememberMaterialSymbolsFont`).

[^2]: The Material Symbols fonts are licensed under the
  [Apache 2.0 license](https://github.com/google/material-design-icons/blob/master/LICENSE)

# Usage

(Optional) Set the default icon parameters:
```kotlin
ProvideIconParameters(
    iconFont = your_font, // ex: for outlined Material Symbols: rememberOutlinedMaterialSymbolsFont()
    tintProvider = LocalContentColor
) {
    // icons here will have by default the params declared above
}
```

> [!TIP]
> Using `ProvideIconParameters` (or equivalent) is recommended. Otherwise, the parameters `iconFont` and `tint` must be 
> provided for each icon.

You can then use the `FontIcon` composable to display an icon:
```kotlin
FontIcon(
    icon = MaterialSymbols.Star,
    contentDescription = null
)
```

An alternative function that takes a `String` instead of a `Char` is also available, for fonts that supports ligatures:
```kotlin
FontIcon(
    iconName = "star",
    contentDescription = null
)
```

Read the full API reference [here](https://tclement0922.github.io/compose-font-icons) for advanced usage and additional information.

# License

```
     Copyright 2024 T. Clément (@tclement0922) and contributors
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
      http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
```
