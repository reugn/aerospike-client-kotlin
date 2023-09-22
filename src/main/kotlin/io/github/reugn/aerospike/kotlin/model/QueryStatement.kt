package io.github.reugn.aerospike.kotlin.model

import com.aerospike.client.Operation
import com.aerospike.client.query.Filter
import com.aerospike.client.query.PartitionFilter
import com.aerospike.client.query.Statement

data class QueryStatement(
    val namespace: String,
    val setName: String? = null,
    val binNames: String? = null,
    val secondaryIndexFilter: Filter? = null,
    val partitionFilter: PartitionFilter? = null,
    val operations: Collection<Operation>? = null,
    val maxRecords: Long? = null,
    val recordsPerSecond: Int? = null
) {
    val statement: Statement by lazy {
        val statement = Statement()
        statement.namespace = namespace
        setName?.let { statement.setName = it }
        binNames?.let { statement.setBinNames(it) }
        secondaryIndexFilter?.let { statement.filter = it }
        operations?.let { statement.operations = it.toTypedArray() }
        maxRecords?.let { statement.maxRecords = it }
        recordsPerSecond?.let { statement.recordsPerSecond = it }
        statement
    }
}
