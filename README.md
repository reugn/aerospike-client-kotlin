# aerospike-client-kotlin

Aerospike Client for Kotlin wraps the Java client callback-based asynchronous methods and provides an idiomatic Kotlin API using coroutines and `Flow`.

The `AerospikeAsyncClient` takes the Java `AerospikeClient` as a constructor parameter and exposes it via the `asJava()` method.
Make sure to set the [ClientPolicy.eventLoops](https://docs.aerospike.com/docs/client/java/usage/async/eventloop.html) when creating the Java client.

## Build from Source
```sh
./gradlew build
```

## Examples
```kotlin
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
fun `Scan partitions`(): Unit = runBlocking {
    val flow = asyncClient.scanPartitions(null, PartitionFilter.all(), namespace, set)
    val recordsNumber = flow.toList().size
    assertEquals(keysSize, recordsNumber)
}
```

## License
Licensed under the [Apache 2.0 License](./LICENSE).
