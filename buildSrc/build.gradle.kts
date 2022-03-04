plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    mavenCentral()
    google()
    maven("https://plugins.gradle.org/m2/")
}

private object Dependencies {
    object BuildScript {
        const val TOOLS_GRADLE = "com.android.tools.build:gradle:${Versions.TOOLS_GRADLE}"
        const val KOTLIN_GRADLE = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"

        const val KOTLIN_SERIALIZATION = "org.jetbrains.kotlin:kotlin-serialization:${Versions.KOTLIN}"

        const val GRADLE_VERSIONS = "com.github.ben-manes:gradle-versions-plugin:${Versions.GRADLE_VERSIONS}"
        const val SPOTLESS = "com.diffplug.spotless:spotless-plugin-gradle:${Versions.SPOTLESS}"

        private object Versions {
            const val TOOLS_GRADLE = "7.0.4"
            const val KOTLIN = "1.6.10"

            const val GRADLE_VERSIONS = "0.39.0"
            const val SPOTLESS = "5.16.0"
        }
    }
}

dependencies {
    implementation(Dependencies.BuildScript.TOOLS_GRADLE)
    implementation(Dependencies.BuildScript.KOTLIN_GRADLE)

    implementation(Dependencies.BuildScript.SPOTLESS)
    implementation(Dependencies.BuildScript.GRADLE_VERSIONS)
}
