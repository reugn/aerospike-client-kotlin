package io.github.reugn.aerospike.kotlin

import com.aerospike.client.*
import com.aerospike.client.cluster.Node
import com.aerospike.client.policy.*
import com.aerospike.client.query.KeyRecord
import com.aerospike.client.query.PartitionFilter
import kotlinx.coroutines.flow.Flow
import java.io.Closeable
import java.util.*

interface IAerospikeAsyncClient : Closeable {

    fun asJava(): AerospikeClient

    //-------------------------------------------------------
    // Write Record Operations
    //-------------------------------------------------------

    suspend fun put(policy: WritePolicy?, key: Key, vararg bins: Bin): Key?

    //-------------------------------------------------------
    // String Operations
    //-------------------------------------------------------

    suspend fun append(policy: WritePolicy?, key: Key, vararg bins: Bin): Key?

    suspend fun prepend(policy: WritePolicy?, key: Key, vararg bins: Bin): Key?

    //-------------------------------------------------------
    // Arithmetic Operations
    //-------------------------------------------------------

    suspend fun add(policy: WritePolicy?, key: Key, vararg bins: Bin): Key?

    //-------------------------------------------------------
    // Delete Operations
    //-------------------------------------------------------

    suspend fun delete(policy: WritePolicy?, key: Key): Boolean

    suspend fun truncate(
        policy: InfoPolicy?, ns: String, set: String,
        beforeLastUpdate: Calendar? = null
    )

    //-------------------------------------------------------
    // Touch Operations
    //-------------------------------------------------------

    suspend fun touch(policy: WritePolicy?, key: Key): Key?

    //-------------------------------------------------------
    // Existence-Check Operations
    //-------------------------------------------------------

    suspend fun exists(policy: Policy?, key: Key): Boolean

    suspend fun existsBatch(policy: BatchPolicy?, keys: List<Key>): List<Boolean>

    //-------------------------------------------------------
    // Read Record Operations
    //-------------------------------------------------------

    suspend fun get(policy: Policy?, key: Key, vararg binNames: String): Record?

    suspend fun getHeader(policy: Policy?, key: Key): Record?

    //-------------------------------------------------------
    // Batch Read Operations
    //-------------------------------------------------------

    suspend fun getBatch(
        policy: BatchPolicy?, keys: List<Key>,
        vararg binNames: String
    ): List<Record>

    suspend fun getHeaderBatch(policy: BatchPolicy?, keys: List<Key>): List<Record>

    //-------------------------------------------------------
    // Generic Database Operations
    //-------------------------------------------------------

    suspend fun operate(policy: WritePolicy?, key: Key, vararg operations: Operation): Record?

    //-------------------------------------------------------
    // Scan Operations
    //-------------------------------------------------------

    suspend fun scanNodeName(
        policy: ScanPolicy?, nodeName: String, ns: String,
        set: String, vararg binNames: String
    ): List<KeyRecord>

    suspend fun scanNode(
        policy: ScanPolicy?, node: Node, ns: String, set: String,
        vararg binNames: String
    ): List<KeyRecord>

    suspend fun scanAll(
        policy: ScanPolicy?, ns: String, set: String,
        vararg binNames: String
    ): Flow<KeyRecord>

    suspend fun scanPartitions(
        policy: ScanPolicy?, filter: PartitionFilter,
        ns: String, set: String, vararg binNames: String
    ): Flow<KeyRecord>
}
