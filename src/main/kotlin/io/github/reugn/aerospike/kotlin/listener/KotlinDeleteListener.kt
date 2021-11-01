package io.github.reugn.aerospike.kotlin.listener

import com.aerospike.client.AerospikeException
import com.aerospike.client.Key
import com.aerospike.client.listener.DeleteListener

class KotlinDeleteListener : BaseListener<Boolean>(), DeleteListener {
    override fun onSuccess(key: Key?, existed: Boolean) {
        success(existed)
    }

    override fun onFailure(exception: AerospikeException?) {
        failure(exception)
    }
}
