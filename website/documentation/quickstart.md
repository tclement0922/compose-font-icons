# Quick start

This library is available on Maven Central and GitHub Packages.

## Modules

Group : `dev.tclement.fonticons`

|  Module  | Description                                                                          | Platforms |
|:--------:|--------------------------------------------------------------------------------------|-----------|
|  `core`  | The main module, providing the core functionality of the library.                    | All       |
| `glance` | A module providing support for AndroidX Glance (Android App Widgets / WearOS Tiles). | Android   |

For Gradle, you can use the core module this way:

```kotlin
dependencies {
    implementation("dev.tclement.fonticons:core:<version>") // [!code focus]
}
```

## Create an icon font

You can initialize a font using either [`rememberStaticIconFont`](/api/core/dev.tclement.fonticons/remember-static-icon-font.md) 
or [`rememberVariableIconFont`](/api/core/dev.tclement.fonticons/remember-variable-icon-font.md):

```kotlin
val iconFont = rememberStaticIconFont(
    fontResource = Res.font.yourFont,
    fontFeatureSettings = "liga", // optional
)
// or
val iconFont = rememberVariableIconFont(
    fontResource = Res.font.yourFont,
    weights = arrayOf(FontWeight.W400, FontWeight.W500, FontWeight.W600, FontWeight.W700), // list of all the weights you want to use
    fontVariationSettings = FontVariation.Settings(FontVariation.slant(1f)), // optional
    fontFeatureSettings = "wght", // optional
)
```

Note that those examples are for Compose Multiplatform Resources. There are also platform-specific overloads for some of
the supported platforms, go check the API reference for more information.

Non-composable versions of those functions are also available, [`createStaticIconFont`](/api/core/dev.tclement.fonticons/create-static-icon-font.md) and 
[`createVariableIconFont`](/api/core/dev.tclement.fonticons/create-variable-icon-font.md), but they are not recommended 
for most use cases. They are if you want to use the library in a non-composable context (e.g. in an Android Service, 
etc.). Moreover, they are considered experimental since they use workarounds, especially the ones using Compose
Multiplatform Resources.

## Global configuration

You will most probably want to use the same options for every icon. To do so, you can use the [`ProvideIconParameters`](/api/core/dev.tclement.fonticons/-provide-icon-parameters.md)
composable:

```kotlin
ProvideIconParameters(
    iconFont = yourFont,
    tintProvider = LocalContentColor, // or tint = yourColor
    size = 24.dp, // optional, 24.dp by default
    weight = FontWeight.Normal, // optional, FontWeight.Normal by default
) {
    // icons in here will have by default the params declared above,
    // this basically works like MaterialTheme
}
```

If you do not use `ProvideIconParameters`, you will have to provide the parameters for each icon.

## Usage

There are two ways to use an icon: using a [layout composable](layout-composable.md) or using a [painter](painter.md).
[Android-specific](android.md) functions/classes are also available.