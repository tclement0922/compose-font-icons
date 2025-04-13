Changes from 2.0.0:
- Added `rememberFontIconPainter` to create a painter from a font icon.
- The `FontIcon` composable rendering method has been changed and should now be more memory efficient.
- For Android: Some fonts are now loaded asynchronously, this includes:
  - Variable fonts created from multiplatform resources
  - Fonts created using the multiplatform resources version of `createVariableIconFont` or `createStaticIconFont`

Built with:
- Compose Multiplatform 1.7.3
- Kotlin 2.1.20
- AndroidX Glance 1.1.1
- Android Gradle plugin 8.7.3

**Full Changelog**: https://github.com/tclement0922/compose-font-icons/compare/v2.0.0...v2.1.0