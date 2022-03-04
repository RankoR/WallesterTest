import dimension.Dimensions
import extension.addCoreTestLibraries
import version.BuildVersions
import version.addVersionsToBuildConfig

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.6.10" // TODO: Move version to constants
    id("kotlin-parcelize")
}

android {
    compileSdk = BuildVersions.COMPILE_SDK_VERSION

    defaultConfig {
        applicationId = "page.smirnov.wallester"

        minSdk = BuildVersions.MIN_SDK_VERSION
        targetSdk = BuildVersions.TARGET_SDK_VERSION

        versionCode = BuildVersions.VERSION_CODE
        versionName = BuildVersions.versionName

        addVersionsToBuildConfig()

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName(Dimensions.BuildType.Debug.NAME) {
            applicationIdSuffix = ".test"

            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName(Dimensions.BuildType.Release.NAME) {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            // signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = BuildVersions.JAVA_VERSION
        targetCompatibility = BuildVersions.JAVA_VERSION
    }

    kotlinOptions {
        jvmTarget = BuildVersions.JAVA_VERSION.toString()
    }

    kapt {
        correctErrorTypes = true
    }

    packagingOptions {
        resources.excludes.add("DebugProbesKt.bin")
    }

    lint {
        isCheckReleaseBuilds = false
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Modules
    implementation(projects.core)
    implementation(projects.corePersistence)
    implementation(projects.coreUi)
    implementation(projects.coreNetwork)

    // Tests
    addCoreTestLibraries()
}
