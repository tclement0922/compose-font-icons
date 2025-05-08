# Composable

The `FontIcon` composable is the main way to display icons in this library. Its usage is similar to the `Icon`
composable from Jetpack Compose, but it is specifically designed for icon fonts.

Here is a simple example of how to use it:

```kotlin
FontIcon(
    icon = MyIconFont.MyIcon, // Here, MyIconFont.MyIcon is a Char equal to the unicode of the icon
    contentDescription = null
)
```

## Parameters

The `FontIcon` composable takes the following parameters:
- `icon`: The icon to display, a `Char`. An overload with a `String` named `iconName` is also available for fonts that support ligatures.
- `contentDescription`: A description of the icon for accessibility purposes. This can be set to `null` if not relevant.
- `modifier`: The Compose `Modifier` to apply to the icon. Optional.
- `tint`: The color to apply to the icon. If not provided, the icon will use the default color.
- `size`: The size of the icon. If not provided, the icon will use the default size.
- `weight`: The weight of the icon. If not provided, the icon will use the default weight.
- `iconFont`: The icon font to use. If not provided, the icon will use the default icon font.