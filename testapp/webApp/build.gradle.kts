import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    unversioned(libs.plugins.kotlin.multiplatform)
    unversioned(libs.plugins.jetbrains.compose)
    unversioned(libs.plugins.kotlin.compose.compiler)
    id("fonticons.java-target")
}

kotlin {
    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":testapp"))
            implementation(libs.jetbrains.compose.ui)
        }
    }
}
