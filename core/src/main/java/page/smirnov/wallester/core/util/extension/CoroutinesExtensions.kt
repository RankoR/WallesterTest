package page.smirnov.wallester.core.util.extension

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

inline fun <reified T : Throwable> Result<*>.except(): Result<*> {
    return onFailure { if (it is T) throw it }
}

inline fun <reified T> Deferred<T>.asFlow(): Flow<T> {
    return flow {
        emit(await())
    }
}
