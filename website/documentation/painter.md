# Painter

Even though the `FontIcon` composable is the main way to display icons, you can also use a `Painter` for specific use cases.
To do so, use the `rememberFontIconPainter` function:
```kotlin
val painter = rememberFontIconPainter(
    icon = MyIconFont.MyIcon
)
```

## Parameters

The `rememberFontIconPainter` function takes the following parameters:
- `icon`: The icon to display, a `Char`. An overload with a `String` named `iconName` is also available for fonts that support ligatures.
- `tint`: The color to apply to the icon. If not provided, the icon will use the default color.
- `weight`: The weight of the icon. If not provided, the icon will use the default weight.
- `iconFont`: The icon font to use. If not provided, the icon will use the default icon font.
- `size`: The size of the icon. If not provided, the icon will use the default size. This will set the intrinsic size of the painter.
