package io.github.reugn.aerospike.kotlin.listener

import com.aerospike.client.AerospikeException
import com.aerospike.client.Key
import com.aerospike.client.listener.ExistsListener

class KotlinExistsListener : BaseListener<Boolean>(), ExistsListener {
    override fun onSuccess(key: Key?, exists: Boolean) {
        success(exists)
    }

    override fun onFailure(exception: AerospikeException?) {
        failure(exception)
    }
}
