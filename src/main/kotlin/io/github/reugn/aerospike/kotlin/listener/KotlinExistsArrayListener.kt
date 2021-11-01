package io.github.reugn.aerospike.kotlin.listener

import com.aerospike.client.AerospikeException
import com.aerospike.client.Key
import com.aerospike.client.listener.ExistsArrayListener

class KotlinExistsArrayListener : BaseListener<List<Boolean>>(), ExistsArrayListener {
    override fun onSuccess(keys: Array<out Key>?, exists: BooleanArray?) {
        success(exists?.toList())
    }

    override fun onFailure(exception: AerospikeException?) {
        failure(exception)
    }
}