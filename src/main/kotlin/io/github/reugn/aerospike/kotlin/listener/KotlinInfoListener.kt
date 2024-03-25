package io.github.reugn.aerospike.kotlin.listener

import com.aerospike.client.AerospikeException
import com.aerospike.client.listener.InfoListener

class KotlinInfoListener : BaseListener<Map<String, String>>(), InfoListener {
    override fun onSuccess(map: Map<String, String>) {
        success(map)
    }

    override fun onFailure(exception: AerospikeException?) {
        failure(exception)
    }
}