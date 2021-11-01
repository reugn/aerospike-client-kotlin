package io.github.reugn.aerospike.kotlin.config

data class AsyncConfig(
    val channelCapacity: Int
) {

    companion object {
        val default = AsyncConfig(
            channelCapacity = 1024
        )
    }
}
