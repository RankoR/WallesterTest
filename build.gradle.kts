import plugin.BuildPlugins

buildscript {
    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        val kotlinVersion = "1.6.10"

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion") // For some reason it doesn't work from buildSrc
    }
}

plugins.apply(BuildPlugins.UPDATE_DEPENDENCIES)

allprojects {
    plugins.apply(BuildPlugins.SPOTLESS)
}
