plugins {
    kotlin("jvm")
    id("fonticons.java-target")
}

group = "dev.tclement"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.jetbrains.dokka.core)
    compileOnly(libs.jetbrains.dokka.base)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.fasterxml.jackson.annotations)
}
