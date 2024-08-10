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
    explicitApi()

    sourceSets {
        commonMain {
            dependencies {
                api(project(":font-symbols"))
                implementation(compose.components.resources)
            }
        }
    }
}

android {
    namespace = "dev.tclement.fonticons.symbols.outlined"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}
