# aerospike-client-kotlin
[![Build](https://github.com/reugn/aerospike-client-kotlin/actions/workflows/build.yml/badge.svg)](https://github.com/reugn/aerospike-client-kotlin/actions/workflows/build.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.reugn/aerospike-client-kotlin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.reugn/aerospike-client-kotlin/)

Aerospike Client for Kotlin wraps the Java client callback-based asynchronous methods and provides an idiomatic Kotlin API using coroutines and `Flow`.

The `AerospikeAsyncClient` takes the Java `AerospikeClient` as a constructor parameter and exposes it via the `asJava()` method.
Make sure to set the [ClientPolicy.eventLoops](https://docs.aerospike.com/docs/client/java/usage/async/eventloop.html) when creating the Java client.

## Getting started
### Add as a dependency
```kotlin
dependencies {
    implementation("io.github.reugn:aerospike-client-kotlin:<version>")
}
```

### Build from source
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
fun `Query all`(): Unit = runBlocking {
    val queryStatement = QueryStatement(namespace, setName = set)
    val flow = asyncClient.query(null, queryStatement)
    val recordsNumber = flow.toList().size
    assertEquals(keysSize, recordsNumber)
}
```

## License
Licensed under the [Apache 2.0 License](./LICENSE).
