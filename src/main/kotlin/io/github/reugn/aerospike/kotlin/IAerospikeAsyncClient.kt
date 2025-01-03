package io.github.reugn.aerospike.kotlin

import com.aerospike.client.*
import com.aerospike.client.cluster.Node
import com.aerospike.client.policy.*
import com.aerospike.client.query.KeyRecord
import io.github.reugn.aerospike.kotlin.model.QueryStatement
import kotlinx.coroutines.flow.Flow
import java.io.Closeable
import java.util.*

interface IAerospikeAsyncClient : Closeable {

    fun asJava(): IAerospikeClient

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

    suspend fun deleteBatch(
        policy: BatchPolicy?, batchDeletePolicy: BatchDeletePolicy?,
        keys: Collection<Key>
    ): BatchResults

    fun truncate(
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

    suspend fun existsBatch(policy: BatchPolicy?, keys: Collection<Key>): List<Boolean>

    //-------------------------------------------------------
    // Read Record Operations
    //-------------------------------------------------------

    suspend fun get(policy: Policy?, key: Key, vararg binNames: String): Record?

    suspend fun getBatch(policy: BatchPolicy?, keys: Collection<Key>, vararg binNames: String): List<Record>

    suspend fun getBatchOp(
        policy: BatchPolicy?, keys: Collection<Key>,
        vararg operations: Operation
    ): List<Record>

    suspend fun getHeader(policy: Policy?, key: Key): Record?

    suspend fun getHeaderBatch(policy: BatchPolicy?, keys: Collection<Key>): List<Record>

    //-------------------------------------------------------
    // Generic Database Operations
    //-------------------------------------------------------

    suspend fun operate(policy: WritePolicy?, key: Key, vararg operations: Operation): Record?

    suspend fun operateBatch(
        policy: BatchPolicy?, batchWritePolicy: BatchWritePolicy?,
        keys: Collection<Key>, vararg operations: Operation
    ): BatchResults

    suspend fun operateBatchRecord(policy: BatchPolicy?, records: Collection<BatchRecord>): Boolean

    //-------------------------------------------------------
    // Multi-Record Transactions
    //-------------------------------------------------------

    suspend fun commit(txn: Txn): CommitStatus

    suspend fun abort(txn: Txn): AbortStatus

    //-------------------------------------------------------
    // Scan/Query Operations
    //-------------------------------------------------------

    suspend fun scanNodeName(
        policy: ScanPolicy?, nodeName: String, ns: String,
        set: String, vararg binNames: String
    ): List<KeyRecord>

    suspend fun scanNode(
        policy: ScanPolicy?, node: Node, ns: String, set: String,
        vararg binNames: String
    ): List<KeyRecord>

    fun query(policy: QueryPolicy?, statement: QueryStatement): Flow<KeyRecord>

    //-----------------------------------------------------------------
    // Async Info functions
    //-----------------------------------------------------------------

    suspend fun info(
        policy: InfoPolicy?,
        node: Node?,
        vararg commands: String
    ): Map<String, String>
}
