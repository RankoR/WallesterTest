package page.smirnov.wallester.core.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.doAfterDelay(delayMillis: Long, block: () -> Unit) {
    launch {
        withContext(Dispatchers.Default) {
            delay(delayMillis)
        }
        withContext(Dispatchers.Main) {
            block()
        }
    }
}

/**
 * Same as [withContext] but wraps [block] in [runCatching] and returns [Result] instead of raw [T]
 */
suspend inline fun <T> withContextCatching(
    context: CoroutineContext,
    crossinline block: suspend CoroutineScope.() -> T
): Result<T> {
    return withContext(context) {
        runCatching {
            block()
        }
    }
}

suspend inline fun <T1, T2, R> combine(
    crossinline f1: suspend () -> Result<T1>,
    crossinline f2: suspend () -> Result<T2>,
    noinline transform: suspend (T1, T2) -> R
): Result<R> {
    val combinedFlow = combine(
        flow { emit(f1()) }.map { it.getOrThrow() },
        flow { emit(f2()) }.map { it.getOrThrow() },
        transform
    )

    return runCatching {
        combinedFlow.first()
    }
}

suspend inline fun <T1, T2, T3, R> combine(
    crossinline f1: suspend () -> Result<T1>,
    crossinline f2: suspend () -> Result<T2>,
    crossinline f3: suspend () -> Result<T3>,
    noinline transform: suspend (T1, T2, T3) -> R
): Result<R> {
    val combinedFlow = combine(
        flow { emit(f1()) }.map { it.getOrThrow() },
        flow { emit(f2()) }.map { it.getOrThrow() },
        flow { emit(f3()) }.map { it.getOrThrow() },
        transform
    )

    return runCatching {
        combinedFlow.first()
    }
}

suspend inline fun <T1, T2, T3, T4, R> combine(
    crossinline f1: suspend () -> Result<T1>,
    crossinline f2: suspend () -> Result<T2>,
    crossinline f3: suspend () -> Result<T3>,
    crossinline f4: suspend () -> Result<T4>,
    noinline transform: suspend (T1, T2, T3, T4) -> R
): Result<R> {
    val combinedFlow = combine(
        flow { emit(f1()) }.map { it.getOrThrow() },
        flow { emit(f2()) }.map { it.getOrThrow() },
        flow { emit(f3()) }.map { it.getOrThrow() },
        flow { emit(f4()) }.map { it.getOrThrow() },
        transform
    )

    return runCatching {
        combinedFlow.first()
    }
}

suspend inline fun <reified R> combine(
    functions: Collection<suspend () -> R>
): Result<Array<R>> {
    return functions
        .map { f ->
            flow { emit(f()) }
        }
        .let { combineFlows(it) }
}

suspend inline fun <reified R> combineFlows(
    flows: Collection<Flow<R>>
): Result<Array<R>> {
    val combinedFlow = combine(flows) { it }

    return runCatching { combinedFlow.first() }
}

suspend inline fun <T1, T2, T3, T4, T5, T6, R> combine(
    crossinline f1: suspend () -> Result<T1>,
    crossinline f2: suspend () -> Result<T2>,
    crossinline f3: suspend () -> Result<T3>,
    crossinline f4: suspend () -> Result<T4>,
    crossinline f5: suspend () -> Result<T5>,
    crossinline f6: suspend () -> Result<T6>,
    noinline transform: suspend (T1, T2, T3, T4, T5, T6) -> R
): Result<R> {
    val flow5 = flow { emit(f5()) }.map { it.getOrThrow() }
    val flow6 = flow { emit(f6()) }.map { it.getOrThrow() }

    val combinedFlow = combine(
        flow { emit(f1()) }.map { it.getOrThrow() },
        flow { emit(f2()) }.map { it.getOrThrow() },
        flow { emit(f3()) }.map { it.getOrThrow() },
        flow { emit(f4()) }.map { it.getOrThrow() },
        flow5.combine(flow6) { r5, r6 -> r5 to r6 }, // Hack for combine, as flows combine doesn't support more than 5 elements
    ) { r1, r2, r3, r4, (r5, r6) ->
        transform(r1, r2, r3, r4, r5, r6)
    }

    return runCatching {
        combinedFlow.first()
    }
}

suspend inline fun <T1, T2, T3, T4, T5, T6, T7, R> combine(
    crossinline f1: suspend () -> Result<T1>,
    crossinline f2: suspend () -> Result<T2>,
    crossinline f3: suspend () -> Result<T3>,
    crossinline f4: suspend () -> Result<T4>,
    crossinline f5: suspend () -> Result<T5>,
    crossinline f6: suspend () -> Result<T6>,
    crossinline f7: suspend () -> Result<T7>,
    noinline transform: suspend (T1, T2, T3, T4, T5, T6, T7) -> R
): Result<R> {
    val flow4 = flow { emit(f4()) }.map { it.getOrThrow() }
    val flow5 = flow { emit(f5()) }.map { it.getOrThrow() }
    val flow6 = flow { emit(f6()) }.map { it.getOrThrow() }
    val flow7 = flow { emit(f7()) }.map { it.getOrThrow() }

    val combinedFlow = combine(
        flow { emit(f1()) }.map { it.getOrThrow() },
        flow { emit(f2()) }.map { it.getOrThrow() },
        flow { emit(f3()) }.map { it.getOrThrow() },
        flow4.combine(flow5) { r4, r5 -> r4 to r5 },
        flow6.combine(flow7) { r6, r7 -> r6 to r7 }
    ) { r1, r2, r3, (r4, r5), (r6, r7) ->
        transform(r1, r2, r3, r4, r5, r6, r7)
    }

    return runCatching {
        combinedFlow.first()
    }
}

/**
 * Emits the current iteration every [timeMillis] ms
 */
fun interval(timeMillis: Long): Flow<Long> {
    return flow {
        var counter: Long = 0

        while (true) {
            delay(timeMillis)
            emit(counter++)
        }
    }
}
