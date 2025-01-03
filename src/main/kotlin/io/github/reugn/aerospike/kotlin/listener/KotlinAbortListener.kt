package io.github.reugn.aerospike.kotlin.listener

import com.aerospike.client.AbortStatus
import com.aerospike.client.listener.AbortListener

class KotlinAbortListener : BaseListener<AbortStatus>(), AbortListener {
    override fun onSuccess(status: AbortStatus?) {
        success(status)
    }
}
