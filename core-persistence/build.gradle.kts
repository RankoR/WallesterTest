import extension.addCoreTestLibraries

plugins {
    `base-lib`
}

dependencies {
    implementation(projects.core)

    // Tests
    addCoreTestLibraries()
}
