import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    unversioned(libs.plugins.kotlin.jvm)
    unversioned(libs.plugins.jetbrains.compose)
    unversioned(libs.plugins.kotlin.compose.compiler)
    id("fonticons.java-target")
}

compose.desktop {
    application {
        mainClass = "dev.tclement.fonticons.testapp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "dev.tclement.fonticons.testapp"
            packageVersion = "1.0.0"
        }
    }
}

dependencies {
    implementation(project(":testapp"))
    implementation(compose.desktop.currentOs)
}
