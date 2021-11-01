package io.github.reugn.aerospike.kotlin.listener

import com.aerospike.client.AerospikeException
import com.aerospike.client.Key
import com.aerospike.client.Record
import com.aerospike.client.listener.RecordListener

class KotlinRecordListener : BaseListener<Record>(), RecordListener {
    override fun onSuccess(key: Key?, record: Record?) {
        success(record)
    }

    override fun onFailure(exception: AerospikeException?) {
        failure(exception)
    }
}
