package io.github.reugn.aerospike.kotlin.listener

import com.aerospike.client.AerospikeException
import com.aerospike.client.BatchRecord
import com.aerospike.client.listener.BatchOperateListListener

class KotlinBatchOperateListListener : BaseListener<Boolean>(), BatchOperateListListener {
    override fun onSuccess(records: MutableList<BatchRecord>?, status: Boolean) {
        success(status)
    }

    override fun onFailure(exception: AerospikeException?) {
        failure(exception)
    }
}
