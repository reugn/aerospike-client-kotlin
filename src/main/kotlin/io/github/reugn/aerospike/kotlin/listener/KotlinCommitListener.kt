package io.github.reugn.aerospike.kotlin.listener

import com.aerospike.client.AerospikeException
import com.aerospike.client.CommitStatus
import com.aerospike.client.listener.CommitListener

class KotlinCommitListener : BaseListener<CommitStatus>(), CommitListener {
    override fun onSuccess(status: CommitStatus?) {
        success(status)
    }

    override fun onFailure(exception: AerospikeException.Commit?) {
        failure(exception)
    }
}
