package io.github.reugn.aerospike.kotlin

import com.aerospike.client.BatchDelete
import com.aerospike.client.BatchWrite
import com.aerospike.client.Bin
import com.aerospike.client.Operation
import com.aerospike.client.exp.Exp
import com.aerospike.client.exp.ExpOperation
import com.aerospike.client.exp.ExpReadFlags
import com.aerospike.client.query.PartitionFilter
import io.github.reugn.aerospike.kotlin.model.QueryStatement
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AerospikeAsyncClientTest : AsyncClientTestBase() {

    @BeforeEach
    fun before() {
        initKeys()
    }

    @AfterEach
    fun after() {
        deleteKeys()
    }

    @Test
    fun `Get record`(): Unit = runBlocking {
        val record = asyncClient.get(null, keys[0])
        assertEquals(0, record?.getInt(intBin))
    }

    @Test
    fun `Get batch of records`(): Unit = runBlocking {
        val records = asyncClient.getBatch(null, keys)
        assertEquals(keysSize, records.size)
    }

    @Test
    fun `Get records with read operation`(): Unit = runBlocking {
        val mulIntBin = "mulIntBin"
        val multiplier = 10L
        val mulExp = Exp.build(Exp.mul(Exp.intBin(intBin), Exp.`val`(multiplier)))
        val result = asyncClient.getBatchOp(null, keys, ExpOperation.read(mulIntBin, mulExp, ExpReadFlags.DEFAULT))
        assertEquals(keysSize, result.size)
        result.mapIndexed { i, rec ->
            val expected = multiplier * i
            assertEquals(expected, rec.getLong(mulIntBin))
        }
    }

    @Test
    fun `Append bin string value`(): Unit = runBlocking {
        asyncClient.append(null, keys[0], Bin(strBin, "_"))
        val record = asyncClient.get(null, keys[0])
        assertEquals("str_0_", record?.getString(strBin))
    }

    @Test
    fun `Prepend bin string value`(): Unit = runBlocking {
        asyncClient.prepend(null, keys[0], Bin(strBin, "_"))
        val record = asyncClient.get(null, keys[0])
        assertEquals("_str_0", record?.getString(strBin))
    }

    @Test
    fun `Add integer bin value`(): Unit = runBlocking {
        asyncClient.add(null, keys[0], Bin(intBin, 10))
        val record = asyncClient.get(null, keys[0])
        assertEquals(10, record?.getInt(intBin))
    }

    @Test
    fun `Delete record`(): Unit = runBlocking {
        asyncClient.delete(null, keys[0])
        val record = asyncClient.get(null, keys[0])
        assertNull(record)
    }

    @Test
    fun `Delete batch of records`(): Unit = runBlocking {
        asyncClient.deleteBatch(null, null, keys)
        val records = asyncClient.getBatch(null, keys).filterNotNull()
        assertTrue { records.isEmpty() }
    }

    @Test
    fun `Whether record exists`(): Unit = runBlocking {
        var exists = asyncClient.exists(null, keys[0])
        assertTrue { exists }
        exists = asyncClient.exists(null, neKey)
        assertFalse { exists }
    }

    @Test
    fun `Whether records exist`(): Unit = runBlocking {
        val exist = asyncClient.existsBatch(null, keys + neKey)
        assertEquals(keysSize + 1, exist.size)
        assertTrue { exist[0] }
        assertFalse { exist[keysSize] }
    }

    @Test
    fun `Perform operations on a key`(): Unit = runBlocking {
        val record = asyncClient.operate(
            null, keys[0],
            Operation.put(Bin(intBin, 100)),
            Operation.get()
        )
        assertEquals(100, record?.getInt(intBin))
    }

    @Test
    fun `Operate batch of records`(): Unit = runBlocking {
        val result = asyncClient.operateBatch(null, null, keys, Operation.put(Bin(intBin, 100)))
        assertTrue(result.status)
        asyncClient.getBatch(null, keys).map {
            assertEquals(100, it.getInt(intBin))
        }
    }

    @Test
    fun `Operate list of BatchRecords`(): Unit = runBlocking {
        val records = listOf(
            listOf(BatchWrite(keys[0], arrayOf(Operation.put(Bin(intBin, 100))))),
            keys.slice(1 until keysSize).map { BatchDelete(it) }
        ).flatten()
        val result = asyncClient.operateBatchRecord(null, records)
        assertTrue(result)
        val getResult = asyncClient.getBatch(null, keys).filterNotNull()
        assertEquals(1, getResult.size)
    }

    @Test
    fun `Query all`(): Unit = runBlocking {
        val queryStatement = QueryStatement(namespace, setName = set)
        val flow = asyncClient.query(null, queryStatement)
        val recordsNumber = flow.toList().size
        assertEquals(keysSize, recordsNumber)
    }

    @Test
    fun `Query partitions`(): Unit = runBlocking {
        val queryStatement = QueryStatement(namespace, setName = set, partitionFilter = PartitionFilter.all())
        val flow = asyncClient.query(null, queryStatement)
        val recordsNumber = flow.toList().size
        assertEquals(keysSize, recordsNumber)
    }
}
