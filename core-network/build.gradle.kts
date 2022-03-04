import extension.addCoreTestLibraries

plugins {
    `base-lib`

    kotlin("plugin.serialization") version "1.6.10" // TODO: Move version to constants
}

dependencies {
    implementation(projects.core)

    // Tests
    addCoreTestLibraries()
}
