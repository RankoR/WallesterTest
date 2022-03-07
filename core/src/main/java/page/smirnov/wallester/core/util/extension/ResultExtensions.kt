package page.smirnov.wallester.core.util.extension

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Transforms [Result] with [T] to [Result] with [Unit]
 */
fun <T> Result<T>.ignoreValue(): Result<Unit> {
    return map { }
}

/**
 * Calls [block] in both [onSuccess] and [onFailure]
 */
inline fun <T> Result<T>.onFinish(crossinline block: () -> Unit): Result<T> {
    return this
        .onSuccess { block() }
        .onFailure { block() }
}

/**
 * Same as [onFinish] but executes [block] on [Dispatchers.Main]
 */
suspend inline fun <T> Result<T>.onFinishWithMainContext(crossinline block: () -> Unit): Result<T> {
    return this
        .onSuccess {
            withContext(Dispatchers.Main) {
                block()
            }
        }
        .onFailure {
            withContext(Dispatchers.Main) {
                block()
            }
        }
}

/**
 * Same as [onFailure] but executes [block] on [Dispatchers.Main]
 */
suspend inline fun <T> Result<T>.onFailureWithMainContext(crossinline block: (t: Throwable) -> Unit): Result<T> {
    return this.onFailure {
        withContext(Dispatchers.Main) {
            block(it)
        }
    }
}

/**
 * Same as [onSuccess] but executes [block] on [Dispatchers.Main]
 */
suspend inline fun <T> Result<T>.onSuccessWithMainContext(crossinline block: (value: T) -> Unit): Result<T> {
    return this.onSuccess { value ->
        withContext(Dispatchers.Main) {
            block(value)
        }
    }
}

/**
 * If [Result] is failure, logs [Throwable] to [Timber.e]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> Result<T>.onFailureLog(): Result<T> {
    return this.onFailure { t ->
        // Here was a Timber call, but as we can't use libraries — here's a generic tag
        Log.e("WTEST", "onFailure", t)
    }
}

/**
 * If [Result] is failure, logs [Throwable] and [lazyMessage] to [Timber.e]
 */
inline fun <T> Result<T>.onFailureLog(crossinline lazyMessage: (() -> Any)): Result<T> {
    return this.onFailure { t ->
        val message = lazyMessage().toString()

        // Here was a Timber call, but as we can't use libraries — here's a generic tag
        Log.e("WTEST", message, t)
    }
}

inline fun <T, R> Result<T>.flatMap(transform: (value: T) -> Result<R>): Result<R> {
    return when (val result = getOrNull()) {
        null -> Result.failure(exceptionOrNull() ?: Throwable("Unknown failure"))
        else -> {
            try {
                transform(result)
            } catch (t: Throwable) {
                Result.failure(t)
            }
        }
    }
}

inline fun <R, T : R> Result<T>.recoverResult(crossinline transform: (exception: Throwable) -> Result<R>): Result<R> {
    return when (val exception = exceptionOrNull()) {
        null -> this
        else -> transform(exception)
    }
}

suspend fun <T> Result<T>.toFlowThrowOnFailure(): Flow<T> {
    return flow { emit(this@toFlowThrowOnFailure) }.map { it.getOrThrow() }
}
