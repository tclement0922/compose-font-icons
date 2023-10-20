# Compose Font Icons for Kotlin Multiplatform

Makes possible to use icons from a font in JetBrains Compose Multiplatform. 
Current supported targets are JVM (desktop) and Android. An additional library is available for
AndroidX Glance (Android App Widgets / WearOS Tiles).

# Usage

Here is how to set the default icon parameters:
```kotlin
ProvideIconParameters(
    iconFont = rememberVariableIconFont(params...), // ex: for outlined symbols: rememberOutlinedMaterialSymbolsFont()
    tint = Color.Black // if using Material3, use LocalContentColor.current
) {
    // icons here will have by default the params declared above
}
```

Here is an example:
```kotlin
FontIcon(
    iconName = "account_circle",
    contentDescription = null,
    tint = Color.Black
)
```

Or with Material Symbols:
```kotlin
FontIcon(
    icon = MaterialSymbols.AccountCircle,
    contentDescription = null,
    tint = Color.Black
)
```

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
    implementation("dev.tclement.fonticons:core:1.0.0")

    // for Glance support
    implementation("dev.tclement.fonticons:core-glance:1.0.0")

    // for Material Symbols, replace 'THEME' with 'outlined', 'rounded' or 'sharp'
    implementation("dev.tclement.fonticons:font-symbols-THEME:1.0.0")
}
```

# License

```
     Copyright 2023 T. Clément (@tclement0922)
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
