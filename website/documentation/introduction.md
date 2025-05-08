# Introduction

This library makes possible to use font-based icons in JetBrains Compose Multiplatform (or just Jetpack Compose).

Every platform supported by Compose Multiplatform is also supported by this library. This includes:
- Android
- Desktop (JVM)
- iOS
- macOS
- JS
- WASM

> [!NOTE]
> While all those platforms are supported, the Apple ones (iOS and macOS) are not tested as much as the other targets. 
> There should not be any issue however, since they use the same rendering as every other non-Android target.

Both variable (e.g. Material Symbols) and non-variable (e.g. FontAwesome) fonts are supported, with automatic optical 
sizing for variable fonts.
