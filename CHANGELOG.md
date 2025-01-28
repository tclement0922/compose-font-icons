Changes from 1.4.1:
- Dropped support for packaged fonts, the accessors should now be manually generated from [the tool available on the new
  website](https://tclement0922.github.io/compose-font-icons/generator)
- Removed every deprecated functions
- Slightly changed the variable fonts APIs: the `fontVariationSettings` parameters are now a `FontVariation.Settings` 
  object instead of an `Array<FontVariation.Setting>`
- The Desktop (JVM) target now targets Java 11 instead of Java 8 since Compose Multiplatform requires Java 11 for the 
  JVM
- Renamed the `core-glance` module to `glance`

Built with:
- Compose Multiplatform 1.7.3
- Kotlin 2.1.0
- AndroidX Glance 1.1.1
- Android Gradle plugin 8.7.3

**Full Changelog**: https://github.com/tclement0922/compose-font-icons/compare/v1.4.0...v1.4.1