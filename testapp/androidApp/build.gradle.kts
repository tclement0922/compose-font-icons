plugins {
    unversioned(libs.plugins.android.application)
    unversioned(libs.plugins.jetbrains.compose)
    unversioned(libs.plugins.kotlin.compose.compiler)
    id("fonticons.java-target")
}

android {
    namespace = "dev.tclement.fonticons.testapp.android"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.tclement.fonticons.testappp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":testapp"))
    implementation(project(":core"))
    implementation(project(":glance"))
    implementation(libs.jetbrains.compose.components.resources) // For CMP ressources classes resolution
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.glance.appwidget)
}
