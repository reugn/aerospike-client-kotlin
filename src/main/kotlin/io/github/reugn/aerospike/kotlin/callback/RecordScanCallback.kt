package io.github.reugn.aerospike.kotlin.callback

import com.aerospike.client.Key
import com.aerospike.client.Record
import com.aerospike.client.ScanCallback
import com.aerospike.client.query.KeyRecord

class RecordScanCallback : ScanCallback {

    private val recordSet: MutableList<KeyRecord> = mutableListOf()

    override fun scanCallback(key: Key?, record: Record?) {
        recordSet.add(KeyRecord(key, record))
    }

    fun getRecordSet(): List<KeyRecord> {
        return recordSet.toList()
    }
}
