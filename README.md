# Compose Font Icons for Kotlin Multiplatform

![License](https://img.shields.io/github/license/tclement0922/compose-font-icons?style=for-the-badge)
![GitHub release](https://img.shields.io/github/v/release/tclement0922/compose-font-icons?style=for-the-badge)


Makes possible to use icons from a font in JetBrains Compose Multiplatform. 
Currently supported targets are Android, Desktop (JVM) and Web (JS). An additional library is available for
AndroidX Glance (Android App Widgets / WearOS Tiles).

# Setup

This library is (for now) only available in GitHub Packages. 

## For Gradle Kotlin Dsl:

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/tclement0922/compose-font-icons")
        credentials {
            // replace with your username
            username = "YOUR_USERNAME"
            // replace with a personal access token (classic) that has at least the :read_packages scope and linked to the username above
            password = "YOUR_GITHUB_TOKEN"
        }
    }
}

dependencies {
    implementation("dev.tclement.fonticons:ARTIFACT:VERSION")
    ...
}
```

## Available artifacts

<table>
    <thead>
        <tr>
            <th rowspan="2">
                Artifact
            </th>
            <th rowspan="2">
                Artifact description
            </th>
            <th colspan="3">Supported platforms</th>
        </tr>
        <tr>
            <th>Android</th>
            <th>Desktop (JVM)</th>
            <th>Web (JS)</th>
        </tr>
    </thead>
    <tbody>
        <tr align="center">
            <td align="start">core</td>
            <td>Main artifact</td>
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
        </tr>
        <tr align="center">
            <td align="start">font-symbols</td>
            <td>Material Symbols common class</td>
            <td>✔️</td>
            <td>✔️</td>
            <td>✔️</td>
        </tr>
        <tr align="center">
            <td align="start">font-symbols-[outlined|rounded|sharp]</td>
            <td>Material Symbols variants</td>
            <td>✔️</td>
            <td>✔️</td>
            <td>❎</td>
        </tr>
    </tbody>
</table>

> [!NOTE]
> Web (WASM) support is planned, but won't happen until JetBrains releases compatible artifacts in their stable maven repo
>
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
