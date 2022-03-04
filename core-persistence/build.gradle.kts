import dependency.Dependencies
import extension.addCoreTestLibraries

plugins {
    `base-lib`
}

dependencies {
    implementation(projects.core)

    // Room
    api(Dependencies.Libraries.ROOM_RUNTIME)
    api(Dependencies.Libraries.ROOM_KTX)

    // Tests
    addCoreTestLibraries()
}
