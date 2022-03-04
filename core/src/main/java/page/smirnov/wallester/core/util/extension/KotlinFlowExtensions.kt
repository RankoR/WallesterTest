package page.smirnov.wallester.core.util.extension

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

fun <T> Flow<T>.onFailureLog(rethrow: Boolean = true, lazyMessage: (() -> Any)? = null): Flow<T> {
    return catch { t ->
        val message = lazyMessage?.invoke()?.toString()

        // Here was a Timber call, but as we can't use libraries — here's a generic tag
        Log.e("TEST", message, t)

        if (rethrow) {
            throw t
        }
    }
}
