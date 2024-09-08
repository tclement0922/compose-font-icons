Changes from 1.3.0:
- Add FontAwesome Free as a packaged font
- Add non-Composable functions to create `IconFont` instances (#4)
- Fix icon alignment in `Canvas.drawIcon` (#5)
- Restrict `rememberVariableIconFont` to Android O and higher
- Deprecate `Canvas.drawIcon` and everything related to it (for replacement see d5ef8d9a34af16a3b1dfe55c66ae57f279cd7f58)

Packaged fonts:
- Material Symbols (revision [e9da219](https://github.com/google/material-design-icons/tree/e9da2194e65080a829d670ae39a99c7b5fc09548))
- FontAwesome Free (version [6.6.0](https://github.com/FortAwesome/Font-Awesome/tree/6.6.0))

Built with:
- Compose Multiplatform 1.6.11
- Kotlin 2.0.20
- AndroidX Glance 1.1.0
- Android Gradle plugin 8.5.2

**Full Changelog**: https://github.com/tclement0922/compose-font-icons/compare/v1.3.0...v1.4.0