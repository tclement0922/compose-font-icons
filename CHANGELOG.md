Changes from 2.1.1:
- Deprecate `ProvideIconParameters`: The two overloads of this function may cause an 'overload resolution ambiguity' if only one of the parameters is specified. The corresponding Glance-specific function (`ProvideGlanceIconParameters`) has also been deprecated for consistency.
- Small measurement change in an attempt to fix #11
  - This change should not do anything appart from fixing this bug, if you notice any inconsistency please open an issue

Built with:
- Compose Multiplatform 1.11.0
- Kotlin 2.3.21
- AndroidX Glance 1.1.1
- Android Gradle plugin 9.0.1

**Full Changelog**: https://github.com/tclement0922/compose-font-icons/compare/v2.1.1...v2.2.0