package io.github.reugn.aerospike.kotlin.listener

import com.aerospike.client.AerospikeException
import com.aerospike.client.BatchRecord
import com.aerospike.client.BatchResults
import com.aerospike.client.listener.BatchRecordArrayListener

class KotlinBatchRecordArrayListener : BaseListener<BatchResults>(), BatchRecordArrayListener {
    override fun onSuccess(records: Array<out BatchRecord>?, status: Boolean) {
        success(BatchResults(records, status))
    }

    override fun onFailure(records: Array<out BatchRecord>?, exception: AerospikeException?) {
        failure(exception)
    }
}
