package io.github.reugn.aerospike.kotlin.listener

import com.aerospike.client.AerospikeException
import com.aerospike.client.Key
import com.aerospike.client.Record
import com.aerospike.client.listener.RecordSequenceListener
import com.aerospike.client.query.KeyRecord
import io.github.reugn.aerospike.kotlin.config.AsyncConfig
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.runBlocking

class ScanRecordSequenceListener(config: AsyncConfig) : RecordSequenceListener {

    private val channel: Channel<KeyRecord> = Channel(capacity = config.channelCapacity)

    override fun onRecord(key: Key?, record: Record?) {
        runBlocking {
            channel.send(KeyRecord(key, record))
        }
    }

    override fun onSuccess() {
        channel.close()
    }

    override fun onFailure(exception: AerospikeException?) {
        channel.close(exception)
    }

    fun consumeAsFlow(): Flow<KeyRecord> {
        return channel.consumeAsFlow()
    }
}
