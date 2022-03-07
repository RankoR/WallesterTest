package page.smirnov.wallester.core_network

import org.junit.Assert

/**
 * Unfortunately, assertThrows is not available on Android
 * As it's a test project, I don't have much time, so I'll just copy-paste it across modules
 * But in a normal situation I'd move it to some shared source set
 */
inline fun <reified T : Throwable> assertThrows(crossinline block: () -> Unit) {
    try {
        block()

        Assert.fail("Expected ${T::class.java.simpleName}, but it was not thrown")
    } catch (e: Throwable) {
        if (e !is T) {
            Assert.fail("Expected ${T::class.java.simpleName} but got other exception: $e")
        }
    }
}
