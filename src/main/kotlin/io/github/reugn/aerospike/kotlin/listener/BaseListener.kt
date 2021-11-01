package io.github.reugn.aerospike.kotlin.listener

import com.aerospike.client.AerospikeException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred

abstract class BaseListener<T> {

    private val deferred = CompletableDeferred<T>()

    protected fun success(value: T?): BaseListener<T> {
        if (value != null) {
            deferred.complete(value)
        } else {
            deferred.cancel()
        }
        return this
    }

    protected fun failure(cause: Throwable?): BaseListener<T> {
        deferred.completeExceptionally(
            cause ?: AerospikeException("${this.javaClass.name} null cause")
        )
        return this
    }

    suspend fun await(): T? {
        return try {
            deferred.await()
        } catch (_: CancellationException) {
            null
        }
    }
}
