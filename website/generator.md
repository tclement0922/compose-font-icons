---
layout: generator
---

# Accessors generator

This page allows you to generate a Kotlin object containing every icon from an icon font file (and optionally to create a subset of this font). Practically, here is what changes when using this:

```kotlin
// Before
FontIcon(iconName = "star_outlined")
// After
FontIcon(icon = MyFontObject.StarOutlined)
```

To start using this, import a font file here:
