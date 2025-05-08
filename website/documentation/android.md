# Android-specific

There are also Android-specific functions/classes available, notably a Bitmap and a Drawable.

## Bitmap

You can use the `FontIconBitmap` function to create a bitmap from a font icon (this function is **not** composable), for
example:

```kotlin
val bitmap = FontIconBitmap(
    icon = MyIconFont.MyIcon,
    iconFont = yourFont,
    tint = yourColor,
    context = context,
    density = Density(context)
)
```

There are multiple overloads for this function, go check the API reference for more information.

## Drawable

You can use the `FontIconDrawable` class to create a drawable from a font icon, for example:

```kotlin
val drawable = FontIconDrawable(
    icon = MyIconFont.MyIcon,
    iconFont = yourFont,
    tint = yourColor,
    context = context,
    density = Density(context)
)
```

There are multiple overloads for the constructor (the sames as the `FontIconBitmap` function), go check the API
reference for more information.

## Canvas extension function

Finally, there is a `drawFontIcon` extension function for the `Canvas` class, which allows you to draw a font icon on a
canvas. You'll probably want to use either `FontIconBitmap` or `FontIconDrawable` instead, but this function might be
useful for use in a custom View for example.

See the API reference for more information.