package dependency

object Dependencies {

    // Libraries
    object Libraries {

        // Kotlin
        const val KOTLIN_STD_LIB = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"

        // Coroutines
        const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
        const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"

        // AndroidX
        const val ANDROID_X_CORE_KTX = "androidx.core:core-ktx:${Versions.ANDROID_X_CORE_KTX}"
        const val ANDROID_X_COLLECTION_KTX = "androidx.collection:collection-ktx:${Versions.ANDROID_X_COLLECTION_KTX}"
        const val ANDROID_X_WORK_RUNTIME_KTX = "androidx.work:work-runtime-ktx:${Versions.ANDROID_X_WORK}"
        const val ANDROID_X_APPCOMPAT = "androidx.appcompat:appcompat:${Versions.ANDROID_X_APPCOMPAT}"
        const val ANDROID_X_CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.ANDROID_X_CONSTRAINT_LAYOUT}"
        const val ANDROID_X_RECYCLER_VIEW = "androidx.recyclerview:recyclerview:${Versions.ANDROID_X_RECYCLER_VIEW}"
        const val ANDROID_X_FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${Versions.ANDROID_X_FRAGMENT_KTX}"
        const val ANDROID_X_LIFECYCLE_RUNTIME_KTX = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.ANDROID_X_LIFECYCLE}"
        const val ANDROID_X_SWIPE_REFRESH_LAYOUT = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.ANDROID_X_SWIPE_REFRESH_LAYOUT}"
        const val ANDROID_X_CARD_VIEW = "androidx.cardview:cardview:${Versions.ANDROID_X_CARD_VIEW}"

        const val ANDROID_X_LIFECYCLE_VIEW_MODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.ANDROID_X_LIFECYCLE}"
        const val ANDROID_X_LIFECYCLE_VIEW_MODEL_SAVED_STATE = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.ANDROID_X_LIFECYCLE}"
        const val ANDROID_X_LIFECYCLE_SERVICE = "androidx.lifecycle:lifecycle-service:${Versions.ANDROID_X_LIFECYCLE}"
        const val ANDROID_X_LIFECYCLE_PROCESS = "androidx.lifecycle:lifecycle-process:${Versions.ANDROID_X_LIFECYCLE}"
        const val ANDROID_X_LIFECYCLE_COMMON_JAVA8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.ANDROID_X_LIFECYCLE}"
        const val ANDROID_X_LIFECYCLE_EXTENSIONS = "androidx.lifecycle:lifecycle-extensions:${Versions.ANDROID_X_LIFECYCLE_EXTENSIONS}"

        // Google material
        const val GOOGLE_MATERIAL = "com.google.android.material:material:${Versions.GOOGLE_MATERIAL}"

        // KotlinX Serialization
        const val KOTLIN_X_SERIALIZATION = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLIN_X_SERIALIZATION}"

        // Room
        const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
        const val ROOM_KTX = "androidx.room:room-ktx:${Versions.ROOM}"
        const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"

        // Test
        const val JUNIT = "junit:junit:${Versions.JUNIT}"
        const val COROUTINES_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES}"
        const val ANDROIDX_TEST_CORE = "androidx.test:core:${Versions.ANDROIDX_TEST}"
        const val ANDROIDX_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROIDX_TEST}"
        const val ANDROIDX_TEST_RULES = "androidx.test:rules:${Versions.ANDROIDX_TEST}"
        const val ANDROIDX_TEST_EXT_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_TEST_EXT_JUNIT}"
        const val MOCKK = "io.mockk:mockk:${Versions.MOCKK}"
        const val MOCKK_AGENT_JVM = "io.mockk:mockk-agent-jvm:${Versions.MOCKK}"

        /**
         * Versions
         */
        private object Versions {
            const val KOTLIN = "1.6.10"

            const val COROUTINES = "1.6.0"

            // AndroidX
            const val ANDROID_X_CORE_KTX = "1.7.0"
            const val ANDROID_X_COLLECTION_KTX = "1.2.0"
            const val ANDROID_X_WORK = "2.7.1"
            const val ANDROID_X_APPCOMPAT = "1.4.1"
            const val ANDROID_X_CONSTRAINT_LAYOUT = "2.1.2"
            const val ANDROID_X_RECYCLER_VIEW = "1.2.1"
            const val ANDROID_X_FRAGMENT_KTX = "1.4.1"
            const val ANDROID_X_SWIPE_REFRESH_LAYOUT = "1.1.0"
            const val ANDROID_X_CARD_VIEW = "1.0.0"
            const val ANDROID_X_LIFECYCLE = "2.4.1"
            const val ANDROID_X_LIFECYCLE_EXTENSIONS = "2.2.0"

            const val GOOGLE_MATERIAL = "1.5.0"

            const val ROOM = "2.4.2"
            // Tests
            const val JUNIT = "4.13.2"
            const val ANDROIDX_TEST = "1.4.0"
            const val ANDROIDX_TEST_EXT_JUNIT = "1.1.3"

            const val KOTLIN_X_SERIALIZATION = "1.3.2"

            const val MOCKK = "1.12.3"
        }
    }
}