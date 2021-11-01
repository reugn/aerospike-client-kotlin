package io.github.reugn.aerospike.kotlin

import com.aerospike.client.*
import com.aerospike.client.cluster.Node
import com.aerospike.client.policy.*
import com.aerospike.client.query.KeyRecord
import com.aerospike.client.query.PartitionFilter
import io.github.reugn.aerospike.kotlin.callback.RecordScanCallback
import io.github.reugn.aerospike.kotlin.config.AsyncConfig
import io.github.reugn.aerospike.kotlin.listener.*
import kotlinx.coroutines.flow.Flow
import java.util.*

class AerospikeAsyncClient(
    private val client: AerospikeClient,
    private val config: AsyncConfig
) : IAerospikeAsyncClient {

    constructor(client: AerospikeClient) :
            this(client, AsyncConfig.default)

    override fun asJava(): AerospikeClient {
        return client
    }

    override suspend fun put(policy: WritePolicy?, key: Key, vararg bins: Bin): Key? {
        val listener = KotlinWriteListener()
        client.put(null, listener, policy, key, *bins)
        return listener.await()
    }

    override suspend fun append(policy: WritePolicy?, key: Key, vararg bins: Bin): Key? {
        val listener = KotlinWriteListener()
        client.append(null, listener, policy, key, *bins)
        return listener.await()
    }

    override suspend fun prepend(policy: WritePolicy?, key: Key, vararg bins: Bin): Key? {
        val listener = KotlinWriteListener()
        client.prepend(null, listener, policy, key, *bins)
        return listener.await()
    }

    override suspend fun add(policy: WritePolicy?, key: Key, vararg bins: Bin): Key? {
        val listener = KotlinWriteListener()
        client.add(null, listener, policy, key, *bins)
        return listener.await()
    }

    override suspend fun delete(policy: WritePolicy?, key: Key): Boolean {
        val listener = KotlinDeleteListener()
        client.delete(null, listener, policy, key)
        return listener.await() ?: false
    }

    override suspend fun truncate(
        policy: InfoPolicy?, ns: String, set: String,
        beforeLastUpdate: Calendar?
    ) {
        return client.truncate(policy, ns, set, beforeLastUpdate)
    }

    override suspend fun touch(policy: WritePolicy?, key: Key): Key? {
        val listener = KotlinWriteListener()
        client.touch(null, listener, policy, key)
        return listener.await()
    }

    override suspend fun exists(policy: Policy?, key: Key): Boolean {
        val listener = KotlinExistsListener()
        client.exists(null, listener, policy, key)
        return listener.await() ?: false
    }

    override suspend fun existsBatch(policy: BatchPolicy?, keys: List<Key>): List<Boolean> {
        val listener = KotlinExistsArrayListener()
        client.exists(null, listener, policy, keys.toTypedArray())
        return listener.await()!!
    }

    override suspend fun get(policy: Policy?, key: Key, vararg binNames: String): Record? {
        val listener = KotlinRecordListener()
        if (binNames.isNotEmpty()) {
            client.get(null, listener, policy, key, *binNames)
        } else {
            client.get(null, listener, policy, key)
        }
        return listener.await()
    }

    override suspend fun getHeader(policy: Policy?, key: Key): Record? {
        val listener = KotlinRecordListener()
        client.getHeader(null, listener, policy, key)
        return listener.await()
    }

    override suspend fun getBatch(
        policy: BatchPolicy?, keys: List<Key>,
        vararg binNames: String
    ): List<Record> {
        val listener = KotlinRecordArrayListener()
        if (binNames.isNotEmpty()) {
            client.get(null, listener, policy, keys.toTypedArray(), *binNames)
        } else {
            client.get(null, listener, policy, keys.toTypedArray())
        }
        return listener.await()!!
    }

    override suspend fun getHeaderBatch(policy: BatchPolicy?, keys: List<Key>): List<Record> {
        val listener = KotlinRecordArrayListener()
        client.getHeader(null, listener, policy, keys.toTypedArray())
        return listener.await()!!
    }

    override suspend fun operate(
        policy: WritePolicy?, key: Key,
        vararg operations: Operation
    ): Record? {
        val listener = KotlinRecordListener()
        client.operate(null, listener, policy, key, *operations)
        return listener.await()
    }

    override suspend fun scanNodeName(
        policy: ScanPolicy?,
        nodeName: String,
        ns: String,
        set: String,
        vararg binNames: String
    ): List<KeyRecord> {
        val callback = RecordScanCallback()
        client.scanNode(policy, nodeName, ns, set, callback, *binNames)
        return callback.getRecordSet()
    }

    override suspend fun scanNode(
        policy: ScanPolicy?,
        node: Node,
        ns: String,
        set: String,
        vararg binNames: String
    ): List<KeyRecord> {
        val callback = RecordScanCallback()
        client.scanNode(policy, node, ns, set, callback, *binNames)
        return callback.getRecordSet()
    }

    override suspend fun scanAll(
        policy: ScanPolicy?,
        ns: String,
        set: String,
        vararg binNames: String
    ): Flow<KeyRecord> {
        val listener = ScanRecordSequenceListener(config)
        client.scanAll(null, listener, policy, ns, set, *binNames)
        return listener.consumeAsFlow()
    }

    override suspend fun scanPartitions(
        policy: ScanPolicy?,
        filter: PartitionFilter,
        ns: String,
        set: String,
        vararg binNames: String
    ): Flow<KeyRecord> {
        val listener = ScanRecordSequenceListener(config)
        client.scanPartitions(null, listener, policy, filter, ns, set, *binNames)
        return listener.consumeAsFlow()
    }

    override fun close() {
        client.close()
    }
}
