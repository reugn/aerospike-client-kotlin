package io.github.reugn.aerospike.kotlin

import com.aerospike.client.AerospikeClient
import com.aerospike.client.Bin
import com.aerospike.client.Host
import com.aerospike.client.Key
import com.aerospike.client.async.EventPolicy
import com.aerospike.client.async.NettyEventLoops
import com.aerospike.client.policy.ClientPolicy
import io.netty.channel.nio.NioEventLoopGroup
import kotlinx.coroutines.runBlocking

open class AsyncClientTestBase {

    protected val asyncClient: IAerospikeAsyncClient

    protected val namespace = "test"
    protected val set = "client"

    protected val intBin = "intBin"
    protected val strBin = "strBin"

    protected val keysSize = 2
    protected val keys: MutableList<Key> = mutableListOf()
    protected val neKey: Key = Key(namespace, set, "ne")

    init {
        val clientPolicy = ClientPolicy()
        clientPolicy.eventLoops = NettyEventLoops(EventPolicy(), NioEventLoopGroup(2))
        val client = AerospikeClient(clientPolicy, Host("localhost", 3000))

        asyncClient = AerospikeAsyncClient(client)
    }

    protected fun initKeys() {
        for (i in 0 until keysSize) {
            val key = Key(namespace, set, "key_$i")
            keys.add(key)
            runBlocking {
                asyncClient.put(
                    null, key, Bin(intBin, i),
                    Bin(strBin, "str_$i")
                )
            }
        }
    }

    protected fun deleteKeys() {
        for (i in 0 until keysSize) {
            runBlocking {
                asyncClient.delete(null, keys[i])
            }
        }
    }
}
