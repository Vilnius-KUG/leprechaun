import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode.BITCODE

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("maven-publish")
}

val myLibraryVersion = "1.0.0"
val myFrameworkName = "leprechaun"

group = "me.rsetkus"
version = myLibraryVersion

kotlin {
    android {
        publishAllLibraryVariants()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = myFrameworkName
        }
    }

    cocoapods {
        // Required properties
        version = "1.0"
        summary = "Leprechaun API for Kotlin/Native library"
        homepage = "https://github.com/Vilnius-KUG/leprechaun"

        specRepos {
            url("https://github.com/Vilnius-KUG/leprechaun-ios-cocoapod.git")
        }


        // Optional properties
        name = "LeprechaunCocoaPod"

        ios.deploymentTarget = "13.0"

        framework {
            // Required properties
            baseName = myFrameworkName

            // Optional properties
            // Specify the framework linking type. It's dynamic by default.
            isStatic = false
            embedBitcode(BITCODE)
        }
    }

    sourceSets {
        val ktorVersion = "2.2.2"

        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }
    }
}

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
}