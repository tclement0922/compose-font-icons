plugins {
    kotlin("jvm")
}

group = "dev.tclement"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":dokka-vitepress-renderer"))
    compileOnly(libs.jetbrains.dokka.core)
    compileOnly(libs.jetbrains.dokka.base)
    compileOnly(libs.jetbrains.dokka.allmodulespage)
    compileOnly(libs.jetbrains.dokka.templating)
}

kotlin {
    jvmToolchain(8)
}