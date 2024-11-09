plugins {
    unversioned(libs.plugins.android.library)
    unversioned(libs.plugins.jetbrains.dokka)
    id("fonticons.multiplatform-structure")
    id("fonticons.publish")
    id("fonticons.symbols-variant")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":font-symbols"))
                implementation(compose.components.resources)
            }
        }
    }
}
