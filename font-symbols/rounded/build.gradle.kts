import dev.tclement.fonticons.setupSourcesForSymbolsVariant

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.dokka)
    id("multiplatform-structure")
    id("publish")
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

setupSourcesForSymbolsVariant("rounded")
