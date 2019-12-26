@file:JvmName("CoroutineHelper.kt")

package com.chiclaim.android.retrofit_sample.helper

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


fun CoroutineScope.uiJob(timeout: Long = 0L, block: suspend CoroutineScope.() -> Unit) {
    startJob(this, Dispatchers.Main, timeout, block)
}


fun startJob(
        parentScope: CoroutineScope,
        coroutineContext: CoroutineContext,
        timeout: Long = 0L,
        block: suspend CoroutineScope.() -> Unit
) {
    parentScope.launch(coroutineContext) {
        supervisorScope {
            if (timeout > 0L) {
                withTimeout(timeout) {
                    block()
                }
            } else {
                block()
            }
        }
    }
}


suspend fun <T> startTask(
        coroutineContext: CoroutineContext,
        timeout: Long = 0L,
        block: suspend CoroutineScope.() -> T
): T {
    return withContext(coroutineContext) {
        return@withContext if (timeout > 0L) {
            withTimeout(timeout) {
                block()
            }
        } else {
            block()
        }
    }
}

suspend fun <T> startTaskAsync(
        parentScope: CoroutineScope,
        coroutineContext: CoroutineContext = Dispatchers.Default,
        timeout: Long = 0L,
        block: suspend CoroutineScope.() -> T
): Deferred<T> {
    return supervisorScope {
        parentScope.async(coroutineContext) {
            if (timeout > 0L) {
                withTimeout(timeout) {
                    block()
                }
            } else {
                block()
            }
        }
    }

}