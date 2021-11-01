package io.github.reugn.aerospike.kotlin.listener

import com.aerospike.client.AerospikeException
import com.aerospike.client.Key
import com.aerospike.client.listener.WriteListener

class KotlinWriteListener : BaseListener<Key>(), WriteListener {
    override fun onSuccess(key: Key?) {
        success(key)
    }

    override fun onFailure(exception: AerospikeException?) {
        failure(exception)
    }
}
