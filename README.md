# Compose Font Icons for Kotlin Multiplatform

![License](https://img.shields.io/github/license/tclement0922/compose-font-icons?style=for-the-badge)
![GitHub release](https://img.shields.io/github/v/release/tclement0922/compose-font-icons?style=for-the-badge)
![Maven Central Version](https://img.shields.io/maven-central/v/dev.tclement.fonticons/core?style=for-the-badge)
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/tclement0922/compose-font-icons/build.yml?style=for-the-badge)


Makes possible to use icons from a font in JetBrains Compose Multiplatform. 
Currently supported targets are Android, Desktop (JVM) and Web (JS and WASM). An additional library is available for
AndroidX Glance (Android App Widgets / WearOS Tiles).

This library supports the [Compose Multiplatform Common resources API](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-images-resources.html)
alongside platform-specific resources like resource IDs for Android, classpath resources for JVM...

# Setup

This library is available on Maven Central and GitHub Packages. 

Add this to your build.gradle(.kts):

```kotlin
dependencies {
    implementation("dev.tclement.fonticons:ARTIFACT:VERSION")
}
```

## Artifacts

<table>
    <thead>
        <tr>
            <th rowspan="2">
                Artifact
            </th>
            <th rowspan="2">
                Artifact description
            </th>
            <th colspan="4">Supported platforms</th>
        </tr>
        <tr>
            <th>Android</th>
            <th>Desktop (JVM)</th>
            <th>Kotlin/JS</th>
            <th>Kotlin/WASM</th>
        </tr>
    </thead>
    <tbody>
        <tr align="center">
            <td align="start">core</td>
            <td>Main artifact</td>
            <td>✔️</td>
            <td>✔️</td>
            <td>✔️</td>
            <td>✔️</td>
        </tr>
        <tr align="center">
            <td align="start">core-glance</td>
            <td>AndroidX Glance support</td>
            <td>✔️</td>
            <td>❎</td>
            <td>❎</td>
            <td>❎</td>
        </tr>
        <tr align="center">
            <td align="start">font-symbols</td>
            <td rowspan="4">Material Symbols fonts</td>
            <td>✔️</td>
            <td>✔️</td>
            <td>✔️</td>
            <td>✔️</td>
        </tr>
        <tr align="center">
            <td align="start">font-symbols-outlined</td>
            <td>✔️</td>
            <td>✔️</td>
            <td>✔️</td>
            <td>✔️</td>
        </tr>
        <tr align="center">
            <td align="start">font-symbols-rounded</td>
            <td>✔️</td>
            <td>✔️</td>
            <td>✔️</td>
            <td>✔️</td>
        </tr>
        <tr align="center">
            <td align="start">font-symbols-sharp</td>
            <td>✔️</td>
            <td>✔️</td>
            <td>✔️</td>
            <td>✔️</td>
        </tr>
    </tbody>
</table>

> [!NOTE]
> IOS won't be supported unless I get a Mac (or someone else contributes)

# Usage

Set the default icon parameters:
```kotlin
ProvideIconParameters(
    iconFont = rememberVariableIconFont(params...), // ex: for outlined symbols: rememberOutlinedMaterialSymbolsFont()
    tintProvider = LocalContentColor
) {
    // icons here will have by default the params declared above
}
```

Then:
```kotlin
FontIcon(
    iconName = "account_circle",
    contentDescription = null
)
```

Or for Material Symbols:
```kotlin
FontIcon(
    icon = MaterialSymbols.AccountCircle,
    contentDescription = null
)
```

Read the full doc [here](https://tclement0922.github.io/compose-font-icons).

# License

```
     Copyright 2024 T. Clément (@tclement0922)
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
