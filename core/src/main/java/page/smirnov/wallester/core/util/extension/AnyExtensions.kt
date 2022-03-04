package page.smirnov.wallester.core.util.extension

inline fun <T> T?.ifNull(block: () -> Unit): T? {
    if (this == null) {
        block()
    }

    return this
}
