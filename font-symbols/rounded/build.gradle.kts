import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    alias(libs.plugins.koltin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.dokka)
    `maven-publish`
}

val libProperties = loadProperties(rootDir.absolutePath + "/library.properties")
val githubProperties = loadProperties(rootDir.absolutePath + "/github.properties")

group = libProperties.getString("group")
version = libProperties.getString("version")

kotlin {
    explicitApi()

    jvm("desktop") {
        jvmToolchain(8)
    }

    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":font-symbols"))
            }
        }
    }
}

android {
    namespace = "dev.tclement.fonticons.symbols.rounded"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    sourceSets["main"].res.srcDir("src/commonMain/resources")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

publishing {
    publications {
        repositories {
            /*maven {
                name = "Local"
                url = uri(rootProject.layout.projectDirectory.dir("maven"))
            }*/
            maven {
                name = "GitHubPackages"
                url = uri(libProperties.getString("packages-url"))
                credentials {
                    username = githubProperties.getString("username")
                    password = githubProperties.getString("token")
                }
            }
        }
    }
}
