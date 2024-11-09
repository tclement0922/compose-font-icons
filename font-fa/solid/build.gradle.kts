plugins {
    unversioned(libs.plugins.android.library)
    unversioned(libs.plugins.jetbrains.dokka)
    id("fonticons.multiplatform-structure")
    id("fonticons.publish")
    id("fonticons.fa-variant")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":font-fa"))
                implementation(compose.components.resources)
            }
        }
    }
}
