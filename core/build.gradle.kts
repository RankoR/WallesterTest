import dependency.Dependencies
import extension.addCoreTestLibraries

plugins {
    `base-lib`
}

dependencies {
    api(Dependencies.Libraries.KOTLIN_STD_LIB)

    api(Dependencies.Libraries.ANDROID_X_CORE_KTX)
    api(Dependencies.Libraries.ANDROID_X_COLLECTION_KTX)
    api(Dependencies.Libraries.ANDROID_X_WORK_RUNTIME_KTX)

    // Coroutines
    api(Dependencies.Libraries.COROUTINES_CORE)
    api(Dependencies.Libraries.COROUTINES_ANDROID)

    // Serialization
    api(Dependencies.Libraries.KOTLIN_X_SERIALIZATION)

    // Tests
    addCoreTestLibraries()
}
