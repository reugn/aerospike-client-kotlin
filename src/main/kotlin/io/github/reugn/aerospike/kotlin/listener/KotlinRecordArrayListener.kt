package io.github.reugn.aerospike.kotlin.listener

import com.aerospike.client.AerospikeException
import com.aerospike.client.Key
import com.aerospike.client.Record
import com.aerospike.client.listener.RecordArrayListener

class KotlinRecordArrayListener : BaseListener<List<Record>>(), RecordArrayListener {
    override fun onSuccess(keys: Array<out Key>?, records: Array<out Record>?) {
        success(records?.toList())
    }

    override fun onFailure(exception: AerospikeException?) {
        failure(exception)
    }
}
