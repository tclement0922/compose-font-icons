# Compose Font Icons for Kotlin Multiplatform

[![License](https://img.shields.io/github/license/tclement0922/compose-font-icons?style=for-the-badge)](LICENSE)
[![GitHub release](https://img.shields.io/github/v/release/tclement0922/compose-font-icons?style=for-the-badge)](https://github.com/tclement0922/compose-font-icons/releases/latest)
[![Maven Central Version](https://img.shields.io/maven-central/v/dev.tclement.fonticons/core?style=for-the-badge&label=MAVEN%20CENTRAL)](https://central.sonatype.com/namespace/dev.tclement.fonticons)
[![Maven Central Snapshot version](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fdev%2Ftclement%2Ffonticons%2Fcore%2Fmaven-metadata.xml&versionSuffix=-SNAPSHOT&style=for-the-badge&label=SNAPSHOT)](https://central.sonatype.com/service/rest/repository/browse/maven-snapshots/dev/tclement/fonticons/)
[![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/tclement0922/compose-font-icons/build.yml?style=for-the-badge)](https://github.com/tclement0922/compose-font-icons/actions)

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
> Due to a limitation in the Android API, Android versions older than Oreo (API 26) don't support variable fonts.

# Setup

This library is available on Maven Central and GitHub Packages. Snapshot versions are also available
on Maven Central's Snapshot repository.

Add this to your build.gradle(.kts):

```kotlin
dependencies {
    implementation("dev.tclement.fonticons:core:2.2.0")
    // ...
}
```

## Modules

### `core`

The main module, providing the core functionality of the library.

### `glance`

A module providing support for AndroidX Glance (Android App Widgets / WearOS Tiles). This module is only compatible with
Android.

# Usage

(Optional) Set the default icon parameters:
```kotlin
CompositionLocalProvider(
    LocalIconFont provides yourFont,
    LocalIconTintProvider provides LocalContentColor
) {
    // icons here will have by default the params declared above
}
```

> [!TIP]
> Providing the default parameters is recommended. Otherwise, the parameters `iconFont` and `tint` must be 
> provided for each icon.

You can then use the `FontIcon` composable to display an icon:
```kotlin
FontIcon(
    icon = MyIconFont.MyIcon,
    contentDescription = null
)
```

An alternative function that takes a `String` instead of a `Char` is also available, for fonts that supports ligatures:
```kotlin
FontIcon(
    iconName = "my_icon",
    contentDescription = null
)
```

Go on the [website](https://tclement0922.github.io/compose-font-icons) for advanced usage and additional information.

# License

```
     Copyright 2024-2025 T. Clément (@tclement0922) and contributors
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
