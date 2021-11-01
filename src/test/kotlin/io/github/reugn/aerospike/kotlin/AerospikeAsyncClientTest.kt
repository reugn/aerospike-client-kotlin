package io.github.reugn.aerospike.kotlin

import com.aerospike.client.Bin
import com.aerospike.client.Operation
import com.aerospike.client.query.PartitionFilter
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
    fun `Get records`(): Unit = runBlocking {
        val records = asyncClient.getBatch(null, keys)
        assertEquals(keysSize, records.size)
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
    fun `Scan all`(): Unit = runBlocking {
        val flow = asyncClient.scanAll(null, namespace, set)
        val recordsNumber = flow.toList().size
        assertEquals(keysSize, recordsNumber)
    }

    @Test
    fun `Scan partitions`(): Unit = runBlocking {
        val flow = asyncClient.scanPartitions(null, PartitionFilter.all(), namespace, set)
        val recordsNumber = flow.toList().size
        assertEquals(keysSize, recordsNumber)
    }
}
