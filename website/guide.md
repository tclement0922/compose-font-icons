# Get started

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
